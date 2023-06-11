package com.jkc.awssecretsmanager;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AwsSecretsManagerApplicationTests {

	@Test
	void contextLoads() {
		postConstruct();
	}

	@PostConstruct
	private void postConstruct() {
		System.out.println();
	}
}
