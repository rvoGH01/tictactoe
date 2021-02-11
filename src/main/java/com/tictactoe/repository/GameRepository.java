package com.tictactoe.repository;

import com.tictactoe.model.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, String> {
    //Game findById(String id);
}
