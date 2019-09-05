package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * TextureAttributeColourShader is a built in shader designed to render objects with position,
 * colour and texture attributes.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class TextureAttributeColourShader extends Shader
{
    /**
     * Create the TextureAttributeColourShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
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
