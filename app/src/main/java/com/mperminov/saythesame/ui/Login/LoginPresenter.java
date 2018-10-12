package com.mperminov.saythesame.ui.Login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.source.remote.FirebaseUserService;
import com.mperminov.saythesame.data.source.remote.UserService;
import com.mperminov.saythesame.ui.Login.fragments.SignUpFragment;
import java.util.Arrays;

public class LoginPresenter implements BasePresenter {
  private LoginActivity activity;
  private FirebaseUserService firebaseUserService;
  private UserService userService;
  private FragmentManager fragmentManager;

  public LoginPresenter(LoginActivity activity, FirebaseUserService firebaseUserService,
      UserService userService) {
    this.activity = activity;
    this.firebaseUserService = firebaseUserService;
    this.userService = userService;
    fragmentManager = this.activity.getSupportFragmentManager();
  }

  @Override public void subscribe() {

  }

  @Override public void unsubscribe() {

  }

  CallbackManager loginWithFacebook() {

    CallbackManager callbackManager = firebaseUserService.getUserWithFacebook();
    LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email",
        "public_profile"));
    LoginManager.getInstance().registerCallback(callbackManager,
        new FacebookCallback<LoginResult>() {
          @Override
          public void onSuccess(LoginResult loginResult) {
            getAuthWithFacebook(loginResult.getAccessToken());
          }

          @Override
          public void onCancel() {
            activity.showLoginFail();
          }

          @Override
          public void onError(FacebookException error) {
            activity.showLoginFail();
            if (error instanceof FacebookAuthorizationException) {
              if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
              }
            }
          }
        });
    return callbackManager;
  }

  private void getAuthWithFacebook(AccessToken accessToken) {
    activity.showLoading(true);
    firebaseUserService.getAuthWithFacebook(accessToken)
        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              activity.showLoading(false);
              for (UserInfo profile : task.getResult().getUser().getProviderData()) {
                debugUserValues(profile);
              }
              processLogin(task.getResult().getUser(),
                  task.getResult().getUser().getProviderData().get(1), null);
            } else {
              Log.d("error before processing", task.getException().toString());
              activity.showLoading(false);
              activity.showLoginFail();
            }
          }
        });
  }

  public Intent loginWithGoogle() {
    return firebaseUserService.getUserWithGoogle(activity);
  }

  public void getAuthWithGoogle(GoogleSignInResult result) {
    activity.showLoading(true);
    if (result.isSuccess()) {
      final GoogleSignInAccount acct = result.getSignInAccount();
      firebaseUserService.getAuthWithGoogle(activity, acct)
          .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                activity.showLoading(false);
                for (UserInfo profile : task.getResult().getUser().getProviderData()) {
                  debugUserValues(profile);
                }
                processLogin(task.getResult().getUser(),
                    task.getResult().getUser().getProviderData().get(1), null);
              } else {
                activity.showLoading(false);
                activity.showLoginFail();
              }
            }
          });
    } else {
      activity.showLoginFail();
    }
  }

  private void debugUserValues(UserInfo profile) {
    String providerId = profile.getProviderId();
    String uid = profile.getUid();
    String name = profile.getDisplayName();
    String email = profile.getEmail();
    Uri photoUri = profile.getPhotoUrl();
    Log.d("New user:",
        providerId
            + " "
            + uid
            + " "
            + name
            + " "
            + email
            + " "
            + photoUri);
  }

  public void loginWithEmail(final String email, final String password) {
    activity.showLoading(true);
    firebaseUserService.getUserWithEmail(email, password)
        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              activity.showLoading(false);
              for (UserInfo profile : task.getResult().getUser().getProviderData()) {
                debugUserValues(profile);
              }
              processLogin(task.getResult().getUser(),
                  task.getResult().getUser().getProviderData().get(1),
                  task.getResult().getUser().getProviderData().get(1).getDisplayName());
            } else {
              Log.d("Login not success", task.getException().toString());
              activity.showLoading(false);
            }
          }
        });
  }

  public void checkNicknameAndProceed(final String email, final String password,
      final String nickname) {
    activity.showLoading(true);
    DatabaseReference nickNameRef = userService.getUserByUsername(nickname);
    ValueEventListener eventListener = new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          activity.showLoading(false);
          SignUpFragment signUpFragment =
              (SignUpFragment) fragmentManager.findFragmentByTag("signUp");
          signUpFragment.showErrorNickname("User with this username already exist");
          //activity.showErrorEmail(13);
        } else {
          createAccount(email, password, nickname);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    };
    nickNameRef.addListenerForSingleValueEvent(eventListener);
  }

  private void createAccount(String email, String password, final String nickname) {
    activity.showLoading(true);
    firebaseUserService.createUserWithEmail(email, password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              processLogin(task.getResult().getUser(),
                  task.getResult().getUser().getProviderData().get(1), nickname);
            } else {
              Log.d("Create not success", task.getException().toString());
              Log.d("message", task.getException().getMessage());
              activity.showLoading(false);
              activity.showLoginFail();
              SignUpFragment signUpFragment =
                  (SignUpFragment) fragmentManager.findFragmentByTag("signUp");
              signUpFragment.showErrorEmail(task.getException().getLocalizedMessage());
            }
          }
        });
  }

  private void processLogin(FirebaseUser firebaseUser, UserInfo userInfo,
      @Nullable String nickname) {
    final String username;
    final User user = User.newInstance(firebaseUser, userInfo);
    if (TextUtils.isEmpty(userInfo.getDisplayName())) {
      username = nickname;
    } else {
      username = userInfo.getDisplayName();
    }
    userService.getUser(user.getUid()).addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            User remoteUser = dataSnapshot.getValue(User.class);
            if (remoteUser == null || remoteUser.getUsername() == null) {
              createUser(user, username);
            } else {
              activity.showLoginSuccess(remoteUser);
            }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
            activity.showLoginFail();
          }
        }
    );
  }

  private void createUser(final User user, final String username) {
    activity.showLoading(true);
    userService.getUserByUsername(username).addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            if (!dataSnapshot.exists()) {
              activity.showLoading(false);
              user.setUsername(username);
              userService.createUser(user);
              activity.showLoginSuccess(user);
            } else {
              activity.showLoading(false);
              activity.showExistUsername(user, username);
            }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
            activity.showLoading(false);
            activity.showInsertUsername(user);
          }
        }
    );
  }

  public void checkNickname(CharSequence charSequence) {
    final SignUpFragment signUpFragment =
        (SignUpFragment) fragmentManager.findFragmentByTag("signUp");
    DatabaseReference dbRef = userService.getUserByUsername(charSequence.toString());
    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if (dataSnapshot.exists()) {
              signUpFragment.showErrorNickname("Username is already registered");
          } else {
              signUpFragment.showErrorNickname(null);
          }
      }

      @Override public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
}
