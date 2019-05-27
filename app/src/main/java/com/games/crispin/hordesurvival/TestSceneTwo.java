package com.games.crispin.hordesurvival;

import android.content.Context;

import com.games.crispin.crispinmobile.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Scene;

public class TestSceneTwo extends Scene {
    static Scene.Constructor TEST_SCENE_TWO_CONSTRUCTION = (context) -> new TestSceneTwo(context);

    public TestSceneTwo(Context context)
    {
        System.out.println("CONSTRUCTED TEST SCENE 2");
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render() {
        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.CYAN);
    }
}
