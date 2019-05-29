package com.games.crispin.crispinmobile;

public class ColourShader extends GLSLShader
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

    // Position attribute handle in the shader
    private int POSITION_ATTRIBUTE_HANDLE;

    // Colour attribute handle in the shader
    private int COLOUR_UNIFORM_HANDLE;

    // Matrix attribute handle in the shader
    private int MATRIX_UNIFORM_HANDLE;

    public ColourShader() throws Exception
    {
        super(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
        POSITION_ATTRIBUTE_HANDLE = getAttribute("vPosition");
        COLOUR_UNIFORM_HANDLE = getUniform("uColour");
        MATRIX_UNIFORM_HANDLE = getUniform("uMatrix");
    }

    public int getPosHandle()
    {
        return POSITION_ATTRIBUTE_HANDLE;
    }

    public int getColourHandle()
    {
        return COLOUR_UNIFORM_HANDLE;
    }

    public int getMatrixHandle()
    {
        return MATRIX_UNIFORM_HANDLE;
    }
}
