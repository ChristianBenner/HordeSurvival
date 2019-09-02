package com.games.crispin.hordesurvival;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Text;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Utilities.OBJModelLoader;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class MyScene extends Scene {
    static Scene.Constructor MY_SCENE = () -> new MyScene();

    RenderObject shotgunModel;
    Texture shotgunTexture;
    Camera3D camera3D;
    Camera2D camera2D;

    Text text;

    public MyScene()
    {
        Crispin.setBackgroundColour(Colour.GREEN);
//        shotgunModel = OBJModelLoader.readObjFile(R.raw.shotty);
//        shotgunTexture = new Texture(R.drawable.shotgun);
//
//        Material m = new Material(shotgunTexture);
//        shotgunModel.setMaterial(m);
//
//        camera3D = new Camera3D();
//        camera3D.setPosition(new Point3D(0.0f, 0.0f, 7.0f));
//
//        camera2D = new Camera2D();
//
//        text = new Text(new Font(R.raw.chunkfiveprint, 64), "SHOTTY!", true, true, Crispin.getSurfaceWidth());

    }

    float angle = 0.0f;

    @Override
    public void update(float deltaTime) {
        angle += 3f;
        shotgunModel.setRotation(0.0f, angle, 0.0f);
    }

    @Override
    public void render() {
        shotgunModel.draw(camera3D);
        text.renderText(camera2D);
    }
}
