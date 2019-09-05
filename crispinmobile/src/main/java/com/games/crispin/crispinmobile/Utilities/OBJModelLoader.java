package com.games.crispin.crispinmobile.Utilities;

import android.content.res.Resources;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectData;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import java.io.InputStream;

public class OBJModelLoader
{
    // Tag used in logging output
    private static final String TAG = "OBJModelLoader";

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
                        case ASCII_N: // 'n' for vertex direction
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
                            renderObjectData.addPositionData(Float.parseFloat(new String(theFile, dataStartIndex, i - dataStartIndex)));
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

                            // Check if we should position counting the amount of face data per line
                            if(!countFaceDataPerLine && numberFaceDataPerLine == 0)
                            {
                                countFaceDataPerLine = true;
                            }

                            // Check if we should position counting the number of face data elements
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
                            renderObjectData.addPositionData(Float.parseFloat(new String(theFile, dataStartIndex, theFile.length - dataStartIndex)));
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
            // Position, texel and direction data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_TEXEL_AND_NORMAL);
        }
        else if(numberFaceDataElements == 2 && numberFaceDataSeparators == 2)
        {
            // Position and direction data has been provided
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
                renderObjectData.setRenderMethod(RenderObject.RenderMethod.POINTS);
                break;
            case 2:
                renderObjectData.setRenderMethod(RenderObject.RenderMethod.LINES);
                break;
            case 3:
                renderObjectData.setRenderMethod(RenderObject.RenderMethod.TRIANGLES);
                break;
            case 4:
                System.err.println("ERROR: QUADS are not supported as they require GLES 30");
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

        return renderObjectData.processData();
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
