package com.games.crispin.crispinmobile.Native;

import com.games.crispin.crispinmobile.Geometry.Point3D;

public class FreeTypeMethods
{
    public static class FreeTypeCharacter
    {
        private byte[] charBytes;
        private int width;
        private int height;
        private int bearingX;
        private int bearingY;
        private int advance;

        public FreeTypeCharacter(byte[] font, int fontSize, byte asciiChar)
        {
            charBytes = FreeTypeMethods.loadCharacter(font, asciiChar, fontSize);
            width = FreeTypeMethods.getFaceWidth();
            height = FreeTypeMethods.getFaceHeight();
            bearingX = FreeTypeMethods.getFaceBearingX();
            bearingY = FreeTypeMethods.getFaceBearingY();
            advance = FreeTypeMethods.getFaceAdvance();
            FreeTypeMethods.freeFace();
        }

        public byte[] getBytes()
        {
            return charBytes;
        }

        public int getWidth()
        {
            return width;
        }

        public int getHeight()
        {
            return height;
        }

        public int getAdvance()
        {
            return advance;
        }

        public int getBearingX()
        {
            return bearingX;
        }

        public int getBearingY()
        {
            return bearingY;
        }
    }

    static
    {
        System.loadLibrary("crispinni");
    }

    // Need a native interface for this


    /**
     * Initialise free type library
     *
     * @return  Return the position of the camera
     * @see     Point3D
     * @since   1.0
     */
    public static native byte[] loadCharacter(byte[] fontBytes,
                                              byte thechar,
                                              int size);

    public static native boolean initFreeType();
    public static native byte[] loadGlyph(byte[] fontBytes,
                                          byte thechar,
                                          int size);

    public static native int getFaceWidth();
    public static native int getFaceHeight();
    public static native int getFaceBearingX();
    public static native int getFaceBearingY();
    public static native int getFaceAdvance();
    public static native void freeFace();
}
