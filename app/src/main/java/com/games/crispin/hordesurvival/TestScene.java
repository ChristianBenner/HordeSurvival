package com.games.crispin.hordesurvival;

import android.content.Context;

import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharacter;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextShader;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextureAttributeColourShader;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = (context) -> new TestScene(context);

    private Square square;
    private Cube cubeTwo;
    private Cube cubeThree;

    private Camera3D camera;
    private Camera2D camera2D;

    private TimeColourShader customShader;
    private TextShader textShader;
    private TextureAttributeColourShader textureAttributeColourShader;
    private float angle = 0.0f;

    private FreeTypeCharacter character;

    public TestScene(Context context)
    {
        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.YELLOW);

        // Create the camera
        camera = new Camera3D();
        camera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        camera2D = new Camera2D();

        // Create the custom shader object
        customShader = new TimeColourShader();

        Font f = new Font(R.raw.sixty, 128);
        character = f.getCharacter('a');

        Material material = new Material(character.texture);

        Material brickMaterial = new Material(new Texture(R.drawable.brick));

        // Create a cube object
        square = new Square(material);
        square.setPosition(new Point3D(0.0f, 000.0f, 0.0f));

        cubeTwo = new Cube(brickMaterial);
        cubeTwo.setPosition(new Point3D(-2.0f, 2.0f, 0.0f));

        cubeThree = new Cube(brickMaterial);
        cubeThree.setPosition(new Point3D(2.0f, 2.0f, 0.0f));

        textShader = new TextShader();
        textureAttributeColourShader = new TextureAttributeColourShader();

        // Apply the custom shader to the cube
        square.useCustomShader(textShader);
        cubeTwo.useCustomShader(textureAttributeColourShader);
        cubeThree.useCustomShader(textureAttributeColourShader);

        square.setColour(Colour.RED);
        square.setScale(new Scale2D(character.width, character.height));

        cubeTwo.setColour(Colour.MAGENTA);
        cubeThree.setColour(Colour.BLUE);
    }

    @Override
    public void update(float deltaTime)
    {
        // Update the custom shader
        customShader.update(deltaTime);

        angle += 1f;
        square.setRotation(angle, 1.0f, 1.0f, 0.0f);
        cubeTwo.setRotation(angle, 0.0f, 1.0f, 1.0f);
        cubeThree.setRotation(angle, 1.0f, 0.0f, 1.0f);
    }

    @Override
    public void render()
    {
        // Draw the cube
        square.draw(camera2D);
        cubeThree.draw(camera);
        cubeTwo.draw(camera);
    }
}
