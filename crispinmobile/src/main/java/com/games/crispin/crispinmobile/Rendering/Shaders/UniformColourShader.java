package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * UniformColourShader is a built in shader designed to render objects with a position attribute. It
 * also allows a uniform colour to be applied to that object - which appears as a solid colour
 * across the object.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class UniformColourShader extends Shader
{
    /**
     * Create the UniformColourShader. This compiles the pre-defined vertex and fragment shader's,
     * and links the attributes to the shader base class for a common form of user interaction.
     *
     * @since   1.0
     */
    public UniformColourShader()
    {
        super(R.raw.uniform_colour_vert, R.raw.uniform_colour_frag);

        positionAttributeHandle = getAttribute("vPosition");
        colourUniformHandle = getUniform("uColour");
        matrixUniformHandle = getUniform("uMatrix");
    }
}
