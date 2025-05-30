//package com.kth.aibook.repository.story;
//
//import com.kth.aibook.dto.story.StoryCreateRequestDto;
//import com.kth.aibook.entity.member.Member;
//import com.kth.aibook.entity.member.MemberRole;
//import com.kth.aibook.entity.story.Story;
//import com.kth.aibook.entity.story.StoryPage;
//import com.kth.aibook.entity.story.StoryTag;
//import com.kth.aibook.entity.story.Tag;
//import com.kth.aibook.repository.member.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest(properties = "classpath:application-test.yml")
//class StoryRepositoryTest {
//
//    @Autowired
//    private StoryRepository storyRepository;
//    @Autowired
//    private TagRepository tagRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//    private Member member;
//    private List<Tag> tagList = new ArrayList<>();
//
//
//
//    @BeforeEach
//    void setUp() {
//        member = memberRepository.save(new Member("email", "testMember", LocalDate.now(), LocalDateTime.now(), MemberRole.MEMBER, null));
//        tagList.add(tagRepository.save(new Tag("액션")));
//        tagList.add(tagRepository.save(new Tag("로맨스")));
//
//    }
//
//    @DisplayName("동화 저장")
//    @Test
//    void saveStory() {
//        String[] selectedContents = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
//        long[] tagIds = new long[]{1L, 2L};
//        StoryCreateRequestDto createRequestDto = new StoryCreateRequestDto("title", "baseStory", selectedContents, true, tagIds);
//        LocalDateTime createdAt = LocalDateTime.now();
//        Story story = createRequestDto.toEntity(member, createdAt);
//
//        for (int pageNumber = 0; pageNumber < selectedContents.length; pageNumber++) {
//            String content = selectedContents[pageNumber];
//            story.addStoryPage(StoryPage.builder().pageNumber(pageNumber).content(content).build());
//        }
//
//        for (Tag tag : tagList) {
//            story.addStoryTag(new StoryTag(tag));
//        }
//
//        Story savedStory = storyRepository.save(story);
//
//        assertThat(savedStory).isNotNull();
//        assertThat(savedStory.getCreatedAt()).isEqualTo(createdAt);
//        assertThat(savedStory.getStoryPages().size()).isEqualTo(selectedContents.length);
//        assertThat(savedStory.getStoryTags().size()).isEqualTo(tagList.size());
//
//
//    }
//}