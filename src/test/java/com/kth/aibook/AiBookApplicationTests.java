package com.kth.aibook;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.yml")
class AiBookApplicationTests {

	@Test
	void contextLoads() {
	}

}
