package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat;
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
    // Position vertex data
    private static final float POSITION_VERTEX_DATA[] =
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

    private static final float COLOUR_VERTEX_DATA[] =
    {
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, 0.0f, 1.0f, //rgb

            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, 0.0f, 1.0f, //rgb

            -1.0f, -1.0f, -1.0f,
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, 1.0f, 1.0f, //rgb

            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, 0.0f, 1.0f, //rgb

            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 0.0f, 1.0f, 1.0f, //rgb

            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
    };

    private static final float TEXEL_VERTEX_DATA[] =
    {
            0.0f, 1.0f, //st
            1.0f, 1.0f, //st
            1.0f, 0.0f, //st
            0.0f, 0.0f, //st
            1.0f, 1.0f, //st
            1.0f, 0.0f, //st

            0.0f, 1.0f, //st
            1.0f, 0.0f, //st
            1.0f, 1.0f, //st
            0.0f, 0.0f, //st
            0.0f, 1.0f, //st
            1.0f, 1.0f, //st

            0.0f, 1.0f, //st
            1.0f, 0.0f, //st
            0.0f, 0.0f, //st
            0.0f, 1.0f, //st
            0.0f, 0.0f, //st
            1.0f, 0.0f, //st

            0.0f, 0.0f, //st
            0.0f, 1.0f, //st
            1.0f, 1.0f, //st
            0.0f, 0.0f, //st
            1.0f, 1.0f, //st
            1.0f, 0.0f, //st

            1.0f, 1.0f, //st
            0.0f, 0.0f, //st
            0.0f, 1.0f, //st
            1.0f, 0.0f, //st
            0.0f, 0.0f, //st
            0.0f, 1.0f, //st

            1.0f, 0.0f, //st
            0.0f, 1.0f, //st
            1.0f, 1.0f, //st
            1.0f, 0.0f, //st
            0.0f, 0.0f, //st
            1.0f, 1.0f, //st
    };
    private static final float VERTEX_DATA_POSITION_COLOUR_AND_TEXEL[] =
    {
            -1.0f, -1.0f, -1.0f,
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, //st
            -1.0f, -1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, //st
            -1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            1.0f, 0.0f, //st
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            -1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, //st
            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 0.0f, //st

            1.0f, -1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, //st
            -1.0f, -1.0f, -1.0f,
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, //st
            1.0f, -1.0f, -1.0f,
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            1.0f, 1.0f, //st
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, //st
            -1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, //st

            -1.0f, -1.0f, -1.0f,
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, //st
            -1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            1.0f, 0.0f, //st
            -1.0f, 1.0f, -1.0f,
            1.0f, 0.0f, 0.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            1.0f, -1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, //st
            -1.0f, -1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            -1.0f, -1.0f, -1.0f,
            0.0f, 1.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, //st

            -1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            -1.0f, -1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, //st
            1.0f, -1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 1.0f, //st
            1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            1.0f, -1.0f, -1.0f,
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, //st
            1.0f, 1.0f, -1.0f,
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 0.0f, //st

            1.0f, -1.0f, -1.0f,
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            1.0f, 1.0f, //st
            1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            1.0f, -1.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f, //rgb
            0.0f, 1.0f, //st
            1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, //st
            1.0f, 1.0f, -1.0f,
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            -1.0f, 1.0f, -1.0f,
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, //st

            1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, //st
            -1.0f, 1.0f, -1.0f,
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 1.0f, //st
            -1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 1.0f, //st
            1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 0.0f, //st
            -1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            0.0f, 0.0f, //st
            1.0f, -1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f, //rgb
            1.0f, 1.0f, //st
    };

    public Cube()
    {
        super(VERTEX_DATA_POSITION_COLOUR_AND_TEXEL,
                new RenderObjectDataFormat(
                        RenderMethod.TRIANGLES,
                        RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_COLOUR_THEN_TEXEL,
                        RenderObjectDataFormat.UNGROUPED,
                        RenderObjectDataFormat.PositionDimensions_t.XYZ,
                        RenderObjectDataFormat.TexelDimensions_t.ST,
                        RenderObjectDataFormat.ColourDimensions_t.RGBA));
    }

    public Cube(Material material)
    {
/*        super(VERTEX_DATA_POSITION_COLOUR_AND_TEXEL,
                new RenderObjectDataFormat(
                        RenderMethod.TRIANGLES,
                        RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_COLOUR_THEN_TEXEL,
                        RenderObjectDataFormat.UNGROUPED,
                        RenderObjectDataFormat.PositionDimensions_t.XYZ,
                        RenderObjectDataFormat.TexelDimensions_t.ST,
                        RenderObjectDataFormat.ColourDimensions_t.RGBA),
                material);*/

        super(POSITION_VERTEX_DATA.length / 3,
                RenderMethod.TRIANGLES,
                POSITION_BUFFER_TYPE,
                POSITION_VERTEX_DATA,
                COLOUR_BUFFER_TYPE,
                COLOUR_VERTEX_DATA,
                TEXEL_BUFFER_TYPE,
                TEXEL_VERTEX_DATA,
                material);
    }
}
