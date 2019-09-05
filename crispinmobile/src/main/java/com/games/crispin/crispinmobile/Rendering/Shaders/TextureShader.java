package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * TextureShader is a built in shader designed to render objects with position and texture
 * attributes.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class TextureShader extends Shader
{
    /**
     * Create the TextureShader. This compiles the pre-defined vertex and fragment shader's, and
     * links the attributes to the shader base class for a common form of user interaction.
     *
     * @since   1.0
     */
    public TextureShader()
    {
        super(R.raw.texture_vert, R.raw.texture_frag);

        positionAttributeHandle = getAttribute("vPosition");
        textureAttributeHandle = getAttribute("vTextureCoordinates");
        matrixUniformHandle = getUniform("uMatrix");
        colourUniformHandle = getUniform("uColour");
        uvMultiplierUniformHandle = getUniform("uUvMultiplier");
        textureUniformHandle = getUniform("uTexture");
    }
}
