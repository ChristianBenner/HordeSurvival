package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class LinearLayout implements UIObject
{
    private static float DEFAULT_PADDING_X = 10.0f;
    private static float DEFAULT_PADDING_Y = 10.0f;

    private boolean vertical;
    private ArrayList<UIObject> uiObjects;
    private float cursorX;
    private float cursorY;
    private float endX;
    private float endY;
    private float paddingX;
    private float paddingY;

    private boolean automaticHeight;

    private Plane background;
    private boolean showBackground;

    private Point2D position;
    private float width;
    private float height;

    public LinearLayout(Point2D position, float width, float height, boolean vertical)
    {
        this.position = position;
        this.width = width;
        this.height = height;
        this.vertical = vertical;
        this.showBackground = false;
        this.background = new Plane(position, new Scale2D(width, height));
        background.setBorderColour(Colour.BLACK);
        this.automaticHeight = height == 0.0f;

        uiObjects = new ArrayList<>();
        endX = 0.0f;
        endY = 0.0f;
        paddingX = DEFAULT_PADDING_X;
        paddingY = DEFAULT_PADDING_Y;
        cursorX = 0.0f;
        cursorY = 0.0f;
    }

    public LinearLayout(Point2D position, float width, float height)
    {
        this(position, width, height, false);
    }

    public LinearLayout(Point2D position)
    {
        this(position, 0.0f, 0.0f, false);
    }

    public LinearLayout(float width, float height)
    {
        this(new Point2D(), width, height, false);
    }

    public LinearLayout(boolean vertical)
    {
        this(new Point2D(), 0.0f, 0.0f, vertical);
    }

    public LinearLayout()
    {
        this(new Point2D(), 0.0f, 0.0f, false);
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
    public void setPosition(float x, float y) {

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
        return 0;
    }

    @Override
    public void setHeight(float height) {

    }

    @Override
    public float getHeight() {
        return 0;
    }

    public void setColour(Colour colour)
    {
        this.showBackground = true;
        this.background.setColour(colour);
    }

    @Override
    public Colour getColour() {
        return null;
    }

    public void add(UIObject uiObject)
    {
        uiObject.setOpacity(background.getOpacity());
        this.uiObjects.add(uiObject);

        if(vertical)
        {
            // UI Start position
            final float POS_X = position.x + cursorX;
            final float POS_Y = position.y + cursorY + paddingY;

            cursorY += paddingY + uiObject.getHeight();
        }
        else
        {
            // Check if the object will fit on the current line
            if(width != 0 && position.x + cursorX + paddingX + uiObject.getWidth() > width -
                    paddingX)
            {
                cursorY += endY;
                cursorX = 0.0f;
                endY = 0.0f;
            }

            // UI start position
            final float POS_X = position.x + cursorX + paddingX;
            final float POS_Y = position.y + cursorY + paddingY;

            // Set the end of the UI object as the new cursorX
            cursorX += paddingX + uiObject.getWidth();

            // If the object and width exceeds the current end X, set the new end X
            if(POS_X + uiObject.getWidth() > endX)
            {
                endX = POS_X + uiObject.getWidth();
            }

            // If the object and height exceeds the current end Y, set the new end Y
            if(uiObject.getHeight() + paddingY > endY)
            {
                endY = uiObject.getHeight() + paddingY;

                final float NEW_HEIGHT = POS_Y + endY - position.y;
                if(automaticHeight && NEW_HEIGHT > height)
                {
                    height = NEW_HEIGHT;
                    background.setHeight(height);
                }
            }

            uiObject.setPosition(POS_X, POS_Y);
        }
    }

    public void setOpacity(float alpha)
    {
        background.setOpacity(alpha);

        for(int i = 0; i < uiObjects.size(); i++)
        {
            uiObjects.get(i).setOpacity(alpha);
        }
    }

    @Override
    public float getOpacity() {
        return 0;
    }

    @Override
    public void draw(Camera2D camera)
    {
        glDisable(GL_DEPTH_TEST);
        if(showBackground)
        {
            background.draw(camera);
        }

        // Iterate through the ui objects drawing them
        for(int i = 0; i < uiObjects.size(); i++)
        {
            uiObjects.get(i).draw(camera);
        }
        glEnable(GL_DEPTH_TEST);
    }
}
