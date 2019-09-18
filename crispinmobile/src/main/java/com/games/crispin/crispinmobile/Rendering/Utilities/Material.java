package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;

/**
 * The material class is designed to hold rendering information that can be used on objects.
 * Rendering information such as colour, texture, direction maps and UV multiplier. This allows you
 * to configure how rendered objects appear.
 *
 * @author      Christian Benner
 * @see         RenderObject
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

    /**
     * Construct the material object. Includes all of the material data elements. The constructor
     * sets the texture, uv multiplier and colour of the material.
     *
     * @param texture       A texture to apply over the render object
     * @param uvMultiplier  The amount to multiply the texture UV co-ordinates (can be used for
     *                      tiling)
     * @param colour        A colour to apply uniformly over the render object
     * @see                 Texture
     * @see                 Scale2D
     * @see                 Colour
     * @since               1.0
     */
    public Material(Texture texture,
                    Scale2D uvMultiplier,
                    Colour colour)
    {
        setTexture(texture);
        setUvMultiplier(uvMultiplier);
        setColour(colour);
        this.ignorePositionData = false;
        this.ignoreTexelData = false;
        this.ignoreColourData = false;
    }

    /**
     * Construct the material object. The constructor sets the texture and colour of the material.
     *
     * @param texture       A texture to apply over the render object
     * @param colour        A colour to apply uniformly over the render object
     * @see                 Texture
     * @see                 Colour
     * @since               1.0
     */
    public Material(Texture texture, Colour colour)
    {
        this(texture,
                DEFAULT_UV_MULTIPLIER,
                colour);
    }

    /**
     * Construct the material object. The constructor sets the texture and uv multiplier.
     *
     * @param texture       A texture to apply over the render object
     * @param uvMultiplier  The amount to multiply the texture UV co-ordinates (can be used for
     *                      tiling)
     * @see                 Texture
     * @see                 Scale2D
     * @since               1.0
     */
    public Material(Texture texture, Scale2D uvMultiplier)
    {
        this(texture,
                uvMultiplier,
                DEFAULT_COLOUR);
    }

    /**
     * Construct the material object. The constructor sets the uv multiplier and colour of the
     * material.
     *
     * @param uvMultiplier  The amount to multiply the texture UV co-ordinates (can be used for
     *                      tiling)
     * @param colour        A colour to apply uniformly over the render object
     * @see                 Scale2D
     * @see                 Colour
     * @since               1.0
     */
    public Material(Scale2D uvMultiplier, Colour colour)
    {
        this(null,
                uvMultiplier,
                colour);
    }

    /**
     * Construct the material object. The constructor sets the texture of the material.
     *
     * @param texture       A texture to apply over the render object
     * @see                 Texture
     * @since               1.0
     */
    public Material(Texture texture)
    {
        this(texture,
                DEFAULT_UV_MULTIPLIER,
                DEFAULT_COLOUR);
    }

    /**
     * Construct the material object. The constructor sets the colour of the material.
     *
     * @param colour        A texture to apply over the render object
     * @see                 Texture
     * @since               1.0
     */
    public Material(Colour colour)
    {
        this(null,
                DEFAULT_UV_MULTIPLIER,
                colour);
    }

    /**
     * Construct the material object. The constructor sets the uv multiplier of the material.
     *
     * @param uvMultiplier  The amount to multiply the texture UV co-ordinates (can be used for
     *                      tiling)
     * @see                 Scale2D
     * @since               1.0
     */
    public Material(Scale2D uvMultiplier)
    {
        this(null,
                uvMultiplier,
                DEFAULT_COLOUR);
    }

    /**
     * Construct the material object. Sets up the material data elements with the default values
     *
     * @since 1.0
     */
    public Material()
    {
        this(null,
                DEFAULT_UV_MULTIPLIER,
                DEFAULT_COLOUR);
    }

    /**
     * Specify what data elements should be ignored by the renderer. Elements ignored by the
     * renderer may prevent some material preferences from being considered e.g. not being able to
     * display a texture because the texture co-ordinate data is ignored
     *
     * @param dataFlags The flags for the data elements that should be ignored
     * @since           1.0
     */
    public void ignoreData(final int dataFlags)
    {
        // Check if the data flag contains ignore position data flag
        if((dataFlags & Material.IGNORE_POSITION_DATA_FLAG) == Material.IGNORE_POSITION_DATA_FLAG)
        {
            setIgnorePositionData(true);
        }

        // Check if the data flag matches the ignore texel data flag
        if((dataFlags & Material.IGNORE_TEXEL_DATA_FLAG) == Material.IGNORE_TEXEL_DATA_FLAG)
        {
            setIgnoreTexelData(true);
        }

        // Check if the data flag matches the ignore colour data flag
        if((dataFlags & Material.IGNORE_COLOUR_DATA_FLAG) == Material.IGNORE_COLOUR_DATA_FLAG)
        {
            setIgnoreColourData(true);
        }
    }

    /**
     * Set the state of ignoring position data
     *
     * @param state The state of ignoring position data
     * @since       1.0
     */
    public void setIgnorePositionData(boolean state)
    {
        ignorePositionData = state;
    }

    /**
     * Get the state of ignoring position data
     *
     * @return  A boolean of the state of ignoring position data
     * @since   1.0
     */
    public boolean isIgnoringPositionData()
    {
        return ignorePositionData;
    }

    /**
     * Set the state of ignoring texel data
     *
     * @param state The state of ignoring texel data
     * @since       1.0
     */
    public void setIgnoreTexelData(boolean state)
    {
        ignoreTexelData = state;
    }

    /**
     * Get the state of ignoring texel data
     *
     * @return  A boolean of the state of ignoring texel data
     * @since   1.0
     */
    public boolean isIgnoringTexelData()
    {
        return ignoreTexelData;
    }

    /**
     * Set the state of ignoring colour data
     *
     * @param state The state of ignoring colour data
     * @since       1.0
     */
    public void setIgnoreColourData(boolean state)
    {
        ignoreColourData = state;
    }

    /**
     * Get the state of ignoring colour data
     *
     * @return  A boolean of the state of ignoring colour data
     * @since   1.0
     */
    public boolean isIgnoringColourData()
    {
        return ignoreColourData;
    }

    /**
     * Set the UV co-ordinate multiplier. The UV co-ordinate multiplier takes the current texel data
     * UV co-ordinates and multiplies them by the stated amount. This can be used in scaling
     * multiplying or scaling textures on an object face.
     *
     * @param uvMultiplier  The new UV co-ordinate multiplier
     * @since               1.0
     */
    public void setUvMultiplier(Scale2D uvMultiplier)
    {
        this.uvMultiplier = uvMultiplier;
    }

    /**
     * Get the UV co-ordinate multiplier
     *
     * @return  A boolean of the state of ignoring colour data
     * @see     Scale2D
     * @since   1.0
     */
    public Scale2D getUvMultiplier()
    {
        return this.uvMultiplier;
    }

    /**
     * Set the texture attached to the material
     *
     * @param texture   The texture of the material
     * @see             Texture
     * @since           1.0
     */
    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    /**
     * Get the texture object attached to the material
     *
     * @return  The texture object attached to the material
     * @see     Texture
     * @since   1.0
     */
    public Texture getTexture()
    {
        return this.texture;
    }

    /**
     * Check if the material has a texture attached.
     *
     * @return  True if the material has a texture, else false
     * @since   1.0
     */
    public boolean hasTexture()
    {
        return texture != null;
    }

    /**
     * Set the uniform colour of the material. Uniform colour is applied across the entirety of the
     * object that the material is attached to. If the object also has per-attribute colour data,
     * the two colours will be blended.
     *
     * @param colour    The uniform colour of the material
     * @see             Colour
     * @since           1.0
     */
    public void setColour(Colour colour)
    {
        this.colour = colour;
    }

    /**
     * Get the uniform colour of the material.
     *
     * @return  The uniform colour of the material
     * @see     Colour
     * @since   1.0
     */
    public Colour getColour()
    {
        return this.colour;
    }

    /**
     * Get the colour data as an array of floats
     *
     * @return  An array of floats containing the colour data in format RGBA
     * @since   1.0
     */
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
}
