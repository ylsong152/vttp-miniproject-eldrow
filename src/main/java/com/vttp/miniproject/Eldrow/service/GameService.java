package com.vttp.miniproject.Eldrow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vttp.miniproject.Eldrow.model.Game;
import com.vttp.miniproject.Eldrow.model.GuessResult;

@Service
public class GameService {
    
    private final APIService apiService;
    private Game currentGame;
    private Map<Character, List<Integer>> targetWordMap;

    @Autowired
    public GameService(APIService apiService) {
        this.apiService = apiService;
        initializeGame();
    }

    public void initializeGame() {
        currentGame = new Game();
        currentGame.setTargetWord(apiService.getWordFromAPI());
        currentGame.setAttemptsRemaining(12);
        currentGame.setScore(0);
        resetTargetWordMap();
    }

    public void setNewTargetWord() {
        currentGame.setTargetWord(apiService.getWordFromAPI());
    }

    public GuessResult handleGuess(String guess) {
        if (currentGame.getAttemptsRemaining() > 0) {
            currentGame.setAttemptsRemaining(currentGame.getAttemptsRemaining() - 1);
        }
    
        String feedback = checkGuess(guess);
        boolean isCorrect = feedback.equals("GGGGG");

        if (isCorrect) {
            currentGame.setLastCorrectWord(currentGame.getTargetWord());
            incrementScore();
        }
    
        Game.GuessFeedback guessFeedback = new Game.GuessFeedback();
        guessFeedback.setGuess(guess);
        guessFeedback.setFeedback(feedback);
        guessFeedback.setCorrect(isCorrect);
        currentGame.getPreviousGuesses().add(guessFeedback);
    
        if (isGameOver()) {
        } else if (isCorrect) {
            setNewTargetWord();
            resetTargetWordMap();
        }
    
        return new GuessResult(feedback, isCorrect);
    }
    

    // Main game logic
    public String checkGuess(String guess) {
        StringBuilder feedback = new StringBuilder(guess.length());
        Map<Character, Integer> charFrequency = new HashMap<>();

        // First, count the frequency of each character in the target word
        for (char c : currentGame.getTargetWord().toCharArray()) {
            charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }

        // First pass: Check for correct position (Green)
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == currentGame.getTargetWord().charAt(i)) {
                feedback.append('G');
                charFrequency.put(guess.charAt(i), charFrequency.get(guess.charAt(i)) - 1);
            } else {
                feedback.append('_');
            }
        }

        // Second pass: Check for wrong position (Yellow & Grey)
        for (int i = 0; i < guess.length(); i++) {
            if (feedback.charAt(i) == '_') {
                char guessChar = guess.charAt(i);
                if (charFrequency.getOrDefault(guessChar, 0) > 0) {
                    feedback.setCharAt(i, 'Y');
                    charFrequency.put(guessChar, charFrequency.get(guessChar) - 1);
                } else {
                    feedback.setCharAt(i, 'X');
                }
            }
        }

        return feedback.toString();
    }


    public boolean isGameOver() {
        return currentGame.getAttemptsRemaining() < 1;
    }

    public boolean isGuessCorrect(String guess) {
        return checkGuess(guess).equals("GGGGG");
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void incrementScore() {
        currentGame.setScore(currentGame.getScore() + 1);
    }

    private void resetTargetWordMap() {
        this.targetWordMap = new HashMap<>();
        for (int i = 0; i < currentGame.getTargetWord().length(); i++) {
            char c = currentGame.getTargetWord().charAt(i);
            targetWordMap.putIfAbsent(c, new ArrayList<>());
            targetWordMap.get(c).add(i);
        }
    }
}
