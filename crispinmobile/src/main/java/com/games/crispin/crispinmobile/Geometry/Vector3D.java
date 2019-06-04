package com.games.crispin.crispinmobile.Geometry;

public class Vector3D
{
    public final float x, y, z;

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length(){
        return (float)Math.sqrt(
                x * x
                        + y * y
                        + z*z);
    }

    // http://en.wikipedia.org/wiki/Cross_product
    public Vector3D crossProduct(Vector3D other) {
        return new Vector3D(
                (y * other.z) - (z * other.y),
                (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x));
    }

    public float dotProduct(Vector3D other)
    {
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    public Vector3D scale(float f)
    {
        return new Vector3D(x * f, y * f, z * f);
    }

    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y + ", z: " + y;
    }
}
