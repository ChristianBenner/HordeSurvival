package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat;
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
        super(VERTEX_DATA,
                new RenderObjectDataFormat(
                        RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_TEXEL,
                        RenderObjectDataFormat.UNGROUPED,
                        RenderObjectDataFormat.PositionDimensions_t.XY,
                        RenderObjectDataFormat.TexelDimensions_t.ST));
    }

    public Square(Material material)
    {
        super(VERTEX_DATA,
                new RenderObjectDataFormat(
                        RenderObjectDataFormat.AttributeOrder_t.POSITION_THEN_TEXEL,
                        RenderObjectDataFormat.UNGROUPED,
                        RenderObjectDataFormat.PositionDimensions_t.XY,
                        RenderObjectDataFormat.TexelDimensions_t.ST),
                material);
    }
}
