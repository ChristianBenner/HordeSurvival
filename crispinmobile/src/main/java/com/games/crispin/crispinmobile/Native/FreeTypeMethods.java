package com.games.crispin.crispinmobile.Native;

public class FreeTypeMethods
{
    static
    {
        System.loadLibrary("crispinni");
    }

    // Need a native interface for this
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
