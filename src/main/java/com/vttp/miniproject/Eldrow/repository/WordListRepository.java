package com.vttp.miniproject.Eldrow.repository;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Repository
public class WordListRepository {

    private StringRedisTemplate redisTemplate;

    public WordListRepository(StringRedisTemplate redisTemplate) throws IOException {
        this.redisTemplate = redisTemplate;
        this.storeWordList();
    }

    public boolean containsWord(String word) {
        List<String> allWords = redisTemplate.opsForList().range("wordList", 0, -1);
        return allWords != null && allWords.contains(word.toLowerCase());
    }

    public void storeWordList() throws IOException {
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