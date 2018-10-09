package com.mperminov.saythesame.data.source.remote;

import android.app.Application;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mperminov.saythesame.data.model.User;

public class UserService {
  private Application application;
  private DatabaseReference databaseRef;

  public UserService(android.app.Application application) {
    this.application = application;
    this.databaseRef = FirebaseDatabase.getInstance().getReference();
  }

  public void createUser(User user) {
    if (user.getPhoto_url() == null) {
      user.setPhoto_url("NOT");
    }
    databaseRef.child("users").child(user.getUid()).setValue(user);
    databaseRef.child("usernames").child(user.getUsername()).setValue(user);
  }

  public DatabaseReference getUser(String userUid) {
    return databaseRef.child("users").child(userUid);
  }

  public DatabaseReference getUserByUsername(String username) {
    return databaseRef.child("usernames").child(username);
  }

  public void updateUser(User user) {

  }

  public void deleteUser(String key) {

  }

  public void setMessagingToken(User user, String token) {
    databaseRef.child("users").child(user.getUid()).child("fcm_token").setValue(token);
  }
}
