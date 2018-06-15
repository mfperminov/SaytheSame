package com.mperminov.saythesame.ui.Login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.ui.Login.LoginPresenter;
import javax.inject.Inject;



public class SignInFragment extends Fragment {


 @BindView(R.id.button_sign_up) Button signUp;
 @BindView(R.id.emailInputLtSignin) TextInputLayout emailInLtSignIn;
 @BindView(R.id.emailInputEdTxtSignIn) TextInputEditText emailEdTxtSignIn;
 @BindView(R.id.pswdInputLtSignIn) TextInputLayout pswdInLtSignIn;
 @BindView(R.id.pswdInputEdTxtSignIn) TextInputEditText pswdEdTxtSignIn;
 @BindView(R.id.btnSignInProcess) Button btnProceedSignIn;
  @Inject
  LoginPresenter presenter;

  private SignUpClickListner signUpClickListner;

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
    signUpClickListner.onbtnSignUpClick();
  }




  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof SignUpClickListner) {
      signUpClickListner = (SignUpClickListner) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    signUpClickListner = null;
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
  public interface SignUpClickListner {
    void onbtnSignUpClick();
  }
}
