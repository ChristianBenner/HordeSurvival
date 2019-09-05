package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES30.GL_LUMINANCE;

public class Font
{
    private Map<Character, FreeTypeCharData> characters;
    private int size;

    // The index of characters to start loading from
    private static char ASCII_START_INDEX = 0;

    // The index of characters to load up to
    private static char ASCII_END_INDEX = 128;

    public static byte[] convertStreamToByteArray(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[10240];
        int i = Integer.MAX_VALUE;
        while ((i = is.read(buff, 0, buff.length)) > 0) {
            baos.write(buff, 0, i);
        }

        return baos.toByteArray(); // be sure to close InputStream in calling function
    }

    public Font(int resourceId, int size)
    {
        characters = new HashMap<>();
        this.size = size;

        InputStream inStream = Crispin.getApplicationContext().getResources().openRawResource(resourceId);
        try
        {
            byte[] sixtyTest = new byte[inStream.available()];
            sixtyTest = convertStreamToByteArray(inStream);

            // Texture options of the character textures
            TextureOptions textureOptions = new TextureOptions();
            textureOptions.internalFormat = GL_LUMINANCE;
            textureOptions.monochrome = true;
            textureOptions.format = GL_LUMINANCE;

            for(char i = ASCII_START_INDEX; i < ASCII_END_INDEX; i++)
            {
                System.out.println("Loading glyph: " + i);

                // load character and store in the character array
                characters.put(i, new FreeTypeCharData(sixtyTest, size, (byte)i, textureOptions));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getSize()
    {
        return size;
    }

    public FreeTypeCharData getCharacter(char character)
    {
        return characters.get(character);
    }
}
