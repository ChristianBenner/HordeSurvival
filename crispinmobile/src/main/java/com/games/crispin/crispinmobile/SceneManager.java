package com.games.crispin.crispinmobile;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

public class SceneManager implements GLSurfaceView.Renderer
{
    // Singleton instance of scene manager
    private static SceneManager sceneManagerInstance;

    // Only allow one instance to be created in application context
    public static SceneManager getInstance(Context context)
    {
        if(sceneManagerInstance == null)
        {
            sceneManagerInstance = new SceneManager(context);
        }

        return sceneManagerInstance;
    }

    class SceneConstructorPair
    {
        public Scene scene;
        public Scene.Constructor constructor;

        public SceneConstructorPair(Scene scene, Scene.Constructor constructor)
        {
            this.scene = scene;
            this.constructor = constructor;
        }
    }

    private ArrayList<Scene> scenes;
    private ArrayList<SceneConstructorPair> scenesAndConstructors;
    private Scene currentScene;

    private final Context CONTEXT;

    private SceneManager(Context context)
    {
        this.CONTEXT = context;
        scenes = new ArrayList<>();
        scenesAndConstructors = new ArrayList<>();
        currentScene = null;
    }

    public void addScene(Scene scene, Scene.Constructor initFunc)
    {
        // Add scene if it doesn't already exist
        if(!scenes.contains(scene))
        {
            scenes.add(scene);

            // Add constructor pair if constructor is provided
            if(initFunc != null)
            {
                scenesAndConstructors.add(new SceneConstructorPair(scene, initFunc));
            }
        }

        if(currentScene == null)
        {
            switchScene(scene);
        }
    }

    public void addScene(Scene scene)
    {
        addScene(scene, null);
    }

    // Returns true if the scene successfully loads or is loaded already
    // todo: Make threaded
    public boolean loadScene(Scene scene)
    {
        if(scene == null)
        {
            // Construct the scene if it has a constructor
            for(SceneConstructorPair sceneConstructorPair : scenesAndConstructors)
            {
                if(sceneConstructorPair.scene == scene)
                {
                    scene = sceneConstructorPair.constructor.init(CONTEXT);
                    break;
                }
            }

            return scene != null;
        }
        else
        {
            System.out.println("INFO: Scene is already loaded");
            return true;
        }
    }

    private void switchScene(Scene scene)
    {
        // If the scene is uninitialised, find its construct method and run it
        if(scene == null)
        {
            for(SceneConstructorPair sceneConstructorPair : scenesAndConstructors)
            {
                if(sceneConstructorPair.scene == scene)
                {
                    scene = sceneConstructorPair.constructor.init(CONTEXT);
                    break;
                }
            }
        }

        currentScene = scene;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {

    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        if(currentScene != null)
        {
            currentScene.render();
        }
    }
}
