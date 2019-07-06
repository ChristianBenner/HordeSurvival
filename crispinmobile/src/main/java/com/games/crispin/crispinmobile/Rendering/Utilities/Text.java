package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharacter;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextShader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Text
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

    private Point2D position;
    private float scale;

    private Camera2D camera;

    private TextShader textShader;

    private ArrayList<Square> squares;
    private boolean wrapWords;
    private boolean centerText;
    private float maxLineWidth;

    public Text(Font font, String textString, boolean wrapWords, boolean centerText, float maxLineWidth)
    {
        this.font = font;
        this.wrapWords = wrapWords;
        this.centerText = centerText;
        this.maxLineWidth = maxLineWidth;

        textShader = new TextShader();

        position = new Point2D(0.0f, 000.0f);
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

                        Square square = new Square(new Material(theChar.texture));
                        square.setPosition(new Point2D(xpos, ypos));
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

                    Square square = new Square(new Material(freeTypeCharacter.texture));
                    square.setPosition(new Point2D(xpos, ypos));
                    square.useCustomShader(textShader);
                    square.setColour(Colour.RED);
                    square.setScale(new Scale2D(width, height));
                    squares.add(square);
                    theX += (freeTypeCharacter.advance >> 6) * scale;
                    System.out.println("*************** PLACED: " + textString.charAt(i) + "**********************");
                }
            }
            else
            {
                // Character wrap text
                // don't actually do this here, do it in the for loop above/look at older commits
            }

        }
    }

    float angle = 0.0f;
    float time = 0.0f;

    public void renderText(Camera2D camera)
    {
        time += 50f;
        //square.draw(camera);
        for(Square square : squares)
        {
            float sinVal = (square.getPosition().x + time) / Crispin.getSurfaceWidth();
            float height = 180f * (((float)Math.sin((double)sinVal) + 1.0f) / 2.0f);

            square.setPosition(square.getPosition().x, square.getPosition().y + height);
           // square.setRotation(angle, 0.0f, 0.0f, 1.0f);
            square.draw(camera);
            square.setPosition(square.getPosition().x, square.getPosition().y - height);
        }

        angle += 1f;
//
//        for(Glyph renderObject : glyphs)
//        {
//          //  renderObject.draw();
//        }

//        textShader.enableIt();
//        glUniform4f(textShader.getColourUniformHandle(),
//                colour.getRed(),
//                colour.getGreen(),
//                colour.getBlue(),
//                colour.getAlpha());
//
//        glUniformMatrix4fv(textShader.getMatrixUniformHandle(), 1, false, camera.getOrthoMatrix(), 0);
//
//        glActiveTexture(GL_TEXTURE0);
//        glBindVertexArray(vao);
//
//        for(int i = 0; i < textString.length(); i++)
//        {
//            FreeTypeCharacter ch = font.getCharacter(textString.charAt(i));
//
//            float xpos = x + ch.width * scale;
//            float ypos = y - (ch.height - ch.bearingY) * scale;
//
//            float w = ch.width * scale;
//            float h = ch.height * scale;
//
//            final float[] vertices =
//            {
//                    xpos, ypos + h, 0.0f, 0.0f,
//                    xpos, ypos, 0.0f, 1.0f,
//                    xpos + w, ypos, 1.0f, 1.0f,
//                    xpos, ypos + h, 0.0f, 0.0f,
//                    xpos + w, ypos, 1.0f, 1.0f,
//                    xpos + w, ypos + h, 1.0f, 0.0f
//            };
//
//            // Initialise a vertex byte buffer for the shape float array
//            final ByteBuffer VERTICES_BYTE_BUFFER = ByteBuffer.allocateDirect(
//                    vertices.length * BYTES_PER_FLOAT);
//
//            // Use the devices hardware's native byte order
//            VERTICES_BYTE_BUFFER.order(ByteOrder.nativeOrder());
//
//            // Create a Float buffer from the ByteBuffer
//            final FloatBuffer VERTEX_BUFFER = VERTICES_BYTE_BUFFER.asFloatBuffer();
//
//            // Add the array of floats to the buffer
//            VERTEX_BUFFER.put(vertices);
//
//            // Set buffer to read the first co-ordinate
//            VERTEX_BUFFER.position(0);
//
//            glBindTexture(GL_TEXTURE_2D, ch.texture.getId());
//            glUniform1i(textShader.getTextureUniformHandle(), 0);
//            glBindBuffer(GL_ARRAY_BUFFER, vbo);
//            glBufferSubData(GL_ARRAY_BUFFER,
//                    0,
//                    vertices.length * BYTES_PER_FLOAT,
//                    VERTEX_BUFFER);
//            glBindBuffer(GL_ARRAY_BUFFER, 0);
//
//            VERTEX_BUFFER.position(0);
//            glEnableVertexAttribArray(textShader.getPositionAttributeHandle());
//            glVertexAttribPointer(textShader.getPositionAttributeHandle(),
//                    2,
//                    GL_FLOAT,
//                    true,
//                    16,
//                    VERTEX_BUFFER);
//            VERTEX_BUFFER.position(0);
//
//            // Enable attrib texture data
//            VERTEX_BUFFER.position(2);
//            glEnableVertexAttribArray(textShader.getTextureAttributeHandle());
//            glVertexAttribPointer(textShader.getTextureAttributeHandle(),
//                    2,
//                    GL_FLOAT,
//                    true,
//                    16,
//                    VERTEX_BUFFER);
//            VERTEX_BUFFER.position(0);
//
//            glDrawArrays(GL_TRIANGLES, 0, 6);
//
//            glDisableVertexAttribArray(textShader.getPositionAttributeHandle());
//            glDisableVertexAttribArray(textShader.getTextureAttributeHandle());
//            x += (ch.advance >> 6) * scale;
//        }
//        glBindVertexArray(0);
//        glBindTexture(GL_TEXTURE_2D, 0);








//        for(int i = 0; i < textString.length(); i++)
//        {
//          //  Font.FreeTypeCharacter character = font.getCharacter()
//        }
//
//        textShader.enableIt();
//        glUniform4f(textShader.getColourUniformHandle(),
//                colour.getRed(),
//                colour.getGreen(),
//                colour.getBlue(),
//                colour.getAlpha());
//
//        glActiveTexture(GL_TEXTURE0);
//        glBindVertexArray();
//
//
//        glBindVertexArray()
//
//        textShader.enableIt();
//
//        float[] modelViewMatrix = new float[16];
//        Matrix.multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, modelMatrix, 0);
//
//        float[] modelViewProjectionMatrix = new float[16];
//        Matrix.multiplyMM(modelViewProjectionMatrix, 0, camera.getPerspectiveMatrix(), 0, modelViewMatrix, 0);
//
//        glUniformMatrix4fv(shader.getMatrixUniformHandle(), 1, false, modelViewProjectionMatrix, 0);
//
//        if(shader.getColourUniformHandle() != -1)
//        {
//            glUniform4fv(shader.getColourUniformHandle(), 1, colourData, 0);
//        }
//
//        if(shader.getTextureUniformHandle() != -1 && material.hasTexture())
//        {
//            glActiveTexture(GL_TEXTURE0);
//            glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
//            glUniform1i(shader.getTextureUniformHandle(), 0);
//        }
//
//        enableAttribs();
//        glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);
//        disableAttribs();
//
//        textShader.disableIt();
    }
}
