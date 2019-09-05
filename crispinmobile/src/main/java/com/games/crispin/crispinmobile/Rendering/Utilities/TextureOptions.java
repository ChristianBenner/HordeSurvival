package com.games.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES30.GL_NEAREST;
import static android.opengl.GLES30.GL_REPEAT;
import static android.opengl.GLES30.GL_RGBA;
import static android.opengl.GLES30.GL_UNSIGNED_BYTE;

/**
 * TextureOptions is a data only class that allows you to configure texture parameters. Texture
 * parameters can change the way that a texture is rendered.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class TextureOptions
{
    // Default minify filter
    private static final int DEFAULT_MIN_FILTER = GL_NEAREST;

    // Default magnifying filter
    private static final int DEFAULT_MAG_FILTER = GL_NEAREST;

    // Default internal colour format
    private static final int DEFAULT_INTERNAL_FORMAT = GL_RGBA;

    // Default colour format
    private static final int DEFAULT_FORMAT = GL_RGBA;

    // Default texture wrap s-coordinate parameter
    private static final int DEFAULT_TEXTURE_WRAP_S = GL_REPEAT;

    // Default texture wrap t-coordinate parameter
    private static final int DEFAULT_TEXTURE_WRAP_T = GL_REPEAT;

    // Default colour data type
    private static final int DEFAULT_TYPE = GL_UNSIGNED_BYTE;

    // Default monochrome state
    private static final boolean DEFAULT_MONOCHROME = false;

    // The minify filter
    public int minFilter = DEFAULT_MIN_FILTER;

    // The magnify filter
    public int magFilter = DEFAULT_MAG_FILTER;

    // Internal colour format
    public int internalFormat = DEFAULT_INTERNAL_FORMAT;

    // Colour format
    public int format = DEFAULT_FORMAT;

    // Texture wrap s-coordinate parameter
    public int textureWrapS = DEFAULT_TEXTURE_WRAP_S;

    // Texture wrap t-coordinate parameter
    public int textureWrapT = DEFAULT_TEXTURE_WRAP_T;

    // The type that the colour data is represented in
    public int type = DEFAULT_TYPE;

    // Whether the texture data is in monochrome format or not
    public boolean monochrome = DEFAULT_MONOCHROME;
}
