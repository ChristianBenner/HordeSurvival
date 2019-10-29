package com.games.crispin.crispinmobile;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.crispinmobile.Utilities.Scene;
import com.games.crispin.crispinmobile.Utilities.SceneManager;

/**
 * Crispin class provides core engine functionality. It is crucial in order to position a graphics
 * application. It can be interacted with via its static public methods, allowing users to access
 * useful methods from anywhere. E.g. Being able to set the scene from within a scene currently
 * running
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Crispin
{
    // Tag for the logger
    private static final String TAG = "Crispin";

    // The target version of OpenGL ES
    public static final int OPENGL_ES_TARGET_VERSION = 3;

    // Required OpenGL ES Version to run the engine
    public static final int REQUIRED_OPENGL_ES_VERSION = 0x30000;

    // Store the static instance of the engine
    private static Crispin crispinInstance = null;

    // Application context
    private final Context CONTEXT;

    // Graphics library surface view
    private GLSurfaceView glSurfaceView;

    // Scene manager
    private SceneManager sceneManager;

    /**
     * Initialises the Crispin engine. Creates a graphical surface and components that enables the
     * engine users to position an application that utilises GPU hardware.
     *
     * @param appCompatActivity     Reference to the application that called the function. It is
     *                              used so that the engine can take control of what is shown. The
     *                              engine also uses it to retrieve the application context and pass
     *                              it down to other components or scenes (this can be useful when
     *                              loading in a texture file for example).
     * @param startSceneConstructor A reference to the scene constructor lambda. This will be used
     *                              to construct the provided scene. It is best for the engine to
     *                              control how and when the scenes are freeTypeInitialised so that
     *                              the user doesn't have to worry about memory management and can
     *                              switch from a global/static context.
     * @see                         AppCompatActivity
     * @see                         Scene.Constructor
     * @since                       1.0
     */
    public static void init(AppCompatActivity appCompatActivity,
                            Scene.Constructor startSceneConstructor)
    {
        crispinInstance = new Crispin(appCompatActivity);
        crispinInstance.sceneManager.setStartScene(startSceneConstructor);
    }

    /**
     * Initialises the Crispin engine. Creates a graphical surface and components that enables the
     * engine users to position an application that utilises GPU hardware.
     *
     * @param appCompatActivity     Reference to the application that called the function. It is
     *                              used so that the engine can take control of what is shown. The
     *                              engine also uses it to retrieve the application context and pass
     *                              it down to other components or scenes (this can be useful when
     *                              loading in a texture file for example).
     * @param frameLayout           The frame layout to place the graphical view onto
     * @param startSceneConstructor A reference to the scene constructor lambda. This will be used
     *                              to construct the provided scene. It is best for the engine to
     *                              control how and when the scenes are freeTypeInitialised so that
     *                              the user doesn't have to worry about memory management and can
     *                              switch from a global/static context.
     * @see                         AppCompatActivity
     * @see                         Scene.Constructor
     * @since                       1.0
     */
    public static void init(AppCompatActivity appCompatActivity,
                            FrameLayout frameLayout,
                            Scene.Constructor startSceneConstructor)
    {
        crispinInstance = new Crispin(appCompatActivity, frameLayout);
        crispinInstance.sceneManager.setStartScene(startSceneConstructor);
    }

    /**
     * Get the application context
     *
     * @return  The application context
     * @see     Context
     * @since   1.0
     */
    public static Context getApplicationContext()
    {
        // Check if the engine is freeTypeInitialised before accessing the context
        if(isInit())
        {
            return crispinInstance.CONTEXT;
        }

        return null;
    }

    /**
     * Flag to the SceneManager to set a new Scene
     *
     * @param sceneConstructor  The new scene to change to.
     * @see                     SceneManager
     * @see                     Scene.Constructor
     * @since                   1.0
     */
    public static void setScene(Scene.Constructor sceneConstructor)
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            crispinInstance.sceneManager.setScene(sceneConstructor);
        }
    }

    /**
     * Set the background colour of the graphics view.
     *
     * @param backgroundColour  The desired background colour.
     * @see                     Colour
     * @since                   1.0
     */
    public static void setBackgroundColour(Colour backgroundColour)
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            crispinInstance.sceneManager.setBackgroundColour(backgroundColour);
        }
    }

    /**
     * Get the background colour of the graphics view.
     *
     * @return  The current background colour
     * @see     Colour
     * @since   1.0
     */
    public static Colour getBackgroundColour()
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            return crispinInstance.sceneManager.getBackgroundColour();
        }

        return Colour.BLACK;
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
    public static void setDepthState(boolean depthState)
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            crispinInstance.sceneManager.setDepthState(depthState);
        }
    }

    /**
     * Get the depth state
     *
     * @return  <code>true</code> if depth is enabled, otherwise <code>false</code>
     * @since   1.0
     */
    public static boolean isDepthEnabled()
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            return crispinInstance.sceneManager.isDepthEnabled();
        }

        return false;
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
    public static void setAlphaState(boolean alphaState)
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            crispinInstance.sceneManager.setAlphaState(alphaState);
        }
    }

    /**
     * Get the alpha state
     *
     * @return  <code>true</code> if alpha is enabled, otherwise <code>false</code>
     * @since   1.0
     */
    public static boolean isAlphaEnabled()
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            return crispinInstance.sceneManager.isAlphaEnabled();
        }

        return false;
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
    public static void setCullFaceState(boolean cullFaceState)
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            crispinInstance.sceneManager.setCullFaceState(cullFaceState);
        }
    }

    /**
     * Get the cull face state
     *
     * @return  <code>true</code> if face culling is enabled, otherwise <code>false</code>
     * @since   1.0
     */
    public static boolean isCullFaceEnabled()
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            return crispinInstance.sceneManager.isCullFaceEnabled();
        }

        return false;
    }

    /**
     * Get the graphics surface width
     *
     * @return  An integer of the graphics surface width in pixels
     * @since               1.0
     */
    public static int getSurfaceWidth()
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            return crispinInstance.sceneManager.getSurfaceWidth();
        }

        return 0;
    }

    /**
     * Get the graphics surface height
     *
     * @return  An integer of the graphics surface height in pixels
     * @since               1.0
     */
    public static int getSurfaceHeight()
    {
        // Check if the engine is freeTypeInitialised before accessing the scene manager
        if(isInit())
        {
            return crispinInstance.sceneManager.getSurfaceHeight();
        }

        return 0;
    }

    /**
     * Checks whether or not the engine has been freeTypeInitialised. If the engine hasn't been freeTypeInitialised
     * then an error message is printed.
     *
     * @return  <code>true</code> if engine is freeTypeInitialised, <code>false</code> otherwise
     * @since   1.0
     */
    private static boolean isInit()
    {
        // Initialised state
        boolean initialised = false;

        // Check if the engine scene manager objects have been created, if they have, return true
        if(crispinInstance != null && crispinInstance.sceneManager != null)
        {
            initialised = true;
        }
        else
        {
            Logger.error(TAG, "ERROR: Crispin has not been freeTypeInitialised, " +
                    "use Crispin.init(AppCompatActivity)");
        }

        return initialised;
    }

    /**
     * Constructs the Crispin engine object. The object handles the major components to the
     * engine such as the graphics surface and the scene manager. Constructor is private because
     * only one should exist at one time. Construction of the object is made strict through a static
     * initialisation function. If the engine fails to position correctly it will use Android UI to
     * inform the application user of errors.
     *
     * @param appCompatActivity Reference to the application that called the function. It is used so
     *                          that the engine can take control of what is shown. The engine also
     *                          uses it to retrieve the application context and pass it down to
     *                          other components or scenes (this can be useful when loading in a
     *                          texture file for example).
     * @see                     AppCompatActivity
     * @see                     Scene.Constructor
     * @see                     #init(AppCompatActivity, Scene.Constructor)
     * @since                   1.0
     */
    private Crispin(AppCompatActivity appCompatActivity)
    {
        // Get the application context
        this.CONTEXT = appCompatActivity.getApplicationContext();

        // Check if OpenGL ES is supported before continuing
        if(isOpenGLESSupported())
        {
            // Use context to initialise a GLSurfaceView
            glSurfaceView = new GLSurfaceView(CONTEXT);

            // Tell the application to use OpenGL ES 3.0
            glSurfaceView.setEGLContextClientVersion(OPENGL_ES_TARGET_VERSION);

            // Get the scene manager instance
            sceneManager = SceneManager.getInstance();

            // Get the display refresh rate
            final float DISPLAY_REFRESH_RATE = appCompatActivity.getWindowManager().
                    getDefaultDisplay().getRefreshRate();

            sceneManager.setTargetRefreshRate(DISPLAY_REFRESH_RATE);

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

    /**
     * Constructs the Crispin engine object. The object handles the major components to the
     * engine such as the graphics surface and the scene manager. Constructor is private because
     * only one should exist at one time. Construction of the object is made strict through a static
     * initialisation function. If the engine fails to position correctly it will use Android UI to
     * inform the application user of errors.
     *
     * @param appCompatActivity Reference to the application that called the function. It is used so
     *                          that the engine can take control of what is shown. The engine also
     *                          uses it to retrieve the application context and pass it down to
     *                          other components or scenes (this can be useful when loading in a
     *                          texture file for example).
     * @param frameLayout       The frame layout to add the graphical view onto
     * @see                     AppCompatActivity
     * @see                     Scene.Constructor
     * @see                     #init(AppCompatActivity, Scene.Constructor)
     * @since                   1.0
     */
    private Crispin(AppCompatActivity appCompatActivity,
                    FrameLayout frameLayout)
    {
        // Get the application context
        this.CONTEXT = appCompatActivity.getApplicationContext();

        // Check if OpenGL ES is supported before continuing
        if(isOpenGLESSupported())
        {
            // Use context to initialise a GLSurfaceView
            glSurfaceView = new GLSurfaceView(CONTEXT);

            // Tell the application to use OpenGL ES 3.0
            glSurfaceView.setEGLContextClientVersion(OPENGL_ES_TARGET_VERSION);

            // Get the scene manager instance
            sceneManager = SceneManager.getInstance();

            // Get the display refresh rate
            final float DISPLAY_REFRESH_RATE = appCompatActivity.getWindowManager().
                    getDefaultDisplay().getRefreshRate();

            sceneManager.setTargetRefreshRate(DISPLAY_REFRESH_RATE);

            // Set the renderer to the scene manager
            glSurfaceView.setRenderer(sceneManager);

            // Set the application view to the graphics view
            frameLayout.addView(glSurfaceView);
        }
        else
        {
            // Set the application view to the graphics view
            appCompatActivity.setContentView(R.layout.activity_unsupported_device);
        }
    }

    /**
     * Checks if the minimum OpenGL ES version (version 2.0), is supported. The check is performed
     * by receiving configuration information associated to the devices hardware.
     *
     * @return  <code>true</code> if engine is freeTypeInitialised, <code>false</code> otherwise
     * @since   1.0
     */
    private boolean isOpenGLESSupported()
    {
        return ((ActivityManager) CONTEXT.getSystemService(Context.ACTIVITY_SERVICE))
                .getDeviceConfigurationInfo().reqGlEsVersion >= REQUIRED_OPENGL_ES_VERSION;
    }
}
