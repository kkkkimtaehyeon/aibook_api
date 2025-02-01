package com.kth.aibook.service.story.impl;

import com.kth.aibook.dto.story.BaseStoryCreateRequestDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createBaseStory(long memberId, BaseStoryCreateRequestDto createRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Story tempStory = Story.builder()
                .baseStory(createRequest.baseStory())
                .member(member)
                .build();
        Story savedStory = storyRepository.save(tempStory);
        return savedStory.getId();
    }
}
