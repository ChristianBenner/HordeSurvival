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
    // The array of shader programs
    private static ArrayList<Shader> shaders = new ArrayList<>();

    /**
     * Register a shader program in the cache
     *
     * @since 1.0
     */
    public static void registerShader(Shader shader)
    {
        shaders.add(shader);
    }

    /**
     * Remove all of the shader programs from the cache
     *
     * @since 1.0
     */
    public static void removeAll()
    {
        // Remove the shader from graphics memory
        for(Shader shader : shaders)
        {
            shader.destroy();
        }

        shaders.clear();
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
        for(Shader shader : shaders)
        {
            try
            {
                shader.reconstruct();
            }
            catch(Exception e)
            {
                System.err.println("Failed to re-initialise shader");
                e.printStackTrace();
            }
        }
    }
}
