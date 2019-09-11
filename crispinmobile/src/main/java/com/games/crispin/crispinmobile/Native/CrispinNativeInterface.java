package com.games.crispin.crispinmobile.Native;

/**
 * The CrispinNativeInterface class provides static functions that are used to interact with the
 * Crispin Native Interface Library programmed in C. The Crispin Native Interface Library is used
 * to interface other C libraries in a JNI friendly way. Currently the library provides an interface
 * to the following C libraries:
 * -    FreeType: Used to generate font data from font files
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class CrispinNativeInterface
{
    static
    {
        // Load the Crispin Native Interface library
        System.loadLibrary("crispinni");
    }

    /**
     * <code>Native Function</code>
     * Load a FreeType character. Initialises FreeType library if it hasn't been already, and then
     * loads a specified character in the given font.
     *
     * @param fontBytes The font as an array of bytes
     * @param asciiChar The ASCII character to load
     * @param size      The size to load the character as (larger size entails larger texture size)
     * @return  Array of bytes containing texture data. The texture is monochrome meaning there is
     *          one byte per pixel as fonts do not contain colour data.
     * @since   1.0
     */
    public static native byte[] loadCharacter(byte[] fontBytes,
                                              byte asciiChar,
                                              int size);

    /**
     * <code>Native Function</code>
     * Discard the currently loaded face object
     *
     * @since   1.0
     */
    public static native void freeFace();

    /**
     * <code>Native Function</code>
     * Get the width of the loaded character face
     *
     * @return  The width of the loaded character face
     * @since   1.0
     */
    public static native int getFaceWidth();

    /**
     * <code>Native Function</code>
     * Get the height of the loaded characters face
     *
     * @return  The height of the loaded character face
     * @since   1.0
     */
    public static native int getFaceHeight();

    /**
     * <code>Native Function</code>
     * Get the x-bearing of the loaded characters face
     *
     * @return  The x-bearing of the loaded character face
     * @since   1.0
     */
    public static native int getFaceBearingX();

    /**
     * <code>Native Function</code>
     * Get the y-bearing of the loaded characters face
     *
     * @return  The y-bearing of the loaded character face
     * @since   1.0
     */
    public static native int getFaceBearingY();

    /**
     * <code>Native Function</code>
     * Get the advance of the loaded characters face
     *
     * @return  The advance of the loaded character face
     * @since   1.0
     */
    public static native int getFaceAdvance();
}
