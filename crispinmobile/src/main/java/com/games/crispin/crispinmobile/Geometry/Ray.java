package com.games.crispin.crispinmobile.Geometry;

/**
 * Ray is a class that represents a virtual ray using position and direction data. A ray is
 * essentially a vector with a position. It is used in geometry calculations. This is not a
 * graphical ray, it is not an object that can be rendered.
 *
 * @author  Christian Benner
 * @version %I%, %G%
 * @see     Point3D
 * @see     Vector3D
 * @see     Geometry
 * @since   1.0
 */
public class Ray
{
    // Tag used in logging output
    private static final String TAG = "Ray";

    // The position of the ray position point
    public final Point3D position;

    // The direction of the ray
    public final Vector3D direction;

    /**
     * Construct a ray object
     *
     * @param position     The position position of the ray
     * @param direction The direction of the ray
     * @since 1.0
     */
    public Ray(Point3D position, Vector3D direction)
    {
        this.position = position;
        this.direction = direction;
    }

    /**
     * Get a string that contains the ray data that can be used in a log
     *
     * @return  String in the format 'Ray{Point3D[x:X,y:Y,z:Z], Vector3D[x:X,y:Y,z:z]}'
     * @since   1.0
     */
    @Override
    public String toString()
    {
        return TAG + "{" + position.toString() + ", " + direction.toString() + "}";
    }
}