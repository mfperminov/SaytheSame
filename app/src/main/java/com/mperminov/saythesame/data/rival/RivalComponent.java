package com.mperminov.saythesame.data.rival;

import com.mperminov.saythesame.base.annotation.GameScope;
import com.mperminov.saythesame.ui.game.GameActivityComponent;
import com.mperminov.saythesame.ui.game.GameActivityModule;
import dagger.Subcomponent;

@GameScope
@Subcomponent(
    modules = {
        RivalModule.class
    }
)
public interface RivalComponent {
    GameActivityComponent plus(GameActivityModule activityModule);
}

