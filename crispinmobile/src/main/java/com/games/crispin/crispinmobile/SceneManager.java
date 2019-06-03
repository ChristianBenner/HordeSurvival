package com.games.crispin.crispinmobile;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * SceneManager class handles scene rendering and updates. The scene manager is the initial place
 * where rendering begins. It calculates the timing for updates so that logic can be updated the
 * same way despite frame rate. It is also responsible for handling Android lifecycle based
 * activities such as surface creations and surface changes. It is designed to keep the engine user
 * from needing to know anything about the Android activity lifecycle and the way it affects OpenGL
 * ES memory. The SceneManager inherits from the <code>GLSurfaceView.Renderer</code> in order to
 * implement and handle Android Activity lifecycle methods.
 *
 * @author      Christian Benner
 * @see         Scene
 * @see         GLSurfaceView.Renderer
 * @version     %I%, %G%
 * @since       1.0
 */
public class SceneManager implements GLSurfaceView.Renderer
{
    // Tag used in logging debug and error messages
    private static final String TAG = "SceneManager";

    // Singleton instance of scene manager
    private static SceneManager sceneManagerInstance;

    // The default background colour of the graphics surface
    private static final Colour DEFAULT_BACKGROUND_COLOUR =
            new Colour(0.25f, 0.25f, 0.25f);

    // The default state of depth being enabled
    private static final boolean DEFAULT_DEPTH_ENABLED_STATE = true;

    // The default state of alpha being enabled
    private static final boolean DEFAULT_ALPHA_ENABLED_STATE = true;

    // The default state of cull face being enabled
    private static final boolean DEFAULT_CULL_FACE_ENABLED_STATE = false;

    // The application context
    private Context context;

    // The current scene
    private Scene currentScene;

    // The current scenes constructor (held if the scene manager hasn't been started yet)
    private Scene.Constructor currentSceneConstructor;

    // The current background colour of the graphics surface
    private Colour backgroundColour;

    // Should the rendering take depth into consideration (not drawing order)
    private boolean depthEnabled;

    // Should the rendering support alpha colour channel
    private boolean alphaEnabled;

    // Should the rendering cull faces (faces that aren't in view)
    private boolean cullFaceEnabled;

    // The width of the graphics surface
    private int surfaceWidth;

    // The height o the graphics surface
    private int surfaceHeight;

    // Has a start scene been specified
    private boolean startSceneSpecified;

    /**
     * Retrieve the singleton instance of the scene manager. The scene manager can only be
     * constructed and retrieved via this method. This is because it follows the singleton design
     * pattern which limits only one to be constructed in the application context. If the object
     * has not been constructed yet, it will first be constructed before it is returned
     *
     * @param context   Reference to the application context. The context can be passed to scenes
     *                  that the engine user sets. This is important when creating new scenes for
     *                  example because loading in assets such as map files requires the context.
     * @return          Reference to the SceneManager singleton instance
     * @see             Context
     * @see             Scene.Constructor
     * @since           1.0
     */
    public static SceneManager getInstance(Context context)
    {
        if(sceneManagerInstance == null)
        {
            // If scene manager hasn't been constructed yet, create one
            sceneManagerInstance = new SceneManager(context);
        }
        else
        {
            // If the scene manager already exists, update its context with the one provided
            sceneManagerInstance.updateContext(context);
        }

        return sceneManagerInstance;
    }

    /**
     * Set the initial scene. This can only happen once per application. Use the
     * <code>setScene</code> method to set a new scene. You must provide a start scene to make use
     * of the <code>SceneManager</code>. From the start scene you can later determine what scenes to
     * run next. You must provide a <code>Scene.Constructor</code> type containing a lambda that
     * constructs the scene. This is because it is up to the <code>SceneManager</code> how to to
     * manage and control Scenes.
     *
     * @param startSceneConstructor Start scene constructor lambda. Should contain function that
     *                              constructs a new <code>Scene</code> so that the Scene
     *                              Manager can decide how and when to construct the Scene.
     * @see                         Scene.Constructor
     * @see                         #setScene(Scene.Constructor)
     * @since                       1.0
     */
    public void setStartScene(Scene.Constructor startSceneConstructor)
    {
        if(currentScene == null)
        {
            startSceneSpecified = true;
            setScene(startSceneConstructor);
        }
        else
        {
            Log.error(TAG, "Failed to set start scene as one has already been specified");
        }
    }

    /**
     * This will replace the current scene being rendered and updated to one associated to the
     * specified constructor lambda. Once the current scene has finished updating and rendering, the
     * associated scene will be constructed and then set. Note that the method can only be used once
     * a start scene has been set using the <code>setStartScene</code> method.
     *
     * @param sceneConstructor  Scene constructor lambda. Should contain function that
     *                          constructs a new <code>Scene</code> so that the Scene Manager
     *                          can decide how and when to construct the Scene.
     * @see                     Scene.Constructor
     * @see                     #setStartScene(Scene.Constructor)
     * @since                   1.0
     */
    public void setScene(Scene.Constructor sceneConstructor)
    {
        if(!startSceneSpecified)
        {
            Log.error(TAG, "Failed to set scene because no start scene has been specified. " +
                    "use method 'setStartScene' before set 'setScene'");
        }
        else
        {
            currentSceneConstructor = sceneConstructor;
            currentScene = null;
            ShaderCache.removeAll();
        }
    }

    /**
     * Set the background colour of the graphics surface. This will only take effect at the
     * beginning of the render cycle.
     *
     * @param backgroundColour  The new background colour for the graphics surface
     * @see                     Colour
     * @since                   1.0
     */
    public void setBackgroundColour(Colour backgroundColour)
    {
        this.backgroundColour = backgroundColour;
    }

    /**
     * Get the current background colour of the graphics surface
     *
     * @retrun  The current background colour
     * @see     Colour
     * @since   1.0
     */
    public Colour getBackgroundColour()
    {
        return this.backgroundColour;
    }

    /**
     * Set the depth state boolean.
     *
     * @param depthState    The new depth state. Setting to <code>true</code> allows depth
     *                      processing - a feature essential to 3D application that means that the Z
     *                      buffer will be taken into consideration when drawing objects in-front or
     *                      behind each other. If <code>false</code>, the objects would be rendered
     *                      on-top of each-other in the order of their render calls (suitable for 2D
     *                      graphical applications).
     * @since               1.0
     */
    public void setDepthState(boolean depthState)
    {
        this.depthEnabled = depthState;
    }

    /**
     * Get the depth state
     *
     * @return  <code>true</code> if depth is enabled, otherwise <code>false</code>
     * @since   1.0
     */
    public boolean isDepthEnabled()
    {
        return this.depthEnabled;
    }

    /**
     * Set the alpha state boolean.
     *
     * @param alphaState    The new alpha state. Setting to <code>true</code> allows alpha blending.
     *                      Alpha blending is essential when creating graphics with transparent
     *                      effects. If set to <code>false</code>, objects will have no
     *                      transparency.
     * @since               1.0
     */
    public void setAlphaState(boolean alphaState)
    {
        this.alphaEnabled = alphaState;
    }

    /**
     * Get the alpha state
     *
     * @return  <code>true</code> if alpha is enabled, otherwise <code>false</code>
     * @since   1.0
     */
    public boolean isAlphaEnabled() {
        return this.alphaEnabled;
    }

    /**
     * Set the cull face state boolean.
     *
     * @param cullFaceState The new cull face state. Setting to <code>true</code> allows face
     *                      culling. This culls the faces of any vertices being drawn
     *                      anti-clockwise. This makes for more efficient rendering in 3D because
     *                      it cuts out the need to render any faces that wouldn't be visible. If
     *                      set to <code>false</code>, faces won't be culled.
     *                      transparency.
     * @since               1.0
     */
    public void setCullFaceState(boolean cullFaceState)
    {
        this.cullFaceEnabled = cullFaceState;
    }

    /**
     * Get the cull face state
     *
     * @return  <code>true</code> if face culling is enabled, otherwise <code>false</code>
     * @since   1.0
     */
    public boolean isCullFaceEnabled() {
        return cullFaceEnabled;
    }

    /**
     * Get the graphics surface width
     *
     * @return  An integer of the graphics surface width in pixels
     * @since   1.0
     */
    public int getSurfaceWidth()
    {
        return this.surfaceWidth;
    }

    /**
     * Get the graphics surface height
     *
     * @return  An integer of the graphics surface height in pixels
     * @since   1.0
     */
    public int getSurfaceHeight()
    {
        return this.surfaceHeight;
    }

    /**
     * The method is overridden from <code>GLSurfaceView.Renderer</code>, it is called when the
     * surface gets created. At this point3D OpenGL ES memory has been destroyed so its a good time to
     * re-initialise components that depend on this memory.
     *
     * @param gl        A reference to the GL10 library. This is a legacy parameter that no longer
     *                  has a use (due to the usage of a newer OpenGL version).
     * @param config    Configuration of OpenGL ES
     * @see             EGLConfig
     * @see             GLSurfaceView.Renderer
     * @since           1.0
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        if(currentScene != null)
        {
            // Re-initialise the shaders because there memory no longer exists
            ShaderCache.reinitialiseAll();
        }
    }

    /**
     * The method is overridden from <code>GLSurfaceView.Renderer</code>, it is called when the
     * surface changes. From this point3D, changes in surface width and height can be detected and
     * the graphics surface can be resized so that it fits the new dimensions.
     *
     * @param gl        A reference to the GL10 library. This is a legacy parameter that no longer
     *                  has a use (due to the usage of a newer OpenGL version).
     * @param width     The new width of the surface
     * @param height    The new height of the surface
     * @see             GLSurfaceView.Renderer
     * @since           1.0
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        this.surfaceWidth = width;
        this.surfaceHeight = height;

        // Set the OpenGL viewport to fill the entire surface
        glViewport(0, 0, width, height);
    }

    /**
     * The method is overridden from <code>GLSurfaceView.Renderer</code>, it is called when then
     * surface is ready to be rendered (which can be many times per second). From this point3D,
     * rendering and logic update mechanics have be implemented so that graphical output can be
     * processed.
     *
     * @param gl        A reference to the GL10 library. This is a legacy parameter that no longer
     *                  has a use (due to the usage of a newer OpenGL version).
     * @see             GLSurfaceView.Renderer
     * @since           1.0
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        // Construct the current scene if it hasn't been already
        if(currentScene == null)
        {
            constructCurrentScene();
        }

        // Always clear the buffer bit
        glClear(GL_COLOR_BUFFER_BIT);

        // Clear the graphics surface to the background colour
        glClearColor(backgroundColour.getRed(),
                backgroundColour.getGreen(),
                backgroundColour.getBlue(),
                backgroundColour.getAlpha());

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

        // If the current scene exists, render it
        if (currentScene != null) {
            currentScene.render();
        }
    }

    /**
     * The constructor for the scene manager sets the member variables for later usage. The
     * application context is first supplied to the scene manager from this point3D.
     *
     * @param context   Reference to the application context
     * @see             Context
     * @since           1.0
     */
    private SceneManager(Context context)
    {
        this.context = context;
        currentScene = null;
        currentSceneConstructor = null;
        setBackgroundColour(DEFAULT_BACKGROUND_COLOUR);
        setDepthState(DEFAULT_DEPTH_ENABLED_STATE);
        setAlphaState(DEFAULT_ALPHA_ENABLED_STATE);
        setCullFaceState(DEFAULT_CULL_FACE_ENABLED_STATE);
        surfaceWidth = 0;
        surfaceHeight = 0;
        startSceneSpecified = false;
    }

    /**
     * Used to construct the current scene using its <code>Scene.Constructor</code>. It uses the
     * lambda provided with the scene constructor object to create the scene and then set it as
     * the current scene (replacing the scene before it). This means that the previous scene looses
     * all references and will therefor be destroyed by Java.
     *
     * @since           1.0
     */
    private void constructCurrentScene()
    {
        if(currentSceneConstructor != null)
        {
            Log.info("Constructing current scene");

            // Create the scene via its constructor lambda and then set it as the current scene
            currentScene = currentSceneConstructor.init(context);
        }
        else
        {
            Log.error(TAG, "Cannot construct the current scene because no scene " +
                            "constructor has been provided");
        }
    }

    /**
     * Update the scene managers application context. Whenever a new context has been provided to
     * the scene manger, update it to make sure we have the most modern and up to date context.
     *
     * @param context   A reference to the application context
     * @since           1.0
     */
    private void updateContext(Context context)
    {
        this.context = context;
    }
}
