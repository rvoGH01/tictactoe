package com.tictactoe.rest;

import com.tictactoe.logic.GameProcessingChain;
import com.tictactoe.model.Game;
import com.tictactoe.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/games")
public class GameRestService {

    private final Logger log = LoggerFactory.getLogger(GameRestService.class);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameProcessingChain gameChain;

    // GET /api/v1/games -- Get all games
    @GetMapping
    public ResponseEntity<Iterable<Game>> getGames() {
        try {
            Iterable<Game> games = gameRepository.findAll();
            return ResponseEntity.ok(games);
        } catch (Exception e) {
            log.error("Failed to get games", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/v1/games/{game_id} -- Get a specific game
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        if (!isValidUUID(id)) {
            log.error("Invalid UUID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            return ResponseEntity.ok(game.get());
        }
        log.warn("There is no game with id: {}", id);
        return ResponseEntity.notFound().build();
    }

    // PUT /api/v1/games/{game_id} -- Post a new move to a game
    @PutMapping(value="/{id}", produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Game> move(@PathVariable String id, @RequestBody Game newGameState) {
        if (!isValidUUID(id)) {
            log.error("Invalid UUID: {}", id);
            return ResponseEntity.badRequest().build();
        }

        Optional<Game> currentGameState = gameRepository.findById(id);

        if (!currentGameState.isPresent()) {
            log.warn("There is no game with id: {}", id);
            return ResponseEntity.notFound().build();
        }

        newGameState.setId(id);

        boolean success = gameChain.process(currentGameState.get(), newGameState);

        Game updatedGameState = success ? gameRepository.save(newGameState) : currentGameState.get();

        return ResponseEntity.ok(updatedGameState);
    }

    // POST /api/v1/games -- Start a new game
    @PostMapping
    public ResponseEntity<String> startNewGame() {
        try {
            Game newGame = gameRepository.save(new Game());

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newGame.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            log.error("Failed to start a new game", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // DELETE /api/v1/games/{game_id} -- Deletes a game
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGameById(@PathVariable String id) {
        if (!isValidUUID(id)) {
            log.error("Invalid UUID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        try {
            Optional<Game> game = gameRepository.findById(id);
            if (game.isPresent()) {
                gameRepository.deleteById(id);
                return ResponseEntity.ok("SUCCESS");
            } else {
                log.warn("There is no game with id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Failed to delete a game with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (Exception ignored) {}
        return false;
    }
}