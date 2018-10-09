package com.mperminov.saythesame.ui.game;

import com.mperminov.saythesame.base.annotation.ActivityScope;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
    modules = {
        GameActivityModule.class
    }
)
public interface GameActivityComponent {
  GameActivity inject(GameActivity activity);
}

