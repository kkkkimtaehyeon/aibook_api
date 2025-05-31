package com.kth.aibook.repository.story;

import com.kth.aibook.dto.story.*;
import com.kth.aibook.dto.story.tag.QTagDto;
import com.kth.aibook.dto.story.tag.TagDto;
import com.kth.aibook.entity.story.Story;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kth.aibook.entity.member.QMember.member;
import static com.kth.aibook.entity.member.QVoice.voice;
import static com.kth.aibook.entity.story.QStory.story;
import static com.kth.aibook.entity.story.QStoryDubbing.storyDubbing;
import static com.kth.aibook.entity.story.QStoryLike.storyLike;
import static com.kth.aibook.entity.story.QStoryTag.storyTag;
import static com.kth.aibook.entity.story.QTag.tag;

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
                        storyLike.count(),
                        story.coverImageUrl
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
                        storyLike.count(),
                        story.coverImageUrl
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
    // NOTE: 원래 메서드(v1) hibernate: 23
//    public Page<StorySimpleResponseDto> findStoryPages(Pageable pageable, StorySearchRequestDto searchRequest, Boolean isPublic, Long memberId) {
//
//        List<StorySimpleResponseDto> storyDtoList = queryFactory
//                .select(new QStorySimpleResponseDto(
//                        story.id,
//                        story.title,
//                        member.id,
//                        member.nickName,
//                        story.viewCount,
//                        storyLike.count()
//                ))
//                .from(story)
//                .innerJoin(story.member, member)
//                .leftJoin(storyLike).on(storyLike.story.eq(story))
//                .leftJoin(storyTag).on(storyTag.story.eq(story))
//                .leftJoin(tag).on(tag.eq(storyTag.tag))
//                .where(
//                        eqIsPublic(isPublic),
//                        likeSearchKeyword(searchRequest),
//                        eqMemberId(memberId),
//                        eqTagId(searchRequest.tagId())
//                )
//                .groupBy(story.id,
//                        story.title,
//                        member.id,
//                        member.nickName,
//                        story.viewCount)
//                .orderBy(getDynamicOrder(searchRequest))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        // 태그 추가
//        for (StorySimpleResponseDto storyDto : storyDtoList) {
//            List<TagDto> tagList = queryFactory
//                    .select(new QTagDto(
//                            tag.id,
//                            tag.name
//                    ))
//                    .from(storyTag)
//                    .innerJoin(tag).on(tag.eq(storyTag.tag))
//                    .where(storyTag.story.id.in(storyDto.getStoryId()))
//                    .fetch();
//            if (!tagList.isEmpty()) {
//                storyDto.setTagList(tagList);
//            }
//        }
//
//        Long countResult = queryFactory
//                .select(story.count())
//                .from(story)
//                .where(
//                        eqIsPublic(isPublic),
//                        likeSearchKeyword(searchRequest)
//                )
//                .fetchOne();
//        long total = countResult != null ? countResult : 0;
//
//        return new PageImpl<>(storyDtoList, pageable, total);
//    }

    // 동화 목록 조회
    // NOTE: ID목록 먼저 조회하고 태그 목록 한방 조회 (v2) hibernate: 5
    public Page<StorySimpleResponseDto> findStoryPages(Pageable pageable, StorySearchRequestDto searchRequest, Boolean isPublic, Long memberId) {
        // 스토리 ID 목록을 먼저 조회

        List<Long> storyIds = queryFactory
                .select(story.id)
                .from(story)
                .innerJoin(story.member, member)
                .leftJoin(storyLike).on(storyLike.story.eq(story))
                .leftJoin(storyTag).on(storyTag.story.eq(story))
                .leftJoin(tag).on(tag.eq(storyTag.tag))
                .where(
                        eqIsPublic(isPublic),
                        likeSearchKeyword(searchRequest),
                        eqMemberId(memberId),
                        eqTagId(searchRequest.tagId())
                )
                .groupBy(story.id)
                .orderBy(getDynamicOrder(searchRequest))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (storyIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // 스토리 기본 정보 조회
        List<StorySimpleResponseDto> storyDtoList = queryFactory
                .select(new QStorySimpleResponseDto(
                        story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount,
                        storyLike.countDistinct(),
                        story.coverImageUrl
                ))
                .from(story)
                .innerJoin(story.member, member)
                .leftJoin(storyLike).on(storyLike.story.eq(story))
                .leftJoin(storyTag).on(storyTag.story.eq(story))
                .leftJoin(tag).on(tag.eq(storyTag.tag))
                .where(story.id.in(storyIds))
                .groupBy(story.id,
                        story.title,
                        member.id,
                        member.nickName,
                        story.viewCount,
                        storyLike)
                .orderBy(getDynamicOrder(searchRequest))
                .fetch();

        // 태그 한 번에 조회
        List<Tuple> tagResults = queryFactory
                .select(storyTag.story.id, new QTagDto(
                        tag.id,
                        tag.name
                ))
                .from(storyTag)
                .innerJoin(tag).on(tag.eq(storyTag.tag))
                .where(storyTag.story.id.in(storyIds))
                .fetch();

        // 태그 정보 매핑
        Map<Long, List<TagDto>> storyTagsMap = new HashMap<>();
        for (Tuple tuple : tagResults) {
            Long storyId = tuple.get(0, Long.class);
            TagDto tagDto = tuple.get(1, TagDto.class);

            storyTagsMap.computeIfAbsent(storyId, k -> new ArrayList<>()).add(tagDto);
        }

        // 태그 리스트 설정
        for (StorySimpleResponseDto storyDto : storyDtoList) {
            List<TagDto> tagList = storyTagsMap.get(storyDto.getStoryId());
            if (tagList != null && !tagList.isEmpty()) {
                storyDto.setTags(tagList);
            }
        }

        // 총 개수 조회 (모든 조건 포함)
        Long countResult = queryFactory
                .select(story.count())
                .from(story)
                .innerJoin(story.member, member)
                .leftJoin(storyLike).on(storyLike.story.eq(story))
                .leftJoin(storyTag).on(storyTag.story.eq(story))
                .leftJoin(tag).on(tag.eq(storyTag.tag))
                .where(
                        eqIsPublic(isPublic),
                        likeSearchKeyword(searchRequest),
                        eqMemberId(memberId),
                        eqTagId(searchRequest.tagId())
                )
                .fetchOne();
        long total = countResult != null ? countResult : 0;

        return new PageImpl<>(storyDtoList, pageable, total);
    }


    // NOTE: transform + groupBy (v3)
//    public Page<StorySimpleResponseDto> findStoryPages(Pageable pageable, StorySearchRequestDto searchRequest, Boolean isPublic, Long memberId) {
//
//        List<StorySimpleResponseDto> storyDtoList = queryFactory
//                .select(new QStorySimpleResponseDto(
//                        story.id,
//                        story.title,
//                        member.id,
//                        member.nickName,
//                        story.viewCount,
//                        storyLike.count()
//                ))
//                .from(story)
//                .innerJoin(story.member, member)
//                .leftJoin(storyLike).on(storyLike.story.eq(story))
//                .leftJoin(storyTag).on(storyTag.story.eq(story))
//                .leftJoin(tag).on(tag.eq(storyTag.tag))
//                .where(
//                        eqIsPublic(isPublic),
//                        likeSearchKeyword(searchRequest),
//                        eqMemberId(memberId),
//                        eqTagId(searchRequest.tagId())
//                )
//                .groupBy(story.id, story.title, member.id, member.nickName, story.viewCount, tag.id, tag.name)
//                .orderBy(getDynamicOrder(searchRequest))
//                .offset(pageable.getOffset()).limit(pageable.getPageSize())
//                .transform(groupBy(
//                        story.id,
//                        story.title,
//                        member.id,
//                        member.nickName,
//                        story.viewCount).list(
//                        Projections.constructor(
//                                StorySimpleResponseDto.class,
//                                story.id,
//                                story.title,
//                                member.id,
//                                member.nickName,
//                                story.viewCount,
//                                storyLike.count(),
//                                list(
//                                        Projections.constructor(TagDto.class,
//                                                tag.id,
//                                                tag.name)
//                                )
//                        ))
//                );
//
//
//        Long countResult = queryFactory
//                .select(story.count())
//                .from(story)
//                .where(
//                        eqIsPublic(isPublic),
//                        likeSearchKeyword(searchRequest),
//                        eqMemberId(memberId),
//                        eqTagId(searchRequest.tagId())
//                )
//                .fetchOne();
//        long total = countResult != null ? countResult : 0;
//
//        return new PageImpl<>(storyDtoList, pageable, total);
//    }
//
//    public Story findLatestStory(Long memberId) {
//        return queryFactory
//                .select(story)
//                .from(story)
//                .innerJoin(member).on(member.eq(story.member))
//                .where(member.id.eq(memberId))
//                .orderBy(story.createdAt.desc())
//                .fetchFirst();
//    }
//
//    public Page<StoryDubbingResponseDto> findStoryDubbings(Long memberId, Pageable pageable) {
//        List<StoryDubbingResponseDto> storyDubbingList = queryFactory
//                .select(new QStoryDubbingResponseDto(
//                                storyDubbing.id,
//                                story.title,
//                                voice.name
//                        )
//                )
//                .from(storyDubbing)
//                .innerJoin(story).on(story.eq(storyDubbing.story))
//                .innerJoin(voice).on(voice.eq(storyDubbing.voice))
//                .innerJoin(member).on(member.eq(storyDubbing.member))
//                .groupBy(storyDubbing.id)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(storyDubbing.dubbedAt.desc()) // 기본 최신순 정렬
//                .where(member.id.eq(memberId))
//                .fetch();
//
//        Long countResult = queryFactory
//                .select(storyDubbing.count())
//                .from(storyDubbing)
//                .innerJoin(story).on(story.eq(storyDubbing.story))
//                .where(storyDubbing.member.id.eq(memberId))
//                .fetchOne();
//
//        return new PageImpl<>(storyDubbingList, pageable, countResult != null ? countResult : 0);
//    }

    public Story findLatestStory(Long memberId) {
        return queryFactory
                .select(story)
                .from(story)
                .innerJoin(member).on(member.eq(story.member))
                .where(member.id.eq(memberId))
                .orderBy(story.createdAt.desc())
                .fetchFirst();
    }

    public Page<StoryDubbingResponseDto> findDubbingStories(Long memberId, Pageable pageable) {
        List<StoryDubbingResponseDto> storyDubbingList = queryFactory
                .select(new QStoryDubbingResponseDto(
                                storyDubbing.id,
                                story.title,
                                story.member.nickName,
                                voice.name,
                                story.coverImageUrl
                        )
                )
                .from(storyDubbing)
                .innerJoin(story).on(story.eq(storyDubbing.story))
                .innerJoin(voice).on(voice.eq(storyDubbing.voice))
                .innerJoin(member).on(member.eq(storyDubbing.member))
                .groupBy(storyDubbing.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(storyDubbing.dubbedAt.desc()) // 기본 최신순 정렬
                .where(member.id.eq(memberId))
                .fetch();

        Long countResult = queryFactory
                .select(storyDubbing.count())
                .from(storyDubbing)
                .innerJoin(story).on(story.eq(storyDubbing.story))
                .where(storyDubbing.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(storyDubbingList, pageable, countResult != null ? countResult : 0);
    }

    private BooleanExpression eqTagId(Long tagId) {
        if (tagId == null) {
            return null;
        }
        return tag.id.eq(tagId);
    }


    private BooleanExpression eqIsPublic(Boolean isPublic) {
        if (isPublic == null) {
            return null;
        }
        if (isPublic) {
            return story.isPublic.eq(true);
        }
        return story.isPublic.eq(false);
    }

    private BooleanExpression eqMemberId(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return member.id.eq(memberId);
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
            case "likeCount" -> new OrderSpecifier<>(orderDirection, storyLike.countDistinct());
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
