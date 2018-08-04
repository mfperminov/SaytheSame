package com.mperminov.saythesame.ui.Login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.ui.Login.LoginActivity;
import com.mperminov.saythesame.ui.Login.LoginActivityComponent;
import com.mperminov.saythesame.ui.Login.LoginActivityModule;
import com.mperminov.saythesame.ui.Login.LoginPresenter;

import javax.inject.Inject;

public class SignInFragment extends Fragment {


 @BindView(R.id.button_sign_up) Button signUp;
 @BindView(R.id.emailInputLtSignIn) TextInputLayout emailInLtSignIn;
 @BindView(R.id.emailInputEdTxtSignIn) TextInputEditText emailEdTxtSignIn;
 @BindView(R.id.pswdInputLtSignIn) TextInputLayout pswdInLtSignIn;
 @BindView(R.id.pswdInputEdTxtSignIn) TextInputEditText pswdEdTxtSignIn;
 @BindView(R.id.btnSignIn) Button btnProceedSignIn;

    @Inject
    LoginPresenter presenter;

    @Inject
    FragmentManager fragMan;



  public SignInFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.button_sign_up)
  public void onBtnSignUpClick(){
      FragmentTransaction fragmentTransaction = fragMan.beginTransaction();
      SignUpFragment fragment = new SignUpFragment();
      fragmentTransaction.replace(R.id.fragment_container, fragment,"signUp");
      fragmentTransaction.disallowAddToBackStack();
      fragmentTransaction.commit();
  }

@OnClick(R.id.btnSignIn)
  public void onBtnProceedSignIn(){
    if(checkInputSignIn()){
      //presenter = signUpClickListener.providePresenterToSignIn();
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


  @Override
  public void onAttach(Context context) {
      BaseApplication.get(context).getAppComponent()
              .plus(new LoginActivityModule((LoginActivity)context))
              .inject(this);
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    //signUpClickListener = null;
  }
}
