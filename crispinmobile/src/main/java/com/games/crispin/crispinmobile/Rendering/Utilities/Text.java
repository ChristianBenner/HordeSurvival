package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Data.FreeTypeCharacter;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_DYNAMIC_DRAW;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glBufferSubData;
import static android.opengl.GLES30.glDisableVertexAttribArray;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniform4f;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glGenVertexArrays;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject.BYTES_PER_FLOAT;

public class Text
{
    private static final int VERTICES_PER_GLPYH = 6;
    private static final int NUM_ELEMENTS_PER_VERTEX = 4; // xy and st

    private Font font;
    private String textString;

    private int vao;
    private int vbo;

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

    private ArrayList<Square> squares;

    private TextShader textShader;

//    private Square square;
 //   private FreeTypeCharacter freeTypeCharacter;

    public Text(Font font, String textString)
    {
        this.font = font;
        textShader = new TextShader();

        position = new Point2D(0.0f, 0.0f);
        scale = 1.0f;

        camera = new Camera2D();
        squares = new ArrayList<>();
        setText(textString);

//        // Create VAO
//        int[] tempVAO = new int[1];
//        glGenVertexArrays(1, tempVAO, 0);
//        vao = tempVAO[0];
//
//        // Create VBO
//        int[] tempVBO = new int[1];
//        glGenBuffers(1, tempVBO, 0);
//        vbo = tempVBO[0];
//
//        glBindVertexArray(vbo);
//        glBindBuffer(GL_ARRAY_BUFFER, vbo);
//        glBufferData(GL_ARRAY_BUFFER,
//                BYTES_PER_FLOAT * VERTICES_PER_GLPYH * NUM_ELEMENTS_PER_VERTEX,
//                null,
//                GL_DYNAMIC_DRAW);
//        glEnableVertexAttribArray(0);
//        glVertexAttribPointer(0,
//                NUM_ELEMENTS_PER_VERTEX,
//                GL_FLOAT,
//                false,
//                NUM_ELEMENTS_PER_VERTEX * BYTES_PER_FLOAT,
//                0);
//        glBindBuffer(GL_ARRAY_BUFFER, 0);
//        glBindVertexArray(0);


    }

    public void setText(String text)
    {
        this.textString = text;
        constructText(position.x, position.y, scale);
    }

    private void constructText(float x, float y, float scale)
    {
        for(int i = 0; i < textString.length(); i++)
        {
            FreeTypeCharacter freeTypeCharacter = font.getCharacter(textString.charAt(i));
            Square square = new Square(new Material(freeTypeCharacter.texture));

            float xpos = x + freeTypeCharacter.bearingX * scale;
            float ypos = y - (freeTypeCharacter.height - freeTypeCharacter.bearingY) * scale;
            float width = freeTypeCharacter.width * scale;
            float height = freeTypeCharacter.height * scale;

            square.setPosition(new Point2D(xpos, ypos));
            square.useCustomShader(textShader);
            square.setColour(Colour.RED);
            square.setScale(new Scale2D(width, height));
            squares.add(square);
        //    theX += freeTypeCharacter.width;
            x += (freeTypeCharacter.advance >> 6) * scale;

//            Square square = new Square(new Material(ch.texture));
//            square.setPosition(x + ch.width * scale, y - (ch.height - ch.bearingY) * scale);
//            square.setScale(ch.width * scale, ch.height * scale);
//            square.setColour(Colour.BLACK);
//            square.setRotation(0.0f, 1.0f, 1.0f, 1.0f);
//            square.useCustomShader(textShader);
//            squares.add(square);
//            x += (ch.advance >> 6) * scale;
        }
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
