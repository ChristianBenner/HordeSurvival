package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;

public class Plane implements UIObject
{
    // The square render object
    private Square square;

    public Plane(Point2D position, float width, float height)
    {
        square = new Square(false);
        square.setPosition(position);
        square.setScale(width, height);
    }

    public Plane(float width, float height)
    {
        this(new Point2D(), width, height);
    }


    /**
     * Set the position of the user interface
     *
     * @param position  The new position for the user interface
     * @since 1.0
     */
    @Override
    public void setPosition(Point3D position)
    {
        square.setPosition(position);
    }

    /**
     * Set the position of the user interface
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param z The z-coordinate
     * @since 1.0
     */
    @Override
    public void setPosition(float x,
                            float y,
                            float z)
    {
        square.setPosition(x, y, z);
    }

    /**
     * Set the position of the user interface
     *
     * @param position  The new position for the user interface
     * @since 1.0
     */
    @Override
    public void setPosition(Point2D position)
    {
        square.setPosition(position);
    }

    /**
     * Set the position of the user interface
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @since 1.0
     */
    @Override
    public void setPosition(float x, float y)
    {
        square.setPosition(x, y);
    }

    /**
     * Get the user interface position
     *
     * @return The user interface position
     * @since 1.0
     */
    @Override
    public Point2D getPosition()
    {
        return square.getPosition();
    }

    /**
     * Set the width of the UI object
     *
     * @param width The new width of the object
     * @since 1.0
     */
    @Override
    public void setWidth(float width)
    {
        square.setScaleX(width);
    }

    /**
     * Get the width of the UI object
     *
     * @return The width of the UI object
     * @since 1.0
     */
    @Override
    public float getWidth()
    {
        return square.getScale().x;
    }

    /**
     * Set the height of the UI object
     *
     * @param height    The new width of the object
     * @since 1.0
     */
    @Override
    public void setHeight(float height)
    {
        square.setScaleY(height);
    }

    /**
     * Get the height of the UI object
     *
     * @return The height of the UI object
     * @since 1.0
     */
    @Override
    public float getHeight()
    {
        return square.getScale().y;
    }

    /**
     * Set the colour of the UI object
     *
     * @param colour    The new colour for the UI object
     * @since 1.0
     */
    @Override
    public void setColour(Colour colour)
    {
        this.square.getMaterial().setColour(colour);
    }

    /**
     * Get the colour of the UI object
     *
     * @return The colour of the UI object
     * @since 1.0
     */
    @Override
    public Colour getColour()
    {
        return this.square.getMaterial().getColour();
    }

    /**
     * Set the opacity of the UI object
     *
     * @param alpha The new alpha channel value for the UI object
     * @since 1.0
     */
    @Override
    public void setOpacity(float alpha)
    {
        this.square.getMaterial().getColour().setAlpha(alpha);
    }

    /**
     * Get the opacity of the UI object
     *
     * @return  The alpha channel value of the UI object
     * @since 1.0
     */
    @Override
    public float getOpacity()
    {
        return this.square.getMaterial().getColour().getAlpha();
    }

    /**
     * Draw function designed to be overridden
     *
     * @since 1.0
     */
    @Override
    public void draw(Camera2D camera)
    {
        square.render(camera);
    }
}
