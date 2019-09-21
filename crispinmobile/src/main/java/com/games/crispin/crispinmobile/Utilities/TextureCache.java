package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

import java.util.ArrayList;

/**
 * Cache textures so that they are only loaded once no matter how many times the texture file
 * resource is referenced. This is used to significantly reduce loading times of scenes, reduce
 * memory and reduce video memory. The class consists of static only functions.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class TextureCache
{
    // Tag used for logging
    private static final String TAG = "TextureCache";

    // Array of textures
    private static ArrayList<Texture> textures = new ArrayList<>();

    /**
     * Register a texture in the cache
     *
     * @since 1.0
     */
    public static void registerTexture(Texture texture)
    {
        textures.add(texture);
    }

    /**
     * Remove all of the textures from the cache
     *
     * @since 1.0
     */
    public static void removeAll()
    {
        // Remove the texture from graphics memory
        for(int i = 0; i < textures.size(); i++)
        {
            textures.get(i).destroy();
        }

        textures.clear();
    }

    /**
     * Re-init all of the textures in the cache. Because OpenGL controlled memory is cleared
     * when an application is stopped/paused, this function provides an easy way to re-initialise
     * the textures (without having to construct them again causing render objects with textures to
     * loose their references to those textures)
     *
     * @since 1.0
     */
    public static void reinitialiseAll()
    {
        // Iterate through the textures array, re-initialising all of them
        for(int i = 0; i < textures.size(); i++)
        {
            // Attempt to reload the texture
            try
            {
                textures.get(i).reload();
            }
            catch(Exception e)
            {
                Logger.error(TAG, "Failed to re-initialise texture");
                e.printStackTrace();
            }
        }
    }
}
