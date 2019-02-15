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

    /*
     * Initialize board and gamemode. Set turn to player one.
     */
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

    /*
     * If game is constructed without gamemode, set defaultMode as gamemode.
     */
    public Game() {
        this(defaultMode);
    }

    /*
     * Checks if given index is blank, if not, set value of index according to players turn.
     * Returns invalid if tile is occupied, the set tile value otherwise.
     */
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

    /*
     * Get the size of the board.
     */
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /*
     * Get tile at given index.
     */
    public TileState getTileAt(int row, int column) {
        return board[row][column];
    }

    /*
     * Get player whose turn it is.
     */
    public GameState getTurn() {
        if (gameOver) return GameState.DRAW;
        if (playerOneTurn) {
            return GameState.PLAYER_ONE_TURN;
        } else {
            return GameState.PLAYER_TWO_TURN;
        }
    }

    /*
     * Get state of the game, checks if any player has won, or if game is a draw. If the game is won
     * return GameState of winning player, return GameState.draw if draw and GameState.IN_PROGRESS
     * if game is still in progress.
     */
    public GameState getGameState() {
        for (int row=0; row<BOARD_SIZE; row++) {
            gameOver = gameOver || (board[row][0] == board[row][1] && board[row][0] == board[row][2] && board[row][0] != TileState.BLANK);
        }
        for (int col=0; col<BOARD_SIZE; col++) {
            gameOver = gameOver || (board[0][col] == board[1][col] && board[0][col] == board[2][col] && board[0][col] != TileState.BLANK);
        }
        gameOver = gameOver || (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != TileState.BLANK);
        gameOver = gameOver || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != TileState.BLANK);

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

    /*
     * Gets the index of a random vacant tile.
     */
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

    /*
     * Returns whether game is over.
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /*
     * Return gamemode.
     */
    public Mode getMode() {
        return mode;
    }
}
