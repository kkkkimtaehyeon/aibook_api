package com.kth.aibook.repository.story;

import com.kth.aibook.dto.story.QStorySimpleResponseDto;
import com.kth.aibook.dto.story.StorySimpleResponseDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<StorySimpleResponseDto> findStoryPages(Pageable pageable, Boolean isPublic) {
        List<StorySimpleResponseDto> contentResult = queryFactory
                .select(Projections.fields(
                        StorySimpleResponseDto.class,
                        story.id.as("storyId"),
                        story.title.as("title"),
                        member.id.as("memberId"),
                        member.nickName.as("memberName"),
                        story.viewCount.as("viewCount")
                ))
                .from(story)
                .innerJoin(member).on(member.eq(story.member))
                .where(eqIsPublic(isPublic))
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
                .select(Projections.fields(
                        StorySimpleResponseDto.class,
                        story.id.as("storyId"),
                        story.title.as("title"),
                        member.id.as("memberId"),
                        member.nickName.as("memberName"),
                        story.viewCount.as("viewCount")
                ))
                .from(story)
                .innerJoin(member).on(member.eq(story.member))
                .where(member.id.eq(memberId))
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
}
