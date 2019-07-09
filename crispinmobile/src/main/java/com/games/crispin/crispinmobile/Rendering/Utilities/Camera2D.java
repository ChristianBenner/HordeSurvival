package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Crispin;

/**
 * Camera2D provides a simple interface to view matrix operations and control. The class integrates
 * with other objects in the engine such as RenderObjects, providing a unified way to render with
 * 2-dimensional views. The public methods give the user full control over things such as the
 * position, field of view and direction of the virtual camera.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Matrix
 * @see         RenderObject
 * @since       1.0
 */
public class Camera2D
{
    // Tag used in logging output
    private static final String TAG = "Camera2D";

    // This is the default near range of the orthographic matrix
    private static float DEFAULT_NEAR = -5.0f;

    // This is the default far range of the orthographic matrix
    private static float DEFAULT_FAR = 5.0f;

    // This is the default left range of the orthographic matrix
    private static float DEFAULT_LEFT = 0.0f;

    // This is the default bottom range of the orthographic matrix
    private static float DEFAULT_BOTTOM = 0.0f;

    // Left position of the orthographic matrix
    private float left;

    // Right position of the orthographic matrix
    private float right;

    // Bottom position of the orthographic matrix
    private float bottom;

    // Top position of the orthographic matrix
    private float top;

    // Near position of the orthographic matrix
    private float near;

    // Far position of the orthographic matrix
    private float far;

    // The orthographic matrix
    private float[] orthoMatrix;

    /**
     * Construct the camera object with the custom values. Then produce the orthographic matrix
     * matrix. The parameters are all in real world co-ordinates. If you use the surface width and
     * height as dimensions, you can place objects using pixels within that surface dimension.
     *
     * @param x         The x position of the camera
     * @param y         The y position of the camera
     * @param width     The width of the camera view
     * @param height    The height of the camera view
     * @param nearBound The near bound of the orthographic matrix. Anything that is not within the
     *                  near and far bounds (on the Z plane) will be clipped
     * @param farBound  The far bound of the orthographic matrix. Anything that is not within the
     *                  near and far bounds (on the Z plane) will be clipped
     * @since           1.0
     */
    public Camera2D(float x,
                    float y,
                    float width,
                    float height,
                    float nearBound,
                    float farBound)
    {
        this.left = x;
        this.bottom = y;
        this.right = width;
        this.top = height;
        this.near = nearBound;
        this.far = farBound;
        this.orthoMatrix = new float[16];
        updateView();
    }

    /**
     * Construct the camera object with the custom values. Then produce the orthographic matrix
     * matrix. The parameters are all in real world co-ordinates. If you use the surface width and
     * height as dimensions, you can place objects using pixels within that surface dimension. This
     * constructor uses default near and far values, if you find 3D objects in your 2D view being
     * clipped on the Z plane, specify different near and far values.
     *
     * @param x         The x position of the camera
     * @param y         The y position of the camera
     * @param width     The width of the camera view
     * @param height    The height of the camera view
     * @since           1.0
     */
    public Camera2D(float x,
                    float y,
                    float width,
                    float height)
    {
        this(
                x,
                y,
                width,
                height,
                DEFAULT_NEAR,
                DEFAULT_FAR);
    }

    /**
     * Construct the camera object with the custom values. Then produce the orthographic matrix
     * matrix. The parameters are all in real world co-ordinates. If you use the surface width and
     * height as dimensions, you can place objects using pixels within that surface dimension. This
     * constructor uses default values for the creation of an orthographic matrix (2D view). The
     * camera has a width equal to the surface width, a height equal to the surface height, and is
     * located at x: 0.0, y: 0.0 in world co-ordinates. This constructor also uses default near and
     * far values, if you find 3D objects in your 2D view being clipped on the Z plane, specify
     * different near and far values.
     *
     * @since           1.0
     */
    public Camera2D()
    {
        this(
                DEFAULT_LEFT,
                DEFAULT_BOTTOM,
                Crispin.getSurfaceWidth(),
                Crispin.getSurfaceHeight());
    }

    /**
     * Get the left bound of the cameras orthographic matrix
     *
     * @return  Return the left bound of the orthographic matrix
     * @since   1.0
     */
    public float getLeft()
    {
        return this.left;
    }

    /**
     * Set the left bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param left  The new left bound of the orthographic matrix
     * @since       1.0
     */
    public void setLeft(float left)
    {
        this.left = left;
        updateView();
    }

    /**
     * Get the right bound of the cameras orthographic matrix
     *
     * @return  Return the right bound of the orthographic matrix
     * @since   1.0
     */
    public float getRight()
    {
        return right;
    }

    /**
     * Set the right bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param right The new right bound of the orthographic matrix
     * @since       1.0
     */
    public void setRight(float right)
    {
        this.right = right;
        updateView();
    }

    /**
     * Get the bottom bound of the cameras orthographic matrix
     *
     * @return  Return the bottom bound of the orthographic matrix
     * @since   1.0
     */
    public float getBottom()
    {
        return bottom;
    }

    /**
     * Set the bottom bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param bottom    The new bottom bound of the orthographic matrix
     * @since           1.0
     */
    public void setBottom(float bottom)
    {
        this.bottom = bottom;
        updateView();
    }

    /**
     * Get the top bound of the cameras orthographic matrix
     *
     * @return  Return the top bound of the orthographic matrix
     * @since   1.0
     */
    public float getTop()
    {
        return top;
    }

    /**
     * Set the top bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param top   The new top bound of the orthographic matrix
     * @since       1.0
     */
    public void setTop(float top)
    {
        this.top = top;
        updateView();
    }

    /**
     * Get the near bound of the cameras orthographic matrix
     *
     * @return  Return the near bound of the orthographic matrix
     * @since   1.0
     */
    public float getNear()
    {
        return near;
    }

    /**
     * Set the near bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param near  The new near bound of the orthographic matrix
     * @since       1.0
     */
    public void setNear(float near)
    {
        this.near = near;
        updateView();
    }

    /**
     * Get the far bound of the cameras orthographic matrix
     *
     * @return  Return the far bound of the orthographic matrix
     * @since   1.0
     */
    public float getFar()
    {
        return far;
    }

    /**
     * Set the far bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param far   The new far bound of the orthographic matrix
     * @since       1.0
     */
    public void setFar(float far)
    {
        this.far = far;
        updateView();
    }

    /**
     * Get the far bound of the cameras orthographic matrix
     *
     * @return  Return the orthographic matrix
     * @since   1.0
     */
    public float[] getOrthoMatrix()
    {
        return orthoMatrix;
    }

    /**
     * Update the view by recreating the orthographic matrix
     *
     * @see     Matrix
     * @since   1.0
     */
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
