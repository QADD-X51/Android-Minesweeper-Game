package com.example.minesweeperbeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnTileClickListener {

    RecyclerView gridRecyclerView;
    MinesweeperGridRecyclerAdapter minesweeperGridRecyclerAdapter;
    MinesweeperGame game;
    TextView smileyFace, timerTextView, flagsCountTextView, flagTextView;
    Handler timerHandler;
    Runnable timerRunnable;
    boolean timerRunning;
    int secondsCount;

    final private int gridSize = 9;
    final private int debugBombCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateTimerHandler();

        flagsCountTextView = findViewById(R.id.activity_main_flagsleft);

        timerTextView = findViewById(R.id.activity_main_timer);
        timerRunning = false;
        secondsCount = 0;

        flagTextView = findViewById(R.id.activity_main_flag);
        flagTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(!game.IsGameOver())
                {
                    game.SetClearingMode(!game.GetClearingMode());
                    if(game.GetClearingMode())
                    {
                        GradientDrawable border = new GradientDrawable();
                        border.setColor(getResources().getColor(R.color.white));
                        flagTextView.setBackground(border);

                        Toast.makeText(getApplicationContext(), R.string.clearingModeON, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(getResources().getColor(R.color.black));
                    border.setStroke(1, getResources().getColor(R.color.black));
                    flagTextView.setBackground(border);

                    Toast.makeText(getApplicationContext(), R.string.clearingModeOFF, Toast.LENGTH_SHORT).show();
                }
            }
        });

        smileyFace = findViewById(R.id.activity_main_smiley);
        smileyFace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                secondsCount = 0;
                timerTextView.setText(R.string.default_count);
                timerRunning = false;
                stopTimer();

                flagsCountTextView.setText(R.string.default_count);

                smileyFace.setText(R.string.smiley);

                game = new MinesweeperGame(gridSize, debugBombCount);
                minesweeperGridRecyclerAdapter.SetTiles(game.GetGrid().GetTiles());
            }
        });

        gridRecyclerView = findViewById(R.id.activity_main_grid);
        gridRecyclerView.setLayoutManager(new GridLayoutManager(this,gridSize));

        game = new MinesweeperGame(gridSize, debugBombCount);
        minesweeperGridRecyclerAdapter = new MinesweeperGridRecyclerAdapter(game.GetGrid().GetTiles(), this);
        gridRecyclerView.setAdapter(minesweeperGridRecyclerAdapter);
    }

    @Override
    public void onTileClick(MinesweeperTile tile) {
        game.TileClickHandler(tile);
        boolean gameWon = false;

        if(!timerRunning)
        {
            timerRunning = true;
            runTimer();
        }

        flagsCountTextView.setText(String.format("%03d", game.GetFlagCount()));

        if(game.CheckWin())
        {
            timerRunning = false;
            smileyFace.setText(R.string.coolSmiley);
            Toast.makeText(getApplicationContext(), R.string.game_won_message, Toast.LENGTH_SHORT).show();
            game.GetGrid().RevealAllBomb();
            gameWon=true;
        }

        if(game.IsGameOver() && !gameWon)
        {
            timerRunning = false;
            Toast.makeText(getApplicationContext(), R.string.game_lost_message, Toast.LENGTH_SHORT).show();
            game.GetGrid().RevealAllBomb();
            smileyFace.setText(R.string.bombedSmiley);
        }

        minesweeperGridRecyclerAdapter.SetTiles(game.GetGrid().GetTiles());

    }

    private void generateTimerHandler()
    {
        timerHandler = new Handler();

        timerRunnable = new Runnable() {
            @Override
            public void run()
            {
                if(timerRunning)
                {
                    ++secondsCount;
                }
                timerTextView.setText(String.format("%03d", secondsCount));
                timerHandler.postDelayed(this, 1000);
            }
        };
    }

    private void runTimer()
    {
        timerHandler.post(timerRunnable);
    }

    private void stopTimer()
    {
        timerHandler.removeCallbacks(timerRunnable);
    }
}