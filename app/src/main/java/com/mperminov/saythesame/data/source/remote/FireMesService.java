package com.mperminov.saythesame.data.source.remote;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.Rival;
import javax.inject.Inject;

public class FireMesService extends FirebaseMessagingService {
  private static final String TAG = "MyFirebaseMsgService";
  //@Inject
  //MenuActivity menuActivity;
  //@Inject
  //User user;
  @Inject
  UserService userService;

  @Override public void onNewToken(String s) {
    super.onNewToken(s);
    Log.d(TAG, "Refreshed token: " + s);
    //userService.get().setMessagingToken(user,s);
  }

  @Override public void onCreate() {
    super.onCreate();
    BaseApplication.get(getApplicationContext()).getAppComponent().inject(this);
  }

  @Override public void onMessageReceived(final RemoteMessage remoteMessage) {

    if (remoteMessage.getData().size() > 0) {

      Log.d(TAG, remoteMessage.getData().toString());
      if (userService == null) {
        Log.d(TAG, "onMessageReceived: userservice is null");
      }
      userService.getUser(remoteMessage.getData().get("rivalUid")).addListenerForSingleValueEvent(
          new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              Rival mRival = dataSnapshot.getValue(Rival.class);
              Log.d("new rival", mRival.getUsername());
              //menuActivity.offerGame(mRival);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
          }
      );
    }
  }
}
