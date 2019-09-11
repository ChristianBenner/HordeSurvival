package com.games.crispin.crispinmobile.Rendering.Data;

import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Utilities.Logger;

import java.util.ArrayList;

import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.
        AttributeOrder_t.POSITION;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.
        AttributeOrder_t.POSITION_THEN_NORMAL;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.
        AttributeOrder_t.POSITION_THEN_TEXEL;
import static com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat.
        AttributeOrder_t.POSITION_THEN_TEXEL_THEN_NORMAL;

/**
 * RenderObjectData is a class designed to build vertex data based on position, texel, normal and
 * face data. This makes it suitable for building vertex data for the GPU from OBJ data formats.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         RenderObject
 * @since       1.0
 */
public class RenderObjectData
{
    /**
     * The type of vertex data that the face data contains.
     *  POSITION_ONLY: The face data only includes positional vertex data
     *  POSITION_AND_TEXEL: The face data includes positional and texel vertex data
     *  POSITION_AND_NORMAL: The face data includes positional and normal vertex data
     *  POSITION_AND_TEXEL_AND_NORMAL: The face data includes positional, texel and normal vertex
     *      data
     *  NONE: FaceData has not been determined to contain any vertex data
     *
     * @since       1.0
     */
    public enum FaceData
    {
        POSITION_ONLY,
        POSITION_AND_TEXEL,
        POSITION_AND_NORMAL,
        POSITION_AND_TEXEL_AND_NORMAL,
        NONE
    }

    /**
     * The position components that the positional vertex data is comprised of.
     *
     * @since       1.0
     */
    public enum PositionComponents
    {
        XYZW,
        XYZ,
        XY,
        NONE
    }

    // Tag used for logging
    private static final String TAG = "RenderObjectData";

    // Start index offset
    static final int START_INDEX_OFFSET = 1;

    // The number of data elements in position only face data
    private static final int NUM_DATA_ELEMENTS_POSITION_ONLY = 1;

    // The number of data elements in position and normal face data
    private static final int NUM_DATA_ELEMENTS_POSITION_AND_NORMAL = 2;

    // The number of data elements in position and texel data
    private static final int NUM_DATA_ELEMENTS_POSITION_AND_TEXEL = 2;

    // The number of data elements in position, texel and normal data
    private static final int NUM_DATA_ELEMENTS_POSITION_AND_TEXEL_AND_NORMAL = 3;

    // The start index for the position data
    private static final int POSITION_START_INDEX = 0;

    // Represent a data start index as unused
    private static final int UNUSED_DATA_ELEMENT = -1;

    // Array holding the position vertex data
    private ArrayList<Float> positionDataArray;

    // Array holding the texel vertex data
    private ArrayList<Float> texelDataArray;

    // Array holding the normal vertex data
    private ArrayList<Float> normalDataArray;

    // Array holding the face data
    private ArrayList<Integer> faceDataArray;

    // The type of face data that is to be loaded
    private FaceData faceData;

    // The render method that has been determined
    private RenderObject.RenderMethod renderMethod;

    // The position components in the data
    private PositionComponents positionComponents;

    // The data stride
    private int dataStride;

    // The start index of the position data
    private int positionStartIndex;

    // The start index of the texel data
    private int texelStartIndex;

    // The start index of the normal data
    private int normalStartIndex;

    // Number of position components
    private int numberOfPositionComponents;

    /**
     * Construct the RenderObjectData object
     *
     * @since   1.0
     */
    public RenderObjectData()
    {
        positionDataArray = new ArrayList<>();
        texelDataArray = new ArrayList<>();
        normalDataArray = new ArrayList<>();
        faceDataArray = new ArrayList<>();
        faceData = FaceData.NONE;
        renderMethod = RenderObject.RenderMethod.NONE;
        positionComponents = PositionComponents.NONE;
        positionStartIndex = UNUSED_DATA_ELEMENT;
        texelStartIndex = UNUSED_DATA_ELEMENT;
        normalStartIndex = UNUSED_DATA_ELEMENT;
        dataStride = 0;
        numberOfPositionComponents = 0;
    }

    /**
     * Attempt to set the position component type
     *
     * @param positionComponents    The components that the position data includes
     * @return  True if the position component type has been set, else false. The function will
     *          return false if the data has already been assigned a position component type
     * @since   1.0
     */
    public boolean setPositionComponents(PositionComponents positionComponents)
    {
        // Check if the position components have been set yet
        if(this.positionComponents == PositionComponents.NONE)
        {
            this.positionComponents = positionComponents;

            // Determine the number of position components depending on the type
            switch (positionComponents)
            {
                case XY:
                    numberOfPositionComponents = 2;
                    break;
                case XYZ:
                    numberOfPositionComponents = 3;
                    break;
                case XYZW:
                    numberOfPositionComponents = 4;
                    break;
                case NONE:
                    break;
            }

            return true;
        }
        else if(this.positionComponents == positionComponents)
        {
            return true;
        }
        else
        {
            Logger.error(TAG,
                    "RenderObjectData already has a different position component type");
            return false;
        }
    }

    /**
     * Set the render method type
     *
     * @param renderMethod  The new render method to set
     * @return  True if render method changed successfully, else false. The function will return
     *          false if the data has already been assigned a render method
     * @since   1.0
     */
    public boolean setRenderMethod(RenderObject.RenderMethod renderMethod)
    {
        // Check if the render method has been set yet
        if(this.renderMethod == RenderObject.RenderMethod.NONE)
        {
            this.renderMethod = renderMethod;
            return true;
        }
        else if(this.renderMethod == renderMethod)
        {
            return true;
        }
        else
        {
            Logger.error(TAG, "RenderObjectData already has a different Render Method type");
            return false;
        }
    }

    /**
     * Set the face data type
     *
     * @param faceDataType  The new face data type
     * @return  True if the face data type has been set successfully, else false. The function will
     *          return false if the data has already been assigned a face data type
     * @since   1.0
     */
    public boolean setFaceDataType(FaceData faceDataType)
    {
        // Check if the face data has been set yet
        if(this.faceData == FaceData.NONE)
        {
            this.faceData = faceDataType;

            // Determine the data start index for different face data types
            switch(faceDataType)
            {
                case POSITION_ONLY:
                    dataStride = NUM_DATA_ELEMENTS_POSITION_ONLY;
                    positionStartIndex = POSITION_START_INDEX;
                    texelStartIndex = UNUSED_DATA_ELEMENT;
                    normalStartIndex = UNUSED_DATA_ELEMENT;
                    break;
                case POSITION_AND_NORMAL:
                    dataStride = NUM_DATA_ELEMENTS_POSITION_AND_NORMAL;
                    positionStartIndex = POSITION_START_INDEX;
                    texelStartIndex = UNUSED_DATA_ELEMENT;
                    normalStartIndex = positionStartIndex + START_INDEX_OFFSET;
                    break;
                case POSITION_AND_TEXEL:
                    dataStride = NUM_DATA_ELEMENTS_POSITION_AND_TEXEL;
                    positionStartIndex = POSITION_START_INDEX;
                    texelStartIndex = positionStartIndex + START_INDEX_OFFSET;
                    normalStartIndex = UNUSED_DATA_ELEMENT;
                    break;
                case POSITION_AND_TEXEL_AND_NORMAL:
                    dataStride = NUM_DATA_ELEMENTS_POSITION_AND_TEXEL_AND_NORMAL;
                    positionStartIndex = POSITION_START_INDEX;
                    texelStartIndex = positionStartIndex + START_INDEX_OFFSET;
                    normalStartIndex = texelStartIndex + START_INDEX_OFFSET;
                    break;
            }

            return true;
        }
        else if(this.faceData == faceDataType)
        {
            return true;
        }
        else
        {
            Logger.error(TAG, "RenderObjectData already has a different FaceData type");
            return false;
        }
    }

    /**
     * Add some position data to be processed later
     *
     * @param vertexData    The position data as a float
     * @since   1.0
     */
    public void addPositionData(float vertexData)
    {
        positionDataArray.add(vertexData);
    }

    /**
     * Add some texel data to be processed later
     *
     * @param texelData The texel data as a float
     * @since   1.0
     */
    public void addTexelData(float texelData)
    {
        texelDataArray.add(texelData);
    }

    /**
     * Add some normal data to be processed later
     *
     * @param normalData    The normal data as a float
     * @since   1.0
     */
    public void addNormalData(float normalData)
    {
        normalDataArray.add(normalData);
    }

    /**
     * Add some face data to be processed later
     *
     * @param faceData  The face data as a float
     * @since   1.0
     */
    public void addFaceData(int faceData)
    {
        faceDataArray.add(faceData);
    }

    /**
     * Get the position dimensions
     *
     * @return  The components that the position data is comprised of
     * @since   1.0
     */
    private RenderObjectDataFormat.PositionDimensions_t getPositionDimensions()
    {
        // Get the position dimensions
        switch (positionComponents)
        {
            case XY:
                return RenderObjectDataFormat.PositionDimensions_t.XY;
            case XYZW:
                return RenderObjectDataFormat.PositionDimensions_t.XYZW;
            case NONE:
            case XYZ:
            default:
                return RenderObjectDataFormat.PositionDimensions_t.XYZ;
        }
    }

    /**
     * Process the data. The function produces the vertex data based on positional, texel and normal
     * face data.
     *
     * @return  A RenderObject built from the processed data
     * @since   1.0
     */
    public RenderObject processData()
    {
        final int NUMBER_OF_FACE_DATA = faceDataArray.size() / dataStride;

        final int NUMBER_OF_POSITION_ELEMENTS = numberOfPositionComponents;
        final int POSITION_BUFFER_SIZE = NUMBER_OF_POSITION_ELEMENTS * NUMBER_OF_FACE_DATA;

        final int NUMBER_OF_TEXEL_ELEMENTS = 2;
        final int TEXEL_BUFFER_SIZE = texelStartIndex == -1 ? 0 : NUMBER_OF_TEXEL_ELEMENTS * NUMBER_OF_FACE_DATA;

        final int NUMBER_OF_NORMAL_ELEMENTS = 3;
        final int NORMAL_BUFFER_SIZE = normalStartIndex == -1 ? 0 : NUMBER_OF_NORMAL_ELEMENTS * NUMBER_OF_FACE_DATA;

        float[] vertexDataBuffer = new float[POSITION_BUFFER_SIZE + TEXEL_BUFFER_SIZE + NORMAL_BUFFER_SIZE];

        int vertexDataBufferIndex = 0;
        // Process the vertex data
        for(int vertexIterator = positionStartIndex;
            vertexIterator != -1 && vertexIterator < faceDataArray.size();
            vertexIterator += dataStride)
        {
            for(int elementIndex = 0;
                elementIndex < NUMBER_OF_POSITION_ELEMENTS;
                elementIndex++)
            {
                vertexDataBuffer[vertexDataBufferIndex] =
                        positionDataArray.get(((faceDataArray.get(vertexIterator) - 1) * NUMBER_OF_POSITION_ELEMENTS) + elementIndex);
                vertexDataBufferIndex++;
            }
        }

        vertexDataBufferIndex = POSITION_BUFFER_SIZE;

        // Process the vertex data
        for(int texelIterator = texelStartIndex;
            texelIterator != -1 && texelIterator < faceDataArray.size();
            texelIterator += dataStride)
        {
            for(int elementIndex = 0;
                elementIndex < NUMBER_OF_TEXEL_ELEMENTS;
                elementIndex++)
            {
                float value = texelDataArray.get((((faceDataArray.get(texelIterator) - 1) * NUMBER_OF_TEXEL_ELEMENTS) + elementIndex));
                vertexDataBuffer[vertexDataBufferIndex] = value;
                vertexDataBufferIndex++;
            }
        }

        vertexDataBufferIndex = POSITION_BUFFER_SIZE + TEXEL_BUFFER_SIZE;

        // Process the vertex data
        for(int normalIterator = normalStartIndex;
            normalIterator != -1 && normalIterator < faceDataArray.size();
            normalIterator += dataStride)
        {
            for(int elementIndex = 0;
                elementIndex < NUMBER_OF_NORMAL_ELEMENTS;
                elementIndex++)
            {
                float value = normalDataArray.get((((faceDataArray.get(normalIterator) - 1) * NUMBER_OF_NORMAL_ELEMENTS) + elementIndex));
                vertexDataBuffer[vertexDataBufferIndex] = value;
                vertexDataBufferIndex++;
            }
        }

        RenderObjectDataFormat.PositionDimensions_t positionDimensions = getPositionDimensions();
        RenderObjectDataFormat rdf;

        switch (faceData)
        {
            case POSITION_AND_TEXEL_AND_NORMAL:
                rdf = new RenderObjectDataFormat(
                        renderMethod,
                        POSITION_THEN_TEXEL_THEN_NORMAL,
                        NUMBER_OF_FACE_DATA,
                        positionDimensions,
                        RenderObjectDataFormat.TexelDimensions_t.ST,
                        RenderObjectDataFormat.NormalDimensions_t.XYZ);
                break;
            case POSITION_AND_NORMAL:
                rdf = new RenderObjectDataFormat(
                        renderMethod,
                        POSITION_THEN_NORMAL,
                        NUMBER_OF_FACE_DATA,
                        positionDimensions,
                        RenderObjectDataFormat.NormalDimensions_t.XYZ);
                break;
            case POSITION_AND_TEXEL:
                rdf = new RenderObjectDataFormat(
                        renderMethod,
                        POSITION_THEN_TEXEL,
                        NUMBER_OF_FACE_DATA,
                        positionDimensions,
                        RenderObjectDataFormat.TexelDimensions_t.ST);
                break;
            case POSITION_ONLY:
            case NONE:
                default:
                    rdf = new RenderObjectDataFormat(
                            renderMethod,
                            POSITION,
                            NUMBER_OF_FACE_DATA,
                            positionDimensions);
                    break;
        }

        return new RenderObject(vertexDataBuffer, rdf);
    }
}
