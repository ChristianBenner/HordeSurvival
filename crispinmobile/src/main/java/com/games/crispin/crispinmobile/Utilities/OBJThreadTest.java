package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

public class OBJThreadTest implements Runnable {
    private int resourceId;
    private RenderObject renderObject;
    private boolean complete = false;

    public OBJThreadTest(int resourceId)
    {
        this.resourceId = resourceId;
    }

    @Override
    public void run()
    {
        renderObject = OBJModelLoader.readObjFile(resourceId);
        complete = true;
    }

    public boolean isComplete()
    {
        return this.complete;
    }

    public RenderObject getRenderObject()
    {
        return renderObject;
    }
}
