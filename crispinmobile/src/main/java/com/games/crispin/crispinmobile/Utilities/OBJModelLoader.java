package com.games.crispin.crispinmobile.Utilities;

import android.content.res.Resources;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectData;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

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
        POSITION,
        TEXEL,
        NORMAL,
        VERTEX,
        COMMENT,
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
            return LineType_t.POSITION;
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
    private static final int NUM_COMPONENTS_XYZW = 4;
    private static final int NUM_COMPONENTS_XYZ = 3;
    private static final int NUM_COMPONENTS_XY = 2;

    private static void processData(LineType_t type, String line, RenderObjectData renderObjectData)
    {
        // Separate into words
        Scanner scanner = new Scanner(line);

        switch (type)
        {
            case FACE:
                if(line.contains("//"))
                {
                    // The face data is VERTEX_AND_NORMAL
                    renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_NORMAL);

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
                            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_TEXEL_AND_NORMAL);
                        }
                        else if(faceDataElements == NUM_FACE_DATA_ELEMENTS_VERTEX_TEXEL)
                        {
                            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_TEXEL);
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
                    renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_ONLY);

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
            case POSITION:
                int dataCount = 0;
                while(scanner.hasNextFloat())
                {
                    renderObjectData.addVertexData(scanner.nextFloat());
                    dataCount++;
                }

                switch (dataCount)
                {
                    case NUM_COMPONENTS_XYZW:
                        renderObjectData.setPositionComponents(RenderObjectData.PositionComponents.XYZW);
                        break;
                    case NUM_COMPONENTS_XYZ:
                        renderObjectData.setPositionComponents(RenderObjectData.PositionComponents.XYZ);
                        break;
                    case NUM_COMPONENTS_XY:
                        renderObjectData.setPositionComponents(RenderObjectData.PositionComponents.XY);
                        break;
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

//    private static void processLine(String line, RenderObjectData renderObjectData)
//    {
//        LineType_t type;
//
//        int wordStartIndex = 0;
//        int wordEndIndex = 0;
//        int numWords = 0;
//
//        // Process each word in the line
//        for(int i = 0; i < line.length(); i++)
//        {
//            if(line.charAt(i) == ' ')
//            {
//                wordEndIndex = i;
//
//                if(numWords == 0 && wordEndIndex > 0)
//                {
//                    type = getType(line.substring(wordStartIndex, wordEndIndex));
//
//                    if(type != LineType_t.NONE)
//                    {
//                        final int LAST_INDEX = line.length() - 1;
//                        final int BEGIN_INDEX = wordEndIndex + 1;
//                        if(BEGIN_INDEX <= LAST_INDEX)
//                        {
//                            long start = System.nanoTime();
//                            processData(type, line.substring(BEGIN_INDEX), renderObjectData);
//                            System.out.println("TimeData: " + (System.nanoTime() - start) + "ns");
//                            break;
//                        }
//                    }
//                }
//
//                wordStartIndex = i;
//            }
//        }
//    }

    public static void processLine(String line, RenderObjectData renderObjectData)
    {
        // Discover the type

        Scanner scanner = new Scanner(line);

        String type = scanner.next();

        if(type.compareTo("f") == 0)
        {
            while(scanner.hasNextInt())
            {
                renderObjectData.addFaceData(scanner.nextInt());
            }
        }
        else if(type.compareTo("v") == 0)
        {
            while(scanner.hasNextFloat())
            {
                renderObjectData.addVertexData(scanner.nextFloat());
            }
        }
        else if(type.compareTo("vt") == 0)
        {
            while(scanner.hasNextFloat())
            {
                renderObjectData.addTexelData(scanner.nextFloat());
            }
        }
        else if(type.compareTo("vn") == 0)
        {
            while (scanner.hasNextFloat())
            {
                renderObjectData.addNormalData(scanner.nextFloat());
            }
        }
    }

    public static void testLine(String line, RenderObjectData data)
    {
        LineType_t type = LineType_t.NONE;
        final char typeChar = line.charAt(0);
        int startIndex = 0;

        switch (typeChar)
        {
            case 'f':
                type = LineType_t.FACE;
                startIndex = 2;
                break;
            case 'v':
                final char typeCharTwo = line.charAt(1);
                switch (typeCharTwo)
                {
                    case ' ':
                        type = LineType_t.POSITION;
                        startIndex = 2;
                        break;
                    case 'n':
                        type = LineType_t.NORMAL;
                        startIndex = 3;
                        break;
                    case 't':
                        type = LineType_t.TEXEL;
                        startIndex = 3;
                        break;
                }
                break;
        }

        if(type == LineType_t.POSITION)
        {
            int startNumberScanIndex = -1;
            for(int i = startIndex; i < line.length(); i++)
            {
                if((line.charAt(i) >= 0x30 && line.charAt(i) <= 0x39) ||
                        line.charAt(i) == 0x2E || line.charAt(i) == 0x2D)
                {
                    if(startNumberScanIndex == -1)
                    {
                        startNumberScanIndex = i;
                    }
                }
                else
                {
                    if(startNumberScanIndex != -1)
                    {
                        // Using the current index, parse the string
                        float f = Float.parseFloat(line.substring(startNumberScanIndex, i));
                        startNumberScanIndex = -1;
                        System.out.println("Parsed Float: " + f);
                    }
                }

                // parse the last float on the line
                if(i == line.length() - 1 && startNumberScanIndex != -1)
                {
                    float f = Float.parseFloat(line.substring(startNumberScanIndex));
                    startNumberScanIndex = -1;
                    System.out.println("Parsed Float: " + f);
                }
            }
        }
    }

    public static void processObj(byte[] theFile, RenderObjectData renderObjectData)
    {
        ArrayList<Integer> faceData = new ArrayList<>();
        ArrayList<Float> positionData = new ArrayList<>();
        ArrayList<Float> texelData = new ArrayList<>();
        ArrayList<Float> normalData = new ArrayList<>();

        // Keep track of the type of data we are looking at
        LineType_t lineType = LineType_t.NONE;

        // Index associated to a float when processing
        int dataStartIndex = -1;

        // Keep the number of different elements in the face data
        int numberFaceDataElements = 0;

        // Keep the number of slashes in the face data
        int numberFaceDataSeparators = 0;

        boolean processingFaceData = false;

        for(int i = 0; i < theFile.length; i++)
        {
            // Look at the line type so we know what we are processing
            switch (lineType)
            {
                case NONE:
                    // Discover what type the line that we are working on is
                    switch (theFile[i])
                    {
                        case 0x23: // '#' for comment
                            lineType = LineType_t.COMMENT;
                            break;
                        case 0x66: // 'f' for face
                            lineType = LineType_t.FACE;
                            break;
                        case 0x76: // 'v' for vertex
                            lineType = LineType_t.VERTEX;
                            break;
                    }
                    break;
                case VERTEX:
                    // We know that the line type is vertex related but we still don't know what it
                    // is, discover its type
                    switch (theFile[i])
                    {
                        case 0x20: // SPACE for vertex position
                            lineType = LineType_t.POSITION;
                            break;
                        case 0x74: // 't' for vertex texel
                            lineType = LineType_t.TEXEL;
                            break;
                        case 0x6E: // 'n' for vertex normal
                            lineType = LineType_t.NORMAL;
                            break;
                        default: // Unsupported or error. Reset the line type
                            lineType = LineType_t.NONE;
                            break;
                    }
                    break;
                case POSITION:
                    // This is float data relevant to vertex position
                    if((theFile[i] >= 0x30 && theFile[i] <= 0x39) || // within 0 and 9 ascii
                            theFile[i] == 0x2E || theFile[i] == 0x2D) // point and minus ascii
                    {
                        if (dataStartIndex == -1)
                        {
                            dataStartIndex = i;
                        }
                    }
                    else
                    {
                        if(dataStartIndex != -1)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addVertexData(Float.parseFloat(new String(theFile, dataStartIndex, i - dataStartIndex)));
                            dataStartIndex = -1;
                        }
                    }
                    break;
                case TEXEL:
                    // This is float data relevant to vertex position
                    if((theFile[i] >= 0x30 && theFile[i] <= 0x39) ||
                            theFile[i] == 0x2E || theFile[i] == 0x2D)
                    {
                        if (dataStartIndex == -1)
                        {
                            dataStartIndex = i;
                        }
                    }
                    else
                    {
                        if(dataStartIndex != -1)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addTexelData(Float.parseFloat(new String(theFile, dataStartIndex, i - dataStartIndex)));
                            dataStartIndex = -1;
                        }
                    }
                    break;
                case NORMAL:
                    // This is float data relevant to vertex position
                    if((theFile[i] >= 0x30 && theFile[i] <= 0x39) ||
                            theFile[i] == 0x2E || theFile[i] == 0x2D)
                    {
                        if (dataStartIndex == -1)
                        {
                            dataStartIndex = i;
                        }
                    }
                    else
                    {
                        if(dataStartIndex != -1)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addNormalData(Float.parseFloat(new String(theFile, dataStartIndex, i - dataStartIndex)));
                            dataStartIndex = -1;
                        }
                    }
                    break;
                case FACE:
                    // Now we have to parse integers
                    if(theFile[i] >= 0x30 && theFile[i] <= 0x39)
                    {
                        // A number
                        if(dataStartIndex == -1)
                        {
                            dataStartIndex = i;
                        }
                    }
                    else
                    {
                        if(!processingFaceData &&
                                numberFaceDataElements == 0 &&
                                numberFaceDataSeparators == 0)
                        {
                            processingFaceData = true;
                        }

                        if(theFile[i] == 0x2F) // forward slash
                        {
                            if(processingFaceData)
                            {
                                numberFaceDataSeparators++;
                            }
                        }

                        if(dataStartIndex != -1)
                        {
                            // We are processing an int and have found the end of it, parse it
                            renderObjectData.addFaceData(Integer.parseInt(new String(theFile, dataStartIndex, i - dataStartIndex)));
                            dataStartIndex = -1;

                            if(processingFaceData)
                            {
                                numberFaceDataElements++;
                            }
                        }

                        if(theFile[i] == 0x20) // space
                        {
                            processingFaceData = false;
                        }
                    }
                    break;
            }

            // Check if the byte represents line feed or new line '/r' or '/n'
            if(theFile[i] == 0x0A || theFile[i] == 0x0D) // '\n' or '\r'
            {
                // Reset the line type for a new line
                lineType = LineType_t.NONE;
                dataStartIndex = -1;
            }

            // If we are processing the last byte, process last bits of data
            if(i == theFile.length -1)
            {
                // Look at the line type so we know what we are processing
                switch (lineType)
                {
                    case POSITION:
                        if (dataStartIndex != -1)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addVertexData(Float.parseFloat(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));
                        }
                        break;
                    case TEXEL:
                        if (dataStartIndex != -1)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addTexelData(Float.parseFloat(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));
                        }
                        break;
                    case NORMAL:
                        if (dataStartIndex != -1) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addNormalData(Float.parseFloat(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));
                        }
                        break;
                    case FACE:
                        if (dataStartIndex != -1) {
                            // We are processing an int and have found the end of it, parse it
                            renderObjectData.addFaceData(Integer.parseInt(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));
                        }
                        break;
                }
            }
        }

        if(numberFaceDataElements == 3 && numberFaceDataSeparators == 2)
        {
            // Position, texel and normal data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_TEXEL_AND_NORMAL);
        }
        else if(numberFaceDataElements == 2 && numberFaceDataSeparators == 2)
        {
            // Position and normal data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_NORMAL);
        }
        else if(numberFaceDataElements == 2 && numberFaceDataSeparators == 1)
        {
            // Position and texel data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_TEXEL);
        }
        else if(numberFaceDataElements == 1 && numberFaceDataSeparators == 0)
        {
            // Only position data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_ONLY);
        }

        renderObjectData.setPositionComponents(RenderObjectData.PositionComponents.XYZ);
        renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.TRIANGLES);
    }

    public static RenderObject readObjFile(int resourceId)
    {
        RenderObjectData renderObjectData = new RenderObjectData();

        try {
            Resources resources = Crispin.getApplicationContext().getResources();

            InputStream inputStream;
            long start = System.nanoTime();
            inputStream = resources.openRawResource(resourceId);
    /*        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    "ASCII");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while(line != null)
            {
                testLine(line, renderObjectData);
                line = reader.readLine();
            }*/

            long end = System.nanoTime();
            System.out.println("TimeTaken: " + ((end - start) / 1000000) + "ms");

            inputStream.reset();
            long b = System.nanoTime();
            byte[] theFile = new byte[inputStream.available()];
            inputStream.read(theFile);
            processObj(theFile, renderObjectData);
            System.out.println("TK: " + ((System.nanoTime() - b) / 1000000) + "ms");

            return renderObjectData.processFaceData();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
