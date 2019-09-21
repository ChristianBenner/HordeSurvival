package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

public class Image implements UIObject
{
    private Square square;

    private Point2D position;

    private float width;

    private float height;

    public Image(Point2D position, float width, float height, int imageResourceId)
    {
        this.position.x = position.x;
        this.position.y = position.y;
        this.width = width;
        this.height = height;

        square = new Square(true);
        square.setScale(width, height);
        square.setPosition(position);
        square.setMaterial(new Material(new Texture(imageResourceId)));
    }

    public Image(Point2D position, int imageResourceId)
    {
        this.position = position;

        square = new Square(true);
        square.setPosition(position);

        Texture texture = new Texture(imageResourceId);
        square.setScale(texture.getWidth(), texture.getHeight());
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        square.setMaterial(new Material(texture));
    }

    public Image(float width, float height, int imageResourceId)
    {
        this(new Point2D(), width, height, imageResourceId);
    }

    public Image(int imageResourceId)
    {
        this(new Point2D(), imageResourceId);
    }

    @Override
    public void setPosition(Point3D position) {

    }

    @Override
    public void setPosition(float x, float y, float z) {

    }

    @Override
    public void setPosition(Point2D position) {

    }

    @Override
    public void setPosition(float x, float y)
    {
        square.setPosition(x, y);
    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void setWidth(float width) {

    }

    @Override
    public float getWidth() {
        return square.getScale().x;
    }

    @Override
    public void setHeight(float height) {

    }

    @Override
    public float getHeight() {
        return square.getScale().y;
    }

    @Override
    public void setColour(Colour colour) {

    }

    @Override
    public Colour getColour() {
        return null;
    }

    @Override
    public void setOpacity(float alpha) {
        square.getMaterial().getColour().setAlpha(alpha);
    }

    @Override
    public float getOpacity() {
        return 0;
    }

    @Override
    public void draw(Camera2D camera)
    {
        square.render(camera);
    }
}
