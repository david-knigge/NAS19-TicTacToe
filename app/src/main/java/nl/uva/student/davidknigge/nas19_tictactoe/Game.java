package nl.uva.student.davidknigge.nas19_tictactoe;

import android.service.quicksettings.Tile;
import android.util.Log;

import java.util.Random;
import java.io.Serializable;

public class Game implements Serializable{

    final private int BOARD_SIZE = 3;
    private TileState[][] board;

    private Boolean playerOneTurn;
    private int movesPlayed;
    private Boolean gameOver;

    private static Mode defaultMode = Mode.MULTI_PLAYER;
    private Mode mode;

    public Game(Mode gameMode) {
        board = new TileState[BOARD_SIZE][BOARD_SIZE];
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                board[i][j] = TileState.BLANK;
        movesPlayed = 0;
        playerOneTurn = true;
        gameOver = false;
        mode = gameMode;
    }

    public Game() {
        this(defaultMode);
    }

    public TileState choose(int row, int column) {
        TileState tile = TileState.INVALID;
        if (!gameOver && board[row][column] == TileState.BLANK) {
            if (playerOneTurn) {
                tile = TileState.CIRCLE;
            } else {
                tile = TileState.CROSS;
            }
            board[row][column] = tile;
            movesPlayed += 1;
            playerOneTurn = !playerOneTurn;
        }
        return tile;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public TileState getTileAt(int row, int column) {
        return board[row][column];
    }

    public GameState getTurn() {
        if (gameOver) return GameState.DRAW;
        if (playerOneTurn) {
            return GameState.PLAYER_ONE_TURN;
        } else {
            return GameState.PLAYER_TWO_TURN;
        }
    }

    public GameState getGameState() {
        for (int row=0; row<BOARD_SIZE; row++) {
            gameOver = gameOver | (board[row][0] == board[row][1] && board[row][0] == board[row][2] && board[row][0] != TileState.BLANK);
        }
        for (int col=0; col<BOARD_SIZE; col++) {
            gameOver = gameOver | (board[0][col] == board[1][col] && board[0][col] == board[2][col] && board[0][col] != TileState.BLANK);
        }
        gameOver = gameOver | (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != TileState.BLANK);
        gameOver = gameOver | (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != TileState.BLANK);

        if (gameOver) {
            if (playerOneTurn) {
                return GameState.PLAYER_TWO;
            } else {
                return GameState.PLAYER_ONE;
            }
        } else if(movesPlayed >= (BOARD_SIZE*BOARD_SIZE)) {
            gameOver = true;
            return GameState.DRAW;
        } else {
            return GameState.IN_PROGRESS;
        }
    }

    public int getComputerMove() {
        Random rand = new Random();
        int tileIndex, row, col;
        TileState tile = TileState.INVALID;
        tileIndex = rand.nextInt((BOARD_SIZE * BOARD_SIZE));

        while (tile != TileState.BLANK) {
            tileIndex = rand.nextInt((BOARD_SIZE * BOARD_SIZE));
            row = tileIndex / BOARD_SIZE;
            col = tileIndex % BOARD_SIZE;
            tile = getTileAt(row, col);
        }
        return tileIndex;
    }

    public boolean gameOver() {
        return gameOver;
    }

    public Mode getMode() {
        return mode;
    }
}
