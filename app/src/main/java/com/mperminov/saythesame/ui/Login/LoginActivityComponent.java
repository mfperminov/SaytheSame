package com.mperminov.saythesame.ui.Login;

import com.mperminov.saythesame.base.annotation.ActivityScope;
import com.mperminov.saythesame.ui.Login.fragments.SignInFragment;
import com.mperminov.saythesame.ui.Login.fragments.SignUpFragment;
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
