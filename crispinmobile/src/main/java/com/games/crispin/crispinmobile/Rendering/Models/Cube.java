package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

// cube renderer object
public class Cube extends RenderObject
{
//    static final float CUBE_VERTEX_DATA[] =
//            {
//                    -1.0f, -1.0f, -1.0f,
//                    -1.0f, -1.0f, 1.0f,
//                    -1.0f, 1.0f, 1.0f,
//                    1.0f, 1.0f, -1.0f,
//                    -1.0f, -1.0f, -1.0f,
//                    -1.0f, 1.0f, -1.0f,
//
//                    1.0f, -1.0f, 1.0f,
//                    -1.0f, -1.0f, -1.0f,
//                    1.0f, -1.0f, -1.0f,
//                    1.0f, 1.0f, -1.0f,
//                    1.0f, -1.0f, -1.0f,
//                    -1.0f, -1.0f, -1.0f,
//
//                    -1.0f, -1.0f, -1.0f,
//                    -1.0f, 1.0f, 1.0f,
//                    -1.0f, 1.0f, -1.0f,
//                    1.0f, -1.0f, 1.0f,
//                    -1.0f, -1.0f, 1.0f,
//                    -1.0f, -1.0f, -1.0f,
//
//                    -1.0f, 1.0f, 1.0f,
//                    -1.0f, -1.0f, 1.0f,
//                    1.0f, -1.0f, 1.0f,
//                    1.0f, 1.0f, 1.0f,
//                    1.0f, -1.0f, -1.0f,
//                    1.0f, 1.0f, -1.0f,
//
//                    1.0f, -1.0f, -1.0f,
//                    1.0f, 1.0f, 1.0f,
//                    1.0f, -1.0f, 1.0f,
//                    1.0f, 1.0f, 1.0f,
//                    1.0f, 1.0f, -1.0f,
//                    -1.0f, 1.0f, -1.0f,
//
//                    1.0f, 1.0f, 1.0f,
//                    -1.0f, 1.0f, -1.0f,
//                    -1.0f, 1.0f, 1.0f,
//                    1.0f, 1.0f, 1.0f,
//                    -1.0f, 1.0f, 1.0f,
//                    1.0f, -1.0f, 1.0f
//            };

        static final float CUBE_VERTEX_DATA[] =
            {
                    -1.0f, -1.0f, -1.0f,
                    0.0f, 1.0f, //st
                    -1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, //st
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, //st
                    1.0f, 1.0f, -1.0f,
                    0.0f, 0.0f, //st
                    -1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, //st
                    -1.0f, 1.0f, -1.0f,
                    1.0f, 0.0f, //st

                    1.0f, -1.0f, 1.0f,
                    0.0f, 1.0f, //st
                    -1.0f, -1.0f, -1.0f,
                    1.0f, 0.0f, //st
                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, //st
                    1.0f, 1.0f, -1.0f,
                    0.0f, 0.0f, //st
                    1.0f, -1.0f, -1.0f,
                    0.0f, 1.0f, //st
                    -1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, //st

                    -1.0f, -1.0f, -1.0f,
                    0.0f, 1.0f, //st
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, //st
                    -1.0f, 1.0f, -1.0f,
                    0.0f, 0.0f, //st
                    1.0f, -1.0f, 1.0f,
                    0.0f, 1.0f, //st
                    -1.0f, -1.0f, 1.0f,
                    0.0f, 0.0f, //st
                    -1.0f, -1.0f, -1.0f,
                    1.0f, 0.0f, //st

                    -1.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, //st
                    -1.0f, -1.0f, 1.0f,
                    0.0f, 1.0f, //st
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, //st
                    1.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, //st
                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, //st
                    1.0f, 1.0f, -1.0f,
                    1.0f, 0.0f, //st

                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, //st
                    1.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, //st
                    1.0f, -1.0f, 1.0f,
                    0.0f, 1.0f, //st
                    1.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, //st
                    1.0f, 1.0f, -1.0f,
                    0.0f, 0.0f, //st
                    -1.0f, 1.0f, -1.0f,
                    0.0f, 1.0f, //st

                    1.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, //st
                    -1.0f, 1.0f, -1.0f,
                    0.0f, 1.0f, //st
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, //st
                    1.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, //st
                    -1.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, //st
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, //st
            };

//    static final float CUBE_VERTEX_DATA[] =
//            {
//                    -1.0f, -1.0f, -1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, -1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f, // rgb
//                    1.0f, 1.0f, -1.0f,
//                    1.0f, 0.0f, 1.0f, 1.0f, // rgb
//                    -1.0f, -1.0f, -1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, 1.0f, -1.0f,
//                    1.0f, 1.0f, 0.0f, 1.0f, // rgb
//
//                    1.0f, -1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, -1.0f, -1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    1.0f, -1.0f, -1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    1.0f, 1.0f, -1.0f,
//                    1.0f, 0.0f, 1.0f, 1.0f, // rgb
//                    1.0f, -1.0f, -1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, -1.0f, -1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//
//                    -1.0f, -1.0f, -1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f, // rgb
//                    -1.0f, 1.0f, -1.0f,
//                    1.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    1.0f, -1.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, -1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, -1.0f, -1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//
//                    -1.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f, // rgb
//                    -1.0f, -1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    1.0f, -1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    1.0f, 1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    1.0f, -1.0f, -1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    1.0f, 1.0f, -1.0f,
//                    1.0f, 0.0f, 1.0f, 1.0f, // rgb
//
//                    1.0f, -1.0f, -1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    1.0f, 1.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    1.0f, -1.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    1.0f, 1.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f, // rgb
//                    1.0f, 1.0f, -1.0f,
//                    1.0f, 0.0f, 1.0f, 1.0f, // rgb
//                    -1.0f, 1.0f, -1.0f,
//                    1.0f, 1.0f, 0.0f, 1.0f, // rgb
//
//                    1.0f, 1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, 1.0f, -1.0f,
//                    1.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f, // rgb
//                    1.0f, 1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//                    -1.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f, // rgb
//                    1.0f, -1.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f, // rgb
//            };

    public Cube()
    {
        super(CUBE_VERTEX_DATA, PositionDimensions_t.XYZ, TexelDimensions_t.ST, AttributeOrder_t.POSITION_THEN_TEXEL);
    }

    public Cube(Material material)
    {
        super(CUBE_VERTEX_DATA,
                PositionDimensions_t.XYZ,
                TexelDimensions_t.ST,
                AttributeOrder_t.POSITION_THEN_TEXEL,
                material);
    }
}
