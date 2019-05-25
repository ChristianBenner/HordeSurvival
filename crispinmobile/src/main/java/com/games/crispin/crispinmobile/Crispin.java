package com.games.crispin.crispinmobile;

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
        if(crispinInstance == null)
        {
            System.err.println("ERROR: Crispin has not been initialised, " +
                    "use Crispin.init(AppCompatActivity)");
        }

        return crispinInstance != null;
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
        if(isInit()) {
            return crispinInstance.sceneManager.isCullFaceEnabled();
        }

        return false;
    }

    public static void setScene(Scene.Constructor sceneConstructor)
    {
        crispinInstance.sceneManager.setScene(sceneConstructor);
    }

/*
    // Add an un-initialised scene to the scene manager
    public static void addScene(Scene scene, Scene.Constructor initFunc)
    {
        if(isInit())
        {
            crispinInstance.sceneManager.addScene(scene, initFunc);
        }
    }

    // Add an initialised scene to the scene manager
    public static void addScene(Scene scene)
    {
        if(isInit())
        {
            crispinInstance.sceneManager.addScene(scene);
        }
    }

    public static void switchScene(Scene scene)
    {
        if(isInit())
        {
            crispinInstance.sceneManager.switchScene(scene);
        }
    }

    // This will initialise a scene that has been previously provided a scene constructor
    public static boolean loadScene(Scene scene)
    {
        if(isInit())
        {
            return crispinInstance.sceneManager.loadScene(scene);
        }

        return false;
    }*/

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

        // Use context to initialise a GLSurfaceView
        glSurfaceView = new GLSurfaceView(CONTEXT);

        // Get the scene manager instance
        sceneManager = SceneManager.getInstance(CONTEXT);

        // Set the renderer to the scene manager
        glSurfaceView.setRenderer(sceneManager);

        // Set the application view to the graphics view
        appCompatActivity.setContentView(glSurfaceView);
    }
}
