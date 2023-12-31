package com.vttp.miniproject.Eldrow.service;

import org.springframework.stereotype.Service;

import com.vttp.miniproject.Eldrow.repository.WordListRepository;

@Service
public class WordValidationService {

    private WordListRepository wordListRepository;

    public WordValidationService(WordListRepository wordListRepository) {
        this.wordListRepository = wordListRepository;
    }

    public boolean isValidWord(String word) {
        return wordListRepository.containsWord(word);
    }
}

