package com.games.crispin.hordesurvival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.games.crispin.crispinmobile.Crispin;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the engine
        Crispin.init(this);

        // Set scene to test scene
        Crispin.setScene(TestScene.TEST_SCENE_CONSTRUCTION);
    }
}
