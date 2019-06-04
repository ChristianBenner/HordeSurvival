package com.games.crispin.hordesurvival;

import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;
import com.games.crispin.crispinmobile.Utilities.FileResourceReader;

import static android.opengl.GLES20.glUniform1f;

public class TimeColourShader extends Shader
{
    private final int TIME_UNIFORM_HANDLE;

    private float time;

    public TimeColourShader()
    {
        super(R.raw.time_colour_shader_vert, R.raw.time_colour_shader_frag);

        positionAttributeHandle = getAttribute("vPosition");
        colourUniformHandle = getUniform("uColour");
        matrixUniformHandle = getUniform("uMatrix");
        TIME_UNIFORM_HANDLE = getUniform("uTime");

        time = 0.0f;
    }

    private void setTime(float time)
    {
        enableIt();
        glUniform1f(TIME_UNIFORM_HANDLE, time);
        disableIt();
    }

    public void update(float deltaTime)
    {
        time += 0.1f * deltaTime;
        setTime(time);
    }
}
