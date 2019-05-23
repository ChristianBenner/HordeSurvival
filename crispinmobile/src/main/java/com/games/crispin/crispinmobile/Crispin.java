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

    // Add an un-initialised scene to the scene manager
    public static void addScene(Scene scene, Scene.Constructor initFunc)
    {
        if(isInit())
        {
            crispinInstance.pAddScene(scene, initFunc);
        }
    }

    // Add an initialised scene to the scene manager
    public static void addScene(Scene scene)
    {
        if(isInit())
        {
            crispinInstance.pAddScene(scene);
        }
    }

    // This will initialise a scene that has been previously provided a scene constructor
    public static boolean loadScene(Scene scene)
    {
        if(isInit())
        {
            return crispinInstance.pLoadScene(scene);
        }

        return false;
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

        // Use context to initialise a GLSurfaceView
        glSurfaceView = new GLSurfaceView(CONTEXT);

        // Get the scene manager instance
        sceneManager = SceneManager.getInstance(CONTEXT);

        // Set the renderer to the scene manager
        glSurfaceView.setRenderer(sceneManager);

        // Set the application view to the graphics view
        appCompatActivity.setContentView(glSurfaceView);
    }

    private void pAddScene(Scene scene, Scene.Constructor initFunc)
    {
        sceneManager.addScene(scene, initFunc);
    }

    private void pAddScene(Scene scene)
    {
        sceneManager.addScene(scene);
    }

    private boolean pLoadScene(Scene scene)
    {
        return sceneManager.loadScene(scene);
    }
}
