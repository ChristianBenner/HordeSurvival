package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class Button implements UIObject
{
    private Text text;
    private Plane plane;

    private Point3D position;
    private Scale2D size;

    public Button(Font font, String text)
    {
        this.size = new Scale2D(200.0f, 200.0f);
        this.position = new Point3D();
        this.text = new Text(font, text, true, true, 200.0f);
        this.text.showBounds();
        plane = new Plane(size);
        plane.setColour(Colour.CYAN);
        plane.setBorderColour(Colour.BLUE);

        updatePosition();
    }

    private void updatePosition()
    {
        this.plane.setPosition(position);

        final float TEXT_POS_Y = plane.getPosition().y + (plane.getHeight() / 2.0f) - (text.getHeight() / 2.0f);
        this.text.setPosition(position.x, TEXT_POS_Y);
    }

    @Override
    public void setPosition(Point3D position) {
        this.position = position;
        updatePosition();
    }

    @Override
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        updatePosition();
    }

    @Override
    public void setPosition(Point2D position) {
        this.position.x = position.x;
        this.position.y = position.y;
        updatePosition();
    }

    @Override
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        updatePosition();
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void setWidth(float width) {
        this.size.x = width;
        updatePosition();
    }

    @Override
    public float getWidth() {
        return size.x;
    }

    @Override
    public void setHeight(float height) {
        this.size.y = height;
        updatePosition();
    }

    @Override
    public float getHeight() {
        return size.y;
    }

    @Override
    public void setColour(Colour colour) {
        this.text.setColour(colour);
    }

    public void setBackgroundColour(Colour colour)
    {
        this.plane.setColour(colour);
    }

    public void setBorderColour(Colour colour)
    {
        this.plane.setBorderColour(colour);
    }

    @Override
    public Colour getColour() {
        return this.text.getColour();
    }

    @Override
    public void setOpacity(float alpha) {
        this.text.setOpacity(alpha);
        this.plane.setOpacity(alpha);
    }

    @Override
    public float getOpacity() {
        return this.plane.getOpacity();
    }

    @Override
    public void draw(Camera2D camera) {
        glDisable(GL_DEPTH_TEST);
        this.plane.draw(camera);
        this.text.draw(camera);
        glEnable(GL_DEPTH_TEST);
    }
}
