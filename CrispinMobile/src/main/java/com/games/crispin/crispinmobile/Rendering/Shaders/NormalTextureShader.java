package com.games.crispin.crispinmobile.Rendering.Shaders;

import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

/**
 * NormalTextureShader is a built in shader that allows you to handle render objects containing position
 * and normal attributes. It also supports a texture. This allows you to render objects with diffuse
 * lighting.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Shader
 * @since       1.0
 */
public class NormalTextureShader extends Shader
{
    /**
     * Create the NormalTextureShader. This compiles the pre-defined vertex and fragment
     * shader's, and links the attributes to the shader base class for a common form of user
     * interaction.
     *
     * @since   1.0
     */
    public NormalTextureShader()
    {
        super(R.raw.normal_texture_vert, R.raw.normal_texture_frag);

        lightingShader = true;

        positionAttributeHandle = getAttribute("aPosition");
        normalAttributeHandle = getAttribute("aNormal");
        colourUniformHandle = getUniform("uColour");
        projectionMatrixUniformHandle = getUniform("uProjection");
        viewMatrixUniformHandle = getUniform("uView");
        modelMatrixUniformHandle = getUniform("uModel");
        textureUniformHandle = getUniform("uTexture");
        textureAttributeHandle = getAttribute("aTextureCoordinates");
        uvMultiplierUniformHandle = getUniform("uUvMultiplier");
        specularMapUniformHandle = getUniform("uSpecularMap");
    }
}