package com.games.crispin.crispinmobile.Utilities;

import android.content.res.Resources;
import android.util.Log;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectData;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

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

    private static final int NUM_VERTEX_QUADS = 4;
    private static final int NUM_VERTEX_TRIANGLES = 3;
    private static final int NUM_VERTEX_LINES = 2;
    private static final int NUM_VERTEX_POINTS = 1;
    private static final int NUM_FACE_DATA_ELEMENTS_VERTEX_TEXEL = 2;
    private static final int NUM_FACE_DATA_ELEMENTS_VERTEX_TEXEL_NORMAL = 3;

    private static void processData(LineType_t type, String line, RenderObjectData renderObjectData)
    {
        // Separate into words
        System.out.println("vvv Process Data: " + line);

        Scanner scanner = new Scanner(line);

        switch (type)
        {
            case FACE:
                if(line.contains("//"))
                {
                    // The face data is VERTEX_AND_NORMAL
                    renderObjectData.setFaceDataType(RenderObjectData.FaceData.VERTEX_NORMAL);

                    int vertexCount = 0;
                    while(scanner.hasNext())
                    {
                        // Get the next word in the face data line
                        String nextWord = scanner.next();
                        vertexCount++;

                        // Create a scanner with that word
                        Scanner faceDataScanner = new Scanner(nextWord);

                        // Iterate through the scanner using the '//' as a delimiter
                        faceDataScanner.useDelimiter("//");

                        while(faceDataScanner.hasNextInt())
                        {
                            renderObjectData.addFaceData(faceDataScanner.nextInt());
                        }
                    }

                    // Decide what render method to use depending on how much vertex data is present
                    switch (vertexCount)
                    {
                        case NUM_VERTEX_POINTS:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.POINTS);
                            break;
                        case NUM_VERTEX_LINES:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.LINES);
                            break;
                        case NUM_VERTEX_TRIANGLES:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.TRIANGLES);
                            break;
                        case NUM_VERTEX_QUADS:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.QUADS);
                            break;
                    }
                }
                else if(line.contains("/"))
                {
                    // The face data could be VERTEX_AND_TEXEL or VERTEX_AND_TEXEL_AND_NORMAL

                    int vertexCount = 0;
                    while(scanner.hasNext())
                    {
                        // Get the next word in the face data line
                        String nextWord = scanner.next();

                        vertexCount++;

                        // Create a scanner with that word
                        Scanner faceDataScanner = new Scanner(nextWord);

                        // Iterate through the scanner using the '/' as a delimiter
                        faceDataScanner.useDelimiter("/");

                        // Keep a count to determine if it contains 2 or 3 elements
                        int faceDataElements = 0;
                        while(faceDataScanner.hasNextInt())
                        {
                            renderObjectData.addFaceData(faceDataScanner.nextInt());
                            faceDataElements++;
                        }

                        // Determine if the face data contains all vertex, texel and normal data
                        // elements or just vertex and texel data elements
                        if(faceDataElements == NUM_FACE_DATA_ELEMENTS_VERTEX_TEXEL_NORMAL)
                        {
                            renderObjectData.setFaceDataType(RenderObjectData.FaceData.VERTEX_TEXEL_NORMAL);
                        }
                        else if(faceDataElements == NUM_FACE_DATA_ELEMENTS_VERTEX_TEXEL)
                        {
                            renderObjectData.setFaceDataType(RenderObjectData.FaceData.VERTEX_TEXEL);
                        }
                    }

                    // Decide what render method to use depending on how much vertex data is present
                    switch (vertexCount)
                    {
                        case NUM_VERTEX_POINTS:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.POINTS);
                            break;
                        case NUM_VERTEX_LINES:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.LINES);
                            break;
                        case NUM_VERTEX_TRIANGLES:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.TRIANGLES);
                            break;
                        case NUM_VERTEX_QUADS:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.QUADS);
                            break;
                    }
                }
                else
                {
                    // The face data consists of only VERTEX data
                    renderObjectData.setFaceDataType(RenderObjectData.FaceData.VERTEX_ONLY);

                    int vertexCount = 0;
                    while(scanner.hasNext())
                    {
                        // We can just look at each word (no need to use a different delimiter)
                        renderObjectData.addFaceData(scanner.nextInt());
                        vertexCount++;
                    }

                    // Decide what render method to use depending on how much vertex data is present
                    switch (vertexCount)
                    {
                        case NUM_VERTEX_POINTS:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.POINTS);
                            break;
                        case NUM_VERTEX_LINES:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.LINES);
                            break;
                        case NUM_VERTEX_TRIANGLES:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.TRIANGLES);
                            break;
                        case NUM_VERTEX_QUADS:
                            renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.QUADS);
                            break;
                    }
                }
                break;
            case VERTEX:
                while(scanner.hasNextFloat())
                {
                    renderObjectData.addVertexData(scanner.nextFloat());
                }
            case TEXEL:
                while(scanner.hasNextFloat())
                {
                    renderObjectData.addTexelData(scanner.nextFloat());
                }
            case NORMAL:
                while(scanner.hasNextFloat())
                {
                    renderObjectData.addNormalData(scanner.nextFloat());
                }
                break;
        }

    }

    private static void processLine(String line, RenderObjectData renderObjectData)
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
                        if(BEGIN_INDEX <= LAST_INDEX)
                        {
                            processData(type, line.substring(BEGIN_INDEX), renderObjectData);
                        }
                    }
                }

                wordStartIndex = i;
            }
        }
    }

    public static void readObjFile(int resourceId)
    {
        RenderObjectData renderObjectData = new RenderObjectData();

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
                processLine(line, renderObjectData);
            }
            while(line != null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
