package com.games.crispin.crispinmobile.Rendering.Utilities;

public class Material
{
    public static Material DEFAULT_MATERIAL = new Material();

    //texture
    //normal map texture

    // colour
    //

    private Texture texture;

    public Material()
    {
        texture = null;
    }

    public Material(Texture texture)
    {
        this.texture = texture;
    }

    public Texture getTexture()
    {
        return this.texture;
    }

    public boolean isLightingEnabled()
    {
        return false;
    }

    public boolean hasTexture()
    {
        return texture != null;
    }

    public boolean hasNormalMap()
    {
        return false;
    }
}
