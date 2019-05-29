package com.games.crispin.crispinmobile;


import android.opengl.Matrix;

public class Camera
{
    float[] viewMatrix = new float[16];
    float[] fustrumMatrix = new float[16];

    Geometry.Point position = new Geometry.Point(0.0f, 0.0f, 0.0f);
    Geometry.Vector right = new Geometry.Vector(1.0f, 0.0f, 0.0f);
    Geometry.Vector direction = new Geometry.Vector(0.0f, 0.0f, -1.0f);
    Geometry.Vector up = right.crossProduct(direction);

    float zNear = 0.1f;
    float zFar = 5.0f;

    public Camera()
    {
        updateLookAt();
    }

    public void setPosition(Geometry.Point position)
    {
        this.position = position;
        updateLookAt();
    }

    private void updateLookAt()
    {
        Matrix.setLookAtM(viewMatrix, 0,
                // x y z
                position.x, position.y, position.z, // eye position
                position.x + direction.x, position.y + direction.y, position.z + direction.z, // center position
                up.x, up.y, up.z); // up position?

        Matrix.perspectiveM(fustrumMatrix, 0, 90f, (float)Crispin.getSurfaceWidth()/Crispin.getSurfaceHeight(), zNear, zFar);
    }

    public float[] getViewMatrix()
    {
        return viewMatrix;
    }

    public float[] getFustrumMatrix()
    {
        return fustrumMatrix;
    }
}
