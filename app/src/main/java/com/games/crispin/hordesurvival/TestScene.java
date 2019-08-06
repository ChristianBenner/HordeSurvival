package com.games.crispin.hordesurvival;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Models.CubeGrouped;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Text;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = () -> new TestScene();

    private CubeGrouped cubeTwo;
    private Cube cubeThree;

    private Camera3D camera;
    private Camera2D camera2D;
    private float angle = 0.0f;

    private Text text;
    private Material brickMaterial;

    private RenderObject renderObject;

    private Text fpsText;
    private Material mRed;

    public TestScene()
    {
        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.LIGHT_GREY);

        // Create the camera
        camera = new Camera3D();
        camera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        camera2D = new Camera2D();




      //  Material brickMaterial = new Material(new Texture(R.drawable.brick), Colour.YELLOW);

        brickMaterial = new Material(new Texture(R.drawable.brick), new Scale2D(0.25f, 0.25f));
       // brickMaterial.ignoreData(Material.IGNORE_TEXEL_DATA_FLAG);


        renderObject = OBJModelLoader.readObjFile(R.raw.dragon);
        renderObject.setPosition(0.0f, -2.5f, 0.0f);
        renderObject.setScale(0.3f, 0.3f, 0.3f);

        mRed = new Material();
        mRed.setColour(Colour.RED);

        renderObject.setMaterial(mRed);
  //3      renderObject.setMaterial(brickMaterial);

        cubeTwo = new CubeGrouped(brickMaterial);
        cubeTwo.setPosition(new Point3D(-0.1f, 3.2f, -2.0f));

        cubeThree = new Cube(brickMaterial);
        cubeThree.setPosition(new Point3D(2.0f, 3.0f, 0.0f));

        Font font = new Font(R.raw.alexbrush, 64);

        text = new Text(font,
                "This is some sample text",
                true,
                true,
                Crispin.getSurfaceWidth());

        text.enableWiggle(-text.getHeight() / 2.0f, Text.WiggleSpeed_E.VERY_FAST);

        // Position the text to the top right corner
        text.setPosition((Crispin.getSurfaceWidth() / 2.0f),
                Crispin.getSurfaceHeight() - text.getHeight());

        Font fontTwo = new Font(R.raw.chunkfiveprint, 64);
        fpsText = new Text(fontTwo,
                "FPS: X",
                true,
                false,
                Crispin.getSurfaceWidth());
        fpsText.setPosition(5.0f, 5.0f);
    }

    @Override
    public void update(float deltaTime)
    {
        angle += 1.0f;
        cubeTwo.setRotation(0.0f, angle, angle);
        cubeThree.setRotation( angle, 0.0f, angle);
        renderObject.setRotation(15.0f, angle, 0.0f);
      //  text.setText("Rotations: " + (int)(angle / 360));
     //   text.setPosition(5.0f, Crispin.getSurfaceHeight() - text.getHeight() - 5f);
    }

    float time = 0.0f;
    float colourR = 0.0f;

    long startF = System.currentTimeMillis();
    int frames = 0;
    @Override
    public void render()
    {
        time += 0.1f;
        colourR = ((float)Math.sin(time) + 1f) / 2f;
        brickMaterial.setColour(new Colour(colourR, 1.0f, 1.0f));

        // Draw the cube
        cubeThree.draw(camera);
        cubeTwo.draw(camera);

        renderObject.draw(camera);

        text.renderText(camera2D);
        fpsText.renderText(camera2D);

        frames++;

        if(System.currentTimeMillis() - startF >= 1000)
        {
            fpsText.setText("FPS: " + frames);
            startF = System.currentTimeMillis();
            frames = 0;
        }
    }
}
