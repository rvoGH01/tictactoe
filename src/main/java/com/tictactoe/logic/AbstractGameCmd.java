package com.tictactoe.logic;

import com.tictactoe.model.Game;

public abstract class AbstractGameCmd {

    protected boolean updateDb = false;

    public abstract boolean canHandle(Game currentState, Game newState);

    public abstract void handle(Game currentState, Game newState);

    public boolean shouldUpdateDb() {
        return updateDb;
    }

    public void printStatus(Game currentState, Game newState) {
        System.out.println(getClass().getSimpleName() + " >> CURRENT=[" + currentState.getStatus() + ", " + currentState.getBoard() + "]; NEW=[" +
                newState.getStatus() + ", " + newState.getBoard() + "]");
    }
}