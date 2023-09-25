package com.example.githubuserapp.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.githubuserapp.R
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {
    val query = "ernestwidjaja"
    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun searchUser() {
        onView(withId(R.id.searchBar)).perform(click())
        onView(AllOf.allOf(supportsInputMethods(), isDescendantOfA(withId(R.id.searchView)))).perform(
            typeText(query), pressImeActionButton()
        )
        onView(withId(R.id.rvListUser)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun switchTheme() {
        openActionBarOverflowOrOptionsMenu(androidx.test.InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.settings)).perform(click())
        onView(withId(R.id.switch_theme)).perform(click())
    }

    @Test
    fun addShowDeleteFavorite() {
        onView(withId(R.id.rvListUser)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
        onView(withId(R.id.fab_favorite)).perform(click())
        openActionBarOverflowOrOptionsMenu(androidx.test.InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.favorite)).perform(click())
        onView(withId(R.id.rvListUser)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.fab_favorite)).perform(click())
    }

}