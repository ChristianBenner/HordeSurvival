package com.games.crispin.crispinmobile.Utilities;

import android.content.res.Resources;

import com.games.crispin.crispinmobile.Crispin;

import java.io.InputStream;

public class FileResourceReader
{
    private static final String TAG = "FileResourceReader";

    // Read a file from resource ID to a string
    public static byte[] readRawResource(int resourceId)
    {
        try
        {
            Resources resources = Crispin.getApplicationContext().getResources();
            InputStream inputStream = resources.openRawResource(resourceId);

            final int SIZE_BYTES = inputStream.available();
            byte[] bytes = new byte[SIZE_BYTES];
            inputStream.read(bytes);

            System.out.println("Read resource file: ID[" +
                    resourceId +
                    "], Bytes[" +
                    SIZE_BYTES +
                    "]");

            return bytes;
        }
        catch (Exception e)
        {
            Logger.error(TAG, "Failed to read resource. ID: " + resourceId);
        }

        return null;
    }
}
