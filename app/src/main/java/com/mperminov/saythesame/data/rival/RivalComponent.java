package com.mperminov.saythesame.data.rival;

import com.mperminov.saythesame.base.annotation.RivalScope;
import com.mperminov.saythesame.ui.game.GameActivityComponent;
import com.mperminov.saythesame.ui.game.GameActivityModule;

import dagger.Subcomponent;

    @RivalScope
    @Subcomponent(
            modules = {
                    RivalModule.class
            }
    )
    public interface RivalComponent {
        GameActivityComponent plus(GameActivityModule activityModule);
    }

