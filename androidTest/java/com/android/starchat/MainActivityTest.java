package com.android.starchat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.starchat.ui.uiMain.mainActivity.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Test
    public void testIfInView() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewClick(){
        ActivityScenario<MainActivity>scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            IdlingRegistry.getInstance().register(activity.getIdlingResource());
        });
        onView(withId(R.id.mainRecyclerViewGroups)).check(matches(isDisplayed()));
        onView(withId(R.id.mainRecyclerViewGroups)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.chatActivity)).check(matches(isDisplayed()));

    }
}