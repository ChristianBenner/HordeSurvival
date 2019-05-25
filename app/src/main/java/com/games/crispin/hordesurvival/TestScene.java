package com.games.crispin.hordesurvival;

import android.content.Context;

import com.games.crispin.crispinmobile.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Scene;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = (context) -> new TestScene(context);

    public TestScene(Context context)
    {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render() {
        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.YELLOW);
    }
}
