package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * AttributeColourShader is a built in shader that allows you to handle render objects containing
 * position and colour attributes. This allows you to render objects with a colour that is defined
 * per vertex.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class AttributeColourShader extends Shader
{
    /**
     * Create the AttributeColourShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public AttributeColourShader()
    {
        super(R.raw.attribute_colour_vert, R.raw.attribute_colour_frag);

        positionAttributeHandle = getAttribute("vPosition");
        colourAttributeHandle = getAttribute("vColour");
        matrixUniformHandle = getUniform("uMatrix");
        colourUniformHandle = getUniform("uColour");
    }
}
