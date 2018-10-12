package com.mperminov.saythesame.data.user;

import com.mperminov.saythesame.base.annotation.UserScope;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.FriendsService;
import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
  private User user;

  public UserModule(User user) {
    this.user = user;
  }

  @Provides
  @UserScope
  User provideUser() {
    return user;
  }

  @Provides
  @UserScope
  FriendsService provideFriendsService() {
    return new FriendsService(user);
  }
}
