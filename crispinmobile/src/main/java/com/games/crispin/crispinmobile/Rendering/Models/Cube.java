package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.AttributeOrder_t.POSITION;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_COLOUR;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_COLOUR_THEN_NORMAL;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_NORMAL;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_TEXEL;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_TEXEL_THEN_COLOUR;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_TEXEL_THEN_NORMAL;

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

    /**
     * Get the attribute order of the model depending on a set of allowed data types
     *
     * @param renderTexels  True if the model is allowed to use texel data, else false
     * @param renderColour  True if the model is allowed to use colour data, else false
     * @param renderNormals True if the model is allowed to use normal data, else false
     *
     * @return  Returns an attribute order based on the set of allowed data types. Position will
     *          always be part of the attribute order. In the attribute order, texel data takes 2nd
     *          priority in the, colour data takes 3rd priority, and normal data takes 4th priority.
     *          For example, if all data types are allowed then the attribute order is
     *          <code>POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL</code>
     * @since   1.0
     */
    private static RenderObjectDataFormat.AttributeOrder_t getAtrributeOrder(boolean renderTexels,
                                                                             boolean renderColour,
                                                                             boolean renderNormals)
    {
        // Check what attribute order to use depending on what data types are allowed
        if (renderTexels && renderColour && renderNormals)
        {
            return POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL;
        }
        else if (renderTexels && renderColour)
        {
            return POSITION_THEN_TEXEL_THEN_COLOUR;
        }
        else if (renderTexels && renderNormals)
        {
            return POSITION_THEN_TEXEL_THEN_NORMAL;
        }
        else if(renderColour && renderNormals)
        {
            return POSITION_THEN_COLOUR_THEN_NORMAL;
        }
        else if(renderTexels)
        {
            return POSITION_THEN_TEXEL;
        }
        else if(renderColour)
        {
            return POSITION_THEN_COLOUR;
        }
        else if(renderNormals)
        {
            return POSITION_THEN_NORMAL;
        }

        return POSITION;
    }

    /**
     * Create a cube with specifically allowed data types. This means that on creation of the object
     * that a controllable amount of vertex data is submitted to a buffer. This can prove efficient
     * in scenarios where multiple cubes are going to be created in a short amount of time such as
     * particle engines as it allows you to prevent the handling of un-required data.
     *
     * @param material      A material to apply to the rendered object
     * @param renderTexels  True if the model is allowed to use texel data, else false
     * @param renderColour  True if the model is allowed to use colour data, else false
     * @since   1.0
     */
    public Cube(Material material,
                boolean renderTexels,
                boolean renderColour)
    {
        super(POSITION_DATA,
                renderTexels ? TEXEL_DATA : null,
                renderColour ? COLOUR_DATA : null,
                null,
                new RenderObjectDataFormat(
                        RenderMethod.TRIANGLES,
                        getAtrributeOrder(renderTexels,
                                renderColour,
                                false),
                        POSITION_DATA.length / 3,
                        RenderObjectDataFormat.PositionDimensions_t.XYZ,
                        renderTexels ? RenderObjectDataFormat.TexelDimensions_t.ST : null,
                        renderColour ? RenderObjectDataFormat.ColourDimensions_t.RGB : null),
                material);
    }

    /**
     * Create a cube with specifically allowed data types. This means that on creation of the object
     * that a controllable amount of vertex data is submitted to a buffer. This can prove efficient
     * in scenarios where multiple cubes are going to be created in a short amount of time such as
     * particle engines as it allows you to prevent the handling of un-required data.
     *
     * @param renderTexels  True if the model is allowed to use texel data, else false
     * @param renderColour  True if the model is allowed to use colour data, else false
     * @since   1.0
     */
    public Cube(boolean renderTexels,
                boolean renderColour)
    {
        this(new Material(), renderTexels, renderColour);
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
     * @since   1.0
     */
    public Cube()
    {
        this(new Material(),
                true,
                false);
    }
}
