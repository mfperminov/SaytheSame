package com.mperminov.saythesame.ui.Login;

import com.mperminov.saythesame.base.annotation.ActivityScope;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
    modules = {
        LoginActivityModule.class
    }
)
public interface LoginActivityComponent {
  LoginActivity inject(LoginActivity activity);
}
