package com.games.crispin.crispinmobile.Rendering.UserInterface;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharacter;
import com.games.crispin.crispinmobile.Rendering.Models.FontSquare;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextShader;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Text extends UIObject
{
    private Font font;
    private String textString;

    class Glyph extends RenderObject
    {
        Glyph(float[] vertexData, Material m)
        {
            super(vertexData,
                    PositionDimensions_t.XY,
                    TexelDimensions_t.ST,
                    AttributeOrder_t.POSITION_THEN_TEXEL,
                    m);
        }
    }

    private ArrayList<Glyph> glyphs;

    private float scale;

    private Camera2D camera;

    private TextShader textShader;

    private ArrayList<FontSquare> squares;
    private boolean wrapWords;
    private boolean centerText;
    private float maxLineWidth;
    private float height;
    private float wiggleAmountPixels;
    private boolean wiggle;

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
        this.height = 0.0f;
        this.wiggleAmountPixels = 0.0f;
        this.wiggle = false;

        textShader = new TextShader();

        position = new Point3D();
        scale = 1.0f;

        camera = new Camera2D();
        squares = new ArrayList<>();

        setText(textString);
    }

    public void setText(String text)
    {
        this.textString = text;
        generateText();
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

        public float addCharacter(FreeTypeCharacter character)
        {
            characters.add(character);

            final float X_POS = startX + (character.bearingX * scale);
            final float WIDTH = character.width * scale;
            length = X_POS + WIDTH;

            // The advance for the next character
            startX += (character.advance >> 6) * scale;

            // Return the last x advance so we can consider where the next word starts
            return (character.advance >> 6) * scale;
        }

        private ArrayList<FreeTypeCharacter> characters;
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

    void generateText()
    {
        if(wrapWords)
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
                    System.out.print("Test Word: '");
                    for(int i = 0; i < w.characters.size(); i++)
                    {
                        System.out.print((char)w.characters.get(i).ascii);
                    }
                    System.out.println("', Length: " + w.length);

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

            final boolean centered = false;

            squares = new ArrayList<>();
            System.out.println("PRINTING WORD WRAPPED TEXT OUTPUT!****");
            float theY = 0.0f;
            for(int pLine = lines.size() - 1; pLine >= 0; pLine--)
            {
                System.out.println("Line length: " + lines.get(pLine).length);
                float theX = centered ? (maxLineWidth - lines.get(pLine).length) / 2.0f : 0.0f;

                ArrayList<Word> pWords = lines.get(pLine).getWords();
                for(int pWord = 0; pWord < pWords.size(); pWord++)
                {
                    ArrayList<FreeTypeCharacter> pCharacters = pWords.get(pWord).characters;

                    for(int pCharacter = 0; pCharacter < pCharacters.size(); pCharacter++)
                    {
                        System.out.print((char)pCharacters.get(pCharacter).ascii);

                        FreeTypeCharacter theChar = pCharacters.get(pCharacter);
                        float xpos = theX + theChar.bearingX * scale;
                        float ypos = theY - ((theChar.height - theChar.bearingY) * scale);
                        float width = theChar.width * scale;
                        float height = theChar.height * scale;

                        FontSquare square = new FontSquare(new Material(theChar.texture));
                        square.setCharacterOffset(new Point2D(xpos, ypos));
                        square.useCustomShader(textShader);
                        square.setColour(Colour.RED);
                        square.setScale(new Scale2D(width, height));
                        squares.add(square);

                        theX += (theChar.advance >> 6) * scale;
                    }
                }

                theY += font.getSize();
                System.out.println();
            }

            // The height of the text is the number of lines * the height of one line (the font
            // size)
            height = lines.size() * font.getSize();

            System.out.println("WORD WRAPPED OUTPUT FINISHED!****");
        }
        else
        {
            if(maxLineWidth == 0.0f)
            {
                // The start x position for the current line
                float theX = 0.0f;
                float currentY = 0.0f;

                for(int i = 0; i < textString.length(); i++)
                {
                    FreeTypeCharacter freeTypeCharacter = font.getCharacter(textString.charAt(i));
                    float xpos = theX + freeTypeCharacter.bearingX * scale;
                    float ypos = currentY - (freeTypeCharacter.height - freeTypeCharacter.bearingY) * scale;
                    float width = freeTypeCharacter.width * scale;
                    float height = freeTypeCharacter.height * scale;

                    FontSquare square = new FontSquare(new Material(freeTypeCharacter.texture));
                    square.setCharacterOffset(new Point2D(xpos, ypos));
                    square.useCustomShader(textShader);
                    square.setColour(Colour.RED);
                    square.setScale(new Scale2D(width, height));
                    squares.add(square);
                    theX += (freeTypeCharacter.advance >> 6) * scale;
                    System.out.println("*************** PLACED: " + textString.charAt(i) + "**********************");
                }

                // The height of the text (one line)
                height = font.getSize();
            }
            else
            {
                // Character wrap text
                // don't actually do this here, do it in the for loop above/look at older commits
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
        this.position = position;
        updateSquarePositions();
    }

    @Override
    public void setPosition(float x, float y, float z)
    {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        updateSquarePositions();
    }

    @Override
    public void setPosition(Point2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
        updateSquarePositions();
    }

    @Override
    public void setPosition(float x, float y)
    {
        this.position.x = x;
        this.position.y = y;
        updateSquarePositions();
    }

    public float getHeight()
    {
        return this.height;
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
    }
}
