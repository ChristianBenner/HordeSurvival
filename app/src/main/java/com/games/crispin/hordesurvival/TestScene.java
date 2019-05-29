package com.games.crispin.hordesurvival;

import android.content.Context;
import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Camera;
import com.games.crispin.crispinmobile.Colour;
import com.games.crispin.crispinmobile.ColourShader;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.GLSLShader;
import com.games.crispin.crispinmobile.Geometry;
import com.games.crispin.crispinmobile.RenderObject;
import com.games.crispin.crispinmobile.Scene;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRUE;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = (context) -> new TestScene(context);

    private RenderObject cube;
    private Camera camera;
    private TimeColourShader customShader;

    public TestScene(Context context)
    {
        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.YELLOW);

        customShader = new TimeColourShader();

        // Create a cube object
        cube = new RenderObject();
        cube.useCustomShader(customShader);

        camera = new Camera();
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

        camera.setPosition(new Geometry.Point(0.0f, 0.0f, 1.0f));
        cube.setScale(0.4f, 0.4f, 0.4f);
        cube.setPosition(new Geometry.Point(0.0f, 0.0f, -2.0f));
        cube.setRotation(45.0f, 1.0f, 1.0f, 0.0f);
        cube.draw(camera);
    }
}
