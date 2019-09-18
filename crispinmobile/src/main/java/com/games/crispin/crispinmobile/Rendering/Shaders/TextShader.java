package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Text;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * TextShader is a built in shader designed to render the built in text user interface object. The
 * shader supports position and texture attributes.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Text
 * @see         Shader
 * @since       1.0
 */
public class TextShader extends Shader
{
    /**
     * Create the TextShader. This compiles the pre-defined vertex and fragment shader's, and links
     * the attributes to the shader base class for a common form of user interaction.
     *
     * @since   1.0
     */
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
