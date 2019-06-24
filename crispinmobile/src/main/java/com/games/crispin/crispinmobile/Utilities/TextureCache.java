package com.games.crispin.crispinmobile.Utilities;

import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

import java.util.ArrayList;

public class TextureCache
{
    private static ArrayList<Texture> textures = new ArrayList<>();
    public static void registerTexture(Texture texture)
    {
        textures.add(texture);
    }

    public static void removeAll()
    {
        // Remove the texture from graphics memory
        for(Texture texture : textures)
        {
            texture.destroy();
        }

        textures.clear();
    }

    public static void reinitialiseAll()
    {
        for(int i = 0; i < textures.size(); i++)
        {
            try
            {
                textures.get(i).reconstruct();
            }
            catch(Exception e)
            {
                System.err.println("Failed to re-initialise texture");
                e.printStackTrace();
            }
        }
    }
}