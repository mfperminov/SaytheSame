package com.mperminov.saythesame.ui.menu;

import com.mperminov.saythesame.base.annotation.ActivityScope;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.UserService;
import dagger.Module;
import dagger.Provides;

@Module
public class MenuActivityModule {
  private MenuActivity activity;
  private User user;


  public MenuActivityModule(MenuActivity activity) {
    this.activity = activity;
  }

  @ActivityScope
  @Provides
  MenuPresenter provideMenuPresenter(User user,
      FirebaseUserService firebaseUserService, UserService userService){
    return new MenuPresenter(activity,user,firebaseUserService,userService);
  }
  @ActivityScope
  @Provides
  MenuActivity provideMenuActivity(){
    return activity;
  }

}
