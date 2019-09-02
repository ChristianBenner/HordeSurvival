package com.games.crispin.crispinmobile.Rendering.Data;

import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

/**
 * RenderObjectDataFormat is a class designed to hold information on how the render data is
 * formatted. This is so the RenderObject class can understand how to interpret/render the vertex
 * data. This includes how many position, texel and normal data elements there are, and in what
 * order.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         RenderObject
 * @since       1.0
 */
public class RenderObjectDataFormat
{
    /**
     * The attribute order type is to store the order that the vertex data elements appear. This
     * means that the data can be submitted to the GPU in the correct order.
     *  POSITION: The position data element is the only one provided
     *  POSITION_THEN_TEXEL: The position data is provided first, then texel data
     *  POSITION_THEN_COLOUR: The position data provided first, then colour
     *  POSITION_THEN_NORMAL: The position data provided first, then normal
     *  POSITION_THEN_TEXEL_THEN_COLOUR: Position data first, then texel, then colour
     *  POSITION_THEN_COLOUR_THEN_TEXEL: Position data first, then colour, then texel
     *  POSITION_THEN_TEXEL_THEN_NORMAL: Position data first, then texel, then normal
     *  POSITION_THEN_NORMAL_THEN_TEXEL: Position data first, then normal, then texel
     *  POSITION_THEN_COLOUR_THEN_NORMAL: Position data first, then colour, then normal
     *  POSITION_THEN_NORMAL_THEN_COLOUR: Position data first, then normal, then colour
     *  POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL: Position data first, then texel, then normal,
     *      then normal
     *  POSITION_THEN_COLOUR_THEN_TEXEL_THEN_NORMAL: Position data first, then colour, then texel,
     *      then normal
     *  POSITION_THEN_TEXEL_THEN_NORMAL_THEN_COLOUR: Position data first, then texel, then normal,
     *      then colour
     *  POSITION_THEN_COLOUR_THEN_NORMAL_THEN_TEXEL: Position data first, then colour, then normal,
     *      then texel
     *  POSITION_THEN_NORMAL_THEN_TEXEL_THEN_COLOUR: Position data first, then normal, then texel,
     *      then colour
     *  POSITION_THEN_NORMAL_THEN_COLOUR_THEN_TEXEL: Position data first, then normal, then colour,
     *      then texel
     *
     * @since       1.0
     */
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

    /**
     * The position dimensions that are existent in the data
     *
     * @since       1.0
     */
    public enum PositionDimensions_t
    {
        XYZW,
        XYZ,
        XY
    }

    /**
     * The texel dimensions that are existent in the data
     *
     * @since       1.0
     */
    public enum TexelDimensions_t
    {
        ST,
        NONE
    }

    /**
     * The colour dimensions that are existent in the data
     *
     * @since       1.0
     */
    public enum ColourDimensions_t
    {
        RGB,
        RGBA,
        NONE
    }

    /**
     * The normal dimensions that are existent in the data
     *
     * @since       1.0
     */
    public enum NormalDimensions_t
    {
        XYZW,
        XYZ,
        XY,
        NONE
    }

    // The 'numVerticesPerGroup' if the vertices are not grouped
    public static final int UNGROUPED = 1;

    // The number of vertices per group
    private final int NUM_VERTICES_GROUP;

    // The attribute order of the data
    private final AttributeOrder_t ATTRIBUTE_ORDER;

    // The position dimensions available in the data
    private final PositionDimensions_t POSITION_DIMENSIONS;

    // The texel dimensions available in the data
    private final TexelDimensions_t TEXEL_DIMENSIONS;

    // The colour dimensions available in the data
    private final ColourDimensions_t COLOUR_DIMENSIONS;

    // The normal dimensions available in the data
    private final NormalDimensions_t NORMAL_DIMENSIONS;

    // The render method that is determined from the number of different face data present
    private final RenderObject.RenderMethod renderMethod;

    // Whether or not the attribute order specifies the use of texel data
    private boolean attributeOrderSupportsTexels;

    // Whether or not the attribute order specifies the use of colour data
    private boolean attributeOrderSupportsColour;

    // Whether or not the attribute order specifies the use of normal data
    private boolean attributeOrderSupportsNormals;

    /**
     * Create a RenderObjectDataFormat with all the parameters.
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param positionDimensions    The components the position data is comprised of
     * @param texelDimensions       The components that the texel data is comprised of
     * @param colourDimensions      The components that the colour data is comprised of
     * @param normalDimensions      The components that the normal data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  TexelDimensions_t texelDimensions,
                                  ColourDimensions_t colourDimensions,
                                  NormalDimensions_t normalDimensions)
    {
        this.renderMethod = renderMethod;
        this.POSITION_DIMENSIONS = positionDimensions;
        this.NUM_VERTICES_GROUP = numVerticesPerGroup;
        this.TEXEL_DIMENSIONS = texelDimensions;
        this.COLOUR_DIMENSIONS = colourDimensions;
        this.NORMAL_DIMENSIONS = normalDimensions;
        this.ATTRIBUTE_ORDER = attributeOrder;
    }

    /**
     * Create a RenderObjectDataFormat that accounts for only position data
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param positionDimensions    The components the position data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions)
    {
        this(renderMethod,
                attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                TexelDimensions_t.NONE,
                ColourDimensions_t.NONE,
                NormalDimensions_t.NONE);
    }

    /**
     * Create a RenderObjectDataFormat that accounts for position, texel and colour data
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param positionDimensions    The components the position data is comprised of
     * @param texelDimensions       The components that the texel data is comprised of
     * @param colourDimensions      The components that the colour data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  TexelDimensions_t texelDimensions,
                                  ColourDimensions_t colourDimensions)
    {
        this(renderMethod,
                attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                texelDimensions,
                colourDimensions,
                NormalDimensions_t.NONE);
    }

    /**
     * Create a RenderObjectDataFormat that accounts for position, texel and normal data
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param positionDimensions    The components the position data is comprised of
     * @param texelDimensions       The components that the texel data is comprised of
     * @param normalDimensions      The components that the normal data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  TexelDimensions_t texelDimensions,
                                  NormalDimensions_t normalDimensions)
    {
        this(renderMethod,
                attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                texelDimensions,
                ColourDimensions_t.NONE,
                normalDimensions);
    }

    /**
     * Create a RenderObjectDataFormat that accounts for position, colour and normal data
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param positionDimensions    The components the position data is comprised of
     * @param colourDimensions      The components that the colour data is comprised of
     * @param normalDimensions      The components that the normal data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  ColourDimensions_t colourDimensions,
                                  NormalDimensions_t normalDimensions)
    {
        this(renderMethod,
                attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                TexelDimensions_t.NONE,
                colourDimensions,
                normalDimensions);
    }

    /**
     * Create a RenderObjectDataFormat that accounts for position and texel data
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param positionDimensions    The components the position data is comprised of
     * @param texelDimensions       The components that the texel data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  TexelDimensions_t texelDimensions)
    {
        this(renderMethod,
                attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                texelDimensions,
                ColourDimensions_t.NONE,
                NormalDimensions_t.NONE);
    }

    /**
     * Create a RenderObjectDataFormat that accounts for position and normal data
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param positionDimensions    The components the position data is comprised of
     * @param normalDimensions      The components that the normal data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  NormalDimensions_t normalDimensions)
    {
        this(renderMethod,
                attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                TexelDimensions_t.NONE,
                ColourDimensions_t.NONE,
                normalDimensions);
    }

    /**
     * Create a RenderObjectDataFormat that accounts for position and colour data
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param positionDimensions    The components the position data is comprised of
     * @param colourDimensions      The components that the colour data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  PositionDimensions_t positionDimensions,
                                  ColourDimensions_t colourDimensions)
    {
        this(renderMethod,
                attributeOrder,
                numVerticesPerGroup,
                positionDimensions,
                TexelDimensions_t.NONE,
                colourDimensions,
                NormalDimensions_t.NONE);
    }

    /**
     * Get the order that the attributes appear in the data
     *
     * @return  The attribute order of the different data elements
     * @since   1.0
     */
    public AttributeOrder_t getAttributeOrder()
    {
        return ATTRIBUTE_ORDER;
    }

    /**
     * Get the components of the position data
     *
     * @return  Components of the position data as a PositionDimension_t type
     * @since   1.0
     */
    public PositionDimensions_t getPositionDimensions()
    {
        return POSITION_DIMENSIONS;
    }

    /**
     * Get the components of the texel data
     *
     * @return  Components of the texel data as a TexelDimensions_t type
     * @since   1.0
     */
    public TexelDimensions_t getTexelDimensions()
    {
        return TEXEL_DIMENSIONS;
    }

    /**
     * Determine if the data format supports texel data
     *
     * @return  True if the data format supports texel data, else false
     * @since   1.0
     */
    public boolean supportsTexelData()
    {
        return (TEXEL_DIMENSIONS != null) && (TEXEL_DIMENSIONS != TexelDimensions_t.NONE);
    }

    /**
     * Get the components of the colour data
     *
     * @return  Components of the colour data as a ColourDimensions_t type
     * @since   1.0
     */
    public ColourDimensions_t getColourDimensions()
    {
        return COLOUR_DIMENSIONS;
    }

    /**
     * Determine if the data format supports colour data
     *
     * @return  True if the data format supports colour data, else false
     * @since   1.0
     */
    public boolean supportsColourData()
    {
        return (COLOUR_DIMENSIONS != null) && (COLOUR_DIMENSIONS != ColourDimensions_t.NONE);
    }

    /**
     * Get the components of the normal data
     *
     * @return  Components of the normal data as a NormalDimensions_t type
     * @since   1.0
     */
    public NormalDimensions_t getNormalDimensions()
    {
        return NORMAL_DIMENSIONS;
    }

    /**
     * Determine if the data format supports normal data
     *
     * @return  True if the data format supports normal data, else false
     * @since   1.0
     */
    public boolean supportsNormalData()
    {
        return (NORMAL_DIMENSIONS != null) && (NORMAL_DIMENSIONS != NormalDimensions_t.NONE);
    }

    /**
     * Get the number of vertices per group
     *
     * @return  An integer of the number of vertices per group
     * @since   1.0
     */
    public int getNumberVerticesPerGroup()
    {
        return NUM_VERTICES_GROUP;
    }

    /**
     * Get the number of position dimensions
     *
     * @return  An integer of the number of position dimensions
     * @since   1.0
     */
    public int getNumberPositionDimensions()
    {
        // Check if position dimensions have not been specified
        if(POSITION_DIMENSIONS == null)
        {
            return 0;
        }

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

    /**
     * Get the number of texel dimensions
     *
     * @return  An integer of the number of texel dimensions
     * @since   1.0
     */
    public int getNumberTexelDimensions()
    {
        // Check if texel dimensions have not been specified
        if(TEXEL_DIMENSIONS == null)
        {
            return 0;
        }

        switch (TEXEL_DIMENSIONS)
        {
            default:
            case ST:
                return 2;
            case NONE:
                return 0;
        }
    }

    /**
     * Get the number of colour dimensions
     *
     * @return  An integer of the number of colour dimensions
     * @since   1.0
     */
    public int getNumberColourDimensions()
    {
        // Check if colour dimensions have not been specified
        if(COLOUR_DIMENSIONS == null)
        {
            return 0;
        }

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

    /**
     * Get the number of normal dimensions
     *
     * @return  An integer of the number of normal dimensions
     * @since   1.0
     */
    public int getNumberNormalDimensions()
    {
        // Check if normal dimensions have not been specified
        if(NORMAL_DIMENSIONS == null)
        {
            return 0;
        }

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

    /**
     * Get the render method
     *
     * @return  The render method
     * @since   1.0
     */
    public RenderObject.RenderMethod getRenderMethod()
    {
        return renderMethod;
    }
}