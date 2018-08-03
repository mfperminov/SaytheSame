package com.mperminov.saythesame.ui.menu;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.UserService;

import java.util.HashMap;
import java.util.Map;

public class MenuPresenter implements BasePresenter {
  private MenuActivity activity;
  private User user;
  private FirebaseUserService firebaseUserService;
  private UserService userService;
  private FirebaseAuth firebaseAuth;
  private FirebaseFunctions mFunctions;
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

  public void queueUp() {
    queueUid(user.getUid()).addOnCompleteListener(new OnCompleteListener<String>() {
      @Override
      public void onComplete(@NonNull Task<String> task) {
        String result = task.getResult().toString();
        activity.updateTestField(result);
      }
    });

  }

  private Task<String> queueUid(String uid) {
    mFunctions = FirebaseFunctions.getInstance("us-central1");
    // Create the arguments to the callable function.
    Map<String, Object> data = new HashMap<>();
    data.put("text", uid);
    data.put("push", true);

    return mFunctions
            .getHttpsCallable("setInQueue")
            .call(data)
            .continueWith(new Continuation<HttpsCallableResult, String>() {
              @Override
              public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                String result = (String) task.getResult().getData();
                return result;
              }
            });
  }
}
