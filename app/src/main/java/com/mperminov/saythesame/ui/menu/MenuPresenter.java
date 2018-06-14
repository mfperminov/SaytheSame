package com.mperminov.saythesame.ui.menu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.UserService;

public class MenuPresenter implements BasePresenter {
  private MenuActivity activity;
  private User user;
  private FirebaseUserService firebaseUserService;
  private UserService userService;
  private FirebaseAuth firebaseAuth;
  private DatabaseReference databaseRef;
  public MenuPresenter(MenuActivity activity, User user,
      FirebaseUserService firebaseUserService, UserService userService) {
    this.activity = activity;
    this.user = user;
    this.firebaseUserService = firebaseUserService;
    this.userService = userService;
    this.firebaseAuth = FirebaseAuth.getInstance();
    this.databaseRef = FirebaseDatabase.getInstance().getReference();
  }
  @Override public void subscribe() {
    if(user != null) {
      activity.sendMessageToBreakPreviousScreen();
    }
  }

  @Override public void unsubscribe() {

  }
}
