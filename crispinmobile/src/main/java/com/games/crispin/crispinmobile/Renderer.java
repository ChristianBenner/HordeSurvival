package com.games.crispin.crispinmobile;

import java.util.ArrayList;

public class Renderer
{
    private ArrayList<RenderObject> renderObjects;

    public Renderer()
    {
        renderObjects = new ArrayList<>();
    }

    public void addRenderObject(RenderObject renderObject)
    {
        this.renderObjects.add(renderObject);
    }

    public void render()
    {
        for(RenderObject renderObject : renderObjects)
        {
            renderObject.getMaterial().getShader().enableIt();

        }
    }
}
