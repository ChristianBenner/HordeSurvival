package com.games.crispin.crispinmobile.Utilities;

import android.content.res.Resources;
import android.util.Log;

import com.games.crispin.crispinmobile.Crispin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject.BYTES_PER_FLOAT;

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

    enum LineType_t
    {
        FACE,
        VERTEX,
        TEXEL,
        NORMAL,
        NONE
    }

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


    private static ArrayList<Float> vertexData = new ArrayList<>();
    private static ArrayList<Float> texelData = new ArrayList<>();
    private static ArrayList<Float> normalData = new ArrayList<>();
    private static ArrayList<Integer> faceData = new ArrayList<>();

    private static LineType_t getType(String word)
    {
        if(word.compareTo("v") == 0)
        {
            return LineType_t.VERTEX;
        }
        else if(word.compareTo("vt") == 0)
        {
            return LineType_t.TEXEL;
        }
        else if(word.compareTo("vn") == 0)
        {
            return LineType_t.NORMAL;
        }
        else if(word.compareTo("f") == 0)
        {
            return LineType_t.FACE;
        }

        return LineType_t.NONE;
    }

    private static void processData(LineType_t type, String line)
    {
        // Separate into words
        System.out.println("vvv Process Data: " + line);

        Scanner scanner = new Scanner(line);

        switch (type)
        {
            case FACE:
                while(scanner.hasNext())
                {
                    String nextWord = scanner.next();
                    Scanner faceDataScanner = new Scanner(nextWord);
                    faceDataScanner.useDelimiter("/");

                    while(faceDataScanner.hasNextInt())
                    {
                        faceData.add(faceDataScanner.nextInt());
                    }
                }
                break;
            case VERTEX:
                while(scanner.hasNextFloat())
                {
                    vertexData.add(scanner.nextFloat());
                }
            case TEXEL:
                while(scanner.hasNextFloat())
                {
                    texelData.add(scanner.nextFloat());
                }
            case NORMAL:
                while(scanner.hasNextFloat())
                {
                    normalData.add(scanner.nextFloat());
                }
                break;
        }

    }

    private static void processLine(String line)
    {
        LineType_t type;

        int wordStartIndex = 0;
        int wordEndIndex = 0;
        int numWords = 0;

        // Process each word in the line
        for(int i = 0; i < line.length(); i++)
        {
            if(line.charAt(i) == ' ')
            {
                wordEndIndex = i;

                if(numWords == 0 && wordEndIndex > 0)
                {
                    type = getType(line.substring(wordStartIndex, wordEndIndex));

                    if(type != LineType_t.NONE)
                    {
                        final int LAST_INDEX = line.length() - 1;
                        final int BEGIN_INDEX = wordEndIndex + 1;
                        if(BEGIN_INDEX < LAST_INDEX)
                        {
                            processData(type, line.substring(BEGIN_INDEX));
                        }
                    }
                }

                wordStartIndex = i;
            }
        }
    }

    public static void readObjFile(int resourceId)
    {
        try {
            Resources resources = Crispin.getApplicationContext().getResources();
            InputStream inputStream = resources.openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    "ASCII");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            ArrayList<String> lines = new ArrayList<>();

            String line;
            do
            {
                line = reader.readLine();
                processLine(line);
            }
            while(line != null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
