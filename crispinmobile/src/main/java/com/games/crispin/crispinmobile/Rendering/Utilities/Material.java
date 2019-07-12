package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;

public class Material
{
    // todo: Add a texture for the normal map. This will be used eventually by lighting shaders

    // Tag used in logging output
    private static final String TAG = "Material";

    // Default Material used by rendering systems when one is not provided
    public static Material DEFAULT_MATERIAL = new Material();

    // Default uv multiplier
    private static final Point2D DEFAULT_UV_MULTIPLIER = new Point2D();

    // Default colour
    private static final Colour DEFAULT_COLOUR = Colour.WHITE;

    private Texture texture;
    private Point2D uvMultiplier;
    private Colour colour;

    public Material(Texture texture, Point2D uvMultiplier, Colour colour)
    {
        setTexture(texture);
        setUvMultiplier(uvMultiplier);
        setColour(colour);
    }

    public Material(Texture texture, Colour colour)
    {
        this(texture, DEFAULT_UV_MULTIPLIER, colour);
    }

    public Material(Texture texture, Point2D uvMultiplier)
    {
        this(texture, uvMultiplier, DEFAULT_COLOUR);
    }

    public Material(Texture texture)
    {
        this(texture, DEFAULT_UV_MULTIPLIER, DEFAULT_COLOUR);
    }

    public Material(Point2D uvMultiplier)
    {
        this(null, uvMultiplier, DEFAULT_COLOUR);
    }

    public Material()
    {
        this(null, DEFAULT_UV_MULTIPLIER, DEFAULT_COLOUR);
    }

    public void setUvMultiplier(Point2D uvMultiplier)
    {
        this.uvMultiplier = uvMultiplier;
    }

    public Point2D getUvMultiplier()
    {
        return this.uvMultiplier;
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
