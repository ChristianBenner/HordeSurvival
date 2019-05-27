package com.games.crispin.crispinmobile;

import java.util.ArrayList;

public class ShaderCache {
    private static ArrayList<GLSLShader> shaders = new ArrayList<>();
    public static void registerShader(GLSLShader shader)
    {
        shaders.add(shader);
    }

    public static void removeAll()
    {
        shaders.clear();
    }

    public static void reinitialiseAll()
    {
        for(GLSLShader shader : shaders)
        {
            try
            {
                shader.reinit();
            }
            catch(Exception e)
            {
                System.err.println("Failed to re-initialise shader");
                e.printStackTrace();
            }
        }
    }
}
