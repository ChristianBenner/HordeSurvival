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

    @Override
    public String toString()
    {
        return "Scale3D[x:" + x + ",y:" + y + ",z:" + z + "]";
    }
}
