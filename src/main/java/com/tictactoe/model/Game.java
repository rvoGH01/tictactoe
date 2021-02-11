package com.tictactoe.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.StringJoiner;

@Entity
public class Game implements Serializable {

    private final static String EMPTY_BOARD = "---_---_---";

    @Id
    @GeneratedValue(generator="uuidGenerator")
    @GenericGenerator(name="uuidGenerator", strategy="org.hibernate.id.UUIDGenerator")
    @Column(name="ID", updatable=false, nullable=false, length=36)
    private String id;

    @Column(name="BOARD", nullable=false, length=13)
    private String board;

    @Column(name="STATUS", nullable=false)
    private GameStatus status;

    public Game() {
        this.board = EMPTY_BOARD;
        this.status = GameStatus.NEW;
    }

    public Game(String board) {
        this.board = board;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public boolean isEmptyBoard() {
        return StringUtils.equals(EMPTY_BOARD, board);
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Game.class.getSimpleName() + "[", "]")
                .add(String.format("id=%s", id))
                .add(String.format("board='%s'", board))
                .add(String.format("status='%s'", status))
                .toString();
    }
}