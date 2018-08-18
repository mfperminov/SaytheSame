package com.mperminov.saythesame.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import javax.inject.Inject;

public class GameActivity extends BaseActivity {
    @BindView(R.id.etMessage)
    EditText edText;
    @BindView(R.id.nickrivalTv)
    TextView nickRivalTv;
    @BindView(R.id.progressBar)
    ProgressBar pBar;
    @BindView(R.id.button_send)
    ImageButton btnSend;
    @BindView(R.id.myLastWordTv)
    TextView myLastWordTv;
    @BindView(R.id.rivalLastWordTv)
    TextView rivalLastWordTv;
    @BindView(R.id.myWord2Tv)
    TextView mySecWordTv;
    @BindView(R.id.rivalWord2Tv)
    TextView rivalSecWordTv;
    @BindView(R.id.myWord3Tv)
    TextView myThWordTv;
    @BindView(R.id.rivalWord3Tv)
    TextView rivalThWordTV;
    @BindView(R.id.timerTv)
    TextView timerTv;
    @BindView(R.id.matchingWordTv)
    TextView matchingWordTv;
    @Inject
    User user;
    @Inject
    GamePresenter presenter;
    @Inject
    Rival rival;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_game);
        ButterKnife.bind(this);
        nickRivalTv.setText(rival.getUsername());
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(!TextUtils.isEmpty(edText.getText())){
                    presenter.sendGuess(edText.getText());
                    myLastWordTv.setText(edText.getText());
                    edText.setText("");
                    showWaitingForOtherPlayer();
                } else {
                    Toast.makeText(GameActivity.this, "Please, take a guess!",
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
        presenter.startTurn();
    }

    private void showWaitingForOtherPlayer() {
        pBar.setVisibility(View.VISIBLE);
    }

    public static void startGame(final BaseActivity activity, final Rival rival) {
        Intent intent = new Intent(activity, GameActivity.class);
        BaseApplication.get(activity).createRivalComponent(rival);
        activity.startActivity(intent);
    }

    @Override
    protected void setupActivityComponent() {
        BaseApplication.get(this).getRivalComponent()
            .plus(new GameActivityModule(this))
            .inject(this);
    }

    void shiftWords(String lastRivalAnswer){
        if(!TextUtils.isEmpty(mySecWordTv.getText())){
            myThWordTv.setText(mySecWordTv.getText());
            rivalThWordTV.setText(rivalSecWordTv.getText());
        }
        if (!TextUtils.isEmpty(myLastWordTv.getText())) {
            mySecWordTv.setText(myLastWordTv.getText());
            rivalSecWordTv.setText(rivalLastWordTv.getText());
        }
        myLastWordTv.setText("");
        rivalLastWordTv.setText(lastRivalAnswer);
    }

    void updateLastRivalWord(String s){
        rivalLastWordTv.setText(s);
    }

    void setSecondsOnTimer(long l) {
        timerTv.setText(String.valueOf(l));
    }

    void showWatingForRivalGuess() {
        Toast.makeText(this, "Waiting for your rival to guess..", Toast.LENGTH_SHORT).show();
    }

    public void showTimeOver() {
        Toast.makeText(this, "Time is over!", Toast.LENGTH_SHORT).show();
    }

    public void showGameWin(String curAnswer) {
        matchingWordTv.setText(curAnswer);
        Toast.makeText(this, "Win", Toast.LENGTH_SHORT).show();
    }
}
