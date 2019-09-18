package com.games.crispin.crispinmobile.Utilities;

import android.content.res.Resources;
import com.games.crispin.crispinmobile.Crispin;
import java.io.InputStream;

/**
 * FileResourceReader is a class comprised of only static function designed to read resource files.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class FileResourceReader
{
    // Tag used for logging
    private static final String TAG = "FileResourceReader";

    /**
     * Read a file from a resource ID.
     *
     * @param resourceId    The resource ID
     * @return  The file as an array of bytes
     * @since   1.0
     */
    public static byte[] readRawResource(int resourceId)
    {
        // Attempt to open a resource file as an input stream
        try
        {
            Resources resources = Crispin.getApplicationContext().getResources();
            InputStream inputStream = resources.openRawResource(resourceId);

            final int SIZE_BYTES = inputStream.available();
            byte[] bytes = new byte[SIZE_BYTES];
            inputStream.read(bytes);

            Logger.debug(TAG, "Read resource file: ID[" + resourceId + "], Bytes[" +
                    SIZE_BYTES + "]");

            return bytes;
        }
        catch (Exception e)
        {
            Logger.error(TAG, "Failed to read resource. ID: " + resourceId);
        }

        return null;
    }
}
