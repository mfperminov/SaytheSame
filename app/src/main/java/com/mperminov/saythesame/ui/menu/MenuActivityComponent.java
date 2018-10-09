package com.mperminov.saythesame.ui.menu;

import com.mperminov.saythesame.base.annotation.ActivityScope;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
    modules = {
        MenuActivityModule.class
    }
)
public interface MenuActivityComponent {
  MenuActivity inject(MenuActivity activity);
}
