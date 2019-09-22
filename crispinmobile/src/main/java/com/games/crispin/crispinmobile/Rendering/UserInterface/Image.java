package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

public class Image extends Plane
{
    public Image(int resourceId)
    {
        super(new Point2D(), new Scale2D(), true);

        final Texture TEXTURE = new Texture(resourceId);
        super.plane.getMaterial().setTexture(TEXTURE);
        super.setWidth(TEXTURE.getWidth());
        super.setHeight(TEXTURE.getHeight());
        super.setBorderColour(Colour.MAGENTA);
    }
}
