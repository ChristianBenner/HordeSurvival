package com.games.crispin.crispinmobile.Rendering.Utilities;


import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Vector3D;

/**
 * Camera3D provides a simple interface to view matrix operations and control. The class integrates
 * with other objects in the engine such as RenderObjects, providing a unified way to render with
 * 3-dimensional views. The public methods give the user full control over things such as the
 * position, field of view and direction of the virtual camera.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Matrix
 * @see         RenderObject
 * @since       1.0
 */
public class Camera3D
{
    // Tag used in logging output
    private static final String TAG = "Camera3D";

    // The number of floats in a 4x4 Matrix
    private static final int NUM_FLOATS_4X4_MATRIX = 16;

    // The default near of the Frustrum Matrix
    private static final float DEFAULT_NEAR = 0.1f;

    // The default far of the Frustrum Matrix
    private static final float DEFAULT_FAR = 10.0f;

    // The default field of view of the Frustrum Matrix
    private static final float DEFAULT_FIELD_OF_VIEW = 90.0f;

    // The default right direction of the 'look at' data
    private static final Vector3D DEFAULT_RIGHT_DIRECTION = new Vector3D(1.0f,
            0.0f,
            0.0f);

    // The default up direction of the 'look at' data
    private static final Vector3D DEFAULT_DIRECTION = new Vector3D(0.0f,
            0.0f,
            -1.0f);

    // Distance to begin the frustrum
    private float near;

    // Distance to end the frustrum
    private float far;

    // Perspective field of view (FOV)
    private float fieldOfView;

    // The position of the camera
    private Point3D position;

    // The right direction
    private Vector3D right;

    // The direction the camera is facing (direction)
    private Vector3D direction;

    // The up direction of the camera
    private Vector3D up;

    // The view matrix
    private float[] viewMatrix;

    // The perspective/frustrum matrix
    private float[] perspectiveMatrix;

    /**
     * Construct the camera object with the default values. Then produce the frustrum/perspective
     * matrix.
     *
     * @since   1.0
     */
    public Camera3D()
    {
        near = DEFAULT_NEAR;
        far = DEFAULT_FAR;
        fieldOfView = DEFAULT_FIELD_OF_VIEW;
        position = new Point3D();
        right = DEFAULT_RIGHT_DIRECTION;
        direction = DEFAULT_DIRECTION;
        up = right.getCrossProduct(DEFAULT_DIRECTION);
        viewMatrix = new float[NUM_FLOATS_4X4_MATRIX];
        perspectiveMatrix = new float[NUM_FLOATS_4X4_MATRIX];

        updateView();
        updatePerspective();
    }

    /**
     * Get the position of the camera
     *
     * @return  Return the position of the camera
     * @see     Point3D
     * @since   1.0
     */
    public Point3D getPosition()
    {
        return position;
    }

    /**
     * Set the position of the camera. Causes a view matrix update.
     *
     * @param position  The new position of the camera
     * @see             Point3D
     * @since           1.0
     */
    public void setPosition(Point3D position)
    {
        this.position = position;
        updateView();
    }

    /**
     * Get the right direction of the camera
     *
     * @return  A 3D direction of the right direction
     * @see     Vector3D
     * @since   1.0
     */
    public Vector3D getRight()
    {
        return right;
    }

    /**
     * Set the right direction of the camera. Causes a view matrix update.
     *
     * @param right The new right direction of the camera
     * @see         Vector3D
     * @since       1.0
     */
    public void setRight(Vector3D right)
    {
        this.right = right;
        updateView();
    }

    /**
     * Get the direction of the camera
     *
     * @return  A 3D direction of the camera facing direction
     * @see     Vector3D
     * @since   1.0
     */
    public Vector3D getDirection()
    {
        return direction;
    }

    /**
     * Set the direction of the camera. Causes a view matrix update.
     *
     * @param direction The new direction of the camera
     * @see             Vector3D
     * @since           1.0
     */
    public void setDirection(Vector3D direction)
    {
        this.direction = direction;
        updateView();
    }

    /**
     * Get the up direction of the camera
     *
     * @return  A 3D direction of the camera up direction
     * @see     Vector3D
     * @since   1.0
     */
    public Vector3D getUp()
    {
        return up;
    }

    /**
     * Set the up direction of the camera. Causes a view matrix update.
     *
     * @param up    The new up direction of the camera
     * @see         Vector3D
     * @since       1.0
     */
    public void setUp(Vector3D up)
    {
        this.up = up;
        updateView();
    }

    /**
     * Get the z-near value of the camera
     *
     * @return  The z-near value of the camera (the near value of the frustrum matrix)
     * @since   1.0
     */
    public float getNear()
    {
        return near;
    }

    /**
     * Set the z-near value of the camera. Causes a perspective matrix update.
     *
     * @param near  The new z-near value of the camera (the near value of the frustrum matrix)
     * @since       1.0
     */
    public void setNear(float near)
    {
        this.near = near;
        updatePerspective();
    }

    /**
     * Get the z-far value of the camera
     *
     * @return  The z-far value of the camera (the far value of the frustrum matrix)
     * @since   1.0
     */
    public float getFar()
    {
        return far;
    }

    /**
     * Set the z-far value of the camera. Causes a perspective matrix update.
     *
     * @param far   The new z-far value of the camera (the far value of the frustrum matrix)
     * @since       1.0
     */
    public void setFar(float far)
    {
        this.far = far;
        updatePerspective();
    }

    /**
     * Get the camera field of view
     *
     * @return  The camera field of view
     * @since   1.0
     */
    public float getFieldOfView()
    {
        return fieldOfView;
    }

    /**
     * Set the camera field of view. Causes a perspective matrix update.
     *
     * @param fieldOfView   The new field of view for the camera
     * @since               1.0
     */
    public void setFieldOfView(float fieldOfView)
    {
        this.fieldOfView = fieldOfView;
        updatePerspective();
    }

    /**
     * Get the cameras view matrix
     *
     * @return  An array of 16 floats containing the view matrix.
     * @since   1.0
     */
    public float[] getViewMatrix()
    {
        return viewMatrix;
    }

    /**
     * Get the cameras perspective matrix
     *
     * @return  An array of 16 floats containing the perspective matrix.
     * @since   1.0
     */
    public float[] getPerspectiveMatrix()
    {
        return perspectiveMatrix;
    }

    /**
     * Update the view matrix. Uses the Matrix setLookAtM function to produce a new view matrix.
     *
     * @see     Matrix
     * @since   1.0
     */
    private void updateView()
    {
        // Camera3D look at/center position
        final Point3D CENTER = new Point3D(
                position.x + direction.x,
                position.y + direction.y,
                position.z + direction.z);

        // Set the view matrix look at
        Matrix.setLookAtM(viewMatrix,
                0,
                position.x,
                position.y,
                position.z,
                CENTER.x,
                CENTER.y,
                CENTER.z,
                up.x,
                up.y,
                up.z);
    }

    /**
     * Update the perspective matrix. Uses the Matrix perspectiveM function to produce a new
     * perspective/frustrum matrix.
     *
     * @see     Matrix
     * @since   1.0
     */
    private void updatePerspective()
    {
        // The aspect ratio of the frustrum
        final float ASPECT_RATIO = (float) Crispin.getSurfaceWidth()/Crispin.getSurfaceHeight();

        // Generate perspective matrix
        Matrix.perspectiveM(perspectiveMatrix,
                0,
                fieldOfView,
                ASPECT_RATIO,
                near,
                far);
    }
}
