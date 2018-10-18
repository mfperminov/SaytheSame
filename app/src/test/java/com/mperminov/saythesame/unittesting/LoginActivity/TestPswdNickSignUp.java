package com.mperminov.saythesame.unittesting.LoginActivity;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import com.google.firebase.FirebaseApp;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.ui.Login.LoginActivity;
import com.mperminov.saythesame.ui.Login.fragments.SignUpFragment;
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
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class TestPswdNickSignUp {
  private TextInputEditText pswdView;
  private TextInputLayout pswdLayout;
  private TextInputEditText nickView;
  private TextInputLayout nickLayout;
  private LoginActivity activity;
  private ImageView googleButton;
  private ImageView facebookButton;

  @Before
  public void setUp() {
    FirebaseApp.initializeApp(RuntimeEnvironment.application);
    activity = Robolectric.setupActivity(LoginActivity.class);
    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    SignUpFragment signUpFragment = (SignUpFragment) fragmentManager
        .findFragmentById(R.id.fragment_container);
    //input for password (on sign up)
    pswdView = signUpFragment.getView().findViewById(R.id.su_et_password);
    pswdLayout = signUpFragment.getView().findViewById(R.id.su_til_password);
    //input for nickname
    nickView = signUpFragment.getView().findViewById(R.id.su_et_nick);
    nickLayout = signUpFragment.getView().findViewById(R.id.su_til_nick);
    // third party auth
    googleButton = activity.findViewById(R.id.btn_google);
    facebookButton = activity.findViewById(R.id.btn_facebook);
  }

  @Test
  public void passwordFieldVisible_ReturnTrue() {
    assertTrue(pswdView.isShown());
  }

  @Test
  public void passwordValidate_lengthMoreThan6_ReturnTrue() {
    pswdView.setText("1234567");
    assertThat("No error for valid password", pswdLayout.getError(), is(nullValue()));
  }

  @Test
  public void passwordValidate_equal6_ReturnTrue() {
    pswdView.setText("123456");
    assertThat("No error for valid password", pswdLayout.getError(), is(nullValue()));
  }

  @Test
  public void passwordValidate_lengthLessThan6_ReturnFalse() {
    pswdView.setText("12345");
    assertThat("Error - password is too short", pswdLayout.getError(), is(notNullValue()));
  }

  @Test
  public void passwordValidate_EmptyString_ReturnFalse() {
    pswdView.setText("");
    assertThat("Error - no password provided", pswdLayout.getError(), is(notNullValue()));
  }

  @Test
  public void passwordValidate_NullString_ReturnFalse() {
    pswdView.setText(null);
    assertThat("Error - null password", pswdLayout.getError(), is(notNullValue()));
  }

  @Test
  public void nickFieldVisible_ReturnTrue() {
    assertTrue(nickView.isShown());
  }

  @Test
  public void nickValidate_EmptyString_ReturnFalse() {
    nickView.setText("");
    assertThat("Error - empty nickname", nickLayout.getError(), is(notNullValue()));
  }

  @Test
  public void nickValidate_NullString_ReturnFalse() {
    nickView.setText(null);
    assertThat("Error - null string for nickname", nickLayout.getError(), is(notNullValue()));
  }

  @Test
  public void nickValidate_Exist_ReturnFalse() {
    nickView.setText("mperm");
    assertThat("Error - this nickname already registered", nickLayout.getError(),
        is(notNullValue()));
  }

  @Test
  public void googleSignInButtonIsVisible() {
    assertTrue(googleButton.isShown());
  }

  @Test
  public void facebookSignInButtonIsVisible() {
    assertTrue(facebookButton.isShown());
  }
}
