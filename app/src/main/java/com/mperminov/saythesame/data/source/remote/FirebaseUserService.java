package com.mperminov.saythesame.data.source.remote;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.ui.Login.LoginActivity;

public class FirebaseUserService {
  private Application application;

  private FirebaseAuth firebaseAuth;

  // for google
  private GoogleApiClient googleApiClient;

  // for facebook
  private CallbackManager callbackManager;

  public FirebaseUserService(Application application) {
    this.application = application;
    this.firebaseAuth = FirebaseAuth.getInstance();
  }

  public Task<AuthResult> getUserWithEmail(String email, String password) {
    return firebaseAuth.signInWithEmailAndPassword(email, password);
  }

  public Task<AuthResult> createUserWithEmail(String email, String password) {
    return firebaseAuth.createUserWithEmailAndPassword(email, password);
  }

  public Intent getUserWithGoogle(BaseActivity activity) {
    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    googleApiClient = new GoogleApiClient.Builder(activity)
        .enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
          @Override
          public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
          }
        })
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();

    return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
  }

  public Task<AuthResult> getAuthWithGoogle(final BaseActivity activity,
      GoogleSignInAccount acct) {
    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    return firebaseAuth.signInWithCredential(credential);
  }

  public CallbackManager getUserWithFacebook() {
    callbackManager = CallbackManager.Factory.create();
    return callbackManager;
  }

  public Task<AuthResult> getAuthWithFacebook(AccessToken token) {
    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    return firebaseAuth.signInWithCredential(credential);
  }

  public void logOut(final BaseActivity activity, String provider) {
    if (provider.equals("facebook.com")) {
      firebaseAuth.signOut();
      LoginManager.getInstance().logOut();
    } else if (provider.equals("google.com")) {
      googleApiClient.connect();
      googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
          firebaseAuth.signOut();
          if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                  @Override
                  public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                      Log.d("google logout", "User Logged out");
                      Intent intent = new Intent(activity, LoginActivity.class);
                      activity.startActivity(intent);
                      activity.finish();
                    }
                  }
                });
          }
        }

        @Override
        public void onConnectionSuspended(int i) {
          Log.d("Google logout", "Google API Client Connection Suspended");
        }
      });
    } else {
      firebaseAuth.signOut();
      Intent intent = new Intent(activity, LoginActivity.class);
      activity.startActivity(intent);
      activity.finish();
    }
  }

  public void deleteUser(String uid) {

  }

  public void updateUser(User user) {

  }
}


