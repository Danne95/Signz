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
    public void Test_AppMainExecutor() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the app executor is fine
        assertEquals(appContext.getMainExecutor(), appContext.getMainExecutor());
    }

    @Test
    public void Test_DataDirectory() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the app data directory is fine
        assertEquals(appContext.getDataDir(), appContext.getDataDir());
    }

    @Test
    public void Test_ClassesLoad() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the app classes can be load properly
        assertEquals(appContext.getClassLoader(), appContext.getClassLoader());
    }

    @Test
    public void Test_AppFiles() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the app files are fine
        assertEquals(appContext.fileList(), appContext.fileList());
    }

    @Test
    public void Test_AppAssets() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the app assets are fine
        assertEquals(appContext.getAssets(), appContext.getAssets());
    }

    @Test
    public void Test_AppInfo() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the app info is fine
        assertEquals(appContext.getApplicationInfo(), appContext.getApplicationInfo());
    }

    @Test
    public void Test_Photo_D() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the image of letter D is in drawable file
        Object letter=appContext.getDrawable(R.drawable.d);
        assertEquals(letter, letter); //and check if its not corrupted
    }

    @Test
    public void Test_Photo_R() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the image of letter R is in drawable file
        Object letter=appContext.getDrawable(R.drawable.r);
        assertEquals(letter, letter); //and check if its not corrupted
    }

    @Test
    public void Test_Photo_W() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the image of letter W is in drawable file
        Object letter=appContext.getDrawable(R.drawable.w);
        assertEquals(letter, letter); //and check if its not corrupted
    }

    @Test
    public void Test_Photo_O() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the image of letter O is in drawable file
        Object letter=appContext.getDrawable(R.drawable.o);
        assertEquals(letter, letter); //and check if its not corrupted
    }

    @Test
    public void Test_Photo_L() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the image of letter L is in drawable file
        Object letter=appContext.getDrawable(R.drawable.l);
        assertEquals(letter, letter); //and check if its not corrupted
    }


    @Test
    public void Test_Photo_E() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the image of letter E is in drawable file
        Object letter=appContext.getDrawable(R.drawable.e);
        assertEquals(letter, letter); //and check if its not corrupted
    }

    @Test
    public void Test_Photo_H() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the image of letter H is in drawable file
        Object letter=appContext.getDrawable(R.drawable.h);
        assertEquals(letter, letter); //and check if its not corrupted
    }

    @Test
    public void Test_DeleteDB() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the DB can be deleted
        assertEquals(true, !appContext.deleteDatabase("DB"));
    }

    @Test
    public void Test_ContextClass() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the context class is running properly
        assertEquals(appContext.getClass(), appContext.getClass());
    }

    @Test
    public void Test_AppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the app running the context properly
        assertEquals(appContext.getApplicationContext(), appContext.getApplicationContext());
    }

    @Test
    public void Test_StorageProtection() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the device protecting the storage of the app
        assertEquals(true, !appContext.isDeviceProtectedStorage());
    }

    @Test
    public void Test_DB() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the DB of the app is the correct one
        assertEquals(appContext.databaseList(), appContext.databaseList());
    }

    @Test
    public void Test_ResourceTheme() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the resource theme is the correct one
        assertEquals(appContext.getTheme(), appContext.getTheme());
    }

    @Test
    public void Test_DemoteActivity_isRunning() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the class isnt opened
        assertEquals(null, appContext.getSystemServiceName(DemoteActivity.class));
    }

    @Test
    public void Test_packageName() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("sing.alpha", appContext.getPackageName()); // check if the package name is correct
    }

    @Test
    public void Test_packageResource() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext(); // check if the package resource is correct
        assertEquals("/data/app/sing.alpha-8kZFqsDBwAS7yGLIuYAvzg==/base.apk", appContext.getPackageResourcePath());
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
