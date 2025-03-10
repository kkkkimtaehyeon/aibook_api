package com.kth.aibook.repository.story;

import com.kth.aibook.dto.story.QStorySimpleResponseDto;
import com.kth.aibook.dto.story.StorySearchRequestDto;
import com.kth.aibook.dto.story.StorySimpleResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kth.aibook.entity.member.QMember.member;
import static com.kth.aibook.entity.story.QStory.story;
import static com.kth.aibook.entity.story.QStoryLike.storyLike;

@RequiredArgsConstructor
@Repository
public class StoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<StorySimpleResponseDto> findMostViewedStories(int size) {
        return queryFactory
                .select(new QStorySimpleResponseDto(
                        story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount,
                        storyLike.count()
                ))
                .from(story)
                .innerJoin(member).on(member.eq(story.member))
                .leftJoin(storyLike).on(storyLike.story.eq(story))
                .limit(size)
                .fetch();
    }

    //    좋아요 많은 동화 조회
//    select s.story_id, s.title, m.member_id, m.nick_name, count(story_like_id) as likeCount
//    from story s
//    inner join aibook.member m on s.member_id = m.member_id
//    left join story_like on s.story_id = story_like.story_id
//    group by s.story_id, s.title, m.member_id, m.nick_name
//    order by count(story_like_id) desc
//    limit 5;
    public List<StorySimpleResponseDto> findMostLikedStories(int size) {
        return queryFactory
                .select(new QStorySimpleResponseDto(
                        story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount,
                        storyLike.count()
                ))
                .from(story)
                .innerJoin(member).on(member.eq(story.member))
                .leftJoin(storyLike).on(storyLike.story.eq(story))
                .orderBy(story.viewCount.desc())
                .groupBy(story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount)
                .limit(size)
                .fetch();
    }

    public Page<StorySimpleResponseDto> findStoryPages(Pageable pageable, StorySearchRequestDto searchRequest, Boolean isPublic) {
        List<StorySimpleResponseDto> contentResult = queryFactory
                .select(new QStorySimpleResponseDto(
                        story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount,
                        storyLike.count()
                ))
                .from(story)
                .innerJoin(member).on(member.eq(story.member))
                .leftJoin(storyLike).on(storyLike.story.eq(story))
                .where(
                        eqIsPublic(isPublic),
                        likeSearchKeyword(searchRequest)
                )
                .groupBy(story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount)
                .orderBy(getDynamicOrder(searchRequest))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long countResult = queryFactory
                .select(story.count())
                .from(story)
                .fetchOne();
        long total = countResult != null ? countResult : 0;

        return new PageImpl<>(contentResult, pageable, total);
    }

    public Page<StorySimpleResponseDto> findMyStoryPages(Long memberId, Pageable pageable) {
        List<StorySimpleResponseDto> contentResult = queryFactory
                .select(new QStorySimpleResponseDto(
                        story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount,
                        storyLike.count()
                ))
                .from(story)
                .innerJoin(member).on(member.eq(story.member))
                .leftJoin(storyLike).on(storyLike.story.eq(story))
                .where(member.id.eq(memberId))
                .groupBy(story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount)
                .orderBy(story.createdAt.desc()) // 기본 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long countResult = queryFactory
                .select(story.count())
                .from(story)
                .fetchOne();
        long total = countResult != null ? countResult : 0;

        return new PageImpl<>(contentResult, pageable, total);
    }

    private BooleanExpression eqIsPublic(Boolean isPublic) {
        if (isPublic == null || isPublic) {
            return story.isPublic.eq(true);
        }
        return story.isPublic.eq(false);
    }

    private OrderSpecifier<?> getDynamicOrder(StorySearchRequestDto searchRequest) {
        String sortBy = searchRequest.sortBy();
        String sortDir = searchRequest.sortDir();
        if (sortBy == null || sortDir == null) {
            return new OrderSpecifier<>(Order.DESC, story.createdAt);
        }
        Order orderDirection = getDirection(sortDir);

        return switch (sortBy) {
            case "createdAt" -> new OrderSpecifier<>(orderDirection, story.createdAt);
            case "viewCount" -> new OrderSpecifier<>(orderDirection, story.viewCount);
            case "likeCount" -> new OrderSpecifier<>(orderDirection, storyLike.count());
            default -> new OrderSpecifier<>(Order.DESC, story.createdAt);
        };
    }

    private Order getDirection(String sortOrder) {
        if (sortOrder.equals("desc")) {
            return Order.DESC;
        }
        return Order.ASC;
    }

    private BooleanExpression likeSearchKeyword(StorySearchRequestDto searchRequest) {
        BooleanExpression result = null;
        String target = searchRequest.searchTarget();
        String keyword = searchRequest.searchKey();

        if (target == null || keyword == null) {
            return null;
        }
        if (target.equals("title")) {
            result = story.title.like("%" + keyword + "%");
        } else if (target.equals("author")) {
            result = member.nickName.like("%" + keyword + "%");
        }
        return result;
    }
}
