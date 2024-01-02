package com.vttp.miniproject.Eldrow.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vttp.miniproject.Eldrow.model.Game;
import com.vttp.miniproject.Eldrow.model.Game.GuessFeedback;
import com.vttp.miniproject.Eldrow.repository.ScoreRepository;
import com.vttp.miniproject.Eldrow.model.Guess;
import com.vttp.miniproject.Eldrow.model.GuessResult;
import com.vttp.miniproject.Eldrow.model.Score;
import com.vttp.miniproject.Eldrow.service.GameService;
import com.vttp.miniproject.Eldrow.service.WordValidationService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/game")
public class GameController {

    private GameService gameService;
    private WordValidationService wordValidationService;
    private ScoreRepository scoreRepository;

    @Autowired
    public GameController(GameService gameService, WordValidationService wordValidationService, ScoreRepository scoreRepository) {
        this.gameService = gameService;
        this.wordValidationService = wordValidationService;
        this.scoreRepository = scoreRepository;
    }

    @GetMapping
    public String game(Model model) {
        Game currentGame = gameService.getCurrentGame();
        model.addAttribute("game", currentGame);
        model.addAttribute("guess", new Guess());
        model.addAttribute("score", currentGame.getScore());
        return "game";
    }

    @PostMapping("/makeGuess")
    public String makeGuess(@Valid Guess guess, BindingResult bindingResult, Model model) {
        Game currentGame = gameService.getCurrentGame();
        List<GuessFeedback> reversedPreviousGuesses = new ArrayList<>(currentGame.getPreviousGuesses());
        Collections.reverse(reversedPreviousGuesses);
        Integer score = currentGame.getScore();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("game", currentGame);
            model.addAttribute("guess", new Guess());
            model.addAttribute("score", score);
            model.addAttribute("reversedPreviousGuesses", reversedPreviousGuesses);
            return "game";
        }

        if (!wordValidationService.isValidWord(guess.getGuess())) {
            model.addAttribute("invalidWord", true);
            model.addAttribute("game", currentGame);
            model.addAttribute("guess", new Guess());
            model.addAttribute("reversedPreviousGuesses", reversedPreviousGuesses);
            model.addAttribute("score", score);
            return "game";
        } 

        boolean guessCorrect = false;
        GuessResult result = gameService.handleGuess(guess.getGuess());
        boolean gameOver = gameService.isGameOver();
        guessCorrect = result.isCorrect();  
        String feedback = result.getFeedback();
        
        
        if (gameService.isGameOver()) {
            model.addAttribute("gameOver", true);
            model.addAttribute("guessCorrect", guessCorrect);
            model.addAttribute("game", currentGame);
            model.addAttribute("guess", new Guess());
            model.addAttribute("reversedPreviousGuesses", reversedPreviousGuesses);
            model.addAttribute("score", score);
            model.addAttribute("targetWord", currentGame.getTargetWord());
            return "game";
         }                  

        model.addAttribute("game", currentGame);
        model.addAttribute("score", gameService.getCurrentGame().getScore());
        model.addAttribute("feedback", feedback);
        model.addAttribute("gameOver", gameOver);
        model.addAttribute("guessCorrect", guessCorrect);
        model.addAttribute("guess", new Guess());
        model.addAttribute("invalidWord", false);
        model.addAttribute("reversedPreviousGuesses", reversedPreviousGuesses);

        // TARGET WORD, USED FOR TESTING
        model.addAttribute("targetWord", currentGame.getTargetWord());
        
        return "game";
    }

    @PostMapping("/startNewGame")
    public String startNewGame(Model model) {
        gameService.initializeGame();
        model.addAttribute("game", gameService.getCurrentGame());
        model.addAttribute("guess", new Guess());
        return "redirect:/game";
    }

    @PostMapping("/saveScore")
    public String saveScore(@RequestParam String playerName, @RequestParam(required = false) Integer score, RedirectAttributes redirectAttributes) {
        Score newScore = new Score();
        newScore.setPlayerName(playerName);
        if (score == null) {
            newScore.setScore(0);
        } else {
            newScore.setScore(score);
        }
        scoreRepository.save(newScore);

        return "redirect:/";
    }
}
