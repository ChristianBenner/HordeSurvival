package com.games.crispin.crispinmobile.Rendering.Models;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;


public class FontSquare extends Square
{
    // The position of the text UI object associated to the character
    private Point3D textPosition;

    // The offset of the character from the text position
    private Point2D characterOffset;

    /**
     * Construct a FontSquare object with a material and position
     *
     * @param material          The material to apply to the object
     * @param textPosition      The position of the text object. This is not the position of the
     *                          character itself, rather the position of the text UI object it is a
     *                          part of
     * @param characterOffset   The position offset of the character from the text position
     * @since 1.0
     */
    public FontSquare(Material material,
                      Point3D textPosition,
                      Point2D characterOffset)
    {
        super(material);

        // Because text shouldn't have colour per vertex ignore the data if it is present
        super.material.ignoreData(Material.IGNORE_COLOUR_DATA_FLAG);
        this.textPosition = textPosition;
        this.characterOffset = characterOffset;

        updatePosition();

    }

    /**
     * Construct a FontSquare object
     *
     * @since 1.0
     */
    public FontSquare()
    {
        super();

        // Because text shouldn't have colour per vertex ignore the data if it is present
        super.material.ignoreData(Material.IGNORE_COLOUR_DATA_FLAG);
        textPosition = new Point3D();
        characterOffset = new Point2D();
    }

    /**
     * Update and calculate the position of the rendered object. The position is calculated from the
     * given text position and character offset.
     *
     * @since 1.0
     */
    private void updatePosition()
    {
        super.setPosition(textPosition.x + characterOffset.x,
                textPosition.y + characterOffset.y,
                textPosition.z);
    }

    /**
     * Set the text position. This should be done if the position of the associated text UI object
     * has been changed. This causes a position update.
     *
     * @since 1.0
     */
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
