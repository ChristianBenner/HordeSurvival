package com.games.crispin.crispinmobile;

// Code is immutable - whenever we make a change we return a new object,
// it may be better to mutate floating point3D arrays with statis functions for top performance
public class Point3D extends Point2D {
    public float z;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D translate(Geometry.Vector vector)
    {
        return new Point3D(
                x + vector.x,
                y + vector.y,
                z + vector.z);
    }

    public Point3D scale(float scale)
    {
        return new Point3D(x * scale, y * scale, z * scale);
    }

    public Point3D invert()
    {
        return new Point3D(-x, -y, -z);
    }

    public float distance(Point3D target)
    {
        final float dX = target.x - x;
        final float dY = target.y - y;
        final float dZ = target.z - z;

        return (float)Math.sqrt((double)((dX * dX) + (dY * dY) + (dZ * dZ)));
    }

    public Point3D distance3D(Point3D target)
    {
        return new Point3D(target.x - x, target.y - y, target.z - z);
    }

    @Override
    public String toString()
    {
        return "Point3D[x:" + x + ",y:" + y + ",z:" + z + "]";
    }
}