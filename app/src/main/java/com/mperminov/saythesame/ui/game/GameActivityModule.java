package com.mperminov.saythesame.ui.game;

import com.google.firebase.functions.FirebaseFunctions;
import com.mperminov.saythesame.base.annotation.ActivityScope;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import dagger.Module;
import dagger.Provides;

@Module
public class GameActivityModule {
    private GameActivity activity;

    public GameActivityModule(GameActivity activity) {
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    GameActivity provideGameActivity() {
        return activity;
    }

    @ActivityScope
    @Provides
    GamePresenter provideGamePresenter(User user, Rival rival) {
        return new GamePresenter(activity, user, rival);
    }

}
