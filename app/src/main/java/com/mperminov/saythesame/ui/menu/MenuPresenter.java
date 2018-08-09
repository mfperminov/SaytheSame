package com.mperminov.saythesame.ui.menu;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.UserService;

import java.util.Date;
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

    @Override
    public void subscribe() {
        if (user != null) {
            activity.sendMessageToBreakPreviousScreen();
        }
    }

    @Override
    public void unsubscribe() {

    }

    public void queueUp() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                queueUid(user.getUid()).addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        String result = task.getResult().toString();
                        activity.updateTestField(result);
                        waitForStart();
                    }
                });
            }
        });
    }

    //
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

    public void waitForStart() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference()
                .child("users")
                .child(user.getUid())
                .child("games").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("1st data change",dataSnapshot.getValue().toString());
                dataSnapshot.getRef().orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    int i = 0;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String rivalUid;
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            Log.d("i",""+i);
                            Log.d("key:",d.getKey());
                            Log.d("snapshot 2", d.getValue().toString());
                            if (i>0) {
                                rivalUid = (String) d.child("id").getValue();
                                Log.d("rivalUid", rivalUid);
                                databaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(rivalUid);
                                databaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String rivalName = (String) dataSnapshot.child("username").getValue();
                                        String rivalPhotoUid = (String) dataSnapshot.child("photo_url").getValue();
                                        Log.d("Rival name",rivalName);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                //Rival rival = new Rival(rivalUid);
                                //activity.updateNickField(rival.getUsername());
                                //GameActivity.startGame((BaseActivity) MenuActivity.this,rival);
                            }
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }
}
