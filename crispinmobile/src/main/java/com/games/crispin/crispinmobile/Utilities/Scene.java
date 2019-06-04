package com.games.crispin.crispinmobile.Utilities;

import android.content.Context;

public abstract class Scene {
    public interface Constructor {
        Scene init(Context context);
    }

    public abstract void update(float deltaTime);
    public abstract void render();
}
