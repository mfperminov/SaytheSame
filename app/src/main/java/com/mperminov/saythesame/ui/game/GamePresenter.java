package com.mperminov.saythesame.ui.game;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;

public class GamePresenter implements BasePresenter {
    private GameActivity mActivity;
    private User mUser;
    private Rival mRival;
    public GamePresenter(GameActivity activity, User user, Rival rival) {
        mActivity = activity;
        mUser = user;
        mRival = rival;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
