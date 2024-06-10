package com.dicoding.com.storyapp.ui.view.activity

import android.view.WindowManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.com.storyapp.R
import com.dicoding.com.storyapp.util.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLogin_Success() {
        val successMessage = "Login successful"
        onView(withId(R.id.ed_login_email)).perform(
            typeText("ikr@gmail.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.ed_login_password)).perform(typeText("ikr123456"), closeSoftKeyboard())

        onView(withId(R.id.btn_login)).perform(click())


    }

    @Test
    fun testLogin_Error() {
        val errMessage = "User not found"
        onView(withId(R.id.ed_login_email)).perform(
            typeText("ikr2345@gmail.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.ed_login_password)).perform(
            typeText("wrongpassword"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.btn_login)).perform(click())

        onView(withText(errMessage))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }


    @Test
    fun testLoginPw_Error() {
        val errMessage = "Invalid password"
        onView(withId(R.id.ed_login_email)).perform(
            typeText("ikr@gmail.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.ed_login_password)).perform(
            typeText("wrongpassword"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.btn_login)).perform(click())

        // Add delay to wait for Toast to appear
        Thread.sleep(3000)

        onView(withText(errMessage))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
        onView(withId(R.id.progress_indicator)).check(matches(isDisplayed()))
    }
}

class ToastMatcher : TypeSafeMatcher<Root>() {

    override fun describeTo(description: Description?) {
        description?.appendText("is toast")
    }

    override fun matchesSafely(item: Root?): Boolean {
        if (item != null) {
            val type = item.windowLayoutParams.get().type
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                val windowToken = item.decorView.windowToken
                val appToken = item.decorView.applicationWindowToken
                if (windowToken === appToken) {
                    return true
                }
            }
        }
        return false
    }
}
