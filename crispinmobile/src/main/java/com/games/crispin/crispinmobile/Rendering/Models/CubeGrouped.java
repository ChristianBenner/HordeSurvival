package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

public class CubeGrouped extends RenderObject {
    static final float CUBE_VERTEX_DATA[] =
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
                    1.0f, 1.0f //st
            };

    public CubeGrouped()
    {
        super(CUBE_VERTEX_DATA,
                new RenderObjectDataFormat(
                        RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_COLOUR_THEN_TEXEL,
                        36,
                        RenderObjectDataFormat.PositionDimensions_t.XYZ,
                        RenderObjectDataFormat.TexelDimensions_t.ST,
                        RenderObjectDataFormat.ColourDimensions_t.RGBA));
    }

    public CubeGrouped(Material material)
    {
        super(CUBE_VERTEX_DATA,
                new RenderObjectDataFormat(
                        RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_COLOUR_THEN_TEXEL,
                        36,
                        RenderObjectDataFormat.PositionDimensions_t.XYZ,
                        RenderObjectDataFormat.TexelDimensions_t.ST,
                        RenderObjectDataFormat.ColourDimensions_t.RGBA),
                material);
    }
}
