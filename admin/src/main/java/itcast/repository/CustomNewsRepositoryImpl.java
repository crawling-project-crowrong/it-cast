package itcast.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import itcast.domain.news.QNews;
import itcast.domain.news.enums.NewsStatus;
import itcast.dto.response.AdminNewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomNewsRepositoryImpl implements CustomNewsRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminNewsResponse> findNewsBYCondition(NewsStatus status, Pageable pageable) {
        QNews news = QNews.news;

        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(news.status.eq(status));
        }

        JPQLQuery<AdminNewsResponse> query = queryFactory
                .select(Projections.constructor(AdminNewsResponse.class,
                        news.id,
                        news.title,
                        news.content,
                        news.originalContent,
                        news.interest,
                        news.publishedAt,
                        news.rating,
                        news.link,
                        news.thumbnail,
                        news.status,
                        news.sendAt
                ))
                .from(news)
                .where(builder)
                .orderBy(news.sendAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<AdminNewsResponse> content = query.fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(news.count())
                .from(news)
                .where(builder);

        return new PageImpl<>(content, pageable, countQuery.fetchOne());
    }
}
