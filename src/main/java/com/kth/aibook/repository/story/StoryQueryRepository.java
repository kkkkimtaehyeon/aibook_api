package com.kth.aibook.repository.story;

import com.kth.aibook.dto.story.StorySimpleResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kth.aibook.entity.member.QMember.member;
import static com.kth.aibook.entity.story.QStory.story;

@RequiredArgsConstructor
@Repository
public class StoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<StorySimpleResponseDto> findStoryPages(Pageable pageable) {
        List<StorySimpleResponseDto> contentResult = queryFactory
                .select(Projections.fields(
                        StorySimpleResponseDto.class,
                        story.id.as("storyId"),
                        story.title.as("title"),
                        member.id.as("memberId"),
                        member.nickName.as("memberName")
                ))
                .from(story)
                .innerJoin(member).on(member.eq(story.member))
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
}
