package com.mperminov.saythesame.data.rival;

import com.mperminov.saythesame.base.annotation.GameScope;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.GameService;
import dagger.Module;
import dagger.Provides;

@Module
public class RivalModule {
  private Rival rival;

  public RivalModule(Rival rival) {
    this.rival = rival;
  }

  @GameScope
  @Provides
  Rival provideRival() {
    return rival;
  }

  @GameScope
  @Provides
  GameService provideGameService(User user) {
    return new GameService(user, rival);
  }
}

