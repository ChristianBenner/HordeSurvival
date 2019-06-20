package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

public class UniformColourShader extends Shader
{
    public UniformColourShader()
    {
        super(R.raw.uniform_colour_vert, R.raw.uniform_colour_frag);

        positionAttributeHandle = getAttribute("vPosition");
        colourUniformHandle = getUniform("uColour");
        matrixUniformHandle = getUniform("uMatrix");
    }
}
