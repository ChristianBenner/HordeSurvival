package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;

// Base class for user interface objects
public class UIObject
{
    protected Point3D position;
    protected Scale3D scale;

    public void setPosition(Point3D position)
    {
        this.position = position;
    }

    public void setPosition(float x, float y, float z)
    {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Point2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public void setPosition(float x, float y)
    {
        this.position.x = x;
        this.position.y = y;
    }

    public Point3D getPosition()
    {
        return this.position;
    }

    public void setScale(Scale3D scale)
    {
        this.scale = scale;
    }

    public void setScale(float x, float y, float z)
    {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;
    }

    public void setScale(Scale2D scale)
    {
        this.scale.x = scale.x;
        this.scale.y = scale.y;
    }

    public void setScale(float x, float y)
    {
        this.scale.x = x;
        this.scale.y = y;
    }

    public Scale3D getScale()
    {
        return this.scale;
    }

    protected UIObject()
    {
        position = new Point3D();
        scale = new Scale3D();
    }
}
