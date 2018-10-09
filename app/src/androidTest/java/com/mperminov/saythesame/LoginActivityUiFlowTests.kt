package com.mperminov.saythesame

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.MediumTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mperminov.saythesame.ui.Login.LoginActivity
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
  @MediumTest
  fun switchingFragments() {
    onView(withId(R.id.button_sign_in))
        .perform(click())
    onView(withId(R.id.sign_in_layout)).check(matches(isDisplayed()))
    onView(withId(R.id.button_sign_up))
        .perform(click())
    onView(withId(R.id.sign_up_layout)).check(matches(isDisplayed()))
  }

}
