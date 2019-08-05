package com.games.crispin.crispinmobile.Rendering.Data;

import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

public class RenderObjectDataFormat
{
    public enum AttributeOrder_t
    {
        POSITION,
        POSITION_THEN_TEXEL,
        POSITION_THEN_COLOUR,
        POSITION_THEN_NORMAL,
        POSITION_THEN_TEXEL_THEN_COLOUR,
        POSITION_THEN_COLOUR_THEN_TEXEL,
        POSITION_THEN_TEXEL_THEN_NORMAL,
        POSITION_THEN_NORMAL_THEN_TEXEL,
        POSITION_THEN_COLOUR_THEN_NORMAL,
        POSITION_THEN_NORMAL_THEN_COLOUR,
        POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL,
        POSITION_THEN_COLOUR_THEN_TEXEL_THEN_NORMAL,
        POSITION_THEN_TEXEL_THEN_NORMAL_THEN_COLOUR,
        POSITION_THEN_COLOUR_THEN_NORMAL_THEN_TEXEL,
        POSITION_THEN_NORMAL_THEN_TEXEL_THEN_COLOUR,
        POSITION_THEN_NORMAL_THEN_COLOUR_THEN_TEXEL
    }

    public enum PositionDimensions_t
    {
        XYZW,
        XYZ,
        XY
    }

    public enum TexelDimensions_t
    {
        ST,
        NONE
    }

    public enum ColourDimensions_t
    {
        RGB,
        RGBA,
        NONE
    }

    public enum NormalDimensions_t
    {
        XYZW,
        XYZ,
        XY,
        NONE
    }

    public static final int UNGROUPED = 1;
    private final AttributeOrder_t ATTRIBUTE_ORDER;
    private final PositionDimensions_t POSITION_DIMENSIONS;
    private final TexelDimensions_t TEXEL_DIMENSIONS;
    private final ColourDimensions_t COLOUR_DIMENSIONS;
    private final NormalDimensions_t NORMAL_DIMENSIONS;
    private final int NUM_VERTICES_GROUP;

    public RenderObjectDataFormat(AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  TexelDimensions_t texelDimensions,
                                  ColourDimensions_t colourDimensions,
                                  NormalDimensions_t normalDimensions)
    {
        this.POSITION_DIMENSIONS = positionDimensions;
        this.NUM_VERTICES_GROUP = numVerticesPerGroup;
        this.TEXEL_DIMENSIONS = texelDimensions;
        this.COLOUR_DIMENSIONS = colourDimensions;
        this.NORMAL_DIMENSIONS = normalDimensions;
        this.ATTRIBUTE_ORDER = attributeOrder;
    }

    public RenderObjectDataFormat(AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions)
    {
        this(attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                TexelDimensions_t.NONE,
                ColourDimensions_t.NONE,
                NormalDimensions_t.NONE);
    }

    public RenderObjectDataFormat(AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  TexelDimensions_t texelDimensions,
                                  ColourDimensions_t colourDimensions)
    {
        this(attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                texelDimensions,
                colourDimensions,
                NormalDimensions_t.NONE);
    }

    public RenderObjectDataFormat(AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  TexelDimensions_t texelDimensions,
                                  NormalDimensions_t normalDimensions)
    {
        this(attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                texelDimensions,
                ColourDimensions_t.NONE,
                normalDimensions);
    }

    public RenderObjectDataFormat(AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  ColourDimensions_t colourDimensions,
                                  NormalDimensions_t normalDimensions)
    {
        this(attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                TexelDimensions_t.NONE,
                colourDimensions,
                normalDimensions);
    }

    public RenderObjectDataFormat(AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  TexelDimensions_t texelDimensions)
    {
        this(attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                texelDimensions,
                ColourDimensions_t.NONE,
                NormalDimensions_t.NONE);
    }

    public RenderObjectDataFormat(AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  NormalDimensions_t normalDimensions)
    {
        this(attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                TexelDimensions_t.NONE,
                ColourDimensions_t.NONE,
                normalDimensions);
    }

    public RenderObjectDataFormat(AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  ColourDimensions_t colourDimensions)
    {
        this(attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                TexelDimensions_t.NONE,
                colourDimensions,
                NormalDimensions_t.NONE);
    }

    public AttributeOrder_t getAttributeOrder()
    {
        return ATTRIBUTE_ORDER;
    }

    public PositionDimensions_t getPositionDimensions()
    {
        return POSITION_DIMENSIONS;
    }

    public TexelDimensions_t getTexelDimensions()
    {
        return TEXEL_DIMENSIONS;
    }

    public boolean supportsTexelData()
    {
        return TEXEL_DIMENSIONS != TexelDimensions_t.NONE;
    }

    public ColourDimensions_t getColourDimensions()
    {
        return COLOUR_DIMENSIONS;
    }

    public boolean supportsColourData()
    {
        return COLOUR_DIMENSIONS != ColourDimensions_t.NONE;
    }

    public NormalDimensions_t getNormalDimensions()
    {
        return NORMAL_DIMENSIONS;
    }

    public boolean supportsNormalData()
    {
        return NORMAL_DIMENSIONS != NormalDimensions_t.NONE;
    }

    public int getNumberVerticesPerGroup()
    {
        return NUM_VERTICES_GROUP;
    }

    public int getNumberPositionDimensions()
    {
        switch (POSITION_DIMENSIONS)
        {
            case XYZW:
                return 4;
            case XYZ:
            default:
                return 3;
            case XY:
                return 2;
        }
    }

    public int getNumberTexelDimensions()
    {
        switch (TEXEL_DIMENSIONS)
        {
            default:
            case ST:
                return 2;
            case NONE:
                return 0;
        }
    }

    public int getNumberColourDimensions()
    {
        switch (COLOUR_DIMENSIONS)
        {
            case RGBA:
                return 4;
            case RGB:
            default:
                return 3;
            case NONE:
                return 0;
        }
    }

    public int getNumberNormalDimensions()
    {
        switch (NORMAL_DIMENSIONS)
        {
            case XYZW:
                return 4;
            case XYZ:
            default:
                return 3;
            case XY:
                return 2;
            case NONE:
                return 0;
        }
    }
}