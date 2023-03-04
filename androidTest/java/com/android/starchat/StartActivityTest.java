package com.android.starchat;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.starchat.ui.uiStart.StartActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StartActivityTest {

    @Test
    public void testActivityInView() {
        ActivityScenario.launch(StartActivity.class);
        onView(withId(R.id.startActivity)).check(matches(isDisplayed()));
    }
}