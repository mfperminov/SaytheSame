package com.mperminov.saythesame.ui.login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mperminov.saythesame.ui.login.LoginActivity;
import com.mperminov.saythesame.ui.login.LoginActivityModule;
import com.mperminov.saythesame.ui.login.LoginPresenter;
import com.mperminov.saythesame.ui.login.LoginTextWatcher;
import javax.inject.Inject;

public class SignUpFragment extends Fragment {

  private static final int MS_WAIT_BEFORE_ERROR_SHOWN = 400;
  @BindView(R.id.su_btn_signin) Button signIn;
  @BindView(R.id.su_til_email) TextInputLayout emailInputLayout;
  @BindView(R.id.su_et_email) TextInputEditText emailEditText;
  @BindView(R.id.su_til_password) TextInputLayout passwordInputLayout;
  @BindView(R.id.su_et_password) TextInputEditText passwordEditText;
  @BindView(R.id.su_til_nick) TextInputLayout nickInputLayout;
  @BindView(R.id.su_et_nick) TextInputEditText nickEditText;
  @BindView(R.id.su_btn_signup) Button proceedSignUpButton;
  @Inject
  LoginPresenter presenter;

  @Inject
  FragmentManager fragmentManager;

  public SignUpFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
    ButterKnife.bind(this, view);
    setListeners();
    return view;
  }

  private void setListeners() {
    // e-mail filed
    LoginTextWatcher emailTextWatcher = new LoginTextWatcher(
        emailInputLayout,
        "Please enter a valid e-mail",
        it -> !android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches());
    emailEditText.addTextChangedListener(emailTextWatcher);

    // password field
    LoginTextWatcher passwordTextWatcher = new LoginTextWatcher(
        passwordInputLayout, "Password  shall contain at least 6 characters",
        it -> it.length() < 6);
    passwordEditText.addTextChangedListener(passwordTextWatcher);

    //nickTextWatcher = new LoginTextWatcher("nickname", nickInputLayout,
    //    "Sorry, this nickname is already registered", it -> presenter.isNicknameBusy(it));
    //nickEditText.addTextChangedListener(nickTextWatcher);

    //nickEditText.addTextChangedListener(new TextWatcher() {
    //  @Override
    //  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    //
    //  }
    //
    //  @Override public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
    //    if (TextUtils.isEmpty(charSequence)) {
    //      nickInputLayout.setError("Please enter a nickname");
    //    } else {
    //      nickInputLayout.setError("");
    //      new Handler().postDelayed(() -> presenter.isNicknameBusy(charSequence),
    //          MS_WAIT_BEFORE_ERROR_SHOWN);
    //
    //    }
    //  }
    //
    //  @Override public void afterTextChanged(Editable editable) {
    //  }
    //});
  }

  @Override
  public void onAttach(Context context) {
    BaseApplication.get(context).getAppComponent()
        .plus(new LoginActivityModule((LoginActivity) context))
        .inject(this);
    super.onAttach(context);
  }

  @OnClick(R.id.su_btn_signin)
  public void onbtnSignIn() {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    SignInFragment fragment = new SignInFragment();
    fragmentTransaction.replace(R.id.fragment_container, fragment, "signIn");
    fragmentTransaction.disallowAddToBackStack();
    fragmentTransaction.commit();
  }

  @OnClick(R.id.su_btn_signup)
  public void onBtnProceedSignUp() {
    if (checkInputSignUp()) {
      presenter.createAccount(emailEditText.getText().toString(),
          passwordEditText.getText().toString(), nickEditText.getText().toString());
    }
  }

  //check that forms are filled correctly
  private boolean checkInputSignUp() {
    Boolean inputNotEmpty = isInputNotEmpty();
    return TextUtils.isEmpty(emailInputLayout.getError())
        && TextUtils.isEmpty(passwordInputLayout.getError())
        && TextUtils.isEmpty(nickInputLayout.getError())
        && inputNotEmpty;
  }

  private boolean isInputNotEmpty() {
    Boolean notEmpty = true;
    if (TextUtils.isEmpty(emailEditText.getText())) {
      notEmpty = false;
      emailInputLayout.setError(getString(R.string.empty_email));
      //emailInputLayout.setError("Please enter an e-mail");
    }
    if (TextUtils.isEmpty(passwordEditText.getText())) {
      notEmpty = false;
      passwordInputLayout.setError(getString(R.string.empty_password));
    }
    if (TextUtils.isEmpty(nickEditText.getText())) {
      notEmpty = false;
      nickInputLayout.setError(getString(R.string.empty_nickname));
    }
    if (notEmpty) {
      emailInputLayout.setError("");
      nickInputLayout.setError("");
      passwordInputLayout.setError("");
    }
    return notEmpty;
  }

  public void showErrorEmail(@Nullable String errorMessage) {
    emailInputLayout.setError(errorMessage);
  }

  public void showErrorNickname(@Nullable String errorMessage) {
    nickInputLayout.setError(errorMessage);
  }
}






