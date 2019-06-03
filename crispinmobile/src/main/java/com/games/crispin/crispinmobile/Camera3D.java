package com.games.crispin.crispinmobile;


import android.opengl.Matrix;

public class Camera3D
{
    // The position of the camera
    private Point3D position;

    // The right vector
    private Geometry.Vector right = new Geometry.Vector(1.0f, 0.0f, 0.0f);

    // The direction the camera is facing (vector)
    private Geometry.Vector direction = new Geometry.Vector(0.0f, 0.0f, -1.0f);

    // The up direction of the camera
    private Geometry.Vector up = right.crossProduct(direction);

    // Distance to begin the frustrum
    private float near = 0.1f;

    // Distance to end the frustrum
    private float far = 5.0f;

    // Perspective field of view (FOV)
    private float fieldOfView = 90.0f;

    // The view matrix
    private float[] viewMatrix = new float[16];

    // The perspective/frustrum matrix
    private float[] perspectiveMatrix = new float[16];

    public Camera3D()
    {
        position = new Point3D(0.0f, 0.0f, 0.0f);
        updateView();
    }

    public Point3D getPosition()
    {
        return position;
    }

    public void setPosition(Point3D position)
    {
        this.position = position;
        updateView();
    }

    public Geometry.Vector getRight()
    {
        return right;
    }

    public void setRight(Geometry.Vector right)
    {
        this.right = right;
        updateView();
    }

    public Geometry.Vector getDirection()
    {
        return direction;
    }

    public void setDirection(Geometry.Vector direction)
    {
        this.direction = direction;
        updateView();
    }

    public Geometry.Vector getUp()
    {
        return up;
    }

    public void setUp(Geometry.Vector up)
    {
        this.up = up;
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

    public float getFieldOfView()
    {
        return fieldOfView;
    }

    public void setFieldOfView(float fieldOfView)
    {
        this.fieldOfView = fieldOfView;
        updateView();
    }

    public float[] getViewMatrix()
    {
        return viewMatrix;
    }

    public float[] getPerspectiveMatrix()
    {
        return perspectiveMatrix;
    }

    private void updateView()
    {
        // Camera3D look at/center position
        final Point3D CENTER = new Point3D(
                position.x + direction.x,
                position.y + direction.y,
                position.z + direction.z);

        // The aspect ratio of the frustrum
        final float ASPECT_RATIO = (float)Crispin.getSurfaceWidth()/Crispin.getSurfaceHeight();

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

        // Generate perspective matrix
        Matrix.perspectiveM(perspectiveMatrix,
                0,
                fieldOfView,
                ASPECT_RATIO,
                near,
                far);
    }
}
