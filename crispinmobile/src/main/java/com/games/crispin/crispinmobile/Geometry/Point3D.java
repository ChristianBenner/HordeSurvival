package com.games.crispin.crispinmobile.Geometry;

/**
 * Point3D provides a singular object that can represent three dimensions (x, y and z). It is
 * a subclass of the Point2D class. Along with the data on three dimensions, it contains useful
 * methods that can be used to operate on the data.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Point2D
 * @since       1.0
 */
public class Point3D extends Point2D
{
    // Tag used in logging output
    private static final String TAG = "Point3D";

    // The z-coordinate
    public float z;

    /**
     * Construct a 3D point object
     *
     * @param x The x co-ordinate
     * @param y The y co-ordinate
     * @param z The z co-ordinate
     * @since 1.0
     */
    public Point3D(float x,
                   float y,
                   float z)
    {
        super(x, y);
        this.z = z;
    }

    /**
     * Construct a 3D point object with an existing 2D point
     *
     * @param point2D A Point2D object containing x and y values
     * @param z The z co-ordinate
     * @since 1.0
     */
    public Point3D(Point2D point2D, float z)
    {
        this(
                point2D.x,
                point2D.y,
                z);
    }

    /**
     * Construct a 2D point object with default values (x: 0.0, y: 0.0, z: 0.0)
     *
     * @since 1.0
     */
    public Point3D()
    {
        super();
        this.z = 0.0f;
    }

    /**
     * Translate the point
     *
     * @param point Point to translate by
     * @since 1.0
     */
    public void translate(Point3D point)
    {
        this.x += point.x;
        this.y += point.y;
        this.z += point.z;
    }

    /**
     * Translate the point
     *
     * @param x The x co-ordinate
     * @param y The y co-ordinate
     * @param z The z co-ordinate
     * @since 1.0
     */
    public void translate(float x,
                          float y,
                          float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Translate the point
     *
     * @param vector    A Vector3D object containing x, y and z translation
     * @since 1.0
     */
    public void translate(Vector3D vector)
    {
        translate(
                vector.x,
                vector.y,
                vector.z);
    }

    /**
     * Scale the object in each dimension
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @param z Scale multiplier for the z dimension
     * @since 1.0
     */
    public void scale(float x,
                      float y,
                      float z)
    {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    /**
     * Scale the object in every dimension
     *
     * @param scale The scale multiplier to scale in all dimensions (x, y and z)
     * @since 1.0
     */
    public void scale(float scale)
    {
        scale(
                scale,
                scale,
                scale);
    }

    /**
     * Scale the object in each dimension using a Scale3D object
     *
     * @param scale The Scale3D object
     * @since 1.0
     */
    public void scale(Scale3D scale)
    {
        scale(
                scale.x,
                scale.y,
                scale.z);
    }

    /**
     * Invert the dimensions in the point
     *
     * @since 1.0
     */
    public void invert()
    {
        x = -x;
        y = -y;
        z = -y;
    }

    /**
     * Get the distance to another point
     *
     * @param x The x co-ordinate of the target point
     * @param y The y co-ordinate of the target point
     * @param z The z co-ordinate of the target point
     * @return  The distance magnitude as a float
     * @since 1.0
     */
    public float getDistance(float x,
                          float y,
                          float z)
    {
        // Distance in the x plane
        final float DX = x - this.x;

        // Distance in the y plane
        final float DY = y - this.y;

        // Distance in the z plane
        final float DZ = z - this.z;

        return (float)Math.sqrt((double)((DX * DX) + (DY * DY) + (DZ * DZ)));
    }

    /**
     * Get the distance to another point
     *
     * @param target    Another point to find the distance to
     * @return          The distance magnitude as a float
     * @since 1.0
     */
    public float getDistance(Point3D target)
    {
        return getDistance(
                target.x,
                target.y,
                target.z);
    }

    /**
     * Get a distance direction to another point (gives distance in each dimension)
     *
     * @param target    Another point to find the distance to
     * @return          The distance Vector3D containing the distance in each dimension
     * @since 1.0
     */
    public Vector3D getDistance3D(Point3D target)
    {
        return new Vector3D(
                target.x - x,
                target.y - y,
                target.z - z);
    }

    /**
     * Get a string that contains the point data that can be used in a log
     *
     * @return  String in the format 'Point3D[x:X,y:Y,z:Z]'
     * @since 1.0
     */
    @Override
    public String toString()
    {
        return TAG + "[x:" + x + ",y:" + y + ",z:" + z + "]";
    }
}