package com.mperminov.saythesame.ui.Login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.ui.Login.LoginPresenter;

public class SignInFragment extends Fragment {


 @BindView(R.id.button_sign_up) Button signUp;
 @BindView(R.id.emailInputLtSignIn) TextInputLayout emailInLtSignIn;
 @BindView(R.id.emailInputEdTxtSignIn) TextInputEditText emailEdTxtSignIn;
 @BindView(R.id.pswdInputLtSignIn) TextInputLayout pswdInLtSignIn;
 @BindView(R.id.pswdInputEdTxtSignIn) TextInputEditText pswdEdTxtSignIn;
 @BindView(R.id.btnSignIn) Button btnProceedSignIn;
 private LoginPresenter presenter;

  private SignUpClickListener signUpClickListener;

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
    signUpClickListener.onbtnSignUpClick();
  }

@OnClick(R.id.btnSignIn)
  public void onBtnProceedSignIn(){
    if(checkInputSignIn()){
      presenter = signUpClickListener.providePresenterToSignIn();
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
    super.onAttach(context);
    if (context instanceof SignUpClickListener) {
      signUpClickListener = (SignUpClickListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    signUpClickListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface SignUpClickListener {
    void onbtnSignUpClick();
    LoginPresenter providePresenterToSignIn();
  }
}
