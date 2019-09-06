package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Geometry;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharData;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat;
import com.games.crispin.crispinmobile.Rendering.Models.FontSquare;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextShader;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

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
    // The font that the text string is in
    private Font font;

    // The text string to display
    private String textString;

    // Scale multiplier
    private float scale;

    private TextShader textShader;

    private ArrayList<FontSquare> squares;
    private boolean wrapWords;
    private boolean centerText;
    private float maxLineWidth;
    private float wiggleAmountPixels;
    private boolean wiggle;

    private float width;
    private float height;

    public enum WiggleSpeed_E
    {
        VERY_FAST,
        FAST,
        MEDIUM,
        SLOW,
        VERY_SLOW
    }

    public Text(Font font, String textString, boolean wrapWords, boolean centerText, float maxLineWidth)
    {
        this.font = font;
        this.wrapWords = wrapWords;
        this.centerText = centerText;
        this.maxLineWidth = maxLineWidth;
        this.wiggleAmountPixels = 0.0f;
        this.wiggle = false;

        this.width = 0.0f;
        this.height = 0.0f;

        textShader = new TextShader();

        position = new Point3D();
        scale = 1.0f;
        squares = new ArrayList<>();

        setText(textString);
    }

    public float getWidth()
    {
        return this.width;
    }

    public float getHeight()
    {
        return this.height;
    }

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

    class Word
    {
        public Word(float startX)
        {
            characters = new ArrayList<>();
            this.startX = startX;
            length = 0.0f;
        }

        public Word()
        {
            this(0.0f);
        }

        public float addCharacter(FreeTypeCharData character)
        {
            characters.add(character);

            final float X_POS = startX + (character.getBearingX() * scale);
            final float WIDTH = character.getWidth() * scale;
            length = X_POS + WIDTH;

            // The advance for the next character
            startX += (character.getAdvance() >> 6) * scale;

            // Return the last x advance so we can consider where the next word starts
            return (character.getAdvance() >> 6) * scale;
        }

        private ArrayList<FreeTypeCharData> characters;
        private float startX;
        private float length;
    }

    class Line
    {
        public Line()
        {
            words = new ArrayList<>();
            length = 0.0f;
        }

        public ArrayList<Word> getWords()
        {
            return this.words;
        }

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

        private ArrayList<Word> words;
        private float length;
    }

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
        for(int pLine = lines.size() - 1; pLine >= 0; pLine--)
        {
            float theX = centerText ? (maxLineWidth - lines.get(pLine).length) / 2.0f : 0.0f;

            ArrayList<Word> pWords = lines.get(pLine).getWords();
            for(int pWord = 0; pWord < pWords.size(); pWord++)
            {
                ArrayList<FreeTypeCharData> pCharacters = pWords.get(pWord).characters;

                for(int pCharacter = 0; pCharacter < pCharacters.size(); pCharacter++)
                {
                    FreeTypeCharData theChar = pCharacters.get(pCharacter);
                    final float CHAR_X = theX + theChar.getBearingX() * scale;
                    final float CHAR_Y = theY - ((theChar.getHeight() - theChar.getBearingY()) * scale);
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

                    theX += (theChar.getAdvance() >> 6) * scale;
                }
            }

            theY += font.getSize();
        }

        // The height of the text is the number of lines * the height of one line (the font
        // size)
        height = lines.size() * font.getSize();

        if(centerText)
        {
            width = maxLineWidth;
        }
    }

    private void generateCentered()
    {

    }

    /**
     * Read a file from a resource ID.

     * @return  The file as an array of bytes
     * @since   1.0
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
                    squares.get(squareIt).setCharacterOffset(Geometry.translate(squares.get(squareIt).getCharacterOffset(), 0.0f, font.getSize() * scale));
                }

                heightCalc += font.getSize() * scale;
            }

            // Calculate the x-position
            final float CHAR_X = cursorX + FREE_TYPE_CHAR_DATA.getBearingX() * scale;

            // Move the cursor along now that we have calculated the x position
            cursorX += (FREE_TYPE_CHAR_DATA.getAdvance() >> 6) * scale;

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
                    Colour.BLACK), position, new Point2D(CHAR_X, CHAR_Y));

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
     * Wrapped and centered sets text width to the length of the line as you are centering in regards to the line length

     * @return  The file as an array of bytes
     * @since   1.0
     */
    private void generateText()
    {
        if(wrapWords)
        {
            generateWrappedWords(centerText);
        }
        {
            if(centerText)
            {

            }
            else
            {
                generate();
            }

        }
    }

    private void updateSquarePositions()
    {
        for(int i = 0; i < squares.size(); i++)
        {
            squares.get(i).setTextPosition(this.position);
        }
    }

    // Sets position of all characters and doesn't work

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

    @Override
    public void setPosition(float x, float y, float z)
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

    @Override
    public void setPosition(Point2D position)
    {
        if(this.position.x != position.x ||
                this.position.y != position.y)
        {
            this.position.x = position.x;
            this.position.y = position.y;
            updateSquarePositions();
        }
    }

    @Override
    public void setPosition(float x, float y)
    {
        if(this.position.x != x ||
                this.position.y != y)
        {
            this.position.x = x;
            this.position.y = y;
            updateSquarePositions();
            System.out.println("UPDATED POSITION");
        }
    }

    float time = 0.0f;
    float wiggleSpeed = 0.0f;

    public void enableWiggle(float amountPixels, WiggleSpeed_E wiggleSpeed)
    {
        this.wiggleAmountPixels = amountPixels;
        this.wiggle = true;

        switch (wiggleSpeed)
        {
            case VERY_FAST:
                this.wiggleSpeed = 50.0f;
                break;
            case FAST:
                this.wiggleSpeed = 40.0f;
                break;
            case MEDIUM:
                this.wiggleSpeed = 30.0f;
                break;
            case SLOW:
                this.wiggleSpeed = 20.0f;
                break;
            case VERY_SLOW:
                this.wiggleSpeed = 10.0f;
                break;
        }
    }

    public void disableWiggle()
    {
        this.wiggle = false;
    }

    public void renderText(Camera2D camera)
    {
        final boolean REENABLE_DEPTH = Crispin.isDepthEnabled();
        glDisable(GL_DEPTH_TEST);

        if(wiggle)
        {
            time += wiggleSpeed;

            for(FontSquare square : squares)
            {
                final float sinVal = (square.getPosition().x + time) / maxLineWidth;
                final float height = wiggleAmountPixels * (((float)Math.sin((double)sinVal) + 1.0f) / 2.0f);
                square.setCharacterOffset(square.getCharacterOffset().x, square.getCharacterOffset().y + height);
                square.draw(camera);
                square.setCharacterOffset(square.getCharacterOffset().x, square.getCharacterOffset().y - height);
            }
        }
        else
        {
            for(FontSquare square : squares)
            {
                square.draw(camera);
            }
        }

        if(REENABLE_DEPTH)
        {
            glEnable(GL_DEPTH_TEST);
        }
    }
}
