package com.i3e3.mindlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;
import java.util.UUID;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class MindletApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindletApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}

/**
 * @TODO API 개발 순서 {
 *     1. 프론트엔드 개발자 담당자랑 API 스펙을 협의
 *     2. 컨트롤러에서 데이터를 전달받기 위해 사용하는 DTO 클래스를 먼저 생성
 *     3. 컨트롤러에서 DTO를 전달받는 메서드 선언부만 작성
 *     4. 포스트맨으로 데이터를 전송해서 컨트롤러에서 데이터들을 잘 받는지 체크
 *     5. 컨트롤러 계층 나머지 코드 작성
 *     6. 서비스 계층 작성 (DTO를 사용하면 DTO 클래스 생성)
 *     7. 레파지토리 계층 작성
 *     8. 레파지토리 계층 테스트 코드 작성
 *     9. 서비스 계층 테스트 코드 작성
 *     10. 커밋할 때는 데이터의 흐름대로 커밋을 한다.
 * }
 */