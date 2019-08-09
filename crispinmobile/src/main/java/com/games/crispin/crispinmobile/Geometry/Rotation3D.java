package com.games.crispin.crispinmobile.Geometry;

/**
 * Rotation3D provides a singular object that can represent three dimensions (x, y and z) of
 * rotation. It is a subclass of the Rotation2D class.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Rotation2D
 * @since       1.0
 */
public class Rotation3D extends Rotation2D
{
    // Tag used in logging output
    private static final String TAG = "Rotation3D";

    // The z rotation multiplier
    public float z;

    /**
     * Construct a 3D rotation object
     *
     * @param x The x rotation multiplier
     * @param y The y rotation multiplier
     * @param z The z rotation multiplier
     * @since 1.0
     */
    public Rotation3D(float x,
                      float y,
                      float z)
    {
        super(x, y);
        this.z = z;
    }

    /**
     * Construct a 3D rotation object with the default values (x: 0.0, y: 0.0 and z: 0.0)
     *
     * @since 1.0
     */
    public Rotation3D()
    {
        super();
        this.z = 0.0f;
    }

    /**
     * Get a string that contains the rotation data that can be used in a log
     *
     * @return  String in the format 'Rotation3D[x:X,y:Y,z:Z]'
     * @since   1.0
     */
    @Override
    public String toString()
    {
        return TAG + "[x:" + x + ",y:" + y + ",z:" + z + "]";
    }
}
