package com.tictactoe.logic;

import com.tictactoe.model.Game;
import com.tictactoe.model.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class UpdateGameStateCmd extends AbstractGameCmd {
    @Override
    public boolean canHandle(Game currentState, Game newState) {
        return true;
    }

    @Override
    public void handle(Game currentState, Game newState) {
        //printStatus(currentState, newState);

        if (checkForWin(newState, 'X')) {
            newState.setStatus(GameStatus.X_WON);
        } else if (checkForWin(newState, 'O')) {
            newState.setStatus(GameStatus.O_WON);
        } else if (isFullBoard(newState)) {
            newState.setStatus(GameStatus.DRAW);
        } else {
            newState.setStatus(GameStatus.RUNNING);
        }
    }

    private boolean isFullBoard(Game newState) {
        return !newState.getBoard().contains("-");
    }

    private boolean checkForWin(Game newState, char mark) {
        return checkRowsForWin(newState, mark) || checkColumnsForWin(newState, mark) || checkDiagonalsForWin(newState, mark);
    }

    private boolean checkRowsForWin(Game newState, char mark) {
        String[] rows = newState.getBoard().split("_");
        return checkRowCol(rows[0].charAt(0), rows[0].charAt(1), rows[0].charAt(2), mark) ||
                checkRowCol(rows[1].charAt(0), rows[1].charAt(1), rows[1].charAt(2), mark) ||
                checkRowCol(rows[2].charAt(0), rows[2].charAt(1), rows[2].charAt(2), mark);
    }

    private boolean checkColumnsForWin(Game newState, char mark) {
        String[] rows = newState.getBoard().split("_");
        return checkRowCol(rows[0].charAt(0), rows[1].charAt(0), rows[2].charAt(0), mark) ||
                checkRowCol(rows[0].charAt(1), rows[1].charAt(1), rows[2].charAt(1), mark) ||
                checkRowCol(rows[0].charAt(2), rows[1].charAt(2), rows[2].charAt(2), mark);
    }

    private boolean checkDiagonalsForWin(Game newState, char mark) {
        String[] rows = newState.getBoard().split("_");
        return checkRowCol(rows[0].charAt(0), rows[1].charAt(1), rows[2].charAt(2), mark) ||
                checkRowCol(rows[0].charAt(2), rows[1].charAt(1), rows[2].charAt(0), mark);
    }

    private boolean checkRowCol(char c1, char c2, char c3, char mark) {
        return ((c1 == mark) && (c1 == c2) && (c2 == c3));
    }
}