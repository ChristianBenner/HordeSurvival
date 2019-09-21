package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

import java.util.ArrayList;

/**
 * Cache shader programs so that they are only loaded once no matter how many times the shader file
 * resource is referenced. This is used to significantly reduce loading times of scenes, reduce
 * memory and reduce video memory. The class consists of static only functions.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class ShaderCache
{
    // Tag used for logging
    private static final String TAG = "ShaderCache";

    // The array of shader programs
    private static ArrayList<Shader> shaderArray = new ArrayList<>();

    /**
     * Register a shader program in the cache
     *
     * @since 1.0
     */
    public static void registerShader(Shader shader)
    {
        shaderArray.add(shader);
    }

    /**
     * Remove all of the shader programs from the cache
     *
     * @since 1.0
     */
    public static void removeAll()
    {
        // Iterate through the shader array, destroying all of the shaders. This will also remove
        // shader from OpenGL controlled memory.
        for(Shader shader : shaderArray)
        {
            shader.destroy();
        }

        shaderArray.clear();
    }

    /**
     * Re-init all of the shader programs in the cache. Because OpenGL controlled memory is cleared
     * when an application is stopped/paused, this function provides an easy way to re-initialise
     * the shader programs.
     *
     * @since 1.0
     */
    public static void reinitialiseAll()
    {
        // Iterate through the shader array, reconstructing all of them
        for(int i = 0; i < shaderArray.size(); i++)
        {
            // Attempt to reconstruct
            try
            {
                shaderArray.get(i).reconstruct();
            }
            catch(Exception e)
            {
                Logger.error(TAG, "Failed to re-initialise shader");
                e.printStackTrace();
            }
        }
    }
}
