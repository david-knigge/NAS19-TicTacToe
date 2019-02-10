package nl.uva.student.davidknigge.nas19_tictactoe;

import android.util.Log;

public class Game {

    final private int BOARD_SIZE = 3;
    private TileState[][] board;

    private Boolean playerOneTurn;  // true if player 1's turn, false if player 2's turn
    private int movesPlayed;
    private Boolean gameOver;

    public Game() {
        board = new TileState[BOARD_SIZE][BOARD_SIZE];
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                board[i][j] = TileState.BLANK;
        movesPlayed = 0;
        playerOneTurn = true;
        gameOver = false;
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
}
