package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Geometry;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class Plane implements UIObject
{
    private static final float BORDER_SIZE_PIXELS = 5;
    protected Point2D position;
    protected Scale2D size;
    private float borderSize;

    protected Square plane;
    private Square border;
    private boolean borderEnabled;

    protected Plane(Point2D position, Scale2D size, boolean renderTexels)
    {
        this.position = position;
        this.size = size;
        this.borderSize = BORDER_SIZE_PIXELS;
        this.borderEnabled = false;

        plane = new Square(renderTexels);
        updatePosition();
    }

    public Plane(Point2D position, Scale2D size)
    {
        this(position, size, false);
    }

    public Plane(Scale2D size)
    {
        this(new Point2D(), size);
    }

    public Plane(Scale2D size, boolean renderTexels)
    {
        this(new Point2D(), size, renderTexels);
    }

    // recalculate positions
    private void updatePosition()
    {
        if(borderEnabled)
        {
            this.border.setScale(size);
            this.border.setPosition(position);
            this.plane.setScale(new Scale2D(size.x - (borderSize * 2f),
                    size.y - (borderSize * 2f)));
            this.plane.setPosition(Geometry.translate(position, borderSize, borderSize));
        }
        else
        {
            this.plane.setScale(new Scale2D(size.x, size.y));
            this.plane.setPosition(position);
        }
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
        this.position = position;
        updatePosition();
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
        this.position.x = x;
        this.position.y = y;
        updatePosition();
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
        this.position = position;
        updatePosition();
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
        this.position.x = x;
        this.position.y = y;
        updatePosition();
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
        return position;
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
        size.x = width;
        updatePosition();
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
        return size.x;
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
        size.y = height;
        updatePosition();
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
        return size.y;
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
        this.plane.getMaterial().setColour(colour);
    }

    public void setBorderColour(Colour colour)
    {
        if(border == null)
        {
            border = new Square(false);
            border.setPosition(position);
        }

        this.border.getMaterial().setColour(colour);
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
        return this.plane.getMaterial().getColour();
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
        this.plane.getMaterial().getColour().setAlpha(alpha);

        if (borderEnabled)
        {
            this.border.getMaterial().getColour().setAlpha(alpha);
        }
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
        return this.plane.getMaterial().getColour().getAlpha();
    }

    /**
     * Draw function designed to be overridden
     *
     * @since 1.0
     */
    @Override
    public void draw(Camera2D camera)
    {
        glDisable(GL_DEPTH_TEST);
        if(borderEnabled)
        {
            border.render(camera);
        }
        plane.render(camera);
        glEnable(GL_DEPTH_TEST);
    }
}
