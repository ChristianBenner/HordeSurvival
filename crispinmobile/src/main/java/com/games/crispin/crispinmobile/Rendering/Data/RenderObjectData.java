package com.games.crispin.crispinmobile.Rendering.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    private ArrayList<Float> vertexDataArray;
    private ArrayList<Float> texelDataArray;
    private ArrayList<Float> normalDataArray;
    private ArrayList<Integer> faceDataArray;
    private FaceData faceData;
    private RenderMethod renderMethod;

    public RenderObjectData()
    {
        vertexDataArray = new ArrayList<>();
        texelDataArray = new ArrayList<>();
        normalDataArray = new ArrayList<>();
        faceDataArray = new ArrayList<>();
        faceData = FaceData.NONE;
        renderMethod = RenderMethod.NONE;
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
        else
        {
            if(this.renderMethod == renderMethod)
            {
                return true;
            }
            else
            {
                System.err.println("ERROR: RenderObjectData already has a different Render Method type");
                return false;
            }
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
                    break;
                case VERTEX_NORMAL:
                    System.out.println("vvv Face Data Type set to: " + "VERTEX_NORMAL");
                    break;
                case VERTEX_TEXEL:
                    System.out.println("vvv Face Data Type set to: " + "VERTEX_TEXEL");
                    break;
                case VERTEX_TEXEL_NORMAL:
                    System.out.println("vvv Face Data Type set to: " + "VERTEX_TEXEL_NORMAL");
                    break;
            }

            return true;
        }
        else
        {
            if(this.faceData == faceDataType)
            {
                return true;
            }
            else
            {
                System.err.println("ERROR: RenderObjectData already has a different FaceData type");
                return false;
            }
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

    public void processFaceData()
    {
        // Look through the faces, creating model data
        for(int i = 0; i < faceDataArray.size(); i++)
        {

        }
    }
}
