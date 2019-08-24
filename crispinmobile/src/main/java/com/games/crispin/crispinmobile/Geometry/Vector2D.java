package com.games.crispin.crispinmobile.Geometry;

/**
 * Vector2D provides a singular object that can represent a two dimensional (x and y) direction. It is
 * the base class for Vector3D.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Vector3D
 * @since       1.0
 */
public class Vector2D
{
    // Tag used in logging output
    private static final String TAG = "Vector2D";

    // The x dimension value
    public float x;

    // The y dimension value
    public float y;

    /**
     * Construct a 2D direction object
     *
     * @param x The x dimension value
     * @param y The y dimension value
     * @since 1.0
     */
    public Vector2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a 2D direction object with default values (x: 0.0 and y: 0.0)
     *
     * @since 1.0
     */
    public Vector2D()
    {
        this(0.0f, 0.0f);
    }

    /**
     * Get the magnitude of the direction
     *
     * @return  The magnitude of the direction as a float
     * @since   1.0
     */
    public float getMagnitude()
    {
        return (float)Math.sqrt(x * x + y * y);
    }

    /**
     * Get the length (magnitude) of the direction
     *
     * @return  The length (magnitude) of the direction as a float
     * @since   1.0
     */
    public float getLength()
    {
        return getMagnitude();
    }

    /**
     * Get the dot product of the direction
     *
     * @return  The dot product of the direction as a float
     * @since   1.0
     */
    public float getDotProduct(Vector2D other)
    {
        return (x * other.x) + (y * other.y);
    }

    /**
     * Scale the direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies both dimensions x and y)
     * @since 1.0
     */
    public void scale(float scale)
    {
        this.x *= scale;
        this.y *= scale;
    }

    /**
     * Scale each dimension of the direction by a specific multiplier
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
     * Get a string that contains the direction data that can be used in a log
     *
     * @return  String in the format 'Vector2D[x:X,y:Y]'
     * @since 1.0
     */
    @Override
    public String toString()
    {
        return TAG + "[x: " + x + ", y: " + y + "]";
    }
}
