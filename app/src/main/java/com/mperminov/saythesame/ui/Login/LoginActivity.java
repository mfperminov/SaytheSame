package com.mperminov.saythesame.ui.Login;

import android.content.Intent;
import android.os.Bundle;
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
import com.mperminov.saythesame.ui.Login.fragments.SignInFragment;
import com.mperminov.saythesame.ui.Login.fragments.SignUpFragment;
import com.mperminov.saythesame.ui.menu.MenuActivity;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements
    SignInFragment.SignUpClickListener, SignUpFragment.SignInClickListener {
  private static final String TAG_FB ="FACEBOOK LOGIN";
  //for facebook login
  private CallbackManager callbackManager;
  @BindView(R.id.btn_google) ImageView gglBtn;
  @BindView(R.id.btn_facebook) ImageView fcbBtn;
  /*@BindView(R.id.sign_in_fb_button) Button fbButton;
  @BindView(R.id.sign_in_google_button) Button gglButton;
  @BindView(R.id.button_sign_up) Button signUp;
  @BindView(R.id.button_sign_in) Button signIn;
  @BindView(R.id.pBar) ProgressBar pb;
  @BindView(R.id.emailInputL)TextInputLayout emailInL;
  @BindView(R.id.emailEdTxt) TextInputEditText emailEdTxt;
  @BindView(R.id.pswdInputL) TextInputLayout passwdInL;
  @BindView(R.id.pswdEdTxt) TextInputEditText pswdEdTxt;
  @BindView(R.id.nicknameInputL) TextInputLayout nickInL;
  @BindView(R.id.nickEdTxt) TextInputEditText nickEdT;
  @BindView(R.id.proceed_sign_up) Button btnProceedSignUp;
  @BindView(R.id.emailInputLtSignin) TextInputLayout emailInLtSignIn;
  @BindView(R.id.emailInputEdTxtSignIn) TextInputEditText emailEdTxtSignIn;
  @BindView(R.id.pswdInputLtSignIn) TextInputLayout pswdInLtSignIn;
  @BindView(R.id.pswdInputEdTxtSignIn) TextInputEditText pswdEdTxtSignIn;
  @BindView(R.id.btnSignInProcess) Button btnProceedSignIn;*/
  @Inject
  LoginPresenter presenter;

  private static final int RC_SIGN_IN_GOOGLE = 9001;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    SignUpFragment fragment = new SignUpFragment();
    fragmentTransaction.add(R.id.fragment_container, fragment);
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
    super.onResume();
    presenter.subscribe();
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.unsubscribe();
  }
  @OnClick(R.id.btn_google)
  public void onBtnSignWithGoogle(){
    Intent intent = presenter.loginWithGoogle();
    startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
  }


  @OnClick(R.id.btn_facebook)
  public void onBtnSignWithFb(){
    callbackManager = presenter.loginWithFacebook();
  }
  public void showLoginFail() {
    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
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



  @Override public void onbtnSignInClick() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    SignInFragment fragment = new SignInFragment();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.disallowAddToBackStack();
    fragmentTransaction.commit();
  }

  @Override public LoginPresenter providePresenterToSignUp() {
    return presenter;
  }

  @Override public void onbtnSignUpClick() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    SignUpFragment fragment = new SignUpFragment();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.disallowAddToBackStack();
    fragmentTransaction.commit();
  }

  @Override public LoginPresenter providePresenterToSignIn() {
    return presenter;
  }
}

