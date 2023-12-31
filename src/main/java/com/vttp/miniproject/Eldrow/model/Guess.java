package com.vttp.miniproject.Eldrow.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Guess {
    @NotBlank(message = "Guess cannot be blank")
    @Size(min = 5, max = 5, message = "Guess must be 5 characters long")
    private String guess;
}
