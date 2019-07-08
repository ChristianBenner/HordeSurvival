package com.games.crispin.crispinmobile.Geometry;

public class Rotation3D extends Rotation2D
{
    public float z;

    public Rotation3D()
    {
        super();
        this.z = 0.0f;
    }

    public Rotation3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString()
    {
        return "Rotation3D[x:" + x + ",y:" + y + ",z:" + z + "]";
    }
}
