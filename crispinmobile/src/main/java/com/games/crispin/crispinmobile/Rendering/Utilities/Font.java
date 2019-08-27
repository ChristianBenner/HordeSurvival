package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Native.FreeTypeMethods;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharacter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES30.GL_LUMINANCE;

public class Font
{
    private Map<Character, FreeTypeCharacter> characters;
    private int size;

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
          //  FreeTypeMethods.initFreeType();

            TextureOptions textureOptions = new TextureOptions();
            textureOptions.internalFormat = GL_LUMINANCE;
            textureOptions.monochrome = true;
            textureOptions.format = GL_LUMINANCE;

            for(char i = 0; i < 128; i++)
            {
                System.out.println("Loading glyph: " + i);
                // load character, apply texture
                FreeTypeMethods.FreeTypeCharacter temp = new FreeTypeMethods.FreeTypeCharacter(sixtyTest, size, (byte)i);
                FreeTypeCharacter character = new FreeTypeCharacter();
                character.texture = new Texture(temp.getBytes(), temp.getWidth(), temp.getHeight(), textureOptions);
                character.width = temp.getWidth();
                character.height = temp.getHeight();
                character.bearingX = temp.getBearingX();
                character.bearingY = temp.getBearingY();
                character.advance = temp.getAdvance();
                character.ascii = (byte)i;
                characters.put(i, character);
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

    public FreeTypeCharacter getCharacter(char character)
    {
        return characters.get(character);
    }
}
