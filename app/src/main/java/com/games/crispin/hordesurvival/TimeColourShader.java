package com.games.crispin.hordesurvival;

import com.games.crispin.crispinmobile.GLSLShader;

import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;

public class TimeColourShader extends GLSLShader
{
    // The vertex shader program
    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;" +
                    "uniform mat4 uMatrix;" +
                    "void main() {" +
                    "gl_Position = uMatrix * vPosition;" +
                    "}";

    // The fragment shader code
    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 uColour;" +
                    "uniform float uTime;" +
                    "void main() {" +
                    "vec4 colour = uColour;" +
                    "colour.r = colour.r * sin(uTime);" +
                    "colour.g = colour.g * cos(uTime);" +
                    "gl_FragColor = colour;" +
                    "}";

    private final int TIME_UNIFORM_HANDLE;

    private float time;

    public TimeColourShader()
    {
        super(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
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
