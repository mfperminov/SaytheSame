package com.mperminov.saythesame.ui.Login.fragments;

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
import com.mperminov.saythesame.ui.Login.LoginActivity;
import com.mperminov.saythesame.ui.Login.LoginActivityModule;
import com.mperminov.saythesame.ui.Login.LoginPresenter;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignInClickListener} interface
 * to handle interaction events.
 */
public class SignUpFragment extends Fragment {
  //private LoginPresenter presenter;
  @Inject
  LoginPresenter presenter;

  @Inject
  FragmentManager fragMan;

  @BindView(R.id.button_sign_in)
  Button signIn;
  @BindView(R.id.emailInputL)
  TextInputLayout emailInL;
  @BindView(R.id.emailEdTxt)
  TextInputEditText emailEdTxt;
  @BindView(R.id.pswdInputL)
  TextInputLayout passwdInL;
  @BindView(R.id.pswdEdTxt)
  TextInputEditText pswdEdTxt;
  @BindView(R.id.nicknameInputL)
  TextInputLayout nickInL;
  @BindView(R.id.nickEdTxt)
  TextInputEditText nickEdT;
  @BindView(R.id.proceed_sign_up)
  Button procdSignUp;


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
    return view;
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
    FragmentTransaction fragmentTransaction = fragMan.beginTransaction();
    SignInFragment fragment = new SignInFragment();
    fragmentTransaction.replace(R.id.fragment_container, fragment,"signIn");
    fragmentTransaction.disallowAddToBackStack();
    fragmentTransaction.commit();
  }


  @OnClick(R.id.proceed_sign_up)
  public void onBtnProceedSignUp() {
    if (checkInputSignUp()) {
      //presenter = signInClickListener.providePresenterToSignUp();
      presenter.checkNicknameAndProceed(emailEdTxt.getText().toString(),
              pswdEdTxt.getText().toString(), nickEdT.getText().toString());
    }
  }

  //check that forms are filled correctly
  //don't need to use db or much resource
  private boolean checkInputSignUp() {
    Boolean isInputValid = true;
    if (TextUtils.isEmpty(emailEdTxt.getText().toString()) ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdTxt.getText().toString()).matches()) {
      emailInL.setError("Please enter a valid e-mail");
      isInputValid = false;
    }
    if (TextUtils.isEmpty(pswdEdTxt.getText().toString())
            || (pswdEdTxt.getText().toString().length() <= 6)) {
      passwdInL.setError("Please enter a correct password (minimum 6 characters)");
      isInputValid = false;
    }
    if (TextUtils.isEmpty(nickEdT.getText().toString())) {
      nickInL.setError("Please enter a nickname");
      isInputValid = false;
    }
    if (isInputValid) {
      emailInL.setError("");
      passwdInL.setError("");
      nickInL.setError("");
    }
    return isInputValid;
  }

  public void showResult(int errorCode) {
    switch (errorCode) {
      case 11:
        emailInL.setError("E-mail is already in use");
        break;
      case 12:
        emailInL.setError("Hui sosi");
        break;
      case 13:
        nickInL.setError("Sorry, this nickname is already registered");
        break;
      default:
        emailInL.setError("");
        nickInL.setError("");
    }
  }
}






