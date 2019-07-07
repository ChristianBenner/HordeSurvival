package com.games.crispin.crispinmobile.Utilities;

public abstract class Scene {
    public interface Constructor {
        Scene init();
    }

    public abstract void update(float deltaTime);
    public abstract void render();
}
