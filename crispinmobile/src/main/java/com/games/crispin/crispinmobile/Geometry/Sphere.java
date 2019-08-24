package com.games.crispin.crispinmobile.Geometry;

/**
 * Sphere is a class that represents a virtual spherical object using position and radius data. The
 * class is used in geometry calculations. This is not a graphical sphere, it is not an object that
 * can be rendered.
 *
 * @author  Christian Benner
 * @version %I%, %G%
 * @see     Point3D
 * @see     Geometry
 * @since   1.0
 */
public class Sphere
{
    // Tag used in logging output
    private static final String TAG = "Sphere";

    // The position of the center of the sphere
    public final Point3D center;

    // The radius of the sphere
    public final float radius;

    /**
     * Construct a sphere object
     *
     * @param center    The position of the center of the sphere
     * @param radius    The radius of the sphere
     * @since 1.0
     */
    public Sphere(Point3D center, float radius)
    {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Get a string that contains the sphere data that can be used in a log
     *
     * @return  String in the format 'Sphere{Point3D[x:X,y:Y,z:Z],float[radius:R]}'
     * @since   1.0
     */
    @Override
    public String toString()
    {
        return TAG + "{" + center.toString() + ", float[radius:" + radius + "]}";
    }
}