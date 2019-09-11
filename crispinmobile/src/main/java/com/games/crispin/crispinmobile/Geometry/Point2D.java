package com.games.crispin.crispinmobile.Geometry;

/**
 * Point2D provides a singular object that can represent two dimensions (x and y). It is the base
 * class for Point3D. Along with the data on two dimensions, it contains useful methods that can be
 * used to operate on the data.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Point3D
 * @since       1.0
 */
public class Point2D
{
    // Tag used in logging output
    private static final String TAG = "Point2D";

    // The x-coordinate
    public float x;

    // The y-coordinate
    public float y;

    /**
     * Construct a 2D point object
     *
     * @param x The x co-ordinate
     * @param y The y co-ordinate
     * @since   1.0
     */
    public Point2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a 2D point object with default values (x: 0.0, y: 0.0)
     *
     * @since           1.0
     */
    public Point2D()
    {
        this(0.0f, 0.0f);
    }

    /**
     * Translate the point
     *
     * @param point Position to translate by
     * @since   1.0
     */
    public void translate(Point2D point)
    {
        this.x += point.x;
        this.y += point.y;
    }

    /**
     * Translate the point
     *
     * @param x The x co-ordinate to translate the point by
     * @param y The y co-ordinate to translate the point by
     * @since   1.0
     */
    public void translate(float x, float y)
    {
        this.x += x;
        this.y += y;
    }

    /**
     * Translate the point
     *
     * @param vector    A Vector2D object containing x and y translation
     * @since 1.0
     */
    public void translate(Vector3D vector)
    {
        translate(vector.x, vector.y);
    }

    /**
     * Scale the object in each dimension
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @since 1.0
     */
    public void scale(float x, float y)
    {
        this.x *= x;
        this.y *= y;
    }

    /**
     * Scale the object in every dimension
     *
     * @param scale The scale multiplier to scale both dimensions (x and y)
     * @since 1.0
     */
    public void scale(float scale)
    {
        scale(scale, scale);
    }

    /**
     * Scale the object in each dimension using a Scale2D object
     *
     * @param scale The Scale2D object
     * @since 1.0
     */
    public void scale(Scale2D scale)
    {
        scale(scale.x, scale.y);
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
    }

    /**
     * Get the distance to another point
     *
     * @param x The x co-ordinate of the target point
     * @param y The y co-ordinate of the target point
     * @return  The distance magnitude as a float
     * @since 1.0
     */
    public float getDistance(float x, float y)
    {
        // Distance in the x plane
        final float DX = x - this.x;

        // Distance in the y plane
        final float DY = y - this.y;

        return (float)Math.sqrt((double)((DX * DX) + (DY * DY)));
    }

    /**
     * Get the distance to another point
     *
     * @param target    Another point to find the distance to
     * @return          The distance magnitude as a float
     * @since 1.0
     */
    public float getDistance(Point2D target)
    {
        return getDistance(target.x, target.y);
    }

    /**
     * Get a distance direction to another point (gives distance in each dimension)
     *
     * @param target    Another point to find the distance to
     * @return          The distance Vector2D containing the distance in each dimension
     * @since 1.0
     */
    public Vector2D getDistance2D(Point3D target)
    {
        return new Vector2D(
                target.x - x,
                target.y - y);
    }

    /**
     * Get a string that contains the point data that can be used in a log
     *
     * @return  String in the format 'Point2D[x:X,y:Y]'
     * @since   1.0
     */
    @Override
    public String toString()
    {
        return TAG + "[x:" + x + ",y:" + y + "]";
    }
}
