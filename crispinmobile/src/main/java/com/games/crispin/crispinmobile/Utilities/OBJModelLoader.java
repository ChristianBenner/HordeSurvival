package com.games.crispin.crispinmobile.Utilities;

import android.content.res.Resources;
import android.util.Base64;
import android.util.Xml;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectData;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    private static final byte FACE = 0x01;
    private static final byte POSITION = 0x02;
    private static final byte TEXEL = 0x03;
    private static final byte NORMAL = 0x04;
    private static final byte VERTEX = 0x05;
    private static final byte COMMENT = 0x06;
    private static final byte NONE = 0x07;

    private static final byte ASCII_HASHTAG = 0x23;
    private static final byte ASCII_F = 0x66;
    private static final byte ASCII_V = 0x76;
    private static final byte ASCII_SPACE = 0x20;
    private static final byte ASCII_T = 0x74;
    private static final byte ASCII_N = 0x6E;
    private static final byte ASCII_0 = 0x30;
    private static final byte ASCII_9 = 0x39;
    private static final byte ASCII_POINT = 0x2E;
    private static final byte ASCII_MINUS = 0x2D;
    private static final byte ASCII_FORWARD_SLASH = 0x2F;
    private static final byte ASCII_NEW_LINE = 0x0A;
    private static final byte ASCII_CARRIAGE_RETURN = 0x0D;

    private static final int NO_START_INDEX = -1;

    public static RenderObject processObj(byte[] theFile, RenderObjectData renderObjectData)
    {
        // Keep track of the type of data we are looking at
        byte lineType = NONE;

        // Index associated to a float when processing
        int dataStartIndex = NO_START_INDEX;

        // Keep the number of different position elements in the face data
        int numberPositionDataElements = 0;

        // Whether or not to count the number of data elements in the position data
        boolean countPositionDataElements = false;

        // Keep the number of different elements in the face data
        int numberFaceDataElements = 0;

        // Keep the number of slashes in the face data
        int numberFaceDataSeparators = 0;

        // Keep the number of face data per line (this will help to determine the render method)
        int numberFaceDataPerLine = 0;

        // Whether or not to count the number of data elements and separators in the face data
        boolean countFaceDataElements = false;

        // Whether or not to count the number of face data per line
        boolean countFaceDataPerLine = false;

        for(int i = 0; i < theFile.length; i++)
        {
            final byte theByte = theFile[i];

            // Look at the line type so we know what we are processing
            switch (lineType)
            {
                case NONE:
                    // Discover what type the line that we are working on is
                    switch (theByte)
                    {
                        case ASCII_HASHTAG: // '#' for comment
                            lineType = COMMENT;
                            break;
                        case ASCII_F: // 'f' for face
                            lineType = FACE;
                            break;
                        case ASCII_V: // 'v' for vertex
                            lineType = VERTEX;
                            break;
                    }
                    break;
                case VERTEX:
                    // We know that the line type is vertex related but we still don't know what it
                    // is, discover its type
                    switch (theByte)
                    {
                        case ASCII_SPACE: // SPACE for vertex position
                            lineType = POSITION;
                            break;
                        case ASCII_T: // 't' for vertex texel
                            lineType = TEXEL;
                            break;
                        case ASCII_N: // 'n' for vertex normal
                            lineType = NORMAL;
                            break;
                        default: // Unsupported or error. Reset the line type
                            lineType = NONE;
                            break;
                    }
                    break;
                case POSITION:
                    // This is float data relevant to vertex position
                    if((theByte >= ASCII_0 && theByte <= ASCII_9) || // within 0 and 9 ascii
                            theByte == ASCII_POINT || theByte == ASCII_MINUS) // point and minus ascii
                    {
                        if (dataStartIndex == NO_START_INDEX)
                        {
                            dataStartIndex = i;

                            if(!countPositionDataElements && numberPositionDataElements == 0)
                            {
                                countPositionDataElements = true;
                            }
                        }
                    }
                    else
                    {
                        if(dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addVertexData(Float.parseFloat(new String(theFile, dataStartIndex, i - dataStartIndex)));
                            dataStartIndex = NO_START_INDEX;

                            if(countPositionDataElements)
                            {
                                numberPositionDataElements++;
                            }
                        }
                    }
                    break;
                case TEXEL:
                    // This is float data relevant to vertex position
                    if((theByte >= ASCII_0 && theByte <= ASCII_9) ||
                            theByte == ASCII_POINT || theByte == ASCII_MINUS)
                    {
                        if (dataStartIndex == NO_START_INDEX)
                        {
                            dataStartIndex = i;
                        }
                    }
                    else
                    {
                        if(dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addTexelData(Float.parseFloat(new String(theFile, dataStartIndex, i - dataStartIndex)));
                            dataStartIndex = NO_START_INDEX;
                        }
                    }
                    break;
                case NORMAL:
                    // This is float data relevant to vertex position
                    if((theByte >= ASCII_0 && theByte <= ASCII_9) ||
                            theByte == ASCII_POINT || theByte == ASCII_MINUS)
                    {
                        if (dataStartIndex == NO_START_INDEX)
                        {
                            dataStartIndex = i;
                        }
                    }
                    else
                    {
                        if(dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addNormalData(Float.parseFloat(new String(theFile, dataStartIndex, i - dataStartIndex)));
                            dataStartIndex = NO_START_INDEX;
                        }
                    }
                    break;
                case FACE:
                    // Now we have to parse integers
                    if(theByte >= ASCII_0 && theByte <= ASCII_9)
                    {
                        // A number
                        if(dataStartIndex == NO_START_INDEX)
                        {
                            dataStartIndex = i;

                            // Check if we should start counting the amount of face data per line
                            if(!countFaceDataPerLine && numberFaceDataPerLine == 0)
                            {
                                countFaceDataPerLine = true;
                            }

                            // Check if we should start counting the number of face data elements
                            // and separators
                            if(!countFaceDataElements &&
                                    numberFaceDataElements == 0 &&
                                    numberFaceDataSeparators == 0)
                            {
                                countFaceDataElements = true;
                            }
                        }
                    }
                    else
                    {
                        if(theByte == ASCII_FORWARD_SLASH && countFaceDataElements) // forward slash
                        {
                            numberFaceDataSeparators++;
                        }

                        if(dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing an int and have found the end of it, parse it
                            renderObjectData.addFaceData(Integer.parseInt(new String(theFile, dataStartIndex, i - dataStartIndex)));

                            dataStartIndex = NO_START_INDEX;

                            // If we have finished processing a chunk of face data
                            if(theByte != ASCII_FORWARD_SLASH && countFaceDataPerLine)
                            {
                                numberFaceDataPerLine++;
                            }

                            if(countFaceDataElements)
                            {
                                numberFaceDataElements++;
                            }
                        }

                        if(theByte == ASCII_SPACE) // space
                        {
                            countFaceDataElements = false;
                        }
                    }
                    break;
            }

            // Check if the byte represents line feed or new line '/r' or '/n'
            if(theByte == ASCII_NEW_LINE || theByte == ASCII_CARRIAGE_RETURN) // '\n' or '\r'
            {
                // Reset the line type for a new line
                lineType = NONE;
                dataStartIndex = NO_START_INDEX;

                countPositionDataElements = false;
                countFaceDataPerLine = false;
            }

            // If we are processing the last byte, process last bits of data
            if(i == theFile.length - 1)
            {
                // Look at the line type so we know what we are processing
                switch (lineType)
                {
                    case POSITION:
                        if (dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addVertexData(Float.parseFloat(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));
                        }
                        break;
                    case TEXEL:
                        if (dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addTexelData(Float.parseFloat(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));
                        }
                        break;
                    case NORMAL:
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addNormalData(Float.parseFloat(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));
                        }
                        break;
                    case FACE:
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing an int and have found the end of it, parse it
                            renderObjectData.addFaceData(Integer.parseInt(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));

                            // If we have finished processing a chunk of face data
                            if(theByte != ASCII_FORWARD_SLASH && countFaceDataPerLine)
                            {
                                numberFaceDataPerLine++;
                            }
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
        else
        {
            // error
        }

        switch (numberFaceDataPerLine)
        {
            case 1:
                renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.POINTS);
                break;
            case 2:
                renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.LINES);
                break;
            case 3:
                renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.TRIANGLES);
                break;
            case 4:
                renderObjectData.setRenderMethod(RenderObjectData.RenderMethod.QUADS);
                break;
            default:
                // error
                break;
        }

        switch (numberPositionDataElements)
        {
            case 2:
                renderObjectData.setPositionComponents(RenderObjectData.PositionComponents.XY);
                break;
            case 3:
                renderObjectData.setPositionComponents(RenderObjectData.PositionComponents.XYZ);
                break;
            case 4:
                renderObjectData.setPositionComponents(RenderObjectData.PositionComponents.XYZW);
                break;
            default:
                // error
                break;
        }

        return renderObjectData.processFaceData();
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
            RenderObject ro = processObj(theFile, renderObjectData);
            System.out.println("TK: " + ((System.nanoTime() - b) / 1000000) + "ms");

            return ro;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
