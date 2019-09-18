package com.games.crispin.crispinmobile.Utilities;

/**
 * Logger is a class comprised of static functions. It provides functions that can be used to log
 * important information to the console at different levels (e.g. debug, info or error). It should
 * be used instead of the built in Java logging so that if necessary the engine can produce an error
 * log file.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Logger
{
    // Enable debug logging (if false debug logs are hidden from console)
    private static boolean debugLoggingEnabled = false;

    /**
     * Enable or disable debug logging
     *
     * @param state The new state of debug logging. True to enable debug logging, else false.
     * @since 1.0
     */
    public static void setDebugLoggingEnabled(boolean state)
    {
        debugLoggingEnabled = state;
    }

    /**
     * Get the debug logging state
     *
     * @return  True if debug logging is enabled, else false
     * @since   1.0
     */
    public static boolean isDebugLoggingEnabled()
    {
        return debugLoggingEnabled;
    }

    /**
     * Log debug text. Logs in the format 'DEBUG[tag]: message'
     *
     * @param tag       The class tag/name as a string that the log originates from
     * @param string    The text string to log
     * @since 1.0
     */
    public static void debug(String tag, String string)
    {
        // Only print debug info if debug logging is enabled
        if(debugLoggingEnabled)
        {
            System.out.println("DEBUG[" + tag + "]: " + string);
        }
    }

    /**
     * Log information text
     *
     * @param string    The text string to log
     * @since 1.0
     */
    public static void info(String string)
    {
        System.out.println(string);
    }

    /**
     * Log error text. Logs in the format 'ERROR[tag]: message'
     *
     * @param tag       The class tag/name as a string that the log originates from
     * @param string    The text string to log
     * @since 1.0
     */
    public static void error(String tag, String string)
    {
        System.err.println("ERROR[" + tag + "]: " + string);
    }
}
