package com.mperminov.saythesame.base;

import com.mperminov.saythesame.data.firebase.FirebaseModule;
import com.mperminov.saythesame.data.source.remote.FireMesService;
import com.mperminov.saythesame.data.user.UserComponent;
import com.mperminov.saythesame.data.user.UserModule;
import com.mperminov.saythesame.ui.login.LoginActivityComponent;
import com.mperminov.saythesame.ui.login.LoginActivityModule;
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

  void inject(FireMesService fireMesService);

  UserComponent plus(UserModule userModule);
}
