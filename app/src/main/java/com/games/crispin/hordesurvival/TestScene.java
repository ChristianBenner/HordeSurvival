package com.games.crispin.hordesurvival;

import android.content.Context;

import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Utilities.Scene;

import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = (context) -> new TestScene(context);

    private Cube cube;
    private Camera3D camera;
    private TimeColourShader customShader;

    public TestScene(Context context)
    {
        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.YELLOW);

        // Create the camera
        camera = new Camera3D();
        camera.setPosition(new Point3D(0.0f, 0.0f, 4.0f));

        // Create the custom shader object
        customShader = new TimeColourShader();

        // Create a cube object
        cube = new Cube();

        // Apply the custom shader to the cube
        cube.useCustomShader(customShader);
    }

    float angle = 0.0f;
    @Override
    public void update(float deltaTime)
    {
        // Update the custom shader
        customShader.update(deltaTime);

        angle += 1f;
        cube.setRotation(angle, 1.0f, 1.0f, 0.0f);
    }

    @Override
    public void render()
    {
        // Draw the cube
        cube.draw(camera);
    }
}
