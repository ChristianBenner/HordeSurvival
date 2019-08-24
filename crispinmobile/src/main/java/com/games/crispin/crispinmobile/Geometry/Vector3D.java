package com.games.crispin.crispinmobile.Geometry;

/**
 * Vector3D provides a singular object that can represent a three dimensional (x, y and z) vector.
 * It is a subclass of the Vector2D class.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Vector2D
 * @since       1.0
 */
public class Vector3D extends Vector2D
{
    // Tag used in logging output
    private static final String TAG = "Vector3D";

    // The z dimension value
    public float z;

    /**
     * Construct a 3D vector object
     *
     * @param x The x dimension value
     * @param y The y dimension value
     * @param z The z dimension value
     * @since 1.0
     */
    public Vector3D(float x,
                    float y,
                    float z)
    {
        super(x, y);
        this.z = z;
    }

    /**
     * Construct a 3D vector object with default values (x: 0.0, y: 0.0 and z: 0.0)
     *
     * @since 1.0
     */
    public Vector3D()
    {
        this(
                0.0f,
                0.0f,
                0.0f);
    }

    /**
     * Get the magnitude of the vector
     *
     * @return  The magnitude of the vector as a float
     * @since   1.0
     */
    public float getMagnitude()
    {
        return (float)Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * Get the length (magnitude) of the vector
     *
     * @return  The length (magnitude) of the vector as a float
     * @since   1.0
     */
    public float getLength()
    {
        return getMagnitude();
    }

    /**
     * Get the cross product of two vectors
     *
     * @param other The other vector
     * @return  The cross product vector
     * @since   1.0
     */
    public Vector3D getCrossProduct(Vector3D other)
    {
        return new Vector3D(
                (y * other.z) - (z * other.y),
                (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x));
    }

    /**
     * Get the dot product of two vectors
     *
     * @param other The other vector
     * @return  The dot product vector
     * @since   1.0
     */
    public float getDotProduct(Vector3D other)
    {
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    /**
     * Scale the vector by a multiplier
     *
     * @param scale Scale multiplier (multiplies all dimensions x, y and z)
     * @since 1.0
     */
    public void scale(float scale)
    {
        super.scale(scale);
        this.z *= scale;
    }

    /**
     * Scale each dimension of the vector by a specific multiplier
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
        super.scale(x, y);
        this.z *= z;
    }

    /**
     * Get a string that contains the vector data that can be used in a log
     *
     * @return  String in the format 'Vector2D[x:X,y:Y]'
     * @since 1.0
     */
    @Override
    public String toString()
    {
        return TAG + "[x: " + x + ", y: " + y + ", z: " + z + "]";
    }
}
