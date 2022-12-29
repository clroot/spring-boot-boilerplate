package io.clroot.boilerplate.user.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.clroot.boilerplate.common.repository.SearchRepository;
import io.clroot.boilerplate.user.dto.UserSearchDto;
import io.clroot.boilerplate.user.model.QUser;
import io.clroot.boilerplate.user.model.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserSearchRepository
    extends SearchRepository<User, UserSearchDto> {
    private final JPAQueryFactory queryFactory;
    private final QUser qUser = QUser.user;

    @Override
    protected JPAQuery<User> searchQuery(UserSearchDto search) {
        return queryFactory.selectFrom(qUser).where(
            eqId(search.id()),
            containUsername(search.username()),
            containName(search.name()),
            containEmail(search.email()),
            containPhoneNumber(search.phoneNumber())
        );
    }

    @Override
    protected JPAQuery<Long> totalCountQuery(UserSearchDto search) {
        var searchQuery = searchQuery(search);
        return searchQuery.select(qUser.count());
    }

    private BooleanExpression eqId(Long id) {
        return id == null ? null : qUser.id.eq(id);
    }

    private BooleanExpression containUsername(String name) {
        return name == null ? null : qUser.username.contains(name);
    }

    private BooleanExpression containName(String name) {
        return name == null ? null : qUser.name.contains(name);
    }

    private BooleanExpression containEmail(String email) {
        return email == null ? null : qUser.email.contains(email);
    }

    private BooleanExpression containPhoneNumber(String phoneNumber) {
        return phoneNumber == null ? null : qUser.phoneNumber.contains(phoneNumber);
    }
}
