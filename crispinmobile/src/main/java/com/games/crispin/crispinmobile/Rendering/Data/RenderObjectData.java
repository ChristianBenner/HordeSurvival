package com.games.crispin.crispinmobile.Rendering.Data;

import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.crypto.spec.DESedeKeySpec;

public class RenderObjectData
{
    public enum FaceData
    {
        VERTEX_ONLY,
        VERTEX_TEXEL,
        VERTEX_NORMAL,
        VERTEX_TEXEL_NORMAL,
        NONE
    }

    public enum RenderMethod
    {
        POINTS,
        LINES,
        TRIANGLES,
        QUADS,
        NONE
    }

    public enum PositionComponents
    {
        XYZW,
        XYZ,
        XY,
        NONE
    }

    private static final int NUM_DATA_ELEMENTS_VERTEX_ONLY = 1;
    private static final int NUM_DATA_ELEMENTS_VERTEX_NORMAL = 2;
    private static final int NUM_DATA_ELEMENTS_VERTEX_TEXEL = 2;
    private static final int NUM_DATA_ELEMENTS_VERTEX_TEXEL_NORMAL = 3;
    private static final int VERTEX_START_INDEX = 0;
    private static final int UNUSED_DATA_ELEMENT = -1;

    private ArrayList<Float> vertexDataArray;
    private ArrayList<Float> texelDataArray;
    private ArrayList<Float> normalDataArray;
    private ArrayList<Integer> faceDataArray;
    private FaceData faceData;
    private RenderMethod renderMethod;
    private PositionComponents positionComponents;
    private int dataStride;
    private int vertexStartIndex;
    private int texelStartIndex;
    private int normalStartIndex;

    public RenderObjectData()
    {
        vertexDataArray = new ArrayList<>();
        texelDataArray = new ArrayList<>();
        normalDataArray = new ArrayList<>();
        faceDataArray = new ArrayList<>();
        faceData = FaceData.NONE;
        renderMethod = RenderMethod.NONE;
        positionComponents = PositionComponents.NONE;
        dataStride = 0;
        vertexStartIndex = UNUSED_DATA_ELEMENT;
        texelStartIndex = UNUSED_DATA_ELEMENT;
        normalStartIndex = UNUSED_DATA_ELEMENT;
    }

    public boolean setPositionComponents(PositionComponents positionComponents)
    {
        if(this.positionComponents == PositionComponents.NONE)
        {
            this.positionComponents = positionComponents;

            switch (positionComponents)
            {
                case XY:
                    System.out.println("vvv Set Position Component to XY");
                    break;
                case XYZ:
                    System.out.println("vvv Set Position Component to XYZ");
                    break;
                case XYZW:
                    System.out.println("vvv Set Position Component to XYZW");
                    break;
                case NONE:
                    System.out.println("vvv Set Position Component to NONE");
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
            System.err.println("ERROR: RenderObjectData already has a different position component type");
            return false;
        }
    }

    public boolean setRenderMethod(RenderMethod renderMethod)
    {
        if(this.renderMethod == RenderMethod.NONE)
        {
            this.renderMethod = renderMethod;

            switch (renderMethod)
            {
                case NONE:
                    System.out.println("vvv Render Method Set to: " + "NONE");
                    break;
                case QUADS:
                    System.out.println("vvv Render Method Set to: " + "QUADS");
                    break;
                case TRIANGLES:
                    System.out.println("vvv Render Method Set to: " + "TRIANGLES");
                    break;
                case LINES:
                    System.out.println("vvv Render Method Set to: " + "LINES");
                    break;
                case POINTS:
                    System.out.println("vvv Render Method Set to: " + "POINTS");
                    break;
            }

            return true;
        }
        else if(this.renderMethod == renderMethod)
        {
            return true;
        }
        else
        {
            System.err.println("ERROR: RenderObjectData already has a different Render Method type");
            return false;
        }
    }

    public boolean setFaceDataType(FaceData faceDataType)
    {
        if(this.faceData == FaceData.NONE)
        {
            this.faceData = faceDataType;

            switch(faceDataType)
            {
                case NONE:
                    System.out.println("vvv Face Data Type set to: " + "NONE");
                    break;
                case VERTEX_ONLY:
                    System.out.println("vvv Face Data Type set to: " + "VERTEX_ONLY");
                    dataStride = NUM_DATA_ELEMENTS_VERTEX_ONLY;
                    vertexStartIndex = VERTEX_START_INDEX;
                    texelStartIndex = UNUSED_DATA_ELEMENT;
                    normalStartIndex = UNUSED_DATA_ELEMENT;
                    break;
                case VERTEX_NORMAL:
                    System.out.println("vvv Face Data Type set to: " + "VERTEX_NORMAL");
                    dataStride = NUM_DATA_ELEMENTS_VERTEX_NORMAL;
                    vertexStartIndex = VERTEX_START_INDEX;
                    texelStartIndex = UNUSED_DATA_ELEMENT;
                    normalStartIndex = vertexStartIndex + 1;
                    break;
                case VERTEX_TEXEL:
                    System.out.println("vvv Face Data Type set to: " + "VERTEX_TEXEL");
                    dataStride = NUM_DATA_ELEMENTS_VERTEX_TEXEL;
                    vertexStartIndex = VERTEX_START_INDEX;
                    texelStartIndex = vertexStartIndex + 1;
                    normalStartIndex = UNUSED_DATA_ELEMENT;
                    break;
                case VERTEX_TEXEL_NORMAL:
                    System.out.println("vvv Face Data Type set to: " + "VERTEX_TEXEL_NORMAL");
                    dataStride = NUM_DATA_ELEMENTS_VERTEX_TEXEL_NORMAL;
                    vertexStartIndex = VERTEX_START_INDEX;
                    texelStartIndex = vertexStartIndex + 1;
                    normalStartIndex = texelStartIndex + 1;
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
            System.err.println("ERROR: RenderObjectData already has a different FaceData type");
            return false;
        }
    }

    public void addVertexData(float vertexData)
    {
        vertexDataArray.add(vertexData);
    }

    public void addTexelData(float texelData)
    {
        texelDataArray.add(texelData);
    }

    public void addNormalData(float normalData)
    {
        normalDataArray.add(normalData);
    }

    public void addFaceData(int faceData)
    {
        faceDataArray.add(faceData);
        System.out.println("vvv Face Data Added: " + faceData);
    }

    private int getNumberPositionElements()
    {
        switch (positionComponents)
        {
            case XYZW:
                return 4;
            case XYZ:
                return 3;
            case XY:
                return 2;
            case NONE:
                default:
                return 0;
        }
    }

    public RenderObject processFaceData()
    {
        final int NUMBER_OF_FACE_DATA = faceDataArray.size() / dataStride;

        final int NUMBER_OF_POSITION_ELEMENTS = getNumberPositionElements();
        final int POSITION_BUFFER_SIZE = NUMBER_OF_POSITION_ELEMENTS * NUMBER_OF_FACE_DATA;

        final int NUMBER_OF_TEXEL_ELEMENTS = 2;
        final int TEXEL_BUFFER_SIZE = texelStartIndex == -1 ? 0 : NUMBER_OF_TEXEL_ELEMENTS * NUMBER_OF_FACE_DATA;

       // final int NUMBER_OF_NORMAL_ELEMENTS = 2;
       // final int NORMAL_BUFFER_SIZE = texelStartIndex == -1 ? 0 : NUMBER_OF_TEXEL_ELEMENTS * NUMBER_OF_FACE_DATA;

        float[] vertexDataBuffer = new float[POSITION_BUFFER_SIZE + TEXEL_BUFFER_SIZE];

        int vertexDataBufferIndex = 0;
        // Process the vertex data
        for(int vertexIterator = vertexStartIndex;
            vertexIterator != -1 && vertexIterator < faceDataArray.size();
            vertexIterator += dataStride)
        {
            for(int elementIndex = 0;
                elementIndex < NUMBER_OF_POSITION_ELEMENTS;
                elementIndex++)
            {
                vertexDataBuffer[vertexDataBufferIndex] =
                        vertexDataArray.get(((faceDataArray.get(vertexIterator) - 1) * NUMBER_OF_POSITION_ELEMENTS) + elementIndex);
                vertexDataBufferIndex++;
            }
            vertexDataBufferIndex += NUMBER_OF_TEXEL_ELEMENTS;
        }

        for(int i = 0; i < texelDataArray.size(); i++)
        {
            System.out.println("vvv Texel Data Array[" + i + "}: " + texelDataArray.get(i));
        }

        vertexDataBufferIndex = NUMBER_OF_POSITION_ELEMENTS;

        // Process the vertex data
        for(int texelIterator = texelStartIndex;
            texelIterator != -1 && texelIterator < faceDataArray.size();
            texelIterator += dataStride)
        {
            for(int elementIndex = 0;
                elementIndex < NUMBER_OF_TEXEL_ELEMENTS;
                elementIndex++)
            {
                int index = (((faceDataArray.get(texelIterator) - 1) * NUMBER_OF_TEXEL_ELEMENTS) + elementIndex);
                float value = texelDataArray.get((((faceDataArray.get(texelIterator) - 1) * NUMBER_OF_TEXEL_ELEMENTS) + elementIndex));
                vertexDataBuffer[vertexDataBufferIndex] = value;
                vertexDataBufferIndex++;

                System.out.println("vvv Texel Index[" + index + "]: " + value);
            }

            vertexDataBufferIndex += NUMBER_OF_POSITION_ELEMENTS;
        }

        for(int i = 0; i < vertexDataBuffer.length; i++)
        {
            System.out.println("vvv VDB: " + vertexDataBuffer[i]);
        }

        return new RenderObject(vertexDataBuffer, RenderObject.PositionDimensions_t.XYZ, RenderObject.TexelDimensions_t.ST);
    }
}
