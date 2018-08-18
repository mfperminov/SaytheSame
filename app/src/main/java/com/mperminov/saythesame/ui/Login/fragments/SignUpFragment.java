package com.mperminov.saythesame.ui.Login.fragments;

import android.content.Context;
import android.os.Bundle;
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
    TextInputLayout nickInputLayout;
    @BindView(R.id.nickEdTxt)
    TextInputEditText nickEditText;
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
        setListeners();
        return view;
    }

    private void setListeners() {
        emailEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence.toString())
                    .matches()) {
                    emailInL.setError("Please enter a valid e-mail");
                } else {
                    emailInL.setError(null);
                }
            }

            @Override public void afterTextChanged(Editable editable) {
            }
        });
        pswdEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 6) {
                    passwdInL.setError(
                        "Password  shall contain at least 6 characters");
                } else {
                    passwdInL.setError(null);
                }
            }

            @Override public void afterTextChanged(Editable editable) {

            }
        });
        nickEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.checkNickname(charSequence);
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
        FragmentTransaction fragmentTransaction = fragMan.beginTransaction();
        SignInFragment fragment = new SignInFragment();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "signIn");
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    @OnClick(R.id.proceed_sign_up)
    public void onBtnProceedSignUp() {
        if (checkInputSignUp()) {
            //presenter = signInClickListener.providePresenterToSignUp();
            presenter.checkNicknameAndProceed(emailEdTxt.getText().toString(),
                pswdEdTxt.getText().toString(), nickEditText.getText().toString());
        }
    }

    //check that forms are filled correctly
    //don't need to use db or much resource
    private boolean checkInputSignUp() {
        Boolean isInputValid = true;
        if (TextUtils.isEmpty(emailEdTxt.getText().toString()) ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdTxt.getText().toString())
                .matches()) {
            emailInL.setError("Please enter a valid e-mail");
            isInputValid = false;
        }
        if (TextUtils.isEmpty(pswdEdTxt.getText().toString())
            || (pswdEdTxt.getText().toString().length() <= 6)) {
            passwdInL.setError("Please enter a correct password (minimum 6 characters)");
            isInputValid = false;
        }
        if (TextUtils.isEmpty(nickEditText.getText().toString())) {
            nickInputLayout.setError("Please enter a nickname");
            isInputValid = false;
        }
        if (isInputValid) {
            emailInL.setError("");
            passwdInL.setError("");
            nickInputLayout.setError("");
        }
        return isInputValid;
    }

    public void showResult(int errorCode) {
        switch (errorCode) {
            case 11:
                emailInL.setError("E-mail is already in use");
                break;
            case 12:
                emailInL.setError("Authentication failed");
                break;
            default:
                emailInL.setError(null);
                nickInputLayout.setError(null);
        }
    }

    public void showNicknameIsBusy() {
        nickInputLayout.setError("Sorry, this nickname is already registered");
    }

    public void nicknameUnsetError() {
        nickInputLayout.setError(null);
    }
}






