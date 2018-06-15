package com.mperminov.saythesame.ui.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
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
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.ui.menu.MenuActivity;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity {
  private static final String TAG_FB ="FACEBOOK LOGIN";
  //for facebook login
  private CallbackManager callbackManager;


  @BindView(R.id.sign_in_fb_button) Button fbButton;
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
  @BindView(R.id.btnSignInProcess) Button btnProceedSignIn;
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
    nickInL.setVisibility(View.VISIBLE);
    btnProceedSignUp.setVisibility(View.VISIBLE);
    btnProceedSignUp.setVisibility(View.VISIBLE);
    emailInLtSignIn.setVisibility(View.GONE);
    pswdInLtSignIn.setVisibility(View.GONE);
    btnProceedSignIn.setVisibility(View.GONE);
  }

  @OnClick(R.id.proceed_sign_up)
  public void onBtnProceedSignUp(){
    if(checkInputSignUp()){
      presenter.createAccount(emailEdTxt.getText().toString(),
          pswdEdTxt.getText().toString(),nickEdT.getText().toString());
    }
  }
  //check that forms are filled correctly
  //don't need to use db or much resource
  private boolean checkInputSignUp() {
    Boolean isInputValid = true;
    if(TextUtils.isEmpty(emailEdTxt.getText().toString()) ||
        !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdTxt.getText().toString()).matches()){
      emailInL.setError("Please enter a valid e-mail");
      isInputValid = false;
    }
    if(TextUtils.isEmpty(pswdEdTxt.getText().toString())
        || (pswdEdTxt.getText().toString().length() <= 6)){
      passwdInL.setError("Please enter a correct password (minimum 6 characters)");
      isInputValid = false;
    }
    if(TextUtils.isEmpty(nickEdT.getText().toString())){
      nickInL.setError("Please enter a nickname");
      isInputValid = false;
    }
    if(isInputValid){
      emailInL.setError("");
      passwdInL.setError("");
      nickInL.setError("");
    }
    return isInputValid;
  }

  @OnClick(R.id.button_sign_in)
  public void onBtnSignIn(){
    emailInL.setVisibility(View.GONE);
    passwdInL.setVisibility(View.GONE);
    nickInL.setVisibility(View.GONE);
    btnProceedSignUp.setVisibility(View.GONE);
    btnProceedSignUp.setVisibility(View.GONE);
    emailInLtSignIn.setVisibility(View.VISIBLE);
    pswdInLtSignIn.setVisibility(View.VISIBLE);
    btnProceedSignIn.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.btnSignInProcess)
  public void onBtnProceedSignIn(){
    if(checkInputSignIn()){
      presenter.loginWithEmail(emailEdTxtSignIn.getText().toString(),
          pswdEdTxtSignIn.getText().toString());
    }
  }

  private boolean checkInputSignIn() {
    Boolean isInputValid = true;
    if(TextUtils.isEmpty(emailEdTxtSignIn.getText().toString()) ||
        !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdTxtSignIn.getText().toString()).matches()){
      emailInLtSignIn.setError("Please enter a valid e-mail");
      isInputValid = false;
    }
    if(TextUtils.isEmpty(pswdEdTxtSignIn.getText().toString())
        || (pswdEdTxtSignIn.getText().toString().length() <= 6)){
      pswdInLtSignIn.setError("Please enter a correct password (minimum 6 characters)");
      isInputValid = false;
    }
    if(isInputValid){
      emailInLtSignIn.setError("");
      pswdInLtSignIn.setError("");
    }
    return isInputValid;
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

