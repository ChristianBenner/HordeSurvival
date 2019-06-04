package com.games.crispin.crispinmobile.Rendering.Data;

public class Colour
{
    public static final Colour RED = new Colour(1.0f, 0.0f, 0.0f);
    public static final Colour GREEN = new Colour(0.0f, 1.0f, 0.0f);
    public static final Colour BLUE = new Colour(0.0f, 0.0f, 1.0f);
    public static final Colour BLACK = new Colour(0.0f, 0.0f, 0.0f);
    public static final Colour WHITE = new Colour(1.0f, 1.0f, 1.0f);
    public static final Colour GREY = new Colour(0.5f, 0.5f, 0.5f);
    public static final Colour LIGHT_GREY = new Colour(0.75f, 0.75f, 0.75f);
    public static final Colour DARK_GREY = new Colour(0.25f, 0.25f, 0.25f);
    public static final Colour YELLOW = new Colour(1.0f, 1.0f, 0.0f);
    public static final Colour CYAN = new Colour(0.0f, 1.0f, 1.0f);
    public static final Colour MAGENTA = new Colour(1.0f, 0.0f, 1.0f);

    private float red;
    private float green;
    private float blue;
    private float alpha;

    public Colour(float red, float green, float blue, float alpha)
    {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    public Colour(float red, float green, float blue)
    {
        this(red, green, blue, 1.0f);
    }

    public void setRed(float red)
    {
        this.red = red;
    }

    public float getRed()
    {
        return this.red;
    }

    public void setGreen(float green)
    {
        this.green = green;
    }

    public float getGreen()
    {
        return this.green;
    }

    public void setBlue(float blue)
    {
        this.blue = blue;
    }

    public float getBlue()
    {
        return this.blue;
    }

    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
    }

    public float getAlpha()
    {
        return this.alpha;
    }
}
