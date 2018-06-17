package com.mperminov.saythesame.ui.Login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mperminov.saythesame.ui.Login.LoginActivity;
import com.mperminov.saythesame.ui.Login.LoginPresenter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignInClickListener} interface
 * to handle interaction events.
 */
public class SignUpFragment extends Fragment {
  private LoginPresenter presenter;
private LoginActivity mActivity;
  @BindView(R.id.button_sign_in) Button signIn;
  @BindView(R.id.emailInputL)TextInputLayout emailInL;
  @BindView(R.id.emailEdTxt) TextInputEditText emailEdTxt;
  @BindView(R.id.pswdInputL) TextInputLayout passwdInL;
  @BindView(R.id.pswdEdTxt) TextInputEditText pswdEdTxt;
  @BindView(R.id.nicknameInputL) TextInputLayout nickInL;
  @BindView(R.id.nickEdTxt) TextInputEditText nickEdT;
  @BindView(R.id.proceed_sign_up) Button procdSignUp;

  private SignInClickListener signInClickListener;

  public SignUpFragment() {
    // Required empty public constructor
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
    super.onAttach(context);
    if (context instanceof SignInClickListener) {
      signInClickListener = (SignInClickListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    signInClickListener = null;
  }

  @OnClick(R.id.button_sign_in)
  public void onbtnSignIn() {
    signInClickListener.onbtnSignInClick();
  }


  @OnClick(R.id.proceed_sign_up)
  public void onBtnProceedSignUp(){
    if(checkInputSignUp()){
      presenter = signInClickListener.providePresenterToSignUp();
      presenter.checkNicknameAndProceed(emailEdTxt.getText().toString(),
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
  public interface SignInClickListener {
    void onbtnSignInClick();
    LoginPresenter providePresenterToSignUp();
    void showResult(int errorCode);
  }

}
