package com.mperminov.saythesame.data.rival;

import com.mperminov.saythesame.base.annotation.RivalScope;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.GameService;

import dagger.Module;
import dagger.Provides;

    @Module
    public class RivalModule {
        private Rival rival;

        public RivalModule(Rival rival) {
            this.rival = rival;
        }

        @RivalScope
        @Provides
        Rival provideRival() {
            return rival;
        }

        @RivalScope
        @Provides
        GameService provideGameService(User user) {
            return new GameService(user, rival);
        }
    }

