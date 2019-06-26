package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharacter;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextShader;

import java.util.ArrayList;

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
        constructText(position.x, position.y, Crispin.getSurfaceWidth(), true, scale);
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
                        squares.get(n).offset(0.0f, height);
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
