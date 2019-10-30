package com.games.crispin.crispinmobile.Utilities;

import android.content.res.Resources;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectData;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import java.io.InputStream;

/**
 * Designed to read OBJ model files and produce a RenderObject that can be drawn on a
 * scene. OBJ models loaded with the class must have triangulated faces. It also doesn't support
 * multiple OBJ models within one file. The class is comprised of static only functions.
 *
 * @see         RenderObject
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class OBJModelLoader
{
    // Tag used in logging output
    private static final String TAG = "OBJModelLoader";

    // Identifier for face data
    private static final byte FACE = 0x01;

    // Identifier for position data
    private static final byte POSITION = 0x02;

    // Identifier for texel data
    private static final byte TEXEL = 0x03;

    // Identifier for normal data
    private static final byte NORMAL = 0x04;

    // Identifier for vertex type data (e.g. position, texel and normal)
    private static final byte VERTEX = 0x05;

    // Identifier for comment
    private static final byte COMMENT = 0x06;

    // Identifier for undefined data type
    private static final byte NONE = 0x07;

    // ASCII value of hash-tag character (this appears before comments)
    private static final byte ASCII_HASHTAG = 0x23;

    // ASCII value of the 'f' character
    private static final byte ASCII_F = 0x66;

    // ASCII value of the 'v' character
    private static final byte ASCII_V = 0x76;

    // ASCII value of the space character
    private static final byte ASCII_SPACE = 0x20;

    // ASCII value of the 't' character
    private static final byte ASCII_T = 0x74;

    // ASCII value of the 'n' character
    private static final byte ASCII_N = 0x6E;

    // ASCII value of the '0' character
    private static final byte ASCII_0 = 0x30;

    // ASCII value of the '9' character
    private static final byte ASCII_9 = 0x39;

    // ASCII value of the '.' character
    private static final byte ASCII_POINT = 0x2E;

    // ASCII value of the '-' character
    private static final byte ASCII_MINUS = 0x2D;

    // ASCII value of the '/' character
    private static final byte ASCII_FORWARD_SLASH = 0x2F;

    // ASCII value of the new line character
    private static final byte ASCII_NEW_LINE = 0x0A;

    // ASCII value of the carriage return character
    private static final byte ASCII_CARRIAGE_RETURN = 0x0D;

    // Represent that the data subject (if there is one) hasn't been assigned a start index
    private static final int NO_START_INDEX = -1;

    // The number to divide nanoseconds by to get milliseconds
    private static final int NANOSECONDS_TO_MILLISECONDS_DIVIDE = 1000000;

    // Four face data elements in the face data
    private static final int FOUR_FACE_DATA_ELEMENTS = 4;

    // Three face data elements in the face data
    private static final int THREE_FACE_DATA_ELEMENTS = 3;

    // Two face data elements in the face data
    private static final int TWO_FACE_DATA_ELEMENTS = 2;

    // One face data element in the face data
    private static final int ONE_FACE_DATA_ELEMENT = 1;

    // Two face data separators in the face data
    private static final int TWO_FACE_DATA_SEPARATORS = 2;

    // One face data separator in the face data
    private static final int ONE_FACE_DATA_SEPARATOR = 1;

    /**
     * Read an OBJ file from a resource ID
     *
     * @param resourceId    The OBJ model file resource ID
     * @return  A RenderObject built from the model data. The model can be rendered to a scene
     * @see     RenderObject
     * @since   1.0
     */
    public static RenderObject readObjFile(int resourceId)
    {
        // Attempt to open and read an OBJ file
        try
        {
            // Measure how long it takes to load load and read the model file
            long start = System.nanoTime();

            Resources resources = Crispin.getApplicationContext().getResources();
            InputStream inputStream;
            inputStream = resources.openRawResource(resourceId);
            inputStream.reset();

            byte[] theFile = new byte[inputStream.available()];
            inputStream.read(theFile);
            RenderObject ro = processObj(theFile);

            // End of time measurement
            long end = System.nanoTime();

            // Log time taken to load the OBJ model
            Logger.debug(TAG, "Model Loaded in: " + ((end - start) /
                    NANOSECONDS_TO_MILLISECONDS_DIVIDE) + "ms");

            return ro;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Process the OBJ model
     *
     * @param theFile   The model file as an array of bytes
     * @return  A RenderObject built from the model data. The model can be rendered to a scene
     * @see     RenderObject
     * @since   1.0
     */
    private static RenderObject processObj(byte[] theFile)
    {
        RenderObjectData renderObjectData = new RenderObjectData();

        // Keep track of the type of data we are looking at
        byte lineType = NONE;

        // Index associated to a float when processing
        int dataStartIndex = NO_START_INDEX;

        // Keep the number of different position elements in the face data
        int numberPositionDataElements = 0;

        // Whether or not to count the number of data elements in the position data
        boolean countPositionDataElements = false;

        // Keep the number of different normal elements in the face data
        int numberNormalDataElements = 0;

        // Whether or not to count the number of data elements in the normal data
        boolean countNormalDataElements = false;

        // Keep the number of different texel elements in the face data
        int numberTexelDataElements = 0;

        // Whether or not to count the number of data elements in the texel data
        boolean countTexelDataElements = false;

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

        // Iterate through all the bytes in the file
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
                    if((theByte >= ASCII_0 && theByte <= ASCII_9) ||
                            theByte == ASCII_POINT || theByte == ASCII_MINUS)
                    {
                        // Check that a start index hasn't already been defined
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
                        // Check if there is a start index before continuing
                        if(dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addPositionData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    i - dataStartIndex)));
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
                        // Check that a start index hasn't already been defined
                        if (dataStartIndex == NO_START_INDEX)
                        {
                            dataStartIndex = i;

                            if(!countTexelDataElements && numberTexelDataElements == 0)
                            {
                                countTexelDataElements = true;
                            }
                        }
                    }
                    else
                    {
                        // Check if there is a start index before continuing
                        if(dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addTexelData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    i - dataStartIndex)));
                            dataStartIndex = NO_START_INDEX;

                            if(countTexelDataElements)
                            {
                                numberTexelDataElements++;
                            }
                        }
                    }
                    break;
                case NORMAL:
                    // This is float data relevant to vertex position
                    if((theByte >= ASCII_0 && theByte <= ASCII_9) ||
                            theByte == ASCII_POINT || theByte == ASCII_MINUS)
                    {
                        // Check that a start index hasn't already been defined
                        if (dataStartIndex == NO_START_INDEX)
                        {
                            dataStartIndex = i;

                            if(!countNormalDataElements && numberNormalDataElements == 0)
                            {
                                countNormalDataElements = true;
                            }
                        }
                    }
                    else
                    {
                        // Check if there is a start index before continuing
                        if(dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addNormalData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    i - dataStartIndex)));
                            dataStartIndex = NO_START_INDEX;

                            if(countNormalDataElements)
                            {
                                numberNormalDataElements++;
                            }
                        }
                    }
                    break;
                case FACE:
                    // Now we have to parse integers
                    if(theByte >= ASCII_0 && theByte <= ASCII_9)
                    {
                        // Check that a start index hasn't already been defined
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
                        // Increment the number of face data separators if one is found
                        if(theByte == ASCII_FORWARD_SLASH && countFaceDataElements) // forward slash
                        {
                            numberFaceDataSeparators++;
                        }

                        // Check if there is a start index before continuing
                        if(dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing an int and have found the end of it, parse it
                            renderObjectData.addFaceData(Integer.parseInt(new String(theFile,
                                    dataStartIndex,
                                    i - dataStartIndex)));

                            dataStartIndex = NO_START_INDEX;

                            // If we have finished processing a chunk of face data
                            if(theByte != ASCII_FORWARD_SLASH && countFaceDataPerLine)
                            {
                                numberFaceDataPerLine++;
                            }

                            // Add face data elements if counting is enabled as we have found one
                            if(countFaceDataElements)
                            {
                                numberFaceDataElements++;
                            }
                        }

                        // If a space has been detected, stop counting face data elements
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
                countTexelDataElements = false;
                countNormalDataElements = false;
                countFaceDataPerLine = false;
            }

            // If we are processing the last byte, process last bits of data
            if(i == theFile.length - 1)
            {
                // Look at the line type so we know what we are processing
                switch (lineType)
                {
                    case POSITION:
                        // Check that a start index has been found before attempting to add position
                        // data (because there will not have been any in the string)
                        if (dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addPositionData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    theFile.length - dataStartIndex)));
                        }
                        break;
                    case TEXEL:
                        // Check that a start index has been found before attempting to add texel
                        // data (because there will not have been any in the string)
                        if (dataStartIndex != NO_START_INDEX)
                        {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addTexelData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    theFile.length - dataStartIndex)));
                        }
                        break;
                    case NORMAL:
                        // Check that a start index has been found before attempting to add normal
                        // data (because there will not have been any in the string)
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addNormalData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    theFile.length - dataStartIndex)));
                        }
                        break;
                    case FACE:
                        // Check that a start index has been found before attempting to add face
                        // data (because there will not have been any in the string)
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing an int and have found the end of it, parse it
                            renderObjectData.addFaceData(Integer.parseInt(new String(theFile,
                                    dataStartIndex,
                                    theFile.length - dataStartIndex)));

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

        // Determine what type of face data the OBJ model file has
        if(numberFaceDataElements == THREE_FACE_DATA_ELEMENTS &&
                numberFaceDataSeparators == TWO_FACE_DATA_SEPARATORS)
        {
            // Position, texel and direction data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.
                    POSITION_AND_TEXEL_AND_NORMAL);
        }
        else if(numberFaceDataElements == TWO_FACE_DATA_ELEMENTS &&
                numberFaceDataSeparators == TWO_FACE_DATA_SEPARATORS)
        {
            // Position and direction data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_NORMAL);
        }
        else if(numberFaceDataElements == TWO_FACE_DATA_ELEMENTS &&
                numberFaceDataSeparators == ONE_FACE_DATA_SEPARATOR)
        {
            // Position and texel data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_TEXEL);
        }
        else if(numberFaceDataElements == ONE_FACE_DATA_ELEMENT &&
                numberFaceDataSeparators == 0)
        {
            // Only position data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_ONLY);
        }
        else
        {
            // error
            Logger.error(TAG, "Failed to determine how many face data elements in an OBJ" +
                    "model files face data");
        }

        // Set the render method depending on how much face data appears in a line
        switch (numberFaceDataPerLine)
        {
            case ONE_FACE_DATA_ELEMENT:
                renderObjectData.setRenderMethod(RenderObject.RenderMethod.POINTS);
                break;
            case TWO_FACE_DATA_ELEMENTS:
                renderObjectData.setRenderMethod(RenderObject.RenderMethod.LINES);
                break;
            case THREE_FACE_DATA_ELEMENTS:
                renderObjectData.setRenderMethod(RenderObject.RenderMethod.TRIANGLES);
                break;
            case FOUR_FACE_DATA_ELEMENTS:
                Logger.error(TAG, "QUADS are not supported as they require GLES 30");
                break;
            default:
                Logger.error(TAG, "Unsupported number of face data per vertex: " +
                        numberFaceDataPerLine);
                break;
        }

        renderObjectData.setNumPositionComponents((byte)numberPositionDataElements);
        renderObjectData.setNumNormalComponents((byte)numberNormalDataElements);
        renderObjectData.setNumTexelComponents((byte)numberTexelDataElements);
        return renderObjectData.processData();
    }
}
