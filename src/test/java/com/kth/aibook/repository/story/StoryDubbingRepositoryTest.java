package com.kth.aibook.repository.story;

import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.MemberRole;
import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryDubbing;
import com.kth.aibook.entity.story.StoryPage;
import com.kth.aibook.entity.story.StoryPageDubbing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.yml")
@DataJpaTest
class StoryDubbingRepositoryTest {
    @Autowired
    TestEntityManager em;

    @Autowired
    private StoryDubbingRepository storyDubbingRepository;

    @BeforeEach
    void setUp() {
        Member member = new Member("email", "nickName", LocalDate.now(), LocalDateTime.now(), MemberRole.MEMBER, null);
        Voice voice = new Voice(1L, "name", "audioUrl", member, null, false);
        Story story = new Story(1L, "coverImageUrl", "basStory", "title", LocalDateTime.now(), true, false, member);
        for(long i = 1L; i <= 10L; i++) {
        story.addStoryPage(new StoryPage());
        }
        StoryDubbing storyDubbing = new StoryDubbing(1L, story, voice, member, LocalDateTime.now());
//        StoryPageDubbing storyPageDubbing = new StoryPageDubbing()
        em.persist(member);
        em.persist(voice);
        em.persist(story);
        em.persist(storyDubbing);
    }

    @DisplayName("StoryDubbing 삭제")
    @Test
    void deleteStoryDubbing() {

    }

}