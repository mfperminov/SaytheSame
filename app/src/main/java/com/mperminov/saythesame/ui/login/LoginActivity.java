package com.mperminov.saythesame.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.CallbackManager;
import com.facebook.internal.CallbackManagerImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.ui.login.fragments.SignUpFragment;
import com.mperminov.saythesame.ui.menu.MenuActivity;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity {

  private static final int RC_SIGN_IN_GOOGLE = 9001;
  @BindView(R.id.btn_google) ImageView gglBtn;
  @BindView(R.id.btn_facebook) ImageView fcbBtn;
  @Inject
  LoginPresenter presenter;
  @Inject
  FragmentManager fragmentManager;
  //for facebook login
  private CallbackManager callbackManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Debug.startMethodTracing();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    SignUpFragment fragment = new SignUpFragment();
    fragmentTransaction.add(R.id.fragment_container, fragment, "signUp");
    fragmentTransaction.commit();
    ButterKnife.bind(this);
  }

  @Override protected void setupActivityComponent() {
    BaseApplication.get(this).getAppComponent()
        .plus(new LoginActivityModule(this))
        .inject(this);
  }

  @Override
  protected void onResume() {
    Debug.stopMethodTracing();
    super.onResume();
    presenter.subscribe();
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.unsubscribe();
  }

  @OnClick(R.id.btn_google)
  public void onBtnSignWithGoogle() {
    Intent intent = presenter.loginWithGoogle();
    startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
  }

  @OnClick(R.id.btn_facebook)
  public void onBtnSignWithFb() {
    callbackManager = presenter.loginWithFacebook();
  }

  public void showLoginFail() {
    Toast.makeText(this,"Login Failed", Toast.LENGTH_LONG).show();
  }

  public void showLoginSuccess(User user) {
    MenuActivity.startWithUser(this, user);
  }

  public void showLoading(boolean loading) {
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // google
    if (requestCode == RC_SIGN_IN_GOOGLE) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      presenter.getAuthWithGoogle(result);
    }
    // facebook
    else if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
      callbackManager.onActivityResult(requestCode, resultCode, data);
    } else if (requestCode == MenuActivity.REQUEST_COMPLETED) finish();
  }

  public void showInsertUsername(User user) {
  }

  public void showExistUsername(User user, String username) {
    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
  }
}

