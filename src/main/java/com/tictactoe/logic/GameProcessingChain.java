package com.tictactoe.logic;

import org.springframework.stereotype.Component;

@Component
public class GameProcessingChain extends AbstractGameChain{
    @Override
    protected Class<?>[] getCommandClasses() {
        return new Class<?>[] {
                ValidateGameMoveCmd.class,
                UpdateGameStateCmd.class,
                //ChangeGamePlayerCmd.class,
                CheckIfDBShouldBeUpdatedCmd.class

        };
    }
}