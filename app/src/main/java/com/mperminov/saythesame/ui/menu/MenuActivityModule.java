package com.mperminov.saythesame.ui.menu;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import com.mperminov.saythesame.base.annotation.ActivityScope;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.FriendsService;
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
      FirebaseUserService firebaseUserService, UserService userService,
      FriendsService friendsService) {
    return new MenuPresenter(activity, user, firebaseUserService, userService, friendsService);
  }

  @ActivityScope
  @Provides
  MenuActivity provideMenuActivity() {
    return activity;
  }

  @ActivityScope
  @Provides
  LinearLayoutManager provideLinearLayoutManager() {
    return new LinearLayoutManager(activity);
  }

  @ActivityScope
  @Provides
  MenuAdapter provideMainAdapter() {
    return new MenuAdapter(activity);
  }

  @Provides
  @ActivityScope
  AlertDialog.Builder provideAlerDialogBuilder() {
    return new AlertDialog.Builder(activity);
  }
}
