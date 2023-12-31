package com.vttp.miniproject.Eldrow.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vttp.miniproject.Eldrow.repository.ScoreRepository;

@Controller
public class LeaderboardController {

    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping("/leaderboard")
    public String leaderboard(@RequestParam Map<String, String> allParams, Model model) {
        if (allParams.isEmpty()) {
            return "redirect:/leaderboard?page=1";
        }

        int page = Integer.parseInt(allParams.getOrDefault("page", "1"));
        model.addAttribute("scores", scoreRepository.findTopScores(page));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", scoreRepository.getTotalPages());
        model.addAttribute("pageSize", ScoreRepository.PAGE_SIZE);
        return "leaderboard";
    }
}

