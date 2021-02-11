package com.tictactoe.rest;

import com.tictactoe.model.Game;
import com.tictactoe.model.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameRestServiceTest {

    private RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort
    private int randomServerPort;

    @Test
    public void shouldRequestAllGames() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/tictactoe/api/v1/games";
        ResponseEntity<String> result = restTemplate.getForEntity(new URI(url), String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("[]", result.getBody());
    }

    @Test
    public void shouldNotBePossibleToMakeTwoSimilarMovesAtOnce() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/tictactoe/api/v1/games";

        // 1. start a new game

        ResponseEntity<String> result = restTemplate.postForEntity(new URI(url), null, String.class);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        URI location = result.getHeaders().getLocation();

        assertNotNull(location);

        // 2. get game by ID

        ResponseEntity<Game> gameResult = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, gameResult.getStatusCode());

        Game game = gameResult.getBody();

        assertTrue(game.isEmptyBoard());
        assertEquals(GameStatus.NEW, game.getStatus());

        // 3. make a client's move (X)

        Game firstClientMove = new Game("X--_---_---");

        restTemplate.put(location, firstClientMove);

        ResponseEntity<Game> updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 3. make a client's move (X)

        Game secondClientMove = new Game("XX-_---_---");

        restTemplate.put(location, secondClientMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());
        assertEquals("board shouldn't be updated", firstClientMove.getBoard(), updatedGameStatus.getBody().getBoard());

        // 4. remove a game
        restTemplate.delete(location);
    }

    @Test
    public void shouldNotStartFromOMove() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/tictactoe/api/v1/games";

        // 1. start a new game

        ResponseEntity<String> result = restTemplate.postForEntity(new URI(url), null, String.class);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        URI location = result.getHeaders().getLocation();

        assertNotNull(location);

        // 2. get game by ID

        ResponseEntity<Game> gameResult = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, gameResult.getStatusCode());

        Game game = gameResult.getBody();

        assertTrue(game.isEmptyBoard());
        assertEquals(GameStatus.NEW, game.getStatus());

        // 3. make a client's move (X)

        Game firstClientMove = new Game("O--_---_---");

        restTemplate.put(location, firstClientMove);

        ResponseEntity<Game> updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.NEW, updatedGameStatus.getBody().getStatus());
        assertEquals("board shouldn't be updated", game.getBoard(), updatedGameStatus.getBody().getBoard());

        // 4. remove a game
        restTemplate.delete(location);
    }

    @Test
    public void gameFlowXWinTest() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/tictactoe/api/v1/games";

        // 1. start a new game

        ResponseEntity<String> result = restTemplate.postForEntity(new URI(url), null, String.class);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        URI location = result.getHeaders().getLocation();

        assertNotNull(location);

        // 2. get game by ID

        ResponseEntity<Game> gameResult = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, gameResult.getStatusCode());

        Game game = gameResult.getBody();

        assertTrue(game.isEmptyBoard());
        assertEquals(GameStatus.NEW, game.getStatus());

        // 3. make a client's move (X)

        Game clientMove = new Game("X--_---_---");

        restTemplate.put(location, clientMove);

        ResponseEntity<Game> updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 4. make a computer's move (O)

        Game computerMove = new Game("X--_-O-_---");

        restTemplate.put(location, computerMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 5. make a client's move (X)

        clientMove = new Game("XX-_-O-_---");

        restTemplate.put(location, clientMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 6. make a computer's move (O)

        computerMove = new Game("XX-_-OO_---");

        restTemplate.put(location, computerMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 7. make a client's move (X)

        clientMove = new Game("XXX_-OO_---");

        restTemplate.put(location, clientMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.X_WON, updatedGameStatus.getBody().getStatus());

        // 8. remove a game
        restTemplate.delete(location);
    }

    @Test
    public void gameFlowDrawTest() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/tictactoe/api/v1/games";

        // 1. start a new game

        ResponseEntity<String> result = restTemplate.postForEntity(new URI(url), null, String.class);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        URI location = result.getHeaders().getLocation();

        assertNotNull(location);

        // 2. get game by ID

        ResponseEntity<Game> gameResult = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, gameResult.getStatusCode());

        Game game = gameResult.getBody();

        assertTrue(game.isEmptyBoard());
        assertEquals(GameStatus.NEW, game.getStatus());

        // 3. make a client's move (X)

        Game clientMove = new Game("X--_---_---");

        restTemplate.put(location, clientMove);

        ResponseEntity<Game> updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 4. make a computer's move (O)

        Game computerMove = new Game("X--_-O-_---");

        restTemplate.put(location, computerMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 5. make a client's move (X)

        clientMove = new Game("XX-_-O-_---");

        restTemplate.put(location, clientMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 6. make a computer's move (O)

        computerMove = new Game("XXO_-O-_---");

        restTemplate.put(location, computerMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 7. make a client's move (X)

        clientMove = new Game("XXO_-O-_X--");

        restTemplate.put(location, clientMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 8. make a computer's move (O)

        computerMove = new Game("XXO_OO-_X--");

        restTemplate.put(location, computerMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 9. make a client's move (X)

        clientMove = new Game("XXO_OOX_X--");

        restTemplate.put(location, clientMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 10. make a computer's move (O)

        computerMove = new Game("XXO_OOX_XO-");

        restTemplate.put(location, computerMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.RUNNING, updatedGameStatus.getBody().getStatus());

        // 11. make a client's move (X)

        clientMove = new Game("XXO_OOX_XOX");

        restTemplate.put(location, clientMove);

        updatedGameStatus = restTemplate.getForEntity(location, Game.class);

        assertEquals(HttpStatus.OK, updatedGameStatus.getStatusCode());
        assertEquals(GameStatus.DRAW, updatedGameStatus.getBody().getStatus());

        // 12. remove a game
        restTemplate.delete(location);
    }
}