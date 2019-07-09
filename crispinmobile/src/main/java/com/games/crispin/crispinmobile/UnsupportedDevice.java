package com.games.crispin.crispinmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.games.crispin.crispinmobile.Utilities.Logger;

/**
 * The Unsupported Device class is an Android activity with a sole purpose of showing the
 * unsupported device view. This view is an XML stored in resources.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         AppCompatActivity
 * @since       1.0
 */
public class UnsupportedDevice extends AppCompatActivity
{
    // Tag used in logging output
    private static final String TAG = "UnsupportedDevice";

    /**
     * An overridden function from the Android AppCompatActivity base class. The
     * <code>onCreate</code> method is called when the activity is to be created and viewed. The
     * method simply sets the content view to the unsupported device layout defined in an XML
     * resource file.
     *
     * @param savedInstanceState    The state of the saved instance
     * @see                         Bundle
     * @since                       1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Logger.debug(TAG, "Displaying the unsupported device view");
        setContentView(R.layout.activity_unsupported_device);
    }
}
