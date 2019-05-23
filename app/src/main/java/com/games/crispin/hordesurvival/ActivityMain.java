package com.games.crispin.hordesurvival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Scene;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Uninitialised scene
        TestScene testScene = null;

        // Initialise the engine
        Crispin.init(this);

        // Add uninitialised scene to engine
        Crispin.addScene(testScene, testScene.TEST_SCENE_CONSTRUCTION);
    }
}
