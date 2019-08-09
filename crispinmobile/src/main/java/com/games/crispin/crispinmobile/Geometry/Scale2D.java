package com.games.crispin.crispinmobile.Geometry;

/**
 * Scale2D provides a singular object that can represent a two dimensional (x and y) scale
 * multiplier. It is the base class for Scale3D.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Scale3D
 * @since       1.0
 */
public class Scale2D
{
    // Tag used in logging output
    private static final String TAG = "Scale2D";

    // The default scale multiplier. It is 1.0 to maintain the scale of the model as it is provided
    protected static final float DEFAULT_SCALE_MULTIPLIER = 1.0f;

    // The x dimension scale multiplier
    public float x;

    // The y dimension scale multiplier
    public float y;

    /**
     * Construct a 2D scale object
     *
     * @param x The x scale multiplier
     * @param y The y scale multiplier
     * @since 1.0
     */
    public Scale2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a 2D scale object with the default values (x: 1.0 and y: 1.0)
     *
     * @since 1.0
     */
    public Scale2D()
    {
        this.x = DEFAULT_SCALE_MULTIPLIER;
        this.y = DEFAULT_SCALE_MULTIPLIER;
    }

    /**
     * Get a string that contains the scale data that can be used in a log
     *
     * @return  String in the format 'Scale2D[x:X,y:Y]'
     * @since 1.0
     */
    @Override
    public String toString()
    {
        return TAG + "[x:" + x + ",y:" + y + "]";
    }
}
