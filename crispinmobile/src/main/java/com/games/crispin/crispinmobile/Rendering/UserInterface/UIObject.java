package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;

/**
 * Base class for user interface objects. Contains functions that should be generic to most user
 * interface objects so that there is a common way to interact with them.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class UIObject
{
    // Position of the user interface
    protected Point3D position;

    // Scale of the user interface
    protected Scale3D scale;

    /**
     * Set the position of the user interface
     *
     * @param position  The new position for the user interface
     * @since 1.0
     */
    public void setPosition(Point3D position)
    {
        this.position = position;
    }

    /**
     * Set the position of the user interface
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param z The z-coordinate
     * @since 1.0
     */
    public void setPosition(float x,
                            float y,
                            float z)
    {
        this.position = position;
    }

    /**
     * Set the position of the user interface
     *
     * @param position  The new position for the user interface
     * @since 1.0
     */
    public void setPosition(Point2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    /**
     * Set the position of the user interface
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @since 1.0
     */
    public void setPosition(float x, float y)
    {
        this.position.x = x;
        this.position.y = y;
    }

    /**
     * Get the user interface position
     *
     * @return The user interface position
     * @since 1.0
     */
    public Point2D getPosition()
    {
        return this.position;
    }

    /**
     * Set the scale of the user interface
     *
     * @param scale The new scale for the user interface
     * @since 1.0
     */
    public void setScale(Scale3D scale)
    {
        this.scale = scale;
    }

    /**
     * Set the scale of the user interface
     *
     * @param x The x-axis multiplier
     * @param y The y-axis multiplier
     * @param z The z-axis multiplier
     * @since 1.0
     */
    public void setScale(float x,
                         float y,
                         float z)
    {
        this.scale = scale;
    }

    /**
     * Set the scale of the user interface
     *
     * @param scale The new scale for the user interface
     * @since 1.0
     */
    public void setScale(Scale2D scale)
    {
        this.scale.x = scale.x;
        this.scale.y = scale.y;
    }

    /**
     * Set the scale of the user interface
     *
     * @param x The x-axis multiplier
     * @param y The y-axis multiplier
     * @since 1.0
     */
    public void setScale(float x, float y)
    {
        this.scale.x = x;
        this.scale.y = y;
    }

    /**
     * Get the current scale of the user interface
     *
     * @return The scale of the user interface
     * @since 1.0
     */
    public Scale3D getScale()
    {
        return this.scale;
    }

    /**
     * Create a user interface object. Designed to be called from a base class. Sets up UI with
     * default values.
     *
     * @since 1.0
     */
    protected UIObject()
    {
        position = new Point3D();
        scale = new Scale3D();
    }
}
