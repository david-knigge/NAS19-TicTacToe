package nl.uva.student.davidknigge.nas19_tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Game game;
    Mode mode;
    Menu optionsMenu;

    /*
     * If bundle is initialized, sets the saved gamestate. Sets tiles appropriately, check if game
     * is over.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Tic Tac Toe");

        if (savedInstanceState != null) {
            game = (Game) savedInstanceState.getSerializable("game");
        } else {
            game = new Game();
        }

        mode = game.getMode();
        setTiles();
        checkGameState();
    }

    /*
     * Save the Game object in Bundle.
     */
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
    }

    /*
     * Set the options menu on this activity.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        optionsMenu = menu;

        setModeIcon();
        return true;
    }

    /*
     * Option buttons listener. If the gamemode button is pressed change the icon and reset the game
     * if reset is clicked, reset the game.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modeButton:
                mode = toggleMode();
                setModeIcon();
            case R.id.resetButton:
                resetClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Tile click listener, obtain row and column number. If tile on coordinates is empty, set it
     * according to which player's turn it is. If Game is in Mode.SINGLE_PLAYER let computer make
     * a move.
     */
    public void tileClicked(View view) {

        Button button = (Button) view;
        int id = ((ViewGroup) view.getParent()).indexOfChild(view);

        int row = id / game.getBoardSize();
        int col = id % game.getBoardSize();

        TileState state = game.choose(row, col);

        switch (state) {
            case CROSS:
                button.setText("X");
                break;
            case CIRCLE:
                button.setText("O");
                break;
            case INVALID:
                return;
        }
        checkGameState();

        if (!game.getGameOver() && mode == Mode.SINGLE_PLAYER && game.getTurn() == GameState.PLAYER_TWO_TURN) {
            computerClick();
            checkGameState();
        }
    }

    /*
     * Simulate computer clicking on tile.
     */
    public void computerClick() {
        int tileIndex = game.getComputerMove();
        ViewGroup gameContainer = findViewById(R.id.game_container);
        View button = gameContainer.getChildAt(tileIndex);
        button.performClick();
    }

    /*
     * Reset the game.
     */
    public void resetClicked() {
        game = new Game(mode);
        setTiles();
        checkGameState();
    }

    /*
     * Loop over board and set tiles according to Gameboard's values.
     */
    public void setTiles() {
        ViewGroup gameContainer = findViewById(R.id.game_container);

        for (int i = 0; i < gameContainer.getChildCount(); i++) {
            int row = i / game.getBoardSize();
            int col = i % game.getBoardSize();

            switch(game.getTileAt(row, col)) {
                case BLANK:
                    ((Button) gameContainer.getChildAt(i)).setText("");
                    break;
                case CROSS:
                    ((Button) gameContainer.getChildAt(i)).setText("X");
                    break;
                case CIRCLE:
                    ((Button) gameContainer.getChildAt(i)).setText("O");
                    break;
                case INVALID:
                    break;
            }
        }
    }

    /*
     * Obtain the GameState, change announcement text accordingly.
     */
    public void checkGameState() {
        GameState state = game.getGameState();
        TextView stateAnnouncement = findViewById(R.id.textViewGameState);

        switch (state) {
            case IN_PROGRESS:
                if (game.getTurn() == GameState.PLAYER_ONE_TURN) {
                    stateAnnouncement.setText("PLAYER ONE'S TURN");
                } else {
                    stateAnnouncement.setText("PLAYER TWO'S TURN");
                }
                break;
            case PLAYER_ONE:
                stateAnnouncement.setText("PLAYER ONE WINS");
                break;
            case PLAYER_TWO:
                stateAnnouncement.setText("PLAYER TWO WINS");
                break;
            case DRAW:
                stateAnnouncement.setText("DRAW");
                break;
        }
    }

    /*
     * Toggle gamemode.
     */
    public Mode toggleMode() {
        if (mode == Mode.MULTI_PLAYER) {
            mode = Mode.SINGLE_PLAYER;
        } else {
            mode = Mode.MULTI_PLAYER;
        }
        return mode;
    }

    /*
     * Set the correct gamemode icon.
     */
    public void setModeIcon() {
        if (optionsMenu != null) {
            if (mode == Mode.MULTI_PLAYER) {
                optionsMenu.findItem(R.id.modeButton).setIcon(R.drawable.ic_twop);
            } else {
                optionsMenu.findItem(R.id.modeButton).setIcon(R.drawable.ic_onep);
            }
        }
    }
}
