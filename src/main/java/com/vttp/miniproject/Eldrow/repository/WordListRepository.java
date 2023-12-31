package com.vttp.miniproject.Eldrow.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WordListRepository {

    private StringRedisTemplate redisTemplate;

    public WordListRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean containsWord(String word) {
        List<String> allWords = redisTemplate.opsForList().range("wordList", 0, -1);
        return allWords != null && allWords.contains(word.toLowerCase());
    }
}