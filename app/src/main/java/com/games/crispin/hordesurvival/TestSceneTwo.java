package com.games.crispin.hordesurvival;

import android.content.Context;

import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class TestSceneTwo extends Scene {
    static Scene.Constructor TEST_SCENE_TWO_CONSTRUCTION = () -> new TestSceneTwo();

    public TestSceneTwo()
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
