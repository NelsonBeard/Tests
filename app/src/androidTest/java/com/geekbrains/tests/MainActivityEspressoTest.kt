package com.geekbrains.tests

import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.geekbrains.tests.view.search.MainActivity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainActivityEspressoTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val searchEditText = it.findViewById<EditText>(R.id.searchEditText)
            assertNotNull(searchEditText)
        }
    }

    @Test
    fun activityButton_NotNull() {
        scenario.onActivity {
            val toDetailsButton = it.findViewById<Button>(R.id.toDetailsActivityButton)
            assertNotNull(toDetailsButton)
        }
    }

    @Test
    fun activityTextView_HasHint() {
        val assertion: ViewAssertion = matches(withHint("Enter keyword e.g. android"))
        onView(withId(R.id.searchEditText)).check(assertion)
    }

    @Test
    fun activityTextView_IsCompletelyDisplayed() {
        onView(withId(R.id.searchEditText)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun activityButton_IsCompletelyDisplayed() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText))
            .perform(click())
            .perform(replaceText("gitHub"))
            .perform(pressImeActionButton())

        onView(withId(R.id.totalCountSearchTextView)).check(
            matches(
                withText(
                    "Number of results: 10393279"
                )
            )
        )
    }

    @After
    fun close() {
        scenario.close()
    }
}