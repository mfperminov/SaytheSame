package com.mperminov.saythesame.data.firebase;

import android.app.Application;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.UserService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class FirebaseModule {
  @Provides
  @Singleton
  public FirebaseUserService provideFirebaseUserService(Application application) {
    return new FirebaseUserService(application);
  }

  @Provides
  @Singleton
  public UserService provideUserService(Application application) {
    return new UserService(application);
  }
}
