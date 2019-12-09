package com.example.hands_showprototype;

import android.content.Context;
import android.content.Intent;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void Test_packageName() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.signz", appContext.getPackageName()); // check if the package name is correct
    }

    @Test
    public void Test_packageResource() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the package resource is correct
        assertEquals("/data/app/com.example.signz-hF_A9rOYO-jxbv0YqXxd8Q==/base.apk", appContext.getPackageResourcePath());
    }

    @Test
    public void Test_mainActivity_isRunning() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the class isnt opened
        assertEquals(null, appContext.getSystemServiceName(MainActivity.class));
    }
    @Test
    public void Test_AdminActivity_isRunning() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the class isnt opened
        assertEquals(null, appContext.getSystemServiceName(AdminActivity.class));
    }
    @Test
    public void Test_LearnLanguage_isRunning() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the class isnt opened
        assertEquals(null, appContext.getSystemServiceName(LearnLanguage.class));
    }
    @Test
    public void Test_PicToTranslate_isRunning() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the class isnt opened
        assertEquals(null, appContext.getSystemServiceName(PicToTranslateActivity.class));
    }
    @Test
    public void Test_ShowPicture_isRunning() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the class isnt opened
        assertEquals(null, appContext.getSystemServiceName(ShowPicture.class));
    }
    @Test
    public void Test_SignIn_isRunning() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the class isnt opened
        assertEquals(null, appContext.getSystemServiceName(SignInActivity.class));
    }
    @Test
    public void Test_SignUp_isRunning() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the class isnt opened
        assertEquals(null, appContext.getSystemServiceName(SignUpActivity.class));
    }

    @Test
    public void Test_cameraPermission() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the camera permission was granted
        assertEquals(-1, appContext.checkPermission("Camera Permission",0,1));
    }


}
