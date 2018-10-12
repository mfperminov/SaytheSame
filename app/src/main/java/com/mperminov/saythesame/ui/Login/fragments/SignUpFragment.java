package com.mperminov.saythesame.ui.Login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.mperminov.saythesame.ui.Login.LoginActivityModule;
import com.mperminov.saythesame.ui.Login.LoginPresenter;
import javax.inject.Inject;

public class SignUpFragment extends Fragment {
  private static final int MS_WAIT_BEFORE_ERROR_SHOWN = 400;
  @Inject
  LoginPresenter presenter;

  @Inject
  FragmentManager fragmentManager;

  @BindView(R.id.button_sign_in) Button signIn;
  @BindView(R.id.emailInputL) TextInputLayout emailInputLayout;
  @BindView(R.id.emailEdTxt) TextInputEditText emailEditText;
  @BindView(R.id.pswdInputL) TextInputLayout passwordInputLayout;
  @BindView(R.id.pswdEdTxt) TextInputEditText passwordEditText;
  @BindView(R.id.nicknameInputL) TextInputLayout nickInputLayout;
  @BindView(R.id.nickEdTxt) TextInputEditText nickEditText;
  @BindView(R.id.proceed_sign_up) Button proceedSignUpButton;

  public SignUpFragment() {
    // Required empty public constructor
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
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
    emailEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence.toString())
            .matches()) {
          emailInputLayout.setError("Please enter a valid e-mail");
        } else {
          emailInputLayout.setError(null);
        }
      }

      @Override public void afterTextChanged(Editable editable) {
      }
    });
    passwordEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() < 6) {
          passwordInputLayout.setError(
              "Password  shall contain at least 6 characters");
        } else {
          passwordInputLayout.setError(null);
        }
      }

      @Override public void afterTextChanged(Editable editable) {

      }
    });
    nickEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
        if (TextUtils.isEmpty(charSequence)) {
          nickInputLayout.setError("Please enter a nickname");
        } else {
          nickInputLayout.setError("");
          new Handler().postDelayed(new Runnable(){
            public void run(){
              presenter.checkNickname(charSequence);
            }},MS_WAIT_BEFORE_ERROR_SHOWN);

        }
      }

      @Override public void afterTextChanged(Editable editable) {
      }
    });
  }

  @Override
  public void onAttach(Context context) {
    BaseApplication.get(context).getAppComponent()
        .plus(new LoginActivityModule((LoginActivity) context))
        .inject(this);
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @OnClick(R.id.button_sign_in)
  public void onbtnSignIn() {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    SignInFragment fragment = new SignInFragment();
    fragmentTransaction.replace(R.id.fragment_container, fragment, "signIn");
    fragmentTransaction.disallowAddToBackStack();
    fragmentTransaction.commit();
  }

  @OnClick(R.id.proceed_sign_up)
  public void onBtnProceedSignUp() {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      public void run() {
        if (checkInputSignUp()) {
          presenter.checkNicknameAndProceed(emailEditText.getText().toString(),
              passwordEditText.getText().toString(), nickEditText.getText().toString());
        }
      }
    }, 500);

  }


  //check that forms are filled correctly
  private boolean checkInputSignUp() {
    return TextUtils.isEmpty(emailInputLayout.getError())
        && TextUtils.isEmpty(passwordInputLayout.getError())
        && TextUtils.isEmpty(nickInputLayout.getError());
  }

  public void showErrorEmail(@Nullable String errorMessage) {
    emailInputLayout.setError(errorMessage);
  }

  public void showErrorNickname(@Nullable String errorMessage) {
    nickInputLayout.setError(errorMessage);
  }

}






