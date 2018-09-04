package com.mperminov.saythesame.unittesting.LoginActivity;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.google.firebase.FirebaseApp;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.ui.Login.LoginActivity;
import com.mperminov.saythesame.ui.Login.fragments.SignInFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class TestEmailSignIn {
  private TextInputEditText emailView;
  private TextInputLayout emailLayout;
  private LoginActivity activity;

  @Before
  public void setUp() {
    FirebaseApp.initializeApp(RuntimeEnvironment.application);
    activity = Robolectric.setupActivity(LoginActivity.class);
    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    SignInFragment signInFragment = new SignInFragment();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.fragment_container,signInFragment);
    transaction.addToBackStack(null);
    transaction.commit();
    emailView = signInFragment.getView().findViewById(R.id.emailInputEdTxtSignIn);
    emailLayout = signInFragment.getView().findViewById(R.id.emailInputLtSignIn);
  }

  @Test
  public void emailValidate_CorrectEmailSimple_ReturnTrue(){
    emailView.setText("name@email.com");
    assertThat("No error for valid e-mail",emailLayout.getError(),is(nullValue()));
  }

  @Test
  public void emailValidate_CorrectEmailSubDomain_ReturnTrue(){
    emailView.setText("name@email.uk.com");
    assertThat("No error for valid e-mail",emailLayout.getError(),is(nullValue()));
  }

  @Test
  public void emailValidate_IncorrectEmailNoDomain_ReturnFalse(){
    emailView.setText("name@email");
    assertThat("Error - no domain",emailLayout.getError(),is(notNullValue()));
  }

  @Test
  public void emailValidate_IncorrectEmailDoubleDot_ReturnFalse(){
    emailView.setText("name@email..com");
    assertThat("Error - double dot",emailLayout.getError(),is(notNullValue()));
  }

  @Test
  public void emailValidate_IncorrectEmailNoUsername_ReturnFalse(){
    emailView.setText("@email.com");
    assertThat("Error - no username",emailLayout.getError(),is(notNullValue()));
  }

  @Test
  public void emailValidate_IncorrectEmailEmptyString_ReturnFalse(){
    emailView.setText("");
    assertThat("Error - empty string",emailLayout.getError(),is(notNullValue()));
  }

  @Test
  public void emailValidate_IncorrectEmailNullString_ReturnFalse(){
    emailView.setText(null);
    assertThat("Error - null string",emailLayout.getError(),is(notNullValue()));
  }

}
