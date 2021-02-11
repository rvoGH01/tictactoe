package com.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "com.tictactoe.*" })
@EntityScan("com.tictactoe.*")
@SpringBootApplication
public class TicTacToeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicTacToeApplication.class, args);
    }
}