package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;

/**
 * The material class is designed to hold rendering information that can be used on objects.
 * Rendering information such as colour, texture, normal maps and UV multiplier. This allows you to
 * configure how rendered objects appear.
 *
 * @author      Christian Benner
 * @see         Texture
 * @see         Colour
 * @version     %I%, %G%
 * @since       1.0
 */
public class Material
{
    // todo: Add a texture for the normal map. This will be used eventually by lighting shaders

    // Tag used in logging output
    private static final String TAG = "Material";

    // Default Material used by rendering systems when one is not provided
    public static Material DEFAULT_MATERIAL = new Material();

    // Default uv multiplier
    private static final Scale2D DEFAULT_UV_MULTIPLIER = new Scale2D();

    // Default colour
    private static final Colour DEFAULT_COLOUR = Colour.WHITE;

    // Flag position for ignoring position data
    public static final int IGNORE_POSITION_DATA_FLAG = 1;

    // Flag position for ignoring texel texel data
    public static final int IGNORE_TEXEL_DATA_FLAG = 2;

    // Flag position for ignoring colour data
    public static final int IGNORE_COLOUR_DATA_FLAG = 4;

    // The material texture
    private Texture texture;

    // The UV multiplier for the texture
    private Scale2D uvMultiplier;

    // The colour
    private Colour colour;

    // Ignore position data in rendering
    private boolean ignorePositionData;

    // Ignore texel data in rendering
    private boolean ignoreTexelData;

    // Ignore colour data in rendering
    private boolean ignoreColourData;

    public Material(Texture texture, Scale2D uvMultiplier, Colour colour)
    {
        setTexture(texture);
        setUvMultiplier(uvMultiplier);
        setColour(colour);
        this.ignorePositionData = false;
        this.ignoreTexelData = false;
        this.ignoreColourData = false;
    }

    public Material(Texture texture, Colour colour)
    {
        this(texture, DEFAULT_UV_MULTIPLIER, colour);
    }

    public Material(Texture texture, Scale2D uvMultiplier)
    {
        this(texture, uvMultiplier, DEFAULT_COLOUR);
    }

    public Material(Texture texture)
    {
        this(texture, DEFAULT_UV_MULTIPLIER, DEFAULT_COLOUR);
    }

    public Material(Scale2D uvMultiplier)
    {
        this(null, uvMultiplier, DEFAULT_COLOUR);
    }

    public Material()
    {
        this(null, DEFAULT_UV_MULTIPLIER, DEFAULT_COLOUR);
    }

    public void ignoreData(final int dataFlags)
    {
        if((dataFlags & Material.IGNORE_POSITION_DATA_FLAG) == Material.IGNORE_POSITION_DATA_FLAG)
        {
            setIgnorePositionData(true);
        }

        if((dataFlags & Material.IGNORE_TEXEL_DATA_FLAG) == Material.IGNORE_TEXEL_DATA_FLAG)
        {
            setIgnoreTexelData(true);
        }

        if((dataFlags & Material.IGNORE_COLOUR_DATA_FLAG) == Material.IGNORE_COLOUR_DATA_FLAG)
        {
            setIgnoreColourData(true);
        }
    }

    public void setIgnorePositionData(boolean state)
    {
        ignorePositionData = state;
    }

    public boolean isIgnoringPositionData()
    {
        return ignorePositionData;
    }

    public void setIgnoreTexelData(boolean state)
    {
        ignoreTexelData = state;
    }

    public boolean isIgnoringTexelData()
    {
        return ignoreTexelData;
    }

    public void setIgnoreColourData(boolean state)
    {
        ignoreColourData = state;
    }

    public boolean isIgnoringColourData()
    {
        return ignoreColourData;
    }

    public void setUvMultiplier(Scale2D uvMultiplier)
    {
        this.uvMultiplier = uvMultiplier;
    }

    public Scale2D getUvMultiplier()
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
