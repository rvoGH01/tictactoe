package com.tictactoe.logic;

import com.tictactoe.model.Game;
import org.springframework.stereotype.Component;

@Component
public class CheckIfDBShouldBeUpdatedCmd extends AbstractGameCmd {
    @Override
    public boolean canHandle(Game currentState, Game newState) {
        return true;
    }

    @Override
    public void handle(Game currentState, Game newState) {
        //printStatus(currentState, newState);
        // update state in DB if this cmd has been reached only
        updateDb = true;
    }
}