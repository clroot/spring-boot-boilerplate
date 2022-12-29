package io.clroot.boilerplate.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.clroot.boilerplate.common.dto.SearchDto;
import io.clroot.boilerplate.common.model.BaseEntity;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQuery;

public abstract class SearchRepository<T extends BaseEntity, S extends SearchDto> {
    public Optional<T> searchOne(S search) throws NonUniqueResultException {
        var query = searchQuery(search);
        return Optional.ofNullable(query.fetchOne());
    }

    public List<T> search(S search) {
        var query = searchQuery(search);
        return query.fetch();
    }

    public Page<T> search(S search, Pageable pageable) {
        JPAQuery<T> query = applyPagination(searchQuery(search), pageable);
        List<T> queryResult = query.fetch();

        JPAQuery<Long> countQuery = totalCountQuery(search);
        long totalCount = Optional.ofNullable(countQuery.fetchOne()).orElse(0L);

        return new PageImpl<>(queryResult, pageable, totalCount);
    }

    private JPAQuery<T> applyPagination(JPAQuery<T> query, Pageable pageable) {
        return pageable == null ? query : query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());
    }

    protected abstract JPAQuery<T> searchQuery(S search);

    protected abstract JPAQuery<Long> totalCountQuery(S search);
}
