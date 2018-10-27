package com.mperminov.saythesame.ui.login;

import com.mperminov.saythesame.base.annotation.ActivityScope;
import com.mperminov.saythesame.ui.login.fragments.SignInFragment;
import com.mperminov.saythesame.ui.login.fragments.SignUpFragment;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
    modules = {
        LoginActivityModule.class
    }
)
public interface LoginActivityComponent {
  LoginActivity inject(LoginActivity activity);

  SignInFragment inject(SignInFragment sginFrag);

  SignUpFragment inject(SignUpFragment sgupFrag);

}
