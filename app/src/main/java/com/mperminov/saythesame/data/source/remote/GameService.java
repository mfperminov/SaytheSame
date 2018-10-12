package com.mperminov.saythesame.data.source.remote;

import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;

public class GameService {
  private User mUser;
  private Rival mRival;

  public GameService(User user, Rival rival) {
    mUser = user;
    mRival = rival;
  }
}
