package com.mperminov.saythesame.ui.menu;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.Friend;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.FriendsService;
import com.mperminov.saythesame.data.source.remote.UserService;
import com.mperminov.saythesame.ui.Login.LoginActivity;
import com.mperminov.saythesame.ui.game.GameActivity;
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
    private FriendsService friendsService;
    private ChildEventListener friedsListRef;


    public MenuPresenter(MenuActivity activity, User user,
        FirebaseUserService firebaseUserService, UserService userService, FriendsService friendsService) {
        this.activity = activity;
        this.user = user;
        this.firebaseUserService = firebaseUserService;
        this.userService = userService;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
        this.friendsService = friendsService;
    }

    @Override
    public void subscribe() {
        if (user != null) {
            activity.sendMessageToBreakPreviousScreen();
        }
        activity.showFriendList();
        processFriends();
        if(userService!=null) {
            Log.d("Menu presenter", "userserice is not null");
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
            .child("games").orderByKey().limitToLast(1).addValueEventListener(
            new ValueEventListener() {
                int counterToDropResultOnAttach = 0;
                @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (counterToDropResultOnAttach >0) {
                        Log.i("snapshot", dataSnapshot.getValue().toString());
                        for (DataSnapshot d:dataSnapshot.getChildren()
                        ) {
                            createRival((String) d.child("uid").getValue(),d.getKey());
                        }
                    } else counterToDropResultOnAttach++;
                }

                @Override public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void createRival(String uid, final String gameId) {
        userService.getUser(uid).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Rival mRival = dataSnapshot.getValue(Rival.class);
                    mRival.setGameId(gameId);
                    GameActivity.startGame(activity,mRival);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }
        );
    }


    public void processFriends() {
        friedsListRef = friendsService.getFriends().addChildEventListener(
            new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("friends child", dataSnapshot.getValue().toString());
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    activity.showAddedFriend(friend);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    activity.showChangedFriend(friend);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // TODO : remove
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    // TODO : moved
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // TODO : cancel
                }
            }
        );
    }
    public void setFriend(final String username) {

        userService.getUserByUsername(username).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        activity.showNotExistFriend(username);
                    } else {
                        Friend friend = dataSnapshot.getValue(Friend.class);
                        friendsService.setFriend(friend);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    activity.showNotExistFriend(username);
                }
            }
        );
    }

    public void createRivalFromFriend(Friend friend) {
        DatabaseReference friendRef = userService.getUserByUsername(friend.getUsername());
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final Rival rival = dataSnapshot.getValue(Rival.class);
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                        new OnSuccessListener<InstanceIdResult>() {
                            @Override public void onSuccess(InstanceIdResult instanceIdResult) {
                                userService.setMessagingToken(user,instanceIdResult.getToken());
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                       sendInviteToGame(rival).addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                Log.d("executed notif","send to rival");
                                            }
                                        });
                                    }
                                });

                            }
                        });
                    Log.d("new rival",rival.getName() + rival.getEmail());

                }
            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private Task<String> sendInviteToGame(Rival rival) {
        mFunctions = FirebaseFunctions.getInstance("us-central1");
        Map<String, Object> data = new HashMap<>();
        data.put("rivalUid", rival.getUid());
        data.put("userUid", user.getUid());

        return mFunctions
            .getHttpsCallable("sendInviteToGame")
            .call(data)
            .continueWith(new Continuation<HttpsCallableResult, String>() {
                @Override
                public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                    String result = "null";
                    try {
                        result = (String) task.getResult().getData();
                    } catch (Exception e) {
                        Log.e("Functions exception",e.getMessage());
                    }
                    return result;
                }
            });
    }
    public void logout() {
        firebaseUserService.logOut(activity, user.getProvider());
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }
}



