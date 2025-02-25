package com.sparta.team30.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team30.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.sparta.team30.user.domain.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(queryFactory
                .selectFrom(user)
                .where(
                        user.username.eq(username),
                        user.deletedAt.isNull(),
                        user.deletedBy.isNull()
                )
                .fetchOne());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(queryFactory
                .selectFrom(user)
                .where(
                        user.email.eq(email),
                        user.deletedAt.isNull(),
                        user.deletedBy.isNull()
                )
                .fetchOne());
    }

    @Override
    public boolean existsByEmail(String email) {
        return queryFactory
                .selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne() != null;
    }

    @Override
    public boolean existsByUsername(String username) {
        return queryFactory
                .selectFrom(user)
                .where(user.username.eq(username))
                .fetchOne() != null;
    }

    @Override
    public void updateCreatedBy(String createdBy, Long id) {
        queryFactory
                .update(user)
                .where(
                        user.id.eq(id),
                        user.deletedAt.isNull(),
                        user.deletedBy.isNull()
                )
                .set(user.createdBy, createdBy)
                .execute();
    }

    @Override
    public Optional<User> findUserByUserId(String username) {
        return Optional.ofNullable(queryFactory
                .selectFrom(user)
                .where(
                        user.username.eq(username),
                        user.deletedAt.isNull(),
                        user.deletedBy.isNull()
                )
                .fetchOne());
    }

}
