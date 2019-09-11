package com.games.crispin.crispinmobile.Rendering.Data;

/**
 * Colour class allows you to store RGBA colour data. The class allows you to create your own
 * colours but also contains some pre-defined ones. Colour channels are represented as floats with a
 * range of 0.0 to 1.0.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Colour
{
    // Pre-defined red colour
    public static final Colour RED = new Colour(1.0f, 0.0f, 0.0f);

    // Pre-defined green colour
    public static final Colour GREEN = new Colour(0.0f, 1.0f, 0.0f);

    // Pre-defined blue colour
    public static final Colour BLUE = new Colour(0.0f, 0.0f, 1.0f);

    // Pre-defined black colour
    public static final Colour BLACK = new Colour(0.0f, 0.0f, 0.0f);

    // Pre-defined white colour
    public static final Colour WHITE = new Colour(1.0f, 1.0f, 1.0f);

    // Pre-defined grey colour
    public static final Colour GREY = new Colour(0.5f, 0.5f, 0.5f);

    // Pre-defined light-grey colour
    public static final Colour LIGHT_GREY = new Colour(0.75f, 0.75f, 0.75f);

    // Pre-defined dark-grey colour
    public static final Colour DARK_GREY = new Colour(0.25f, 0.25f, 0.25f);

    // Pre-defined yellow colour
    public static final Colour YELLOW = new Colour(1.0f, 1.0f, 0.0f);

    // Pre-defined cyan colour
    public static final Colour CYAN = new Colour(0.0f, 1.0f, 1.0f);

    // Pre-defined magenta colour
    public static final Colour MAGENTA = new Colour(1.0f, 0.0f, 1.0f);

    // Pre-defined orange colour
    public static final Colour ORANGE = new Colour(1.0f, 0.415f, 0.0f);

    // Default alpha value if one is not provided
    private static final float DEFAULT_ALPHA_VALUE = 1.0f;

    // Default red value if one is not provided
    private static final float DEFAULT_RED_VALUE = 1.0f;

    // Default green value if one is not provided
    private static final float DEFAULT_GREEN_VALUE = 1.0f;

    // Default blue value if one is not provided
    private static final float DEFAULT_BLUE_VALUE = 1.0f;

    // Red channel value
    private float red;

    // Green channel value
    private float green;

    // Blue channel value
    private float blue;

    // Alpha channel value
    private float alpha;

    /**
     * Construct a colour object with RGBA values
     *
     * @param red   The red colour value (0.0 for no red, 1.0 for maximum red)
     * @param green The green colour value (0.0 for no green, 1.0 for maximum green)
     * @param blue  The blue colour value (0.0 for no blue, 1.0 for maximum blue)
     * @param alpha The alpha colour value (0.0 for no alpha, 1.0 for maximum alpha)
     * @since 1.0
     */
    public Colour(float red,
                  float green,
                  float blue,
                  float alpha)
    {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    /**
     * Construct a colour object with RGB values. Uses default alpha value of 1.0
     *
     * @param red   The red colour value (0.0 for no red, 1.0 for maximum red)
     * @param green The green colour value (0.0 for no green, 1.0 for maximum green)
     * @param blue  The blue colour value (0.0 for no blue, 1.0 for maximum blue)
     * @since   1.0
     */
    public Colour(float red,
                  float green,
                  float blue)
    {
        this(red,
                green,
                blue,
                DEFAULT_ALPHA_VALUE);
    }

    /**
     * Construct a colour object with default RGBA values (Red: 1.0, Green: 1.0, Blue: 1.0, Alpha:
     * 1.0)
     *
     * @since   1.0
     */
    public Colour()
    {
        this(DEFAULT_RED_VALUE,
                DEFAULT_GREEN_VALUE,
                DEFAULT_BLUE_VALUE,
                DEFAULT_ALPHA_VALUE);
    }

    /**
     * Set the value for the red channel (range 0.0 to 1.0)
     *
     * @param red   New value for the red channel
     * @since   1.0
     */
    public void setRed(float red)
    {
        this.red = red;
    }

    /**
     * Get the value of the red channel
     *
     * @return  Floating point from 0.0-1.0 representing the intensity of the red channel
     * @since   1.0
     */
    public float getRed()
    {
        return this.red;
    }

    /**
     * Set the value for the green channel (range 0.0 to 1.0)
     *
     * @param green New value for the green channel
     * @since   1.0
     */
    public void setGreen(float green)
    {
        this.green = green;
    }

    /**
     * Get the value of the green channel
     *
     * @return  Floating point from 0.0-1.0 representing the intensity of the green channel
     * @since   1.0
     */
    public float getGreen()
    {
        return this.green;
    }

    /**
     * Set the value for the blue channel (range 0.0 to 1.0)
     *
     * @param blue  New value for the blue channel
     * @since   1.0
     */
    public void setBlue(float blue)
    {
        this.blue = blue;
    }

    /**
     * Get the value of the blue channel
     *
     * @return  Floating point from 0.0-1.0 representing the intensity of the blue channel
     * @since   1.0
     */
    public float getBlue()
    {
        return this.blue;
    }

    /**
     * Set the value for the alpha channel (range 0.0 to 1.0)
     *
     * @param alpha New value for the alpha channel
     * @since   1.0
     */
    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
    }

    /**
     * Get the value of the alpha channel
     *
     * @return  Floating point from 0.0-1.0 representing the intensity of the alpha channel
     * @since   1.0
     */
    public float getAlpha()
    {
        return this.alpha;
    }
}
