package com.mperminov.saythesame.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.Rival;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GameActivity extends BaseActivity {
    @BindView(R.id.nickrivalTv)
    TextView nickRivalTv;
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
}
