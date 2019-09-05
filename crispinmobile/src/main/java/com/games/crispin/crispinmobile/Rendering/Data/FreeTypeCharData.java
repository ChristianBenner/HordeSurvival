package com.games.crispin.crispinmobile.Rendering.Data;

import com.games.crispin.crispinmobile.Native.FreeTypeMethods;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Rendering.Utilities.TextureOptions;

/**
 * FreeTypeCharData class holds important data on FreeType character faces. This is so that a
 * RenderObject can be created, assigned a texture and positioned in the correct place in respect to
 * other characters.
 *
 * @see         com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class FreeTypeCharData
{
    // The width of the character face
    private int width;

    // The height of the character face
    private int height;

    // The x-bearing of the character face
    private int bearingX;

    // The y-bearing of the character face
    private int bearingY;

    // The advance of the character face
    private int advance;

    // The ASCII character the data represents
    private byte ascii;

    // The texture of the character
    public Texture texture;

    /**
     * Create a FreeTypeCharData object. The constructor will interface with the native FreeType
     * library through the CrispinNativeInterface (crispinni) library to load FreeType characters
     * and obtain the necessary data.
     *
     * @param fontBytes         The font as a byte array
     * @param fontSize          The size of the font (bigger font size loads a larger texture)
     * @param asciiChar         The character to load (ASCII character)
     * @param textureOptions    Options to apply to the texture data
     * @since 1.0
     */
    public FreeTypeCharData(byte[] fontBytes,
                            int fontSize,
                            byte asciiChar,
                            TextureOptions textureOptions)
    {
        byte[] charBytes = FreeTypeMethods.loadCharacter(fontBytes, asciiChar, fontSize);
        width = FreeTypeMethods.getFaceWidth();
        height = FreeTypeMethods.getFaceHeight();
        bearingX = FreeTypeMethods.getFaceBearingX();
        bearingY = FreeTypeMethods.getFaceBearingY();
        advance = FreeTypeMethods.getFaceAdvance();
        this.ascii = asciiChar;
        FreeTypeMethods.freeFace();

        // Create the texture
        texture = new Texture(charBytes, width, height, textureOptions);
    }

    /**
     * Get the width of the character
     *
     * @return  The width of the character width
     * @since   1.0
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Get the height of the character
     *
     * @return  The height of the character width
     * @since   1.0
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Get the advance of the character
     *
     * @return  The advance of the character width
     * @since   1.0
     */
    public int getAdvance()
    {
        return advance;
    }

    /**
     * Get the x-bearing of the character
     *
     * @return  The x-bearing of the character width
     * @since   1.0
     */
    public int getBearingX()
    {
        return bearingX;
    }

    /**
     * Get the y-bearing of the character
     *
     * @return  The y-bearing of the character width
     * @since   1.0
     */
    public int getBearingY()
    {
        return bearingY;
    }

    /**
     * Get the ASCII character
     *
     * @return  The ASCII character
     * @since   1.0
     */
    public byte getAscii()
    {
        return ascii;
    }

    /**
     * Get the texture of the character
     *
     * @return  The texture of the character
     * @since   1.0
     */
    public Texture getTexture()
    {
        return texture;
    }
}