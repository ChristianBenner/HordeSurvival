package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

public class Square extends RenderObject
{
    private static final float[] VERTEX_DATA =
    {
            0.0f, 1.0f,
            0.0f, 0.0f, //st
            0.0f, 0.0f,
            0.0f, 1.0f, //st
            1.0f, 0.0f,
            1.0f, 1.0f, //st
            1.0f, 1.0f,
            1.0f, 0.0f, //st
            0.0f, 1.0f,
            0.0f, 0.0f, //st
            1.0f, 0.0f,
            1.0f, 1.0f, //st
    };

    public Square()
    {
        super(VERTEX_DATA, PositionDimensions_t.XY, TexelDimensions_t.ST, AttributeOrder_t.POSITION_THEN_TEXEL);
    }

    public Square(Material material)
    {
        super(VERTEX_DATA,
                PositionDimensions_t.XY,
                TexelDimensions_t.ST,
                AttributeOrder_t.POSITION_THEN_TEXEL,
                material);
    }
}
