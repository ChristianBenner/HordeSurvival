package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

/**
 * A class that is currently under development. It is used to load OBJ models on another thread (in
 * the background) so that they can be added into a scene in real time. This reduces the load time
 * at the beginning of a scene but not all models may be visible so it is recommended to instead
 * present a loading bar whilst the threaded models load. The class uses the OBJModelLoader to load
 * OBJ models and does it on another thread so that it doesn't lag or pause the main loop.
 *
 * @see         RenderObject
 * @see         OBJModelLoader
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class OBJThreadTest implements Runnable
{
    // The resource ID of the model
    private int resourceId;

    // The render object
    private RenderObject renderObject;

    // Whether or not the model has been loaded or not
    private boolean complete = false;

    /**
     * Create an OBJThreadTest obj to load an OBJ model on another thread
     *
     * @param resourceId    The OBJ model file resource ID
     * @since   1.0
     */
    public OBJThreadTest(int resourceId)
    {
        this.resourceId = resourceId;
    }

    /**
     * Overridden from the thread base class
     *
     * @since   1.0
     */
    @Override
    public void run()
    {
        renderObject = OBJModelLoader.readObjFile(resourceId);
        complete = true;
    }

    /**
     * Check if the model has loaded
     *
     * @return  The state of the model loading. True if the model has finished loading, else false
     * @since   1.0
     */
    public boolean isComplete()
    {
        return this.complete;
    }

    /**
     * Get the render object that is loaded
     *
     * @return  The state of the model loading. True if the model has finished loading, else false
     * @since   1.0
     */
    public RenderObject getRenderObject()
    {
        return renderObject;
    }
}
