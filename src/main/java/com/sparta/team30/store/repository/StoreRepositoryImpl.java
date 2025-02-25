package com.sparta.team30.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.sparta.team30.category.domain.Category;
import com.sparta.team30.store.domain.QStore;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.dto.StoreUpdateRequestDto;
import com.sparta.team30.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static com.sparta.team30.store.domain.QStore.store;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private final EntityManager entityManager;

    @Override
    public boolean isDuplicateStore (
            UUID categoryId,
            String storeName,
            String storePhone,
            String storePostcode,
            String storeAddress1)
    {
        long count = jpaQueryFactory
                .select(store.count())
                .from(store)
                .where(
                        store.category.categoryId.eq(categoryId),
                        store.storeName.eq(storeName),
                        store.storePhone.eq(storePhone),
                        store.storePostcode.eq(storePostcode),
                        store.storeAddress1.eq(storeAddress1)
                )
                .fetchOne();
        return count > 0;
    }

    @Override
    public Page<Store> getStores(Pageable pageable, String sortBy, String order, String search, UUID categoryId) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order);
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(store.isDeleted.eq(false));
        builder.and(store.isApproved.eq(true));
        if (categoryId != null)
            builder.and(store.category.categoryId.eq(categoryId));
        if (search != null && !search.isEmpty())
            builder.and(store.storeName.containsIgnoreCase(search));

        List<Store> storeList = jpaQueryFactory
                .selectFrom(store)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();
        long total = jpaQueryFactory
                .select(store.count())
                .from(store)
                .where(builder)
                .fetchOne();
        return new PageImpl<>(storeList, pageable, total);
    }

    @Override
    public Store getMyStore(User user) {
        Store myStore = jpaQueryFactory
                .selectFrom(store)
                .where(
                        store.owner.eq(user)
                )
                .fetchOne();

        return myStore;
    }

    @Override
    public Page<Store> findUnapprovedStores(Pageable pageable, String sortBy, String order) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order);
        List<Store> storeList = jpaQueryFactory
                .selectFrom(store)
                .where(store.isApproved.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();
        long total = jpaQueryFactory
                .select(store.count())
                .from(store)
                .where(
                        store.isApproved.eq(false),
                        store.isDeleted.eq(false)
                )
                .fetchOne();
        return new PageImpl<>(storeList, pageable, total);
    }

    @Override
    public boolean isOwner(UUID storeId, User user) {
        long count = jpaQueryFactory
                .select(store.count())
                .from(store)
                .where(
                        store.storeId.eq(storeId),
                        store.owner.eq(user)
                )
                .fetchOne();
        return count > 0;
    }

    @Override
    public boolean isAlreadyOwner(User user) {
        long count = jpaQueryFactory
                .select(store.count())
                .from(store)
                .where(
                        store.owner.eq(user)
                )
                .fetchOne();
        return count > 0;
    }

    @Override
    public long updateStore(UUID storeId, StoreUpdateRequestDto requestDto) {
        QStore store = QStore.store;
        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, store);

        if(requestDto.getCategoryId() != null) {
            Category category = entityManager.find(Category.class, requestDto.getCategoryId());
            updateClause.set(store.category, category);
        }

        if (requestDto.getStoreName() != null && !requestDto.getStoreName().isEmpty()) {
            updateClause.set(store.storeName, requestDto.getStoreName());
        }

        if (requestDto.getStorePhone() != null && !requestDto.getStorePhone().isEmpty()) {
            updateClause.set(store.storePhone, requestDto.getStorePhone());
        }

        if (
                (requestDto.getStorePostcode() != null && !requestDto.getStorePostcode().isEmpty())
                        &&
                        (requestDto.getStoreAddress1() != null && !requestDto.getStoreAddress1().isEmpty())
        ) {
            updateClause
                    .set(store.storePostcode, requestDto.getStorePostcode())
                    .set(store.storeAddress1, requestDto.getStoreAddress1());

            if (requestDto.getStoreAddress2() != null && !requestDto.getStoreAddress2().isEmpty()) {
                updateClause.set(store.storeAddress2, requestDto.getStoreAddress2());
            }
        }

        return updateClause
                .where(store.storeId.eq(storeId))
                .execute();

    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, String order) {
        Order direction = order.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;
        if ("updatedAt".equals(sortBy)) {
            return new OrderSpecifier<>(direction, store.updatedAt);
        } else {
            return new OrderSpecifier<>(direction, store.createdAt);
        }
    }
}