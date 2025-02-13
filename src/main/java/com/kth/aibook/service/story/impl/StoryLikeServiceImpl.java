package com.kth.aibook.service.story.impl;

import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryLike;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.exception.story.StoryNotFoundException;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.story.StoryLikeRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.story.StoryLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoryLikeServiceImpl implements StoryLikeService {
    private final StoryRepository storyRepository;
    private final MemberRepository memberRepository;
    private final StoryLikeRepository storyLikeRepository;

    @Override
    public void likeStory(Long memberId, Long storyId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Story story = storyRepository
                .findById(storyId)
                .orElseThrow(() -> new StoryNotFoundException("존재하지 않는 동화입니다."));

        // 이미 좋아요 눌렀으면 취소
        if (isAlreadyLiked(memberId, storyId)) {
            storyLikeRepository.deleteByMemberIdAndStoryId(memberId, storyId);
            return;
        }
        storyLikeRepository.save(new StoryLike(member, story));
    }

    private boolean isAlreadyLiked(Long memberId, Long storyId) {
        return storyLikeRepository.existsByMemberIdAndStoryId(memberId, storyId);
    }
}
