package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

public class TextureShader extends Shader
{
    public TextureShader()
    {
        super(R.raw.texture_vert, R.raw.texture_frag);

        positionAttributeHandle = getAttribute("vPosition");
        textureAttributeHandle = getAttribute("vTextureCoordinates");
        matrixUniformHandle = getUniform("uMatrix");
        colourUniformHandle = getUniform("uColour");
        textureUniformHandle = getUniform("uTexture");
    }
}
