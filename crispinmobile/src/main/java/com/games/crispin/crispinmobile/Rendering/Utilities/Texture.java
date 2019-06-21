package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.crispinmobile.Utilities.TextureCache;

import java.nio.ByteBuffer;

import static android.opengl.GLES20.GL_INVALID_VALUE;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_REPEAT;
import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_UNPACK_ALIGNMENT;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glPixelStorei;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameteri;

public class Texture
{
    // TODO: MAKE CRISPIN ENGINE RELOAD ALL OF THE TEXTURES IN THE TEXTURE CACHE
    private static final String TAG = "Texture";
    private static final TextureOptions DEFAULT_OPTIONS = new TextureOptions();
    private static final int NO_RESOURCE_ID = -1;

    private int textureId;
    private int width;
    private int height;
    private int resourceId;
    private TextureOptions options;
    private ByteBuffer buffer;

    public Texture(Bitmap bitmap)
    {
        this(bitmap, DEFAULT_OPTIONS);
    }

    public Texture(Bitmap bitmap, TextureOptions options)
    {
        textureId = GL_INVALID_VALUE;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        resourceId = NO_RESOURCE_ID;
        this.options = options;
        buffer = bitmapToBuffer(bitmap);

        loadTexture(buffer);

        bitmap.recycle();
    }

    public Texture(int resourceId)
    {
        this(resourceId, DEFAULT_OPTIONS);
    }

    public Texture(int resourceId, TextureOptions options)
    {
        textureId = GL_INVALID_VALUE;
        this.resourceId = resourceId;

        final Bitmap BITMAP = resourceToBitmap(resourceId);

        width = BITMAP.getWidth();
        height = BITMAP.getHeight();
        this.options = options;

        // In the scenario of loading from a resource id (file), we don't have to store a copy of
        // the image data in memory when reloading the program. This is because we know where to
        // find the image data in storage. This is not the case when data is passed as a byte array
        // or a bitmap (in that scenario we are forced to hold onto the image data as it may not
        // be contained within storage).
        buffer = null;

        loadTexture(bitmapToBuffer(BITMAP));

        BITMAP.recycle();
    }

    public Texture(byte[] bytes, int width, int height)
    {
        this(bytes, width, height, DEFAULT_OPTIONS);
    }

    public Texture(byte[] bytes, int width, int height, TextureOptions textureOptions)
    {
        textureId = GL_INVALID_VALUE;
        this.width = width;
        this.height = height;
        resourceId = NO_RESOURCE_ID;
        this.options = textureOptions;
        buffer = arrayToBuffer(bytes);

        loadTexture(buffer);
    }

    public int getId()
    {
        return textureId;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void reconstruct()
    {
        // Reload the texture to graphics memory
        if(resourceId == NO_RESOURCE_ID)
        {
            // The byte buffer contains the memory of the texture
            loadTexture(buffer);
        }
        else
        {
            // Using the resource ID, reload the texture
            loadTexture(bitmapToBuffer(resourceToBitmap(resourceId)));
        }
    }

    public void destroy()
    {
        int[] TEMP_BUFFER = new int[1];
        TEMP_BUFFER[0] = textureId;

        // Remove the texture from graphics memory
        glDeleteTextures(1, TEMP_BUFFER, 0);

        textureId = GL_INVALID_VALUE;
    }

    private ByteBuffer bitmapToBuffer(Bitmap bitmap)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer;
    }

    private ByteBuffer arrayToBuffer(byte[] byteArray)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteArray.length);
        byteBuffer.put(byteArray);
        byteBuffer.rewind();
        return byteBuffer;
    }

    private Bitmap resourceToBitmap(int resourceId)
    {
        final BitmapFactory.Options IMAGE_OPTIONS = new BitmapFactory.Options();
        IMAGE_OPTIONS.inScaled = false;

        final Bitmap BITMAP = BitmapFactory.decodeResource(
                Crispin.getApplicationContext().getResources(),
                resourceId,
                IMAGE_OPTIONS);

        return BITMAP;
    }

    private void loadTexture(ByteBuffer buffer)
    {
        final int[] TEXTURE_OBJECT_ID = new int[1];

        glGenTextures(1, TEXTURE_OBJECT_ID, 0);

        if(TEXTURE_OBJECT_ID[0] == GL_INVALID_VALUE)
        {
            Logger.error(TAG, "Failed to generate texture handle [GL_INVALID_VALUE]");
            return;
        }

        textureId = TEXTURE_OBJECT_ID[0];

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, options.minFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, options.magFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, options.textureWrapS);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, options.textureWrapT);

        // If a texture is monochrome set the unpack alignment to one (because we only have one
        // colour channel)
        if(options.monochrome)
        {
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        }

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                options.internalFormat,
                getWidth(),
                getHeight(),
                0,
                options.format,
                options.type,
                buffer);

        // Reset back to default value (unpack alignment has initial value of 4)
        if(options.monochrome)
        {
            glPixelStorei(GL_UNPACK_ALIGNMENT, 4);
        }

        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        // Register the texture with the shader cache
        TextureCache.registerTexture(this);
    }
}
