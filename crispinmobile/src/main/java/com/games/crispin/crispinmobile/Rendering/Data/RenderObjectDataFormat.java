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

    private final byte NUM_POSITION_COMPONENTS;
    private final byte NUM_TEXEL_COMPONENTS;
    private final byte NUM_COLOUR_COMPONENTS;
    private final byte NUM_NORMAL_COMPONENTS;

    // The render method that is determined from the number of different face data present
    private final RenderObject.RenderMethod renderMethod;

    /**
     * Create a RenderObjectDataFormat with all the parameters.
     *
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param numPositionComponents The number of components the position data is comprised of
     * @param numTexelComponents    The number of components that the texel data is comprised of
     * @param numColourComponents   The number of components that the colour data is comprised of
     * @param numNormalComponents   The number components that the normal data is comprised of
     * @since 1.0
     */
    public RenderObjectDataFormat(RenderObject.RenderMethod renderMethod,
                                  AttributeOrder_t attributeOrder,
                                  int numVerticesPerGroup,
                                  byte numPositionComponents,
                                  byte numTexelComponents,
                                  byte numColourComponents,
                                  byte numNormalComponents)
    {
        this.renderMethod = renderMethod;
        this.NUM_VERTICES_GROUP = numVerticesPerGroup;
        this.NUM_POSITION_COMPONENTS = numPositionComponents;
        this.NUM_TEXEL_COMPONENTS = numTexelComponents;
        this.NUM_COLOUR_COMPONENTS = numColourComponents;
        this.NUM_NORMAL_COMPONENTS = numNormalComponents;
        this.ATTRIBUTE_ORDER = attributeOrder;
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
     * Get the number of position data dimensions (e.g. XYZ contains three and XY contains two)
     *
     * @return  The number of position data dimensions
     * @since   1.0
     */
    public byte getNumberPositionDimensions()
    {
        return NUM_POSITION_COMPONENTS;
    }

    /**
     * Get the number of texel data dimensions (e.g. ST contains two)
     *
     * @return  The number of texel data dimensions
     * @since   1.0
     */
    public byte getNumberTexelDimensions()
    {
        return NUM_TEXEL_COMPONENTS;
    }

    /**
     * Get the number of colour data dimensions (e.g. RGBA contains four and RGB contains three)
     *
     * @return  The number of colour data dimensions
     * @since   1.0
     */
    public byte getNumberColourDimensions()
    {
        return NUM_COLOUR_COMPONENTS;
    }

    /**
     * Get the number of normal data dimensions (e.g. XYZ contains three and XY contains two)
     *
     * @return  The number of normal data dimensions
     * @since   1.0
     */
    public byte getNumberNormalDimensions()
    {
        return NUM_NORMAL_COMPONENTS;
    }

    /**
     * Determine if the data format supports texel data
     *
     * @return  True if the data format supports texel data, else false
     * @since   1.0
     */
    public boolean supportsTexelData()
    {
        return NUM_TEXEL_COMPONENTS != 0;
    }

    /**
     * Determine if the data format supports colour data
     *
     * @return  True if the data format supports colour data, else false
     * @since   1.0
     */
    public boolean supportsColourData()
    {
        return NUM_COLOUR_COMPONENTS != 0;
    }

    /**
     * Determine if the data format supports normal data
     *
     * @return  True if the data format supports normal data, else false
     * @since   1.0
     */
    public boolean supportsNormalData()
    {
        return NUM_NORMAL_COMPONENTS != 0;
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