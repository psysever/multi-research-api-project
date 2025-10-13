package com.research1.api.domain.jpa.repository;

import com.research1.api.domain.jpa.entity.Admin;
import com.research1.api.domain.jpa.entity.QAdmin;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Admin> searchAdmins(String keyword, int offset, int limit) {
        QAdmin admin = QAdmin.admin;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(admin.delFl.eq(0));

        if (keyword != null && !keyword.trim().isEmpty()) {
            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            builder.and(
                    admin.username.lower().like(likeKeyword)
                            .or(admin.name.lower().like(likeKeyword))
            );
        }

        return queryFactory.selectFrom(admin)
                .where(builder)
                .orderBy(admin.createYmd.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public long countAdmins(String keyword) {
        QAdmin admin = QAdmin.admin;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(admin.delFl.eq(0));

        if (keyword != null && !keyword.trim().isEmpty()) {
            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            builder.and(
                    admin.username.lower().like(likeKeyword)
                            .or(admin.name.lower().like(likeKeyword))
            );
        }

        return queryFactory.select(admin.count())
                .from(admin)
                .where(builder)
                .fetchOne();
    }
}
