package com.sparta.team30.category.respository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team30.category.domain.Category;
import lombok.RequiredArgsConstructor;
import static com.sparta.team30.category.domain.QCategory.category;

import java.util.List;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean isCategoryNameExists(String categoryName) {
        long count = jpaQueryFactory
                .select(category.count())
                .from(category)
                .where(
                        category.categoryName.eq(categoryName),
                        category.isDeleted.eq(false)
                )
                .fetchOne();
        return count > 0;
    }

    @Override
    public List<Category> getCategories() {
        return jpaQueryFactory
                .selectFrom(category)
                .where(category.isDeleted.eq(false))
                .fetch();
    }
}
