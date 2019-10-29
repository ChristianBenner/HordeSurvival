package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

/**
 * Cube class is a default 3D model of a cube. It is a render object and therefor can be drawn to
 * the display. It contains texture, colour and positional data.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         RenderObject
 * @since       1.0
 */
public class Cube extends RenderObject
{
    // The number of position components in the position data (3 because its XYZ)
    private static final byte NUMBER_POSITION_COMPONENTS = 3;

    // The number of texel components in the texel data (2 because its ST)
    private static final byte NUMBER_TEXEL_COMPONENTS = 2;

    // The number of colour components in the colour data (3 because its RGB)
    private static final byte NUMBER_COLOUR_COMPONENTS = 3;

    // The number of normal components in the normal data (3 because its xyz)
    private static final byte NUMBER_NORMAL_COMPONENTS = 3;

    // The render method to draw the data with (triangles because that's how the data is
    // constructed)
    private static final RenderMethod RENDER_METHOD = RenderMethod.TRIANGLES;

    // Position vertex data that contains XYZ components
    private static final float POSITION_DATA[] =
    {
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,

            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
    };

    // Colour vertex data that contains RGB components
    private static final float COLOUR_DATA[] =
    {
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,

            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,

            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,

            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,

            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
    };

    // Texel vertex data that contains ST components
    private static final float TEXEL_DATA[] =
    {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,

            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,

            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
    };

    // Normal vertex data that contains XYZ components
    private static final float NORMAL_DATA[] =
    {
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,

            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,

            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,

            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,

            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f
    };

    /**
     * Create a cube with specifically allowed data types. This means that on creation of the object
     * that a controllable amount of vertex data is submitted to a buffer. This can prove efficient
     * in scenarios where multiple cubes are going to be created in a short amount of time such as
     * particle engines as it allows you to prevent the handling of un-required data.
     *
     * @param material      A material to apply to the rendered object
     * @param renderTexels  True if the model is allowed to use texel data, else false
     * @param renderNormals True if the model is allowed to use normal data, else false
     * @param renderColour  True if the model is allowed to use colour data, else false
     * @since   1.0
     */
    public Cube(Material material,
                boolean renderTexels,
                boolean renderNormals,
                boolean renderColour)
    {
        super(POSITION_DATA,
                renderTexels ? TEXEL_DATA : null,
                renderColour ? COLOUR_DATA : null,
                renderNormals ? NORMAL_DATA : null,
                RENDER_METHOD,
                POSITION_DATA.length / NUMBER_POSITION_COMPONENTS,
                NUMBER_POSITION_COMPONENTS,
                NUMBER_TEXEL_COMPONENTS,
                NUMBER_COLOUR_COMPONENTS,
                NUMBER_NORMAL_COMPONENTS,
                material);
    }

    /**
     * Create a cube with specifically allowed data types. This means that on creation of the object
     * that a controllable amount of vertex data is submitted to a buffer. This can prove efficient
     * in scenarios where multiple cubes are going to be created in a short amount of time such as
     * particle engines as it allows you to prevent the handling of un-required data.
     *
     * @param renderTexels  True if the model is allowed to use texel data, else false
     * @param renderNormals True if the model is allowed to use normal data, else false
     * @param renderColour  True if the model is allowed to use colour data, else false
     * @since   1.0
     */
    public Cube(boolean renderTexels,
                boolean renderNormals,
                boolean renderColour)
    {
        this(new Material(),
                renderTexels,
                renderNormals,
                renderColour);
    }

    /**
     * Create a cube with default properties. By default, the cube will upload texel data
     * (supporting textures) and normal data (supporting lighting). De-activating unused data types
     * on the cube may result in more efficient construction so it is recommended to use a different
     * Cube constructor unless you know you want all the default data (or just don't care). You
     * could also use ignore data flags on an attached material, however this wouldn't provide the
     * efficiency of not uploading the data in the first place.
     *
     * @param material  Material to apply to the object
     * @since 1.0
     */
    public Cube(Material material)
    {
        this(material,
                true,
                false,
                false);
    }

    /**
     * Create a cube with default properties. By default, the cube will upload texel data
     * (supporting textures) and normal data (supporting lighting). De-activating unused data types
     * on the cube may result in more efficient construction so it is recommended to use a different
     * Cube constructor unless you know you want all the default data (or just don't care). You
     * could also use ignore data flags on an attached material, however this wouldn't provide the
     * efficiency of not uploading the data in the first place.
     *
     * @since 1.0
     */
    public Cube()
    {
        this(new Material(),
                true,
                false,
                false);
    }
}
