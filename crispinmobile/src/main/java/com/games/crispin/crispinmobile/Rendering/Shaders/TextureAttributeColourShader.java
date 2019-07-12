package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

public class TextureAttributeColourShader extends Shader
{
    public TextureAttributeColourShader()
    {
        super(R.raw.texture_attribute_colour_vert, R.raw.texture_attribute_colour_frag);

        positionAttributeHandle = getAttribute("vPosition");
        colourAttributeHandle = getAttribute("vColour");
        colourUniformHandle = getUniform("uColour");
        textureAttributeHandle = getAttribute("vTextureCoordinates");
        matrixUniformHandle = getUniform("uMatrix");
        uvMultiplierUniformHandle = getUniform("uUvMultiplier");
        textureUniformHandle = getUniform("uTexture");
    }
}
