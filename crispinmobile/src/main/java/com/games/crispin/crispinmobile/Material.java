package com.games.crispin.crispinmobile;

public class Material
{
    public static Material DEFAULT_MATERIAL = new Material();

    //texture
    //normal map texture

    public Material()
    {
    }

    public boolean isLightingEnabled()
    {
        return false;
    }

    public boolean hasTexture()
    {
        return false;
    }

    public boolean hasNormalMap()
    {
        return false;
    }
}
