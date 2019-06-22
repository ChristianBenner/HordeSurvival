package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Crispin;

public class Camera2D
{
    private static float DEFAULT_NEAR = -5.0f;
    private static float DEFAULT_FAR = 5.0f;

    private float left;
    private float right;
    private float bottom;
    private float top;
    private float near;
    private float far;

    private float[] orthoMatrix;

    public Camera2D()
    {
        left = 0.0f;
        right = Crispin.getSurfaceWidth();
        bottom = 0.0f;
        top = Crispin.getSurfaceHeight();
        near = DEFAULT_NEAR;
        far = DEFAULT_FAR;
        orthoMatrix = new float[16];
        
        updateView();
    }

    public void setLeft(float left)
    {
        this.left = left;
        updateView();
    }

    public float getRight()
    {
        return right;
    }

    public void setRight(float right)
    {
        this.right = right;
        updateView();
    }

    public float getBottom()
    {
        return bottom;
    }

    public void setBottom(float bottom)
    {
        this.bottom = bottom;
        updateView();
    }

    public float getTop()
    {
        return top;
    }

    public void setTop(float top)
    {
        this.top = top;
        updateView();
    }

    public float getNear()
    {
        return near;
    }

    public void setNear(float near)
    {
        this.near = near;
        updateView();
    }

    public float getFar()
    {
        return far;
    }

    public void setFar(float far)
    {
        this.far = far;
        updateView();
    }

    public float[] getOrthoMatrix()
    {
        return orthoMatrix;
    }

    public void updateView()
    {
        Matrix.orthoM(orthoMatrix,
                0,
                left,
                right,
                bottom,
                top,
                near,
                far);
    }
}
