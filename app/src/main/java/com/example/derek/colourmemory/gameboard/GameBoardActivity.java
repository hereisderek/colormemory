package com.example.derek.colourmemory.gameboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.derek.colourmemory.R;
import com.example.derek.colourmemory.base.BaseActivity;
import com.example.derek.colourmemory.util.ActivityUtils;

import timber.log.Timber;

import static com.example.derek.colourmemory.util.Util.checkNotNull;

/**
 * Created by derek on 15/10/17.
 */

public class GameBoardActivity extends BaseActivity implements GameBoardContract.GameBoardView, GameTilesFragment.OnTileClickedListener, View.OnClickListener {
    private GameTilesFragment gameTilesFragment;
    private GameBoardContract.Presenter mPresenter;

    private TextView currentScoreTextView;
    private TextView highestScoreTextView;
    private TextView restartTextView;
    private TextView submitTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameboard);

        currentScoreTextView = findViewById(R.id.game_board_current_score);
        highestScoreTextView = findViewById(R.id.game_board_highest_score);
        submitTextView = findViewById(R.id.game_footer_control_submit);
        restartTextView = findViewById(R.id.game_footer_control_restart);

        currentScoreTextView.setOnClickListener(this);
        highestScoreTextView.setOnClickListener(this);
        submitTextView.setOnClickListener(this);
        restartTextView.setOnClickListener(this);

        gameTilesFragment = (GameTilesFragment) getSupportFragmentManager().findFragmentById(R.id.game_tile_root);
        if (gameTilesFragment == null) {
            Timber.d("unable to find retained fragment, creating new instance");
            gameTilesFragment = GameTilesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), gameTilesFragment, R.id.game_tile_root);
        } else {
            Timber.d("found and reuse retained fragment");
        }

        new GameBoardPresenter(this, checkNotNull(gameTilesFragment));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().beginTransaction().remove(gameTilesFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotNull(mPresenter);
        mPresenter.start();
    }


    @Override
    public void onClick(View v) {
        if (v == highestScoreTextView) {
            // TODO: 16/10/17 implementation
        } else if (v == submitTextView) {
            showSubmitScoreDialog();
        } else if (v == restartTextView) {
            mPresenter.restartGame();
        }
    }

    @Override
    public void onTileClicked(int index) {
        checkNotNull(mPresenter).onTileClicked(index);
    }

    @Override
    public void updateHighestScore(int score) {
        checkNotNull(currentScoreTextView).setText(getString(R.string.game_board_highest_score, score));
    }

    @Override
    public void updateCurrentScore(int score) {
        checkNotNull(highestScoreTextView).setText(getString(R.string.game_board_current_score, score));
    }

    @Override
    public void updateLogo() {

    }

    @Override
    public void updateTimer(long currentTimeMilliSecond) {
        Timber.d("updating time: %d ms, %d s", currentTimeMilliSecond, Math.round(currentTimeMilliSecond / 1000));
    }

    @Override
    public void setSubmitVisibility(boolean visibility) {
        checkNotNull(submitTextView).setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showSubmitScoreDialog() {
        // TODO: 17/10/17
        checkNotNull(mPresenter).submitScore("dummy");
    }

    @Override
    public void setPresenter(GameBoardContract.Presenter presenter) {
        this.mPresenter = checkNotNull(presenter);
    }
}
