package com.vttp.miniproject.Eldrow.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.vttp.miniproject.Eldrow.model.Score;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ScoreRepository {

    private static final String LEADERBOARD_KEY = "leaderboard";
    public static final int PAGE_SIZE = 5;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void save(Score score) {
        Double currentScore = redisTemplate.opsForZSet().score(LEADERBOARD_KEY, score.getPlayerName());
        
        if (currentScore == null || score.getScore() > currentScore) {
            redisTemplate.opsForZSet().add(LEADERBOARD_KEY, score.getPlayerName(), score.getScore());
        }
    }

    public List<Score> findTopScores(int page) {
        int start = (page - 1) * PAGE_SIZE;
        int end = start + PAGE_SIZE - 1;

        Set<Object> playerNames = redisTemplate.opsForZSet().reverseRange(LEADERBOARD_KEY, start, end);

        return playerNames.stream()
                .map(playerName -> {
                    Double score = redisTemplate.opsForZSet().score(LEADERBOARD_KEY, playerName);
                    Score scoreObj = new Score();
                    scoreObj.setPlayerName((String) playerName);
                    scoreObj.setScore(score != null ? score.intValue() : 0);
                    return scoreObj;
                })
                .collect(Collectors.toList());
    }

    public long getTotalCount() {
        return redisTemplate.opsForZSet().size(LEADERBOARD_KEY);
    }

    public int getTotalPages() {
        long totalCount = getTotalCount();
        return (int) Math.ceil((double) totalCount / PAGE_SIZE);
    }
}
