package com.games.crispin.hordesurvival;

import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.Utilities.Text;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = () -> new TestScene();

    private Cube cubeTwo;
    private Cube cubeThree;

    private Camera3D camera;
    private Camera2D camera2D;
    private float angle = 0.0f;

    private Text text;

    public TestScene()
    {
        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.YELLOW);

        // Create the camera
        camera = new Camera3D();
        camera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        camera2D = new Camera2D();

        Font font = new Font(R.raw.opensans, 32);

        Material brickMaterial = new Material(new Texture(R.drawable.brick));

        cubeTwo = new Cube(brickMaterial);
        cubeTwo.setPosition(new Point3D(-2.0f, 2.0f, 0.0f));
        cubeTwo.ignoreData(RenderObject.IGNORE_TEXEL_DATA_FLAG |
                RenderObject.IGNORE_COLOUR_DATA_FLAG);

        cubeThree = new Cube(brickMaterial);
        cubeThree.setPosition(new Point3D(2.0f, 2.0f, 0.0f));

        cubeTwo.setColour(Colour.MAGENTA);
        cubeThree.setColour(Colour.BLUE);

        text = new Text(font,
                "A paragraph is a series of sentences that are organized and " +
                "coherent, and are all related to a single topic. Almost every piece of writing" +
                "you do that is longer than a few sentences should be organized into paragraphs." +
                "A paragraph is a series of sentences that are organized and coherent, and are" +
                "all related to a single topic. Almost every piece of writing you do that is" +
                "longer than a few sentences should be organized into paragraphs. A paragraph is" +
                "a series of sentences that are organized and coherent, and are all related to a" +
                "single topic. Almost every piece of writing you do that is longer than a few" +
                "sentences should be organized into paragraphs.",
                true,
                false,
                Crispin.getSurfaceWidth());
    }

    @Override
    public void update(float deltaTime)
    {
        angle += 1.0f;
        cubeTwo.setRotation(angle, 0.0f, 1.0f, 1.0f);
        cubeThree.setRotation(angle, 1.0f, 0.0f, 1.0f);
    }

    @Override
    public void render()
    {
        // Draw the cube
        cubeThree.draw(camera);
        cubeTwo.draw(camera);

        text.renderText(camera2D);
    }
}
