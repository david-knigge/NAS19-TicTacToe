package nl.uva.student.davidknigge.nas19_tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new Game();
        setGameState();
        checkGameState();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
    }

    public void resetClicked(View v) {
        game = new Game();
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
}
