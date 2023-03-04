package com.android.starchat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

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

    @Test
    public void openNewGroupAndClose() {
        ActivityScenario.launch(MainActivity.class);
        Context context = getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        String newGroupText = context.getResources().getString(R.string.menu_main_newGroup);
        onView(withText(newGroupText)).perform(click());
        onView(withId(R.id.fragmentChooser)).check(matches(isDisplayed()));
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressBack();
        onView(withId(R.id.fragmentChooser)).check(doesNotExist());
    }
}