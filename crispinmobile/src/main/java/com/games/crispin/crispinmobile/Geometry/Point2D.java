package com.games.crispin.crispinmobile.Geometry;

public class Point2D
{
    public float x, y;

    public Point2D()
    {
        this.x = x;
        this.y = y;
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
}
