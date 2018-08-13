package com.mperminov.saythesame.ui.Login;

import android.support.v4.app.FragmentManager;
import com.mperminov.saythesame.base.annotation.ActivityScope;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.UserService;
import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {
    private LoginActivity activity;

    public LoginActivityModule(LoginActivity activity) {
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    LoginActivity provideLoginActivity() {
        return activity;
    }

    @ActivityScope
    @Provides
    LoginPresenter provideLoginPresenter(FirebaseUserService firebaseUserService,
        UserService userService) {
        return new LoginPresenter(activity, firebaseUserService, userService);
    }

    @ActivityScope
    @Provides
    FragmentManager provideFragmentManager() {
        return activity.getSupportFragmentManager();
    }
}
