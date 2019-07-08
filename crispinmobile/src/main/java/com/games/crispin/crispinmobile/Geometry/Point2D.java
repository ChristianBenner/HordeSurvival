package com.games.crispin.crispinmobile.Geometry;

public class Point2D
{
    public float x, y;

    public Point2D()
    {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Point2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Point2D traverse(float x, float y)
    {
        return new Point2D(this.x + x, this.y + y);
    }

    @Override
    public String toString()
    {
        return "Point2D[x:" + x + ",y:" + y + "]";
    }
}
