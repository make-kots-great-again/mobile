package com.morgan.make_kots_great_again;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestLogin {
    NetworkChecks networkChecks = new NetworkChecks();

    @Test
    public void test_isHostUp(){
        // Test passed if line 1-2 are true and line 3-4 are false
        assertTrue(networkChecks.isHostUp("8.8.8.8")); // Ok
        assertTrue(networkChecks.isHostUp("google.com")); // Ok
        assertFalse(networkChecks.isHostUp("aaaa")); // Not Ok
        assertFalse(networkChecks.isHostUp("192.168.0.99")); // Not Ok
    }
}