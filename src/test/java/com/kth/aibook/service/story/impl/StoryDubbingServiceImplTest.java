//package com.kth.aibook.service.story.impl;
//
//import com.kth.aibook.common.exception.StoryDubbingException;
//import com.kth.aibook.dto.story.VoiceDubbingResponseDto;
//import com.kth.aibook.entity.member.Member;
//import com.kth.aibook.entity.member.Voice;
//import com.kth.aibook.entity.story.Story;
//import com.kth.aibook.entity.story.StoryDubbing;
//import com.kth.aibook.entity.story.StoryPage;
//import com.kth.aibook.repository.member.MemberRepository;
//import com.kth.aibook.repository.member.VoiceRepository;
//import com.kth.aibook.repository.story.StoryDubbingRepository;
//import com.kth.aibook.repository.story.StoryRepository;
//import com.kth.aibook.service.dubbing.DubbingService;
//import com.kth.aibook.service.story.impl.StoryDubbingServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//@TestPropertySource(locations = "classpath:application-test.yml")
//@ExtendWith(MockitoExtension.class)
//class StoryDubbingServiceImplTest {
//
//    @Mock
//    private StoryRepository storyRepository;
//    @Mock
//    private MemberRepository memberRepository;
//    @Mock
//    private VoiceRepository voiceRepository;
//    @Mock
//    private StoryDubbingRepository storyDubbingRepository;
//    @Mock
//    private DubbingService dubbingService;
//
//    @InjectMocks
//    private StoryDubbingServiceImpl storyDubbingService;
//
//    private Story story;
//    private Member member;
//    private Voice voice;
//    private VoiceDubbingResponseDto responseDto;
//
//    @BeforeEach
//    void setUp() {
//        story = mock(Story.class);
//        member = mock(Member.class);
//        voice = mock(Voice.class);
//        responseDto = mock(VoiceDubbingResponseDto.class);
//    }
//
//    @Test
//    void saveDubbing_V1_success() throws StoryDubbingException {
//        Long storyId = 1L;
//        Long memberId = 2L;
//        Long voiceId = 3L;
//
//        when(storyRepository.findById(storyId)).thenReturn(Optional.of(story));
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(voiceRepository.findById(voiceId)).thenReturn(Optional.of(voice));
//        when(dubbingService.uploadDubbingAudio(anyString())).thenReturn("audioUrl");
//
//        VoiceDubbingResponseDto response = new VoiceDubbingResponseDto();
//    }
//}
