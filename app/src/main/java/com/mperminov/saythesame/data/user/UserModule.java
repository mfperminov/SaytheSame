package com.mperminov.saythesame.data.user;

import com.mperminov.saythesame.base.annotation.UserScope;
import com.mperminov.saythesame.data.model.User;
import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
  User user;

  public UserModule(User user) {
    this.user = user;
  }

  @Provides
  @UserScope
  User provideUser() {
    return user;
  }
}
