package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Rendering.Data.Colour;

public class Material
{
    // todo: Add a texture for the normal map. This will be used eventually by lighting shaders

    // Tag used in logging output
    private static final String TAG = "Material";

    // Default Material used by rendering systems when one is not provided
    public static Material DEFAULT_MATERIAL = new Material();

    private Texture texture;
    private Colour colour;

    public Material(Texture texture, Colour colour)
    {
        setTexture(texture);
        setColour(colour);
    }

    public Material(Texture texture)
    {
        this(texture, Colour.WHITE);
    }

    public Material()
    {
        this(null);
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public Texture getTexture()
    {
        return this.texture;
    }

    public void setColour(Colour colour)
    {
        this.colour = colour;
    }

    public Colour getColour()
    {
        return this.colour;
    }

    public float[] getColourData()
    {
        return new float[]
                {
                        colour.getRed(),
                        colour.getGreen(),
                        colour.getBlue(),
                        colour.getAlpha()
                };
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
