package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.Shader;

import java.util.ArrayList;

public class ShaderCache {
    private static ArrayList<Shader> shaders = new ArrayList<>();
    public static void registerShader(Shader shader)
    {
        shaders.add(shader);
    }

    public static void removeAll()
    {
        // Remove the shader from graphics memory
        for(Shader shader : shaders)
        {
            shader.destroy();
        }

        shaders.clear();
    }

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
