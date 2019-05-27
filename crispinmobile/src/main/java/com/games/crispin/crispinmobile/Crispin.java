package com.games.crispin.crispinmobile;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;

/**
 * Crispin class provides core engine functionality. It is crucial in order to start a graphics
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
    // Store the instance of the engine
    private static Crispin crispinInstance = null;

    /**
     * Draws as much of the specified image as is currently available
     * with its northwest corner at the specified coordinate (x, y).
     * This method will return immediately in all cases, even if the
     * entire image has not yet been scaled, dithered and converted
     * for the current output device.
     *
     * @param appCompatActivity     Reference to the application that called the function. It is
     *                              used so that the engine can take control of what is shown. The
     *                              engine also uses it to retrieve the application context and pass
     *                              it down to other components or scenes (this can be useful when
     *                              loading in a texture file for example).
     * @param startSceneConstructor A reference to the scene constructor lambda. This will be used
     *                              to construct the provided scene. It is best for the engine to
     *                              control how and when the scenes are initialised so that the
     *                              user doesn't have to worry about memory management and can
     *                              switch from a global/static context.
     * @see                         AppCompatActivity
     * @see                         Scene.Constructor
     * @since                       1.0
     */
    public static void init(AppCompatActivity appCompatActivity, Scene.Constructor startSceneConstructor)
    {
        crispinInstance = new Crispin(appCompatActivity);
        crispinInstance.sceneManager.setStartScene(startSceneConstructor);
    }

    /**
     * Checks whether or not the engine has been initialised. If the engine hasn't been initialised
     * then an error message is printed.
     *
     * @return  <code>true</code> if engine is initialised, <code>false</code> otherwise
     * @since   1.0
     */
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

    /**
     * Set the background colour of the graphics view.
     *
     * @param backgroundColour  The desired background colour.
     * @see                     Colour
     * @since                   1.0
     */
    public static void setBackgroundColour(Colour backgroundColour)
    {
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
        if(isInit())
        {
            return crispinInstance.sceneManager.getBackgroundColour();
        }

        return Colour.BLACK;
    }

    /**
     * Set the depth state boolean. Setting to <code>true</code> allows depth processing - essential
     * to most 3D graphical applications. Depth processing means that the Z buffer will be taken
     * into consideration when drawing objects in-front or behind each other. If
     * <code>false</code>, the objects would be rendered on-top of each-other in the order of their
     * render calls (suitable for 2D graphical applications).
     *
     * @param depthState    The desired background colour.
     * @since               1.0
     */
    public static void setDepthState(boolean depthState)
    {
        if(isInit()) {
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
