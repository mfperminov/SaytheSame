package com.mperminov.saythesame

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.MediumTest
import android.support.test.filters.SmallTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mperminov.saythesame.EspressoTextInputLayoutTest.onErrorViewWithinTilWithId
import com.mperminov.saythesame.R.id
import com.mperminov.saythesame.ui.login.LoginActivity
import com.mperminov.saythesame.ui.login.fragments.SignInFragment
import com.mperminov.saythesame.ui.login.fragments.SignUpFragment
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityUiFlowTests {

  @Rule
  @JvmField
  var loginActivityRule: ActivityTestRule<LoginActivity> =
    ActivityTestRule(LoginActivity::class.java)

  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getTargetContext()
    assertEquals("com.mperminov.saythesame", appContext.packageName)
  }

  @Test
  @SmallTest
  fun switchingFragments() {
    onView(withId(R.id.su_btn_signin))
        .perform(click())
    onView(withId(R.id.sign_in_layout)).check(matches(isDisplayed()))
    onView(withId(R.id.si_btn_signup))
        .perform(click())
    onView(withId(R.id.sign_up_layout)).check(matches(isDisplayed()))
  }

  @Test
  @MediumTest
  // must give error if trying to create account with empty input
  fun createAccountEmptyInput() {
    onView(withId(R.id.su_et_email)).perform(clearText(), replaceText(""))
    onView(withId(R.id.su_et_password)).perform(clearText(), replaceText(""))
    onView(withId(R.id.su_et_nick)).perform(clearText(), replaceText(""))
    onView(withId(R.id.su_btn_signup))
        .perform(click())
    onErrorViewWithinTilWithId(R.id.su_til_email).check(
        matches(withText(loginActivityRule.activity.getString(R.string.empty_email)))
    )
    onErrorViewWithinTilWithId(R.id.su_til_password).check(
        matches(withText(loginActivityRule.activity.getString(R.string.empty_password)))
    )

  }

  @Test
  @MediumTest
  // must give error if trying to sign in with empty input
  fun signInEmptyInput() {
    switchToFragment("signIn")
    onView(withId(R.id.si_et_email)).perform(clearText(), replaceText(""))
    onView(withId(R.id.si_et_password)).perform(clearText(), replaceText(""))
    onView(withId(R.id.si_btn_signin))
        .perform(click())
    onErrorViewWithinTilWithId(R.id.si_til_email).check(
        matches(withText(loginActivityRule.activity.getString(R.string.empty_email)))
    )
    onErrorViewWithinTilWithId(R.id.si_til_password).check(
        matches(withText(loginActivityRule.activity.getString(R.string.empty_password)))
    )

  }

  private fun switchToFragment(fragmentTag: String) {
    val ft = loginActivityRule.activity
        .supportFragmentManager
        .beginTransaction()

    val fragment = if (fragmentTag == "signUp") SignUpFragment() else SignInFragment()
    ft.replace(id.fragment_container, fragment, fragmentTag)
    ft.disallowAddToBackStack()
    ft.commit()
  }
}
