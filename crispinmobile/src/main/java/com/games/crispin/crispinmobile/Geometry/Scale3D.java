package com.games.crispin.crispinmobile.Geometry;

public class Scale3D extends Scale2D
{
    public float z;

    public Scale3D()
    {
        super();
        z = 1.0f;
    }

    public Scale3D(float x, float y, float z)
    {
        super(x, y);
        this.z = z;
    }
}
