package com.games.crispin.hordesurvival;

import android.content.Context;

import com.games.crispin.crispinmobile.Camera3D;
import com.games.crispin.crispinmobile.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Point3D;
import com.games.crispin.crispinmobile.RenderObject;
import com.games.crispin.crispinmobile.Scene;

import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = (context) -> new TestScene(context);

    private RenderObject cube;
    private Camera3D camera;
    private TimeColourShader customShader;

    public TestScene(Context context)
    {
        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.YELLOW);

        customShader = new TimeColourShader();

        // Create a cube object
        cube = new RenderObject();
        cube.useCustomShader(customShader);

        camera = new Camera3D();
    }

    @Override
    public void update(float deltaTime) {

    }

    float time = 0.0f;
    @Override
    public void render()
    {
        customShader.setTime(time);
        time += 0.1f;

        camera.setPosition(new Point3D(0.0f, 0.0f, 1.0f));
        cube.setScale(0.4f, 0.4f, 0.4f);
        cube.setPosition(new Point3D(0.0f, 0.0f, -2.0f));
        cube.setRotation(45.0f, 1.0f, 1.0f, 0.0f);
        cube.draw(camera);
    }
}
