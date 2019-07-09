package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

public class AttributeColourShader extends Shader
{
    public AttributeColourShader()
    {
        super(R.raw.attribute_colour_vert, R.raw.attribute_colour_frag);

        positionAttributeHandle = getAttribute("vPosition");
        colourAttributeHandle = getAttribute("vColour");
        matrixUniformHandle = getUniform("uMatrix");
        colourUniformHandle = getUniform("uColour");
    }
}
