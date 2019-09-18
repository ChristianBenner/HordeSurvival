package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Geometry;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharData;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Models.FontSquare;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextShader;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

/**
 * Text class is a UIObject designed to render text strings. It requires a Font to be loaded before
 * use.
 *
 * @see         Font
 * @see         UIObject
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Text extends UIObject
{
    /**
     * Word class is designed to store data on multiple characters
     *
     * @author      Christian Benner
     * @version     %I%, %G%
     * @since       1.0
     */
    class Word
    {
        // Character data array
        private ArrayList<FreeTypeCharData> characters;

        // The start position x-coordinate
        private float startX;

        // Length of the word
        private float length;

        /**
         * Construct a word object.
         *
         * @param startX    The start position of the word
         * @since 1.0
         */
        public Word(float startX)
        {
            characters = new ArrayList<>();
            this.startX = startX;
            length = 0.0f;
        }

        /**
         * Construct a word object with a default start position of zero
         *
         * @since 1.0
         */
        public Word()
        {
            this(0.0f);
        }

        /**
         * Add a character to the word
         *
         * @param character FreeTypeCharData to add to the word
         * @return The start character advancement
         * @since 1.0
         */
        public float addCharacter(FreeTypeCharData character)
        {
            characters.add(character);

            final float X_POS = startX + (character.getBearingX() * scale);
            final float WIDTH = character.getWidth() * scale;
            length = X_POS + WIDTH;

            // The advance for the next character
            startX += (character.getAdvance() >> ADVANCE_TRANSFORMATION) * scale;

            // Return the last x advance so we can consider where the next word starts
            return (character.getAdvance() >> ADVANCE_TRANSFORMATION) * scale;
        }
    }

    /**
     * A line is comprised of multiple word objects. It contains data on the text and is used in
     * formatting calculations.
     *
     * @see         Word
     * @author      Christian Benner
     * @version     %I%, %G%
     * @since       1.0
     */
    class Line
    {
        // Word array
        private ArrayList<Word> words;

        // Length of the line
        private float length;

        /**
         * Construct a line object.
         *
         * @since 1.0
         */
        public Line()
        {
            words = new ArrayList<>();
            length = 0.0f;
        }

        /**
         * Retrieve an array of words that the line is comprised of
         *
         * @return An array list of words that the line is comprised of
         * @since 1.0
         */
        public ArrayList<Word> getWords()
        {
            return this.words;
        }

        /**
         * Add a word to the line
         *
         * @param word      The word to add to the line
         * @param maxLength The maximum length of the line
         * @return True if successfully added, false if the word didn't fit on the line
         * @since 1.0
         */
        public boolean addWord(Word word, float maxLength)
        {
            // If the word fits on the line, add it and increase the length
            // If the word doesn't fit on the line however there is no words currently on the line,
            // add it anyway
            if(word.length + length <= maxLength || words.isEmpty())
            {
                words.add(word);
                length += word.length;

                return true;
            }

            // Word doesn't fit on the line, return false
            return false;
        }
    }

    // Speed of the wiggle motion
    public enum WiggleSpeed_E
    {
        VERY_FAST,
        FAST,
        MEDIUM,
        SLOW,
        VERY_SLOW
    }

    // Tag used in logging
    private static final String TAG = "Text";

    // The default scale value if one is not provided
    private static final float DEFAULT_SCALE = 1.0f;

    // Transform the FreeType advance value
    private static final int ADVANCE_TRANSFORMATION = 6;

    // Value to divide the line width by to find half of the line width
    private static final float CENTER_LINE_DIVIDE = 2.0f;

    // The very fast wiggle speed
    private static final float WIGGLE_SPEED_VERY_FAST = 50.0f;

    // The fast wiggle speed
    private static final float WIGGLE_SPEED_FAST = 40.0f;

    // The medium wiggle speed
    private static final float WIGGLE_SPEED_MEDIUM = 30.0f;

    // The slow wiggle speed
    private static final float WIGGLE_SPEED_SLOW = 20.0f;

    // The very slow wiggle speed
    private static final float WIGGLE_SPEED_VERY_SLOW = 10.0f;

    // This is used in a calculation to make the sin wave produce an output from 0.0 to 1.0
    private static final float SIN_WAVE_OFFSET = 1.0f;

    // This is used in a calculation to make the sin wave produce an output from 0.0 to 1.0
    private static final float SIN_WAVE_DIVIDE = 2.0f;

    // The font that the text string is in
    private Font font;

    // The text string to display
    private String textString;

    // Wrap words state
    private boolean wrapWords;

    // Center text state
    private boolean centerText;

    // Max line width (before wrapping the string)
    private float maxLineWidth;

    // Scale multiplier
    private float scale;

    // The width of the text
    private float width;

    // The height of the text
    private float height;

    // Amount in pixels that the text can wiggle
    private float wiggleAmountPixels;

    // Wiggle state
    private boolean wiggle;

    // Text shader designed to render the characters
    private TextShader textShader;

    // Array of font square render objects (render objects designed to handle character strings)
    private ArrayList<FontSquare> squares;

    // Timing used in wiggle calculation
    private float wiggleTime;

    // Speed of the wiggle
    private float wiggleSpeed;

    /**
     * Construct a text user interface object
     *
     * @param font          The font to fetch the character data from
     * @param textString    Text string to generate
     * @param wrapWords     True to wrap text by words, else wrap text by characters. For text to
     *                      wrap there must also be a defined maxLineWidth.
     * @param centerText    True to center the text in the middle of the maxLineWidth
     * @param maxLineWidth  Max line width to generate the text in. If the characters exceed the
     *                      line width, the text will be wrapped. The way the text wrapped is
     *                      determined on the wrapWords parameter.
     * @param scale         Scale multiplier to apply to the text. Note that this scales the
     *                      texture. When upscaling text may appear fuzzy. When downscaling it may
     *                      produce artifacts.
     * @since   1.0
     */
    public Text(Font font,
                String textString,
                boolean wrapWords,
                boolean centerText,
                float maxLineWidth,
                float scale)
    {
        this.font = font;
        this.wrapWords = wrapWords;
        this.centerText = centerText;
        this.maxLineWidth = maxLineWidth;
        this.scale = scale;

        wiggleAmountPixels = 0.0f;
        wiggleTime = 0.0f;
        wiggleSpeed = 0.0f;
        wiggle = false;
        width = 0.0f;
        height = 0.0f;

        textShader = new TextShader();
        position = new Point3D();
        squares = new ArrayList<>();

        setText(textString);
    }

    /**
     * Construct a text user interface object
     *
     * @param font          The font to fetch the character data from
     * @param textString    Text string to generate
     * @param wrapWords     True to wrap text by words, else wrap text by characters. For text to
     *                      wrap there must also be a defined maxLineWidth.
     * @param centerText    True to center the text in the middle of the maxLineWidth
     * @param maxLineWidth  Max line width to generate the text in. If the characters exceed the
     *                      line width, the text will be wrapped. The way the text wrapped is
     *                      determined on the wrapWords parameter.
     * @since   1.0
     */
    public Text(Font font,
                String textString,
                boolean wrapWords,
                boolean centerText,
                float maxLineWidth)
    {
        this(font,
                textString,
                wrapWords,
                centerText,
                maxLineWidth,
                DEFAULT_SCALE);
    }

    /**
     * Construct a text user interface object
     *
     * @param font          The font to fetch the character data from
     * @param textString    Text string to generate
     * @param maxLineWidth  Max line width to generate the text in. If the characters exceed the
     *                      line width, the text will be wrapped. The way the text wrapped is
     *                      determined on the wrapWords parameter.
     * @since   1.0
     */
    public Text(Font font,
                String textString,
                float maxLineWidth)
    {
        this(font,
                textString,
                false,
                false,
                maxLineWidth,
                DEFAULT_SCALE);
    }

    /**
     * Construct a text user interface object
     *
     * @param font          The font to fetch the character data from
     * @param textString    Text string to generate
     * @since   1.0
     */
    public Text(Font font,
                String textString)
    {
        this(font,
                textString,
                false,
                false,
                0.0f,
                DEFAULT_SCALE);
    }

    /**
     * Get the width of the text
     *
     * @return  The width of the text string. If the text comprises of multiple lines, the width
     *          is the length of the largest line.
     * @since   1.0
     */
    public float getWidth()
    {
        return this.width;
    }

    /**
     * Get the height of the text
     *
     * @return  The height of the text string
     * @since   1.0
     */
    public float getHeight()
    {
        return this.height;
    }

    /**
     * Set text and generate a new set of characters from a string
     *
     * @return  The new text to generate
     * @since   1.0
     */
    public void setText(String text)
    {
        // Only change the text if it isn't the same as before
        if(this.textString == null ||
                text.compareTo(this.textString) != 0)
        {
            this.textString = text;
            generateText();
        }
    }

    /**
     * Generate a new set of characters and text that can be rendered. When words exceed the maximum
     * line width, they will be wrapped (put onto the next line) unless the max line length is zero.
     *
     * @param centerText    Whether to center the text or not. True to center text, else false
     * @since 1.0
     */
    private void generateWrappedWords(boolean centerText)
    {
        // Save a queue with all the words in
        Queue<Word> words = new LinkedList<>();

        // Temp current word
        Word tempWord = new Word();

        // Iterate through the text string
        for(int i = 0; i < textString.length(); i++)
        {
            // Get the x advance so that when we create the next word, we know how much to offset
            // it. This will likely just be the x advance of a space ' ',
            float xAdvance = tempWord.addCharacter(font.getCharacter(textString.charAt(i)));

            // If the character isn't a space, add it to the temp word. Also check if it is the last
            // character in the string, if it is we might want to push he word current word into the
            // word list.
            if(((textString.charAt(i) == ' ') && (!tempWord.characters.isEmpty())) ||
                    ((i == textString.length() - 1 && !tempWord.characters.isEmpty())))
            {
                // If the character is a space and the current word is not empty, add it to the
                // word array
                words.add(tempWord);

                // Create a new word for next iteration
                tempWord = new Word(xAdvance);
            }
        }

        // Array list of lines
        LinkedList<Line> lines = new LinkedList<>();

        // The current line
        Line tempLine = new Line();

        // Go through each word attempting to add them to lines
        while(!words.isEmpty())
        {
            if(tempLine.addWord(words.peek(), maxLineWidth))
            {
                Word w = words.peek();

                // Word has been added to the line, we can now remove the word from the queue
                words.remove();

                // If the word was the last one, add the line (because the loop wont run again)
                if(words.isEmpty())
                {
                    lines.add(tempLine);
                }
            }
            else
            {
                // Add the temp line
                lines.add(tempLine);

                // Create a new line object that has a length of 0
                tempLine = new Line();
            }
        }

        squares = new ArrayList<>();
        float theY = 0.0f;

        // Iterate through the lines (starting from the end of the array). Add each line
        for(int pLine = lines.size() - 1; pLine >= 0; pLine--)
        {
            float theX = centerText ? (maxLineWidth - lines.get(pLine).length) /
                    CENTER_LINE_DIVIDE : 0.0f;

            ArrayList<Word> pWords = lines.get(pLine).getWords();

            // Iterate through the words in the line. Add each word
            for(int pWord = 0; pWord < pWords.size(); pWord++)
            {
                ArrayList<FreeTypeCharData> pCharacters = pWords.get(pWord).characters;

                // Iterate through the characters in the word, adding each one
                for(int pCharacter = 0; pCharacter < pCharacters.size(); pCharacter++)
                {
                    FreeTypeCharData theChar = pCharacters.get(pCharacter);
                    final float CHAR_X = theX + theChar.getBearingX() * scale;
                    final float CHAR_Y = theY - ((theChar.getHeight() - theChar.getBearingY()) *
                            scale);
                    final float CHAR_WIDTH = theChar.getWidth() * scale;
                    final float CHAR_HEIGHT = theChar.getHeight() * scale;

                    // If the character exceeds the current width, set the width as its pos + width
                    if(CHAR_X + CHAR_WIDTH > width)
                    {
                        width = CHAR_X + CHAR_WIDTH;
                    }

                    FontSquare square = new FontSquare(new Material(theChar.texture, Colour.BLACK),
                            position, new Point2D(CHAR_X, CHAR_Y));
                    square.useCustomShader(textShader);
                    square.setScale(new Scale2D(CHAR_WIDTH, CHAR_HEIGHT));
                    squares.add(square);

                    theX += (theChar.getAdvance() >> ADVANCE_TRANSFORMATION) * scale;
                }
            }

            theY += font.getSize() * scale;
        }

        // The height of the text is the number of lines * the height of one line (the font
        // size)
        height = lines.size() * font.getSize() * scale;

        // If the text is centered the width is the maximum line length
        if(centerText)
        {
            width = maxLineWidth;
        }
    }

    /**
     * Generate a new set of characters and text that can be rendered. When characters exceed the
     * maximum line width, they will be wrapped (put onto the next line) unless the max line length
     * is zero. The text is centered.
     *
     * @since 1.0
     */
    private void generateCentered()
    {
        // The first task is to calculate the width of each line. To do this we need to add as many
        // characters to a virtual line before it exceeds the maximum line length.
        // Then we use the width of each line to calculate a starting position for each character
        // when generating the text

        // A line comprised of characters (not words as we are not word wrapping)
        class CharLine
        {
            ArrayList<FreeTypeCharData> line = new ArrayList<>();
            float lineLength = 0.0f;
        }

        // Create an array of lines to store words in
        ArrayList<CharLine> lines = new ArrayList<>();

        // Used while calculating the height of the text
        float heightCalc = 0.0f;

        float cursorX = 0.0f;
        CharLine currentLine = new CharLine();
        for(int i = 0; i < textString.length(); i++)
        {
            // Get the data of the current character
            final FreeTypeCharData FREE_TYPE_CHAR_DATA = font.getCharacter(textString.charAt(i));

            // If adding the next character to the line will exceed the maximum line length, push
            // the line onto the array of lines and create a new current line.
            if((cursorX + FREE_TYPE_CHAR_DATA.getBearingX() * scale) +
                    (FREE_TYPE_CHAR_DATA.getWidth() * scale) >= maxLineWidth)
            {
                // Push the current line onto the array of lines and create a new line
                lines.add(currentLine);

                currentLine = new CharLine();
                cursorX = 0.0f;

                heightCalc += font.getSize() * scale;
            }

            // Calculate the x-position
            final float CHAR_X = cursorX + FREE_TYPE_CHAR_DATA.getBearingX() * scale;

            // Calculate the width
            final float CHAR_WIDTH = FREE_TYPE_CHAR_DATA.getWidth() * scale;

            // Move the cursor along now that we have calculated the x position
            cursorX += (FREE_TYPE_CHAR_DATA.getAdvance() >> ADVANCE_TRANSFORMATION) * scale;

            currentLine.line.add(FREE_TYPE_CHAR_DATA);

            // Check if the character exceeds the current line length and if it does set the new
            // line length
            if(CHAR_X + CHAR_WIDTH >= currentLine.lineLength)
            {
                currentLine.lineLength = CHAR_X + CHAR_WIDTH;
            }
        }

        // Add the final line
        if(!currentLine.line.isEmpty())
        {
            lines.add(currentLine);
            heightCalc += font.getSize() * scale;
        }

        squares = new ArrayList<>();
        float cursorY = 0.0f;

        // Iterate through the lines, creating characters that can be rendered (iterate from the end
        // to the beginning of the array)
        for(int i = lines.size() - 1; i >= 0; i--)
        {
            // The starting x co-ordinate
            cursorX = (maxLineWidth - lines.get(i).lineLength) / CENTER_LINE_DIVIDE;

            // Iterate through the characters in the line, creating objects that can be rendered
            for(int charIt = 0; charIt < lines.get(i).line.size(); charIt++)
            {
                // Get the data of the current character
                final FreeTypeCharData FREE_TYPE_CHAR_DATA = lines.get(i).line.get(charIt);

                // Calculate the x-position
                final float CHAR_X = cursorX + FREE_TYPE_CHAR_DATA.getBearingX() * scale;

                // Move the cursor along now that we have calculated the x position
                cursorX += (FREE_TYPE_CHAR_DATA.getAdvance() >> ADVANCE_TRANSFORMATION) * scale;

                // Calculate the y-position
                final float CHAR_Y = cursorY - (FREE_TYPE_CHAR_DATA.getHeight() -
                        FREE_TYPE_CHAR_DATA.getBearingY()) * scale;

                // Calculate the width
                final float CHAR_WIDTH = FREE_TYPE_CHAR_DATA.getWidth() * scale;

                // Calculate the height
                final float CHAR_HEIGHT = FREE_TYPE_CHAR_DATA.getHeight() * scale;

                // Create the render object square
                FontSquare square = new FontSquare(new Material(FREE_TYPE_CHAR_DATA.texture,
                        Colour.BLACK), position, new Point2D(CHAR_X, CHAR_Y));

                // Force the use of the text shader (optimised for text rendering)
                square.useCustomShader(textShader);

                // Scale the square to the width and height of the character
                square.setScale(new Scale2D(CHAR_WIDTH, CHAR_HEIGHT));

                // Add the square to the square array for rendering later
                squares.add(square);
            }

            cursorY += font.getSize() * scale;
        }

        height = heightCalc;
        width = maxLineWidth;
    }

    /**
     * Generate a new set of characters and text that can be rendered. When characters exceed the
     * maximum line width, they will be wrapped (put onto the next line) unless the max line length
     * is zero.
     *
     * @since 1.0
     */
    private void generate()
    {
        // This is text that is not wrapped or centered. Start from the position and just keep
        // adding more letters until the max line width is met (if there is line width defined)

        // X position of the cursor
        float cursorX = 0.0f;

        // Y position of the cursor
        float cursorY = 0.0f;

        // Used while calculating the height of the text
        float heightCalc = font.getSize() * scale;

        // Iterate through the text string
        for(int i = 0; i < textString.length(); i++)
        {
            // Get the data of the current character
            final FreeTypeCharData FREE_TYPE_CHAR_DATA = font.getCharacter(textString.charAt(i));

            // If there is a max line width, see if the character x position + width is greater than
            // the line width. If it is, put it on a new line
            if(maxLineWidth != 0.0f &&
                    (cursorX + FREE_TYPE_CHAR_DATA.getBearingX() * scale) +
                            (FREE_TYPE_CHAR_DATA.getWidth() * scale) >= maxLineWidth)
            {
                cursorX = 0.0f;

                for(int squareIt = 0; squareIt < squares.size(); squareIt++)
                {
                    squares.get(squareIt).setCharacterOffset(
                            Geometry.translate(squares.get(squareIt).getCharacterOffset(),
                                    0.0f,
                                    font.getSize() * scale));
                }

                heightCalc += font.getSize() * scale;
            }

            // Calculate the x-position
            final float CHAR_X = cursorX + FREE_TYPE_CHAR_DATA.getBearingX() * scale;

            // Move the cursor along now that we have calculated the x position
            cursorX += (FREE_TYPE_CHAR_DATA.getAdvance() >> ADVANCE_TRANSFORMATION) * scale;

            // Calculate the y-position
            final float CHAR_Y = cursorY - (FREE_TYPE_CHAR_DATA.getHeight() -
                    FREE_TYPE_CHAR_DATA.getBearingY()) * scale;

            // Calculate the width
            final float CHAR_WIDTH = FREE_TYPE_CHAR_DATA.getWidth() * scale;

            // If the character exceeds the known width, set its pos + width as the new width
            if(CHAR_X + CHAR_WIDTH >= width)
            {
                width = CHAR_X + CHAR_WIDTH;
            }

            // Calculate the height
            final float CHAR_HEIGHT = FREE_TYPE_CHAR_DATA.getHeight() * scale;

            // Create the render object square
            FontSquare square = new FontSquare(new Material(FREE_TYPE_CHAR_DATA.texture,
                    Colour.BLACK),
                    position,
                    new Point2D(CHAR_X, CHAR_Y));

            // Force the use of the text shader (optimised for text rendering)
            square.useCustomShader(textShader);

            // Scale the square to the width and height of the character
            square.setScale(new Scale2D(CHAR_WIDTH, CHAR_HEIGHT));

            // Add the square to the square array for rendering later
            squares.add(square);
        }

        height = heightCalc;
    }

    /**
     * Wrapped and centered sets text width to the length of the line as you are centering in
     * regards to the line length
     *
     * @return  The file as an array of bytes
     * @since   1.0
     */
    private void generateText()
    {
        // If word wrapping is enabled then use the word wrap function, otherwise use the non-word
        // wrap functions.
        if(wrapWords)
        {
            generateWrappedWords(centerText);
        }
        else
        {
            if(centerText)
            {
                generateCentered();
            }
            else
            {
                generate();
            }

        }
    }

    /**
     * Set the colour of the text. This will loop through each character and set the colour of the
     * applied materials colour to the one provided.
     *
     * @param colour
     * @since 1.0
     */
    public void setColour(Colour colour)
    {
        // Iterate through all of the characters setting their new material colours
        for(int i = 0; i < squares.size(); i++)
        {
            squares.get(i).getMaterial().setColour(colour);
        }
    }

    /**
     * Update the position of the text by updating all of the characters text position
     *
     * @since 1.0
     */
    private void updateSquarePositions()
    {
        // Iterate through all of the characters updating the text position (they will re-calculate
        // their position based on the text position by using their character offset data)
        for(int i = 0; i < squares.size(); i++)
        {
            squares.get(i).setTextPosition(this.position);
        }
    }

    /**
     * If the new position for the text is different to the current, set the new position and do a
     * position update. The position of the text is based from the bottom left point.
     *
     * @param position  The new position
     * @since 1.0
     */
    @Override
    public void setPosition(Point3D position)
    {
        // Only update the position if the position has changed
        if(this.position.x != position.x ||
            this.position.y != position.y ||
            this.position.z != position.z)
        {
            this.position = position;
            updateSquarePositions();
        }
    }

    /**
     * If the new position for the text is different to the current, set the new position and do a
     * position update. The position of the text is based from the bottom left point.
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @param z The new z-coordinate
     * @since 1.0
     */
    @Override
    public void setPosition(float x,
                            float y,
                            float z)
    {
        // Only update the position if the position has changed
        if(this.position.x != x ||
                this.position.y != y ||
                this.position.z != z)
        {
            this.position.x = x;
            this.position.y = y;
            this.position.z = z;
            updateSquarePositions();
        }
    }

    /**
     * If the new position for the text is different to the current, set the new position and do a
     * position update. The position of the text is based from the bottom left point.
     *
     * @param position  The new position
     * @since 1.0
     */
    @Override
    public void setPosition(Point2D position)
    {
        // Only update the position if the position has changed
        if(this.position.x != position.x ||
                this.position.y != position.y)
        {
            this.position.x = position.x;
            this.position.y = position.y;
            updateSquarePositions();
        }
    }

    /**
     * If the new position for the text is different to the current, set the new position and do a
     * position update. The position of the text is based from the bottom left point.
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @since 1.0
     */
    @Override
    public void setPosition(float x, float y)
    {
        // Only update the position if the position has changed
        if(this.position.x != x ||
                this.position.y != y)
        {
            this.position.x = x;
            this.position.y = y;
            updateSquarePositions();
        }
    }

    /**
     * Enable text wiggle. This is the vertical movement of characters in a sin wave style motion.
     * The speed and amount of wiggle is adjustable
     *
     * @param amountPixels  The amount of pixels to wiggle characters by (vertically)
     * @param wiggleSpeed   The speed in which to wiggle the text
     * @since 1.0
     */
    public void enableWiggle(float amountPixels, WiggleSpeed_E wiggleSpeed)
    {
        this.wiggleAmountPixels = amountPixels;
        this.wiggle = true;

        // Switch through the different wiggle speeds
        switch (wiggleSpeed)
        {
            case VERY_FAST:
                this.wiggleSpeed = WIGGLE_SPEED_VERY_FAST;
                break;
            case FAST:
                this.wiggleSpeed = WIGGLE_SPEED_FAST;
                break;
            case MEDIUM:
                this.wiggleSpeed = WIGGLE_SPEED_MEDIUM;
                break;
            case SLOW:
                this.wiggleSpeed = WIGGLE_SPEED_SLOW;
                break;
            case VERY_SLOW:
                this.wiggleSpeed = WIGGLE_SPEED_VERY_SLOW;
                break;
        }
    }

    /**
     * Disable text wiggle
     *
     * @since 1.0
     */
    public void disableWiggle()
    {
        this.wiggle = false;
    }

    /**
     * Render the text to the display using a 2D camera object.
     *
     * @param camera
     * @since 1.0
     */
    public void draw(Camera2D camera)
    {
        final boolean REENABLE_DEPTH = Crispin.isDepthEnabled();
        glDisable(GL_DEPTH_TEST);

        // If wiggle is enabled, apply the
        if(wiggle)
        {
            wiggleTime += wiggleSpeed;

            // Iterate through all of the characters and render them
            for(int i = 0; i < squares.size(); i++)
            {
                final FontSquare square = squares.get(i);

                final float SIN_VALUE = (square.getPosition().x + wiggleTime) / maxLineWidth;
                final float WIGGLE_OFFSET = wiggleAmountPixels *
                        (((float)Math.sin((double)SIN_VALUE) + SIN_WAVE_OFFSET) / SIN_WAVE_DIVIDE);
                square.setCharacterOffset(square.getCharacterOffset().x,
                        square.getCharacterOffset().y + WIGGLE_OFFSET);
                square.render(camera);
                square.setCharacterOffset(square.getCharacterOffset().x,
                        square.getCharacterOffset().y - WIGGLE_OFFSET);
            }
        }
        else
        {
            // Iterate through all of the characters and render them
            for(int i = 0; i < squares.size(); i++)
            {
                squares.get(i).render(camera);
            }
        }

        // If depth was enabled before calling the function then re-enable it
        if(REENABLE_DEPTH)
        {
            glEnable(GL_DEPTH_TEST);
        }
    }
}
