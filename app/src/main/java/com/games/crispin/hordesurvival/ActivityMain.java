package com.games.crispin.hordesurvival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Demos.TextDemoScene;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the engine with the first scene
        Crispin.init(this, TestScene.TEST_SCENE_CONSTRUCTION);
       // Crispin.init(this, TextDemoScene::new);
    }
}
