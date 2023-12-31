package com.vttp.miniproject.Eldrow;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class EldrowApplication implements CommandLineRunner{

	@Autowired
	private StringRedisTemplate redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(EldrowApplication.class, args);
		
	}

	@Override
    public void run(String... args) throws Exception {
        Resource resource = new ClassPathResource("static/word-list.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            redisTemplate.delete("wordList");
            while ((line = reader.readLine()) != null) {
                listOps.rightPush("wordList", line.trim());
            }
        }
    }

}
