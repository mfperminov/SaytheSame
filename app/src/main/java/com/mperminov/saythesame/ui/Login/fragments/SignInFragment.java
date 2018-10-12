package com.mperminov.saythesame.ui.Login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.ui.Login.LoginActivity;
import com.mperminov.saythesame.ui.Login.LoginActivityModule;
import com.mperminov.saythesame.ui.Login.LoginPresenter;
import com.mperminov.saythesame.ui.Login.LoginTextWatcher;
import javax.inject.Inject;

public class SignInFragment extends Fragment {

  @BindView(R.id.button_sign_up) MaterialButton btnSignUp;
  @BindView(R.id.emailInputLtSignIn) TextInputLayout emailInputLayout;
  @BindView(R.id.emailInputEdTxtSignIn) TextInputEditText emailEditText;
  @BindView(R.id.pswdInputLtSignIn) TextInputLayout passwordInputLayout;
  @BindView(R.id.pswdInputEdTxtSignIn) TextInputEditText passwordEditText;
  @BindView(R.id.btnSignIn) MaterialButton btnProceedSignIn;

  @Inject
  LoginPresenter presenter;

  @Inject
  FragmentManager fragmentManager;

  public SignInFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
    ButterKnife.bind(this, view);
    setListeners();
    return view;
  }

  private void setListeners() {
    // e-mail filed
    LoginTextWatcher emailTextWatcher = new LoginTextWatcher(emailInputLayout,
        "Please enter a valid e-mail",
        it -> !android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches());
    emailEditText.addTextChangedListener(emailTextWatcher);

    // password field
    LoginTextWatcher passwordTextWatcher = new LoginTextWatcher(passwordInputLayout,
        "Password  shall contain at least 6 characters",it -> it.length() < 6);
    passwordEditText.addTextChangedListener(passwordTextWatcher);
  }

  @OnClick(R.id.button_sign_up)
  public void onBtnSignUpClick() {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    SignUpFragment fragment = new SignUpFragment();
    fragmentTransaction.replace(R.id.fragment_container, fragment, "btnSignUp");
    fragmentTransaction.disallowAddToBackStack();
    fragmentTransaction.commit();
  }

  @OnClick(R.id.btnSignIn)
  public void onBtnProceedSignIn() {
    if (checkInputSignIn()) {
      presenter.loginWithEmail(emailEditText.getText().toString(),
          passwordEditText.getText().toString());
    }
  }

  private boolean checkInputSignIn() {
    Boolean isInputValid = true;
    if (TextUtils.isEmpty(emailEditText.getText().toString()) ||
        !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString())
            .matches()) {
      emailInputLayout.setError("Please enter a valid e-mail");
      isInputValid = false;
    }
    if (TextUtils.isEmpty(passwordEditText.getText().toString())
        || (passwordEditText.getText().toString().length() <= 6)) {
      passwordInputLayout.setError("Please enter a correct password (minimum 6 characters)");
      isInputValid = false;
    }
    if (isInputValid) {
      emailInputLayout.setError("");
      passwordInputLayout.setError("");
    }
    return isInputValid;
  }

  @Override
  public void onAttach(Context context) {
    BaseApplication.get(context).getAppComponent()
        .plus(new LoginActivityModule((LoginActivity) context))
        .inject(this);
    super.onAttach(context);
  }
}
