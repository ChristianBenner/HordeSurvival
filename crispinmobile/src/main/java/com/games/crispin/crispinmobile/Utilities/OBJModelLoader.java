package com.games.crispin.crispinmobile.Utilities;

import android.util.Log;

import java.util.ArrayList;

public class OBJModelLoader
{
    // Tag used in logging output
    private static final String TAG = "OBJModelLoader";

    private static ArrayList<ArrayList<ArrayList<Byte>>> readAsLinesAndWords(byte[] bytes)
    {
        // The array of lines
        ArrayList<ArrayList<ArrayList<Byte>>> lines = new ArrayList<>();

        // The array of words
        ArrayList<ArrayList<Byte>> currentLine = new ArrayList<>();

        // The current line (array of words)
        ArrayList<Byte> currentWord = new ArrayList<>();

        for(int i = 0; i < bytes.length; i++)
        {
            // Check if character is line feed '/r' or carriage return '/n' (new line)
            if((bytes[i] == 0x0A || bytes[i] == 0x0D))
            {
                if(!currentWord.isEmpty())
                {
                    currentLine.add(currentWord);
                    currentWord = new ArrayList<>();
                }

                if(!currentLine.isEmpty())
                {
                    lines.add(currentLine);
                    currentLine = new ArrayList<>();
                }
            }
            else
            {
                // Space ASCII
                if(bytes[i] == 0x20)
                {
                    if(!currentWord.isEmpty())
                    {
                        currentLine.add(currentWord);
                        currentWord = new ArrayList<>();
                    }
                }
                else
                {
                    currentWord.add(bytes[i]);
                }
            }
        }

        if(!currentWord.isEmpty())
        {
            currentLine.add(currentWord);
        }

        if(!currentLine.isEmpty())
        {
            lines.add(currentLine);
        }

        return lines;
    }

    public static void readObjFile(int resourceId)
    {
        // Get the file in bytes
        byte[] bytes = FileResourceReader.readRawResource(resourceId);

        // Get the file bytes as Array of Array of Array of characters. Allows us to split up the
        // bytes into words and lines.
        ArrayList<ArrayList<ArrayList<Byte>>> words = readAsLinesAndWords(bytes);

        System.out.println("PRINTING FILE********************");
        for(int lineIndex = 0; lineIndex < words.size(); lineIndex++)
        {
            for(int wordIndex = 0; wordIndex < words.get(lineIndex).size(); wordIndex++)
            {
                for(int charIndex = 0; charIndex < words.get(lineIndex).get(wordIndex).size(); charIndex++)
                {
                    byte theByte = words.get(lineIndex).get(wordIndex).get(charIndex);
                    System.out.print((char)theByte);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("PRINTED FILE***********************");
    }
}
