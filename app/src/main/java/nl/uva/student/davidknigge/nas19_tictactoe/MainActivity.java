package nl.uva.student.davidknigge.nas19_tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new Game();

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
                // findViewById(R.id.)
                break;
        }
    }
}
