package com.mperminov.saythesame.data.source.remote;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mperminov.saythesame.data.model.Friend;
import com.mperminov.saythesame.data.model.User;

public class FriendsService {
  private User user;
  private DatabaseReference databaseRef;

  public FriendsService(User user) {
    this.user = user;
    this.databaseRef = FirebaseDatabase.getInstance().getReference();
  }

  public DatabaseReference getFriends() {
    return databaseRef.child("users").child(user.getUid()).child("friends");
  }

  // TODO
  public void getFriend(String username) {

  }

  public void setFriend(Friend friend) {
    String photo_url = friend.getPhoto_url();
    if (photo_url == null) {
      friend.setPhoto_url("NOT");
    }
    databaseRef.child("users").child(user.getUid()).child("friends").push()
        .setValue(friend);
  }

  // TODO
  public void updateFriend(Friend friend) {

  }

  // TODO
  public void deleteFriend(String username) {

  }
}
