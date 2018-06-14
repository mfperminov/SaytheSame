package com.mperminov.saythesame.ui.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.CallbackManager;
import com.facebook.internal.CallbackManagerImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.ui.menu.MenuActivity;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity {
  private static final String TAG_FB ="FACEBOOK LOGIN";
  //for facebook login
  private CallbackManager callbackManager;


  @BindView(R.id.sign_in_fb_button)
  Button fbButton;
  @BindView(R.id.sign_in_google_button)
  Button gglButton;
  @BindView(R.id.button_sign_up)
  Button signUp;
  @BindView(R.id.button_sign_in)
  Button signIn;
  @BindView(R.id.pBar)
  ProgressBar pb;
  @BindView(R.id.emailInputL)
  TextInputLayout emailInL;
  @BindView(R.id.pswdInputL)
  TextInputLayout passwdInL;
  @Inject
  LoginPresenter presenter;

  private static final int RC_SIGN_IN_GOOGLE = 9001;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
  }

  @Override protected void setupActivityComponent() {
    BaseApplication.get(this).getAppComponent()
        .plus(new LoginActivityModule(this))
        .inject(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    presenter.subscribe();
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.unsubscribe();
  }
  @OnClick(R.id.sign_in_google_button)
  public void onBtnSignWithGoogle(){
    Intent intent = presenter.loginWithGoogle();
    startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
  }

  @OnClick(R.id.sign_in_fb_button)
  public void onBtnSignWithFb(){
    callbackManager = presenter.loginWithFacebook();
  }

  @OnClick(R.id.button_sign_up)
  public void onBtnSignUp(){
    emailInL.setVisibility(View.VISIBLE);
    passwdInL.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.button_sign_in)
  public void onBtnSignIn(){
    emailInL.setVisibility(View.GONE);
    passwdInL.setVisibility(View.GONE);
  }

  public void showLoginFail() {
    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
  }

  public void showLoginSuccess(User user) {
    MenuActivity.startWithUser(this, user);
  }

  public void showLoading(boolean loading) {
    pb.setVisibility(loading ? View.VISIBLE : View.GONE);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // google
    if(requestCode == RC_SIGN_IN_GOOGLE) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      presenter.getAuthWithGoogle(result);
    }
    // facebook
    else if(requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
      callbackManager.onActivityResult(requestCode, resultCode, data);
    }
  }

  public void showInsertUsername(User user) {
  }

  public void showExistUsername(User user, String username) {
    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
  }
}

