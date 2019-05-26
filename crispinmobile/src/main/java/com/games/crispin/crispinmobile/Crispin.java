package com.games.crispin.crispinmobile;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;

public class Crispin {
    // Store the instance of the engine
    private static Crispin crispinInstance = null;

    public static void init(AppCompatActivity appCompatActivity) {
        // Prevent engine from being re-initialised
        if (crispinInstance == null) {
            crispinInstance = new Crispin(appCompatActivity);
        }
    }

    private static boolean isInit()
    {
        // Initialised state
        boolean initialised = false;

        if(crispinInstance != null && crispinInstance.sceneManager != null)
        {
            initialised = true;
        }
        else
        {
            System.err.println("ERROR: Crispin has not been initialised, " +
                    "use Crispin.init(AppCompatActivity)");
        }

        return initialised;
    }

    public static void setBackgroundColour(Colour backgroundColour)
    {
        if(isInit())
        {
            crispinInstance.sceneManager.setBackgroundColour(backgroundColour);
        }
    }

    public static Colour getBackgroundColour()
    {
        if(isInit())
        {
            return crispinInstance.sceneManager.getBackgroundColour();
        }

        return Colour.BLACK;
    }

    public static void setDepthState(boolean depthState)
    {
        if(isInit()) {
            crispinInstance.sceneManager.setDepthState(depthState);
        }
    }

    public static boolean isDepthEnabled()
    {
        if(isInit())
        {
            return crispinInstance.sceneManager.isDepthEnabled();
        }

        return false;
    }

    public static void setAlphaState(boolean alphaState)
    {
        if(isInit())
        {
            crispinInstance.sceneManager.setAlphaState(alphaState);
        }
    }

    public static boolean isAlphaEnabled()
    {
        if(isInit())
        {
            return crispinInstance.sceneManager.isAlphaEnabled();
        }

        return false;
    }

    public static void setCullFaceState(boolean cullFaceState)
    {
        if(isInit())
        {
            crispinInstance.sceneManager.setCullFaceState(cullFaceState);
        }
    }

    public static boolean isCullFaceEnabled()
    {
        if(isInit())
        {
            return crispinInstance.sceneManager.isCullFaceEnabled();
        }

        return false;
    }

    public static int getSurfaceWidth()
    {
        if(isInit())
        {
            return crispinInstance.sceneManager.getSurfaceWidth();
        }

        return 0;
    }

    public static int getSurfaceHeight()
    {
        if(isInit())
        {
            return crispinInstance.sceneManager.getSurfaceHeight();
        }

        return 0;
    }

    public static void setScene(Scene.Constructor sceneConstructor)
    {
        if(isInit())
        {
            crispinInstance.sceneManager.setScene(sceneConstructor);
        }
    }

    // The application context
    private final Context CONTEXT;

    // The GL surface view
    private GLSurfaceView glSurfaceView;

    // The scene manager
    private SceneManager sceneManager;

    private Crispin(AppCompatActivity appCompatActivity)
    {
        // Get the application context
        this.CONTEXT = appCompatActivity.getApplicationContext();

        if(isOpenGLESSupported())
        {
            // Use context to initialise a GLSurfaceView
            glSurfaceView = new GLSurfaceView(CONTEXT);

            // Tell the application to use OpenGL ES 2.0
            glSurfaceView.setEGLContextClientVersion(2);

            // Get the scene manager instance
            sceneManager = SceneManager.getInstance(CONTEXT);

            // Set the renderer to the scene manager
            glSurfaceView.setRenderer(sceneManager);

            // Set the application view to the graphics view
            appCompatActivity.setContentView(glSurfaceView);
        }
        else
        {
            // Set the application view to the graphics view
            appCompatActivity.setContentView(R.layout.activity_unsupported_device);
        }
    }

    // Check if the OpenGL ES version 2.0 is supported
    private boolean isOpenGLESSupported()
    {
        return ((ActivityManager) CONTEXT.getSystemService(Context.ACTIVITY_SERVICE))
                .getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
    }
}
