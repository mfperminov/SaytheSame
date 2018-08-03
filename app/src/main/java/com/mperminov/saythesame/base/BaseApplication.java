package com.mperminov.saythesame.base;

import android.app.Application;
import android.content.Context;
import com.mperminov.saythesame.data.firebase.FirebaseModule;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.rival.RivalComponent;
import com.mperminov.saythesame.data.rival.RivalModule;
import com.mperminov.saythesame.data.user.UserComponent;
import com.mperminov.saythesame.data.user.UserModule;

public class BaseApplication extends Application {
  private AppComponent appComponent;
  private UserComponent userComponent;
  private RivalComponent RivalComponent;

  public static BaseApplication get(Context context) {
    return (BaseApplication) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    initAppComponent();
  }

  private void initAppComponent() {
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .firebaseModule(new FirebaseModule())
        .build();
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }

  public UserComponent createUserComponent(User user) {
    userComponent = appComponent.plus(new UserModule(user));
    return userComponent;
  }

  public UserComponent getUserComponent() {
    return userComponent;
  }

  public void releaseUserComponent() {
    userComponent = null;
  }

  /*public UserComponent createUserComponent(User user) {
    userComponent = appComponent.plus(new UserModule(user));
    return userComponent;
  }

  public UserComponent getUserComponent() {
    return userComponent;
  }

  public void releaseUserComponent() {
    userComponent = null;
  }*/

  public RivalComponent createRivalComponent(Rival rival) {
    RivalComponent = userComponent.plus(new RivalModule(rival));
    return RivalComponent;
  }

  public RivalComponent getRivalComponent() {
    return RivalComponent;
  }

  public void releaseRivalComponent() {
    RivalComponent = null;
  }
}
