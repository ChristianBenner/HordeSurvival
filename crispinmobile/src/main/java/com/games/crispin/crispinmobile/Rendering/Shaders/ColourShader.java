package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

public class ColourShader extends Shader
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
                    "void main() {" +
                    "vec4 colour = uColour;" +
                    "gl_FragColor = colour;" +
                    "}";

    public ColourShader()
    {
        super(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
        positionAttributeHandle = getAttribute("vPosition");
        colourUniformHandle = getUniform("uColour");
        matrixUniformHandle = getUniform("uMatrix");
    }
}
