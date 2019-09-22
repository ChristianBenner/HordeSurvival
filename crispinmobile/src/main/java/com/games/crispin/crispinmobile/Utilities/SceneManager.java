package com.games.crispin.crispinmobile.Utilities;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.games.crispin.crispinmobile.Rendering.Data.Colour;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_BLEND;
import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_CULL_FACE;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_TEST;
import static android.opengl.GLES30.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES30.GL_SRC_ALPHA;
import static android.opengl.GLES30.glBlendFunc;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.GLES30.glDisable;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glViewport;

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

    // The default delta time value
    private static final float DEFAULT_DELTA_TIME = 1.0f;

    // The default refresh rate
    private static final float DEFAULT_REFRESH_RATE = 60.0f;

    // Number of frames to skip before re-calculating delta time value
    private static final int FRAMES_TO_CALCULATE = 15;

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

    // Has a position scene been specified
    private boolean startSceneSpecified;

    // The update count (frame count)
    private int updateCount;

    // Delta time value
    private float deltaTime;

    // Target update rate (should be the maximum screen refresh rate)
    private float targetRefreshRate;

    // The start time used in the timing calculations
    private long startNanoTime;

    // Time paused in nanoseconds during the timing calculations
    private long timePausedInNanos;

    /**
     * Retrieve the singleton instance of the scene manager. The scene manager can only be
     * constructed and retrieved via this method. This is because it follows the singleton design
     * pattern which limits only one to be constructed in the application context. If the object
     * has not been constructed yet, it will first be constructed before it is returned
     *
     * @see             Context
     * @see             Scene.Constructor
     * @since           1.0
     */
    public static SceneManager getInstance()
    {
        // Check if a scene manager exists before creating one
        if(sceneManagerInstance == null)
        {
            // If scene manager hasn't been constructed yet, create one
            sceneManagerInstance = new SceneManager();
        }

        return sceneManagerInstance;
    }

    /**
     * Set the initial scene. This can only happen once per application. Use the
     * <code>setScene</code> method to set a new scene. You must provide a position scene to make
     * use of the <code>SceneManager</code>. From the position scene you can later determine what
     * scenes to run next. You must provide a <code>Scene.Constructor</code> type containing a
     * lambda that constructs the scene. This is because it is up to the <code>SceneManager</code>
     * how to to manage and control Scenes.
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
        // Check if there is already a position scene
        if(currentScene == null)
        {
            startSceneSpecified = true;
            setScene(startSceneConstructor);
        }
        else
        {
            Logger.error(TAG,
                    "Failed to set position scene as one has already been specified");
        }
    }

    /**
     * This will replace the current scene being rendered and updated to one associated to the
     * specified constructor lambda. Once the current scene has finished updating and rendering, the
     * associated scene will be constructed and then set. Note that the method can only be used once
     * a position scene has been set using the <code>setStartScene</code> method.
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
        // Check if the a position scene has been selected first
        if(!startSceneSpecified)
        {
            Logger.error(TAG, "Failed to set scene because no position scene has been " +
                    "specified. Use method 'setStartScene' before set 'setScene'");
        }
        else
        {
            currentSceneConstructor = sceneConstructor;
            currentScene = null;

            // Clear the shader and texture cache. The data in the previous scene is no longer
            // relevant so it should be freed.
            ShaderCache.removeAll();
            TextureCache.removeAll();
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
    public boolean isAlphaEnabled()
    {
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
    public boolean isCullFaceEnabled()
    {
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
     * Set the target refresh rate for the update timing method.
     *
     * @since 1.0
     */
    public void setTargetRefreshRate(float targetRefreshRate)
    {
        this.targetRefreshRate = targetRefreshRate;
    }

    /**
     * The method is overridden from <code>GLSurfaceView.Renderer</code>, it is called when the
     * surface gets created. At this position OpenGL ES memory has been destroyed so its a good time
     * to re-initialise components that depend on this memory.
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
        // Check if there is currently a scene bound (before attempting to re-initialise its
        // OpenGL ES memory components)
        if(currentScene != null)
        {
            // Re-initialise the shaders and textures because there memory no longer exists
            ShaderCache.reinitialiseAll();
            TextureCache.reinitialiseAll();
        }

        resetTimingValues();
    }

    /**
     * The method is overridden from <code>GLSurfaceView.Renderer</code>, it is called when the
     * surface changes. From this position, changes in surface width and height can be detected and
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
    public void onSurfaceChanged(GL10 gl,
                                 int width,
                                 int height)
    {
        this.surfaceWidth = width;
        this.surfaceHeight = height;

        resetTimingValues();

        // Set the OpenGL viewport to fill the entire surface
        glViewport(0,
                0,
                width,
                height);
    }

    /**
     * The method is overridden from <code>GLSurfaceView.Renderer</code>, it is called when then
     * surface is ready to be rendered (which can be many times per second). From this position,
     * rendering and logic update mechanics have be implemented so that graphical output can be
     * processed.
     *
     * @param gl    A reference to the GL10 library. This is a legacy parameter that no longer has a
     *              use (due to the usage of a newer OpenGL version).
     * @see         GLSurfaceView.Renderer
     * @since       1.0
     */
    @Override
    public void onDrawFrame(GL10 gl)
    {
        // Construct the current scene if it hasn't been already
        if(currentScene == null)
        {
            constructCurrentScene();
        }

        // This method calls the update functionality with a delta time value so that all things
        // updated move at the same speed independent on the rate the update function is called.
        if(updateCount == FRAMES_TO_CALCULATE)
        {
            deltaTime = targetRefreshRate / (1000 /
                    (((System.nanoTime() - startNanoTime - timePausedInNanos) / 1000000) /
                            (float)FRAMES_TO_CALCULATE));
            startNanoTime = System.nanoTime();

            //  System.out.println("Debug : DeltaTime (" + deltaTime + ")");
            updateCount = 0;
        }

        updateCount++;

        currentScene.update(deltaTime);

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
        if (currentScene != null)
        {
            currentScene.render();
        }
    }

    /**
     * The constructor for the scene manager sets the member variables for later usage. The
     * application context is first supplied to the scene manager from this position.
     *
     * @since 1.0
     */
    private SceneManager()
    {
        currentScene = null;
        currentSceneConstructor = null;
        setBackgroundColour(DEFAULT_BACKGROUND_COLOUR);
        setDepthState(DEFAULT_DEPTH_ENABLED_STATE);
        setAlphaState(DEFAULT_ALPHA_ENABLED_STATE);
        setCullFaceState(DEFAULT_CULL_FACE_ENABLED_STATE);
        surfaceWidth = 0;
        surfaceHeight = 0;
        startSceneSpecified = false;
        targetRefreshRate = DEFAULT_REFRESH_RATE;
        resetTimingValues();
    }

    /**
     * Used to construct the current scene using its <code>Scene.Constructor</code>. It uses the
     * lambda provided with the scene constructor object to create the scene and then set it as
     * the current scene (replacing the scene before it). This means that the previous scene looses
     * all references and will therefor be destroyed by Java.
     *
     * @since 1.0
     */
    private void constructCurrentScene()
    {
        // Check if the current scene has a constructor
        if(currentSceneConstructor != null)
        {
            Logger.info("Constructing current scene");

            // Create the scene via its constructor lambda and then set it as the current scene
            currentScene = currentSceneConstructor.init();
        }
        else
        {
            Logger.error(TAG, "Cannot construct the current scene because no scene " +
                            "constructor has been provided");
        }
    }

    /**
     * Reset the timing values used in the delta time calculation. This will restore them to default
     * values.
     *
     * @since 1.0
     */
    private void resetTimingValues()
    {
        updateCount = 0;
        deltaTime = DEFAULT_DELTA_TIME;
        startNanoTime = System.nanoTime();
        timePausedInNanos = 0;
    }
}
