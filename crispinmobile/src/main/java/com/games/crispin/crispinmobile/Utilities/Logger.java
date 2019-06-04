package com.games.crispin.crispinmobile.Utilities;

public class Logger
{
    // Enable debug logging
    private static boolean debugLoggingEnabled = false;

    public static void setDebugLoggingEnabled(boolean state)
    {
        debugLoggingEnabled = state;
    }

    public static void debug(String tag, String string)
    {
        if(debugLoggingEnabled)
        {
            System.out.println("DEBUG[" + tag + "]: " + string);
        }
    }

    public static void info(String string)
    {
        System.out.println(string);
    }

    public static void error(String tag, String string)
    {
        System.err.println("ERROR[" + tag + "]: " + string);
    }
}
