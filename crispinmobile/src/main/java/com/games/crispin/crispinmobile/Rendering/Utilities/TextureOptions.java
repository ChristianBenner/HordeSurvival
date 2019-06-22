package com.games.crispin.crispinmobile.Rendering.Utilities;

import static android.opengl.GLES30.GL_NEAREST;
import static android.opengl.GLES30.GL_REPEAT;
import static android.opengl.GLES30.GL_RGBA;
import static android.opengl.GLES30.GL_UNSIGNED_BYTE;

public class TextureOptions
{
    private static final int DEFAULT_MIN_FILTER = GL_NEAREST;
    private static final int DEFAULT_MAG_FILTER = GL_NEAREST;
    private static final int DEFAULT_INTERNAL_FORMAT = GL_RGBA;
    private static final int DEFAULT_FORMAT = GL_RGBA;
    private static final int DEFAULT_TEXTURE_WRAP_S = GL_REPEAT;
    private static final int DEFAULT_TEXTURE_WRAP_T = GL_REPEAT;
    private static final int DEFAULT_TYPE = GL_UNSIGNED_BYTE;
    private static final boolean DEFAULT_MONOCHROME = false;

    public int minFilter = DEFAULT_MIN_FILTER;
    public int magFilter = DEFAULT_MAG_FILTER;
    public int internalFormat = DEFAULT_INTERNAL_FORMAT;
    public int format = DEFAULT_FORMAT;
    public int textureWrapS = DEFAULT_TEXTURE_WRAP_S;
    public int textureWrapT = DEFAULT_TEXTURE_WRAP_T;
    public int type = DEFAULT_TYPE;
    public boolean monochrome = DEFAULT_MONOCHROME;
}
