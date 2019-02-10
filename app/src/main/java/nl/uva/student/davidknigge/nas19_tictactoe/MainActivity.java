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
        setGameState();
        checkGameState();
        setModeIcon();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        optionsMenu = menu;
        return true;
    }

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

        if (!game.gameOver() && mode == Mode.SINGLE_PLAYER && game.getTurn() == GameState.PLAYER_TWO_TURN) {
            computerClick();
            checkGameState();
        }
    }

    public void resetClicked() {
        game = new Game(mode);
        setGameState();
        checkGameState();
    }

    public void setGameState() {
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

    public Mode toggleMode() {
        if (mode == Mode.MULTI_PLAYER) {
            mode = Mode.SINGLE_PLAYER;
        } else {
            mode = Mode.MULTI_PLAYER;
        }
        return mode;
    }

    public void computerClick() {
        int tileIndex = game.getComputerMove();
        ViewGroup gameContainer = findViewById(R.id.game_container);
        View button = gameContainer.getChildAt(tileIndex);
        button.performClick();
    }

    public void setModeIcon() {
        if (mode == Mode.MULTI_PLAYER) {
            optionsMenu.findItem(R.id.modeButton).setIcon(R.drawable.ic_twop);
        } else {
            optionsMenu.findItem(R.id.modeButton).setIcon(R.drawable.ic_onep);
        }
    }
}
