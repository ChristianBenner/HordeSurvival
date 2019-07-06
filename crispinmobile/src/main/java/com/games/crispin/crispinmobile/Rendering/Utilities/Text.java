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


//    class Line
//    {
//        public Line(float maxLineWidth)
//        {
//            this.maxLineWidth = maxLineWidth;
//
//            squares = new ArrayList<>();
//            lineWidth = 0;
//            lineHeight = 0;
//        }
//
//        public boolean inLine(float endX)
//        {
//            if(endX )
//        }
//
//        public void addChar(Square square)
//        {
//            squares.add(square);
//        }
//
//        public float maxLineWidth;
//
//        public ArrayList<Square> squares;
//        public float lineWidth;
//        public float lineHeight;
//    }

//    class Line
//    {
//        public Line()
//        {
//            // Add the text to the current line
//        }
//
//        // Keep track of the current length of the line, if it is less than the specified line length
//        // then attempt to add another char
//        public Line addText(String text)
//        {
//            // Set the text for this line, return the
//        }
//
//        private Line nextLine;
//    }

   // private ArrayList<Line> lines;

    private ArrayList<Square> squares;

    public Text(Font font, String textString)
    {
        this.font = font;
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
       // constructText(position.x, position.y, Crispin.getSurfaceWidth(), true, scale);

        wordWrap(Crispin.getSurfaceWidth());
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

    void wordWrap(float lineWidth)
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
            if(tempLine.addWord(words.peek(), lineWidth))
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
            float theX = centered ? (lineWidth - lines.get(pLine).length) / 2.0f : 0.0f;

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

    private void constructText(float x, float y, float maxLineWidth, boolean wordWrap, float scale)
    {
//        // For word wrap enabled, create an array of words. The word class should contain data such
//        // as width
//        class Word
//        {
//            class CharData
//            {
//                public CharData(float x,
//                                float y,
//                                float w,
//                                float h,
//                                int textureId)
//                {
//                    this.x = x;
//                    this.y = y;
//                    this.w = w;
//                    this.h = h;
//                    this.textureId = textureId;
//                }
//
//                public float x, y, w, h;
//                public int textureId;
//            }
//
//            public Word()
//            {
//                characters = new ArrayList<>();
//            }
//
//            public void addCharacter(FreeTypeCharacter character, float scale)
//            {
//                // Calculate the total width of the word
//                CharData charData = new CharData(
//                        character.bearingX * scale,
//                        (character.height - character.bearingY) * scale,
//                        character.width * scale,
//                        character.height * scale,
//                        character.texture
//            }
//
//            public void offset(float x, float y)
//            {
//                for(int i = 0; i < characters.size(); i++)
//                {
//                    characters.get(i).x += x;
//                    characters.get(i).y += y;
//                }
//            }
//
//            public ArrayList<CharData> getCharacters()
//            {
//                return this.characters;
//            }
//
//            private ArrayList<CharData> characters;
//        }
//
//        ArrayList<ArrayList<CharData>> lines;
//
//        int currentLine = 0;
//        for(int i = 0; i < textString.length(); i++)
//        {
//            FreeTypeCharacter freeTypeCharacter = font.getCharacter(textString.charAt(i));
//            float xpos = x + (freeTypeCharacter.bearingX * scale);
//            float width = freeTypeCharacter.width * scale;
//
//            if(xpos + width >= x + maxLineWidth)
//            {
//                // Reset the xpos to 0
//                lines.get(0).remove(20);
//            }
//        }
//



        // The start x position for the current line
        float theX = x;
        float startX = 0;
        float currentY = y;

        // Have we started writing to the current line
        boolean started = false;

        int lastSpaceIndex = 0;

        for(int i = 0; i < textString.length(); i++)
        {
            if(textString.charAt(i) == ' ')
            {
                lastSpaceIndex = i;
            }

            FreeTypeCharacter freeTypeCharacter = font.getCharacter(textString.charAt(i));
            float xpos = theX + freeTypeCharacter.bearingX * scale;
            float ypos = currentY - (freeTypeCharacter.height - freeTypeCharacter.bearingY) * scale;
            float width = freeTypeCharacter.width * scale;
            float height = freeTypeCharacter.height * scale;

            if (!started)
            {
                started = true;
                startX = xpos;

                // We can't even put the first character on the line because the max line width
                // is too small. Don't go any further
                if (xpos + width > startX + maxLineWidth)
                {
                    break;
                }
            }

            if (xpos + width > startX + maxLineWidth)
            {
                // Create a new line
                started = false;
                theX = x;

                for(int n = 0; n < i; n++)
                {
                    // If word wrap is enabled and we are looking at the word that should be wrapped
                    // on the next line, instead of pushing it up, re-calculate its position
                    if(wordWrap && n >= lastSpaceIndex + 1)
                    {
                        // Re-calculate position
                        FreeTypeCharacter temp = font.getCharacter(textString.charAt(n));
                        squares.get(n).setPosition(theX + temp.bearingX * scale,
                                currentY - (temp.height - temp.bearingY) * scale);
                        theX += (temp.advance >> 6) * scale;
                    }
                    else
                    {
                        // Push up all of the old chars
                        squares.get(n).offset(0.0f, 64);
                    }

                    // Re-calculate the x position of the current char (as it has changed from
                    // wrapping the last word or char
                    xpos = theX + freeTypeCharacter.bearingX * scale;
                }

                System.out.println("*************** NEW LINE **********************");
            }

            Square square = new Square(new Material(freeTypeCharacter.texture));
            square.setPosition(new Point2D(xpos, ypos));
            square.useCustomShader(textShader);
            square.setColour(Colour.RED);
            square.setScale(new Scale2D(width, height));
            squares.add(square);
            theX += (freeTypeCharacter.advance >> 6) * scale;
            System.out.println("*************** PLACED: " + textString.charAt(i) + "**********************");
        }
//
//        for(int i = 0; i < textString.length(); i++)
//        {
//
//        }
//
//        for(int i = 0; i < textString.length(); i++)
//        {
//            FreeTypeCharacter freeTypeCharacter = font.getCharacter(textString.charAt(i));
//            Square square = new Square(new Material(freeTypeCharacter.texture));
//
//            float xpos = x + freeTypeCharacter.bearingX * scale;
//            float ypos = y - (freeTypeCharacter.height - freeTypeCharacter.bearingY) * scale;
//            float width = freeTypeCharacter.width * scale;
//            float height = freeTypeCharacter.height * scale;
//
//            square.setPosition(new Point2D(xpos, ypos));
//            square.useCustomShader(textShader);
//            square.setColour(Colour.RED);
//            square.setScale(new Scale2D(width, height));
//            squares.add(square);
//        //    theX += freeTypeCharacter.width;
//            x += (freeTypeCharacter.advance >> 6) * scale;
//
////            Square square = new Square(new Material(ch.texture));
////            square.setPosition(x + ch.width * scale, y - (ch.height - ch.bearingY) * scale);
////            square.setScale(ch.width * scale, ch.height * scale);
////            square.setColour(Colour.BLACK);
////            square.setRotation(0.0f, 1.0f, 1.0f, 1.0f);
////            square.useCustomShader(textShader);
////            squares.add(square);
////            x += (ch.advance >> 6) * scale;
//        }
    }

    public void renderText(Camera2D camera)
    {
        //square.draw(camera);
        for(Square square : squares)
        {
            square.draw(camera);
        }
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
