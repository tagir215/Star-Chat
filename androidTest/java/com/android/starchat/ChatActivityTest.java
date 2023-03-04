package com.android.starchat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.CoreMatchers.allOf;

import android.content.Context;

import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.android.starchat.ui.uiChat.ChatActivity;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {
    @Test
    public void testActivityInView() {
        ActivityScenario.launch(ChatActivity.class);
        onView(withId(R.id.chatActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuOpenGroupInfoAndCloseByPressingBack() {
        openGroupFragment();
        onView(withId(R.id.fragmentGroup)).check(matches(isDisplayed()));
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressBack();
        onView(withId(R.id.fragmentGroup)).check(doesNotExist());
    }

    @Test
    public void testMenuOpenGroupInfoAndCloseByPressingNavigationButton() {
        openGroupFragment();
        onView(withId(R.id.fragmentGroup)).check(matches(isDisplayed()));
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withId(R.id.fragmentGroup)).check(doesNotExist());
    }

    private void openGroupFragment(){
        ActivityScenario.launch(ChatActivity.class);
        Context context = getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        String menuSetting = context.getResources().getString(R.string.groupInfo);
        onView(withText(menuSetting)).check(matches(isDisplayed()));
        onView(withText(menuSetting)).perform(click());
    }
}