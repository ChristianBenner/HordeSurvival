package com.games.crispin.crispinmobile;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ALPHA;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

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

    private Scene currentScene;

    // The default background colour of the graphics surface
    private static final Colour DEFAULT_BACKGROUND_COLOUR =
            new Colour(0.25f, 0.25f, 0.25f);

    // The default state of depth being enabled
    private static final boolean DEFAULT_DEPTH_ENABLED_STATE = true;

    // The default state of alpha being enabled
    private static final boolean DEFAULT_ALPHA_ENABLED_STATE = true;

    // The default state of cull face being enabled
    private static final boolean DEFAULT_CULL_FACE_ENABLED_STATE = true;

    // The current background colour of the graphics surface
    private Colour backgroundColour;

    // Should the rendering take depth into consideration (not drawing order)
    private boolean depthEnabled;

    // Should the rendering support alpha colour channel
    private boolean alphaEnabled;

    // Should the rendering cull faces (faces that aren't in view)
    private boolean cullFaceEnabled;

    private final Context CONTEXT;

    private SceneManager(Context context)
    {
        this.CONTEXT = context;
        currentScene = null;

        setBackgroundColour(DEFAULT_BACKGROUND_COLOUR);
        setDepthState(DEFAULT_DEPTH_ENABLED_STATE);
        setAlphaState(DEFAULT_ALPHA_ENABLED_STATE);
        setCullFaceState(DEFAULT_CULL_FACE_ENABLED_STATE);
    }

    public void setBackgroundColour(Colour backgroundColour)
    {
        this.backgroundColour = backgroundColour;
    }

    public Colour getBackgroundColour()
    {
        return this.backgroundColour;
    }

    public void setDepthState(boolean depthState)
    {
        this.depthEnabled = depthState;
    }

    public boolean isDepthEnabled()
    {
        return this.depthEnabled;
    }

    public void setAlphaState(boolean alphaState)
    {
        this.alphaEnabled = alphaState;
    }

    public boolean isAlphaEnabled() {
        return this.alphaEnabled;
    }

    public void setCullFaceState(boolean cullFaceState)
    {
        this.cullFaceEnabled = cullFaceState;
    }

    public boolean isCullFaceEnabled() {
        return cullFaceEnabled;
    }

    private HashMap<Integer, Scene.Constructor> sceneConstructorMap = new HashMap<Integer, Scene.Constructor>();

    public void setScene(Scene.Constructor sceneConstructor)
    {
        currentScene = sceneConstructor.init(CONTEXT);
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
        // Always clear the buffer bit
        glClear(GL_COLOR_BUFFER_BIT);

        // If depth is enabled, clear the depth buffer bit and enable it in OpenGL ES. Otherwise
        // disable in OpenGL ES
        if(isDepthEnabled())
        {
            glClear(GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
        }
        else
        {
            glDisable(GL_DEPTH_TEST);
        }

        // If alpha is enabled, enable blend functionality in OpenGL ES and supply a blend function.
        // Otherwise disable in OpenGL ES
        if(isAlphaEnabled())
        {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
        else
        {
            glDisable(GL_BLEND);
        }

        // If cull face is enabled, enable cull face in OpenGL ES, otherwise disable in OpenGL ES.
        if(isCullFaceEnabled())
        {
            glEnable(GL_CULL_FACE);
        }
        else
        {
            glDisable(GL_CULL_FACE);
        }

        // Clear the graphics surface to the background colour
        glClearColor(backgroundColour.getRed(),
                backgroundColour.getGreen(),
                backgroundColour.getBlue(),
                backgroundColour.getAlpha());

        // If the current scene exists, render it
        if(currentScene != null)
        {
            currentScene.render();
        }
    }
}
