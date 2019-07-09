package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

public class FontSquare extends Square
{
    private Point2D characterOffset;
    private Point3D textPosition;

    public FontSquare()
    {
        super();

        characterOffset = new Point2D();
        textPosition = new Point3D();

        // Because text shouldn't have colour per vertex ignore the data if it is present
        super.ignoreData(IGNORE_COLOUR_DATA_FLAG);
    }

    public FontSquare(Material material, Point3D textPosition, Point2D characterOffset)
    {
        super(material);

        this.textPosition = textPosition;
        this.characterOffset = characterOffset;
        updatePosition();

        // Because text shouldn't have colour per vertex ignore the data if it is present
        super.ignoreData(IGNORE_COLOUR_DATA_FLAG);
    }

    private void updatePosition()
    {
        super.setPosition(textPosition.x + characterOffset.x,
                textPosition.y + characterOffset.y,
                textPosition.z);
    }

    public void setTextPosition(Point3D textPosition)
    {
        this.textPosition = textPosition;
        updatePosition();
    }

    public void setTextPosition(float x, float y, float z)
    {
        this.textPosition.x = x;
        this.textPosition.y = y;
        this.textPosition.z = z;
        updatePosition();
    }

    public void setTextPosition(Point2D textPosition)
    {
        this.textPosition.x = textPosition.x;
        this.textPosition.y = textPosition.y;
        updatePosition();
    }

    public void setTextPosition(float x, float y)
    {
        this.textPosition.x = x;
        this.textPosition.y = y;
        updatePosition();
    }

    public void setCharacterOffset(Point2D characterOffset)
    {
        this.characterOffset = characterOffset;
        updatePosition();
    }

    public void setCharacterOffset(float x, float y)
    {
        this.characterOffset.x = x;
        this.characterOffset.y = y;
        updatePosition();
    }

    public Point2D getCharacterOffset()
    {
        return this.characterOffset;
    }

    public Point3D getTextPosition()
    {
        return this.textPosition;
    }
}
