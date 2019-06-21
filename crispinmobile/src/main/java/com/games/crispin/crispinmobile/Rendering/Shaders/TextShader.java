package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

public class TextShader extends Shader
{
    public TextShader()
    {
        super(R.raw.text_vert, R.raw.text_frag);

        positionAttributeHandle = getAttribute("vPosition");
        textureAttributeHandle = getAttribute("vTextureCoordinates");
        matrixUniformHandle = getUniform("uMatrix");
        colourUniformHandle = getUniform("uColour");
        textureUniformHandle = getUniform("uTexture");
    }
}
