package com.mperminov.saythesame.base;

import com.mperminov.saythesame.data.firebase.FirebaseModule;
import com.mperminov.saythesame.data.user.UserComponent;
import com.mperminov.saythesame.data.user.UserModule;
import com.mperminov.saythesame.ui.Login.LoginActivityComponent;
import com.mperminov.saythesame.ui.Login.LoginActivityModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        AppModule.class,
        FirebaseModule.class
    }
)
public interface AppComponent {
    LoginActivityComponent plus(LoginActivityModule activityModule);

    UserComponent plus(UserModule userModule);
}
