package com.vttp.miniproject.Eldrow.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Game {
    private String targetWord;
    private String lastCorrectWord;
    private int attemptsRemaining;
    private int score;
    private List<GuessFeedback> previousGuesses;

    public Game() {
        this.previousGuesses = new ArrayList<>();
    }

    @Data
    public static class GuessFeedback {
        private String guess;
        private String feedback;
        private boolean isCorrect;
    }
}
