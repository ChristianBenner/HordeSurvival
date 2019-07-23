package com.games.crispin.crispinmobile.Utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class OBJModelLoader
{
    // Tag used in logging output
    private static final String TAG = "OBJModelLoader";

    private static void readAsLinesAndWords(byte[] bytes)
    {
        // Num of lines
        int lineCount = 1;

        // Find number of lines
        for(int i = 0; i < bytes.length; i++)
        {
            if(bytes[i] == 0x0A) // Checks for line feed '\n'
            {
                // Increase the number of lines that are in the file
                lineCount++;
            }
            else if(bytes[i] == 0x0D) // Checks for carriage return '\r'
            {
                // DOS/Windows use \r\n line endings, so skip one iteration
                i++;
                lineCount++;
            }
        }

        System.out.println("Lines: " + lineCount);

        byte[][] structuredBytes = new byte[lineCount][];
        int currentLine = 0;
        for(int i = 0; i < bytes.length; i++)
        {
           // structuredBytes[0][]
            if(bytes[i] == 0x20) // ASCII Space
            {
                // New word
            }
        }
    }



    private static void scanLine(ArrayList<ArrayList<Byte>> line)
    {

    }

    enum LINE_TYPE
    {
        FACE,
        VERTEX,
        TEXEL,
        NORMAL
    }

//    private static LINE_TYPE scanLineType(ArrayList<Byte> word)
//    {
//        return LINE_TYPE.FACE;
//    }
//
//    private static float scanWordFloat(ArrayList<Byte> word)
//    {
//        Byte[] byteArray = (Byte[])word.toArray();
//        String str = new String(byteArray);
//    }
//
//    private static ArrayList<Integer> scanFaceWord(ArrayList<Byte> word)
//    {
//
//    }


    public static void readObjFile(int resourceId)
    {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "ASCII");
        BufferedReader reader = new BufferedReader(inputStreamReader);

        ArrayList<String> lines = new ArrayList<>();
        String line = reader.readLine();
        while(line != null)
        {
            lines.add(line);
            line = reader.readLine();
        }

        if(line != null)
        {
            lines.add(line);
        }

        for(int i = 0; i < lines.size(); i++)
        {
            System.out.println("LINE: " + lines.get(i));
        }

        // Get the file in bytes
        byte[] bytes = FileResourceReader.readRawResource(resourceId);
        //readAsLinesAndWords(bytes);

        // Get the file bytes as Array of Array of Array of characters. Allows us to split up the
        // bytes into words and lines.
        //ArrayList<ArrayList<byte[]>> words = readAsLinesAndWords(bytes);

//        System.out.println("PRINTING FILE********************");
//        for(int lineIndex = 0; lineIndex < words.size(); lineIndex++)
//        {
//            for(int wordIndex = 0; wordIndex < words.get(lineIndex).size(); wordIndex++)
//            {
//                for(int charIndex = 0; charIndex < words.get(lineIndex).get(wordIndex).size(); charIndex++)
//                {
//                    byte theByte = words.get(lineIndex).get(wordIndex).get(charIndex);
//                    System.out.print((char)theByte);
//                }
//                System.out.print(" ");
//            }
//            System.out.println();
//        }
//        System.out.println("PRINTED FILE***********************");
    }
}
