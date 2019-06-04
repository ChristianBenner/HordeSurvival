package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

public class ColourShader extends Shader
{
    public ColourShader()
    {
        super(R.raw.colour_vert, R.raw.colour_frag);

        positionAttributeHandle = getAttribute("vPosition");
        colourUniformHandle = getUniform("uColour");
        matrixUniformHandle = getUniform("uMatrix");
    }
}
