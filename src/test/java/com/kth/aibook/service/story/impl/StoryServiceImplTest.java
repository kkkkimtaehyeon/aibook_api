package com.kth.aibook.service.story.impl;

import com.kth.aibook.dto.story.StoryCreateRequestDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.MemberRole;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryPage;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.story.StoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class StoryServiceImplTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StoryRepository storyRepository;

    private StoryServiceImpl storyService;
    private Long memberId;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder()
                .email("email")
                .role(MemberRole.MEMBER)
                .nickName("name")
                .createdAt(LocalDateTime.now())
                .birthDate(LocalDate.now())
                .build());
        memberId = member.getId();
        storyService = new StoryServiceImpl(storyRepository, memberRepository);
    }

    @Transactional
    @DisplayName("story 저장")
    @Test
    void createStory() {
        // given
        String id = "testId";
        String title = "testTitle";
        String baseStory = "baseStory";
        String[] sentences = new String[10];
        for(int i = 0; i < 10; i++) {
            sentences[i] = "sentence" + i;
        }

        // when
        Long storyId = storyService.createStory(memberId, new StoryCreateRequestDto(title,baseStory,sentences));

        // given
        Optional<Story> optionalStory = storyRepository.findById(storyId);
        assertThat(optionalStory).isPresent();
        Story story = optionalStory.get();
        assertThat(story.getBaseStory()).isEqualTo(baseStory);
        assertThat(story.getTitle()).isEqualTo(title);
        List<StoryPage> pages = story.getStoryPages();
        assertThat(pages).isNotEmpty();
        for(int i = 0; i < 10; i++) {
            assertThat(pages.get(i).getContent()).isEqualTo(sentences[i]);
        }
    }

    @Test
    void removeStory() {
    }

    @Test
    void patchStory() {
    }


}