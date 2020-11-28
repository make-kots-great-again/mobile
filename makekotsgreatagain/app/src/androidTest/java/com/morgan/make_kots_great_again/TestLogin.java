package com.morgan.make_kots_great_again;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestLogin {

    @Rule
    public ActivityScenarioRule<Login> activityRule = new ActivityScenarioRule<>(Login.class);

    //Test de la fonction "isConnectedToInternet()" permettant de savoir si un utilisateur à accès à internet.
    @Test
    public void test_isConnectedToInternet(){
        activityRule.getScenario().onActivity(activity -> {
            Login login = new Login();
            login.isConnectedToInternet(activity);
        });
    }

    //Test de la fonction "launch_page2()" permettant d'accéder à la page2.
    @Test
    public void test_launch_page2(){
        activityRule.getScenario().onActivity(activity -> {
            Login login = new Login();
            login.launch_page2(activity);
        });
    }
}