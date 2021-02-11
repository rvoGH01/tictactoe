package com.tictactoe.logic;

import com.tictactoe.model.Game;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ValidateGameMoveCmd extends AbstractGameCmd {
    @Override
    public boolean canHandle(Game currentState, Game newState) {
        if (isFirstMove(currentState, newState)) {
            return isFirstXMove(currentState, newState);
        } else {
            return isOneMove(currentState, newState);
        }
    }

    private boolean isOneMove(Game currentState, Game newState) {
        // check if 'X' move hasn't been made two or more times
        int currentX = StringUtils.countMatches(currentState.getBoard(), 'X');
        int newX = StringUtils.countMatches(newState.getBoard(), 'X');

        // check if 'O' move hasn't been made two or more times
        int currentO = StringUtils.countMatches(currentState.getBoard(), 'O');
        int newO = StringUtils.countMatches(newState.getBoard(), 'O');

        //return newX - currentX == 1 || newO - currentO == 1;
        return newX - currentO < 2 && newO - currentX < 2;
    }

    private boolean isFirstXMove(Game currentState, Game newState) {
        int currentX = StringUtils.countMatches(currentState.getBoard(), 'X');
        int newX = StringUtils.countMatches(newState.getBoard(), 'X');
        return currentX == 0 && newX == 1;
    }

    private boolean isFirstMove(Game currentState, Game newState) {
        int currentX = StringUtils.countMatches(currentState.getBoard(), 'X');
        int currentO = StringUtils.countMatches(currentState.getBoard(), 'O');
        int newX = StringUtils.countMatches(newState.getBoard(), 'X');
        int newO = StringUtils.countMatches(newState.getBoard(), 'O');
        return (currentX == 0 && newX == 1 && currentO == 0 && newO == 0) || (currentO == 0 && newO == 1 && currentX == 0 && newX == 0);
    }

    @Override
    public void handle(Game currentState, Game newState) {
        //printStatus(currentState, newState);
    }
}