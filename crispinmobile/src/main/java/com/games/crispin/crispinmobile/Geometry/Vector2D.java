package com.games.crispin.crispinmobile.Geometry;

public class Vector2D
{
    public final float x, y;

    public Vector2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float length()
    {
        return (float)Math.sqrt(x * x + y * y);
    }

    public float dotProduct(Vector3D other)
    {
        return (x * other.x) + (y * other.y);
    }

    public Vector2D scale(float f)
    {
        return new Vector2D(x * f, y * f);
    }

    @Override
    public String toString()
    {
        return "Vector2D[x: " + x + ", y: " + y + "]";
    }
}
