package com.games.crispin.crispinmobile.Geometry;

public class Rotation2D
{
    public float x, y;

    public Rotation2D()
    {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Rotation2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "Rotation2D[x:" + x + ",y:" + y + "]";
    }
}
