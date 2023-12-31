package com.vttp.miniproject.Eldrow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vttp.miniproject.Eldrow.model.Guess;
import com.vttp.miniproject.Eldrow.service.GameService;

@Controller
@RequestMapping({"/", "index.html"})
public class IndexController {

     private GameService gameService;

     @Autowired
     public IndexController(GameService gameService) {
        this.gameService = gameService;
     }

    @GetMapping("/startNewGame")
    public String startNewGame(Model model) {
        gameService.initializeGame();
        model.addAttribute("game", gameService.getCurrentGame());
        model.addAttribute("guess", new Guess());
        return "redirect:/game";
    }
}
