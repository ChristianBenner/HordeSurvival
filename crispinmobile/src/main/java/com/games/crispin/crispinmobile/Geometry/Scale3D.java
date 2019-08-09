package com.games.crispin.crispinmobile.Geometry;

/**
 * Scale3D provides a singular object that can represent a three dimensional (x, y and z) scale
 * multiplier. It is a subclass of the Scale2D class.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Scale2D
 * @since       1.0
 */
public class Scale3D extends Scale2D
{
    // Tag used in logging output
    private static final String TAG = "Scale2D";

    // The z dimension scale multiplier
    public float z;

    /**
     * Construct a 2D scale object
     *
     * @param x The x scale multiplier
     * @param y The y scale multiplier
     * @param z The z scale multiplier
     * @since 1.0
     */
    public Scale3D(float x,
                   float y,
                   float z)
    {
        super(x, y);
        this.z = z;
    }

    /**
     * Construct a 3D scale object with the default values (x: 1.0, y: 1.0 and z: 1.0)
     *
     * @since 1.0
     */
    public Scale3D()
    {
        super();
        z = DEFAULT_SCALE_MULTIPLIER;
    }

    /**
     * Get a string that contains the scale data that can be used in a log
     *
     * @return  String in the format 'Scale3D[x:X,y:Y,z:Z]'
     * @since 1.0
     */
    @Override
    public String toString()
    {
        return TAG + "[x:" + x + ",y:" + y + ",z:" + z + "]";
    }
}
