package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glDisableVertexAttribArray;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniform4fv;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glVertexAttribPointer;

/**
 * Render object is a base class for any graphical object. It handles an objects shader (based on
 * its material if a custom one isn't allocated), vertex data upload to the graphics memory and
 * drawing of objects.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */

public class RenderObject
{
    // Tag used in logging output
    private static final String TAG = "RenderObject";

    // ability to change for dimensions?
    static public final int BYTES_PER_FLOAT = 4;

    private int elementsPerPosition;
    private int positionStrideBytes;
    private int positionDataOffset;
    private int elementsPerTexel;
    private int texelStrideBytes;
    private int texelDataOffset;
    private int elementsPerColour;
    private int colourStrideBytes;
    private int colourDataOffset;
    private int totalStrideBytes;

    // Float buffer that holds all the triangle co-ordinate data
    private FloatBuffer VERTEX_BUFFER;

    private float scaleX;
    private float scaleY;
    private float scaleZ;
    private Point3D position;
    private float angle;
    private float rotationX;
    private float rotationY;
    private float rotationZ;

    float[] modelMatrix = new float[16];

    private Material material;

    private Shader shader;
    private boolean hasCustomShader;

    final int VERTEX_COUNT;

    private Colour colour;
    private float[] colourData;

    private final AttributeOrder_t ATTRIBUTE_ORDER;
    private final PositionDimensions_t POSITION_DIMENSIONS;
    private final TexelDimensions_t TEXEL_DIMENSIONS;
    private final ColourDimensions_t COLOUR_DIMENSIONS;

    public enum AttributeOrder_t
    {
        POSITION,
        POSITION_THEN_TEXEL,
        POSITION_THEN_COLOUR,
        POSITION_THEN_TEXEL_THEN_COLOUR,
        POSITION_THEN_COLOUR_THEN_TEXEL
    }

    public enum PositionDimensions_t
    {
        XYZ,
        XY
    }

    public enum TexelDimensions_t
    {
        ST,
        NONE
    }

    public enum ColourDimensions_t
    {
        RGB,
        RGBA,
        NONE
    }

    protected RenderObject(float[] vertexData,
                           PositionDimensions_t positionDimensions,
                           TexelDimensions_t texelDimensions,
                           ColourDimensions_t colourDimensions,
                           AttributeOrder_t attributeOrder,
                           Material material)
    {
        this.POSITION_DIMENSIONS = positionDimensions;
        this.TEXEL_DIMENSIONS = texelDimensions;
        this.COLOUR_DIMENSIONS = colourDimensions;
        this.ATTRIBUTE_ORDER = attributeOrder;
        this.rotationX = 0.0f;
        this.rotationY = 0.0f;
        this.rotationZ = 0.0f;
        this.angle = 0.0f;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.scaleZ = 1.0f;
        this.position = new Point3D(0.0f, 0.0f, 0.0f);

        // Figure out the stride
        resolveStride(POSITION_DIMENSIONS,
                TEXEL_DIMENSIONS,
                COLOUR_DIMENSIONS);

        resolveAttributeOffsets(ATTRIBUTE_ORDER);

        // Initialise a vertex byte buffer for the shape float array
        final ByteBuffer VERTICES_BYTE_BUFFER = ByteBuffer.allocateDirect(
                vertexData.length * BYTES_PER_FLOAT);

        // Use the devices hardware's native byte order
        VERTICES_BYTE_BUFFER.order(ByteOrder.nativeOrder());

        // Create a Float buffer from the ByteBuffer
        VERTEX_BUFFER = VERTICES_BYTE_BUFFER.asFloatBuffer();

        // Add the array of floats to the buffer
        VERTEX_BUFFER.put(vertexData);

        // Set buffer to read the first co-ordinate
        VERTEX_BUFFER.position(0);

        // Calculate the number of vertices in the data
        VERTEX_COUNT = vertexData.length /
                (elementsPerPosition +
                        elementsPerTexel +
                        elementsPerColour);

        setMaterial(material);
        setColour(Colour.LIGHT_GREY);

        hasCustomShader = false;
    }

    protected RenderObject(float[] vertexData,
                           PositionDimensions_t positionDimensions,
                           TexelDimensions_t texelDimensions,
                           ColourDimensions_t colourDimensions,
                           AttributeOrder_t attributeOrder)
    {
        this(vertexData,
                positionDimensions,
                texelDimensions,
                colourDimensions,
                attributeOrder,
                Material.DEFAULT_MATERIAL);
    }

    protected RenderObject(float[] vertexData,
                           PositionDimensions_t positionDimensions)
    {
        this(vertexData,
                positionDimensions,
                TexelDimensions_t.NONE,
                ColourDimensions_t.NONE,
                AttributeOrder_t.POSITION,
                Material.DEFAULT_MATERIAL);
    }

    protected RenderObject(float[] vertexData,
                           PositionDimensions_t positionDimensions,
                           Material material)
    {
        this(vertexData,
                positionDimensions,
                TexelDimensions_t.NONE,
                ColourDimensions_t.NONE,
                AttributeOrder_t.POSITION,
                material);
    }

    protected RenderObject(float[] vertexData,
                           PositionDimensions_t positionDimensions,
                           TexelDimensions_t texelDimensions,
                           AttributeOrder_t attributeOrder)
    {
        this(vertexData,
                positionDimensions,
                texelDimensions,
                ColourDimensions_t.NONE,
                attributeOrder,
                Material.DEFAULT_MATERIAL);
    }

    protected RenderObject(float[] vertexData,
                           PositionDimensions_t positionDimensions,
                           TexelDimensions_t texelDimensions,
                           AttributeOrder_t attributeOrder,
                           Material material)
    {
        this(vertexData,
                positionDimensions,
                texelDimensions,
                ColourDimensions_t.NONE,
                attributeOrder,
                material);
    }

    protected RenderObject(float[] vertexData,
                           PositionDimensions_t positionDimensions,
                           ColourDimensions_t colourDimensions,
                           AttributeOrder_t attributeOrder)
    {
        this(vertexData,
                positionDimensions,
                TexelDimensions_t.NONE,
                colourDimensions,
                attributeOrder,
                Material.DEFAULT_MATERIAL);
    }

    protected RenderObject(float[] vertexData,
                           PositionDimensions_t positionDimensions,
                           ColourDimensions_t colourDimensions,
                           AttributeOrder_t attributeOrder,
                           Material material)
    {
        this(vertexData,
                positionDimensions,
                TexelDimensions_t.NONE,
                colourDimensions,
                attributeOrder,
                material);
    }

    public void setMaterial(Material material)
    {
        this.material = material;
        updateShader();
    }

    public void setColour(Colour colour)
    {
        this.colour = colour;

        colourData = new float[]{
                colour.getRed(),
                colour.getGreen(),
                colour.getBlue(),
                colour.getAlpha()
        };
    }

    private void updateShader()
    {
        if(!hasCustomShader)
        {
            // Determine the best shader to used depending on the material
            if(material.isLightingEnabled() && material.hasTexture() && material.hasNormalMap())
            {
                // Use lighting, texture/normal map supporting shader
            }
            else if(material.isLightingEnabled() && material.hasTexture())
            {
                // Use lighting, texture supporting shader
            }
            else if(material.isLightingEnabled())
            {
                // Use lighting supporting shader
            }
            else
            {
                // Just use a colour shader
                shader = new UniformColourShader();
            }
        }
    }

    public void useCustomShader(Shader customShader)
    {
        if(customShader != null)
        {
            hasCustomShader = true;
            shader = customShader;
        }
        else
        {
            Logger.error(TAG, "Custom shader supplied is null");
        }

    }

    public Material getMaterial()
    {
        return this.material;
    }

    public void setScale(float x, float y, float z)
    {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public void setPosition(Point3D position)
    {
        this.position = position;
    }

    public void setRotation(float angle, float x, float y, float z)
    {
        this.angle = angle;
        this.rotationX = x;
        this.rotationY = y;
        this.rotationZ = z;
    }

    private void updateModelMatrix()
    {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, scaleZ);
        Matrix.translateM(modelMatrix, 0, position.x, position.y, position.z);
        if(angle != 0.0f)
        {
            Matrix.rotateM(modelMatrix, 0, angle, rotationX, rotationY, rotationZ);
        }
    }

    private void attribPositionData()
    {
        VERTEX_BUFFER.position(positionDataOffset);
        glEnableVertexAttribArray(shader.getPositionAttributeHandle());
        glVertexAttribPointer(shader.getPositionAttributeHandle(),
                elementsPerPosition,
                GL_FLOAT,
                true,
                totalStrideBytes,
                VERTEX_BUFFER);
        VERTEX_BUFFER.position(0);
    }

    private void attribTexelData()
    {
        if(TEXEL_DIMENSIONS != TexelDimensions_t.NONE)
        {
            // Enable attrib texture data
            VERTEX_BUFFER.position(texelDataOffset);
            glEnableVertexAttribArray(shader.getTextureAttributeHandle());
            glVertexAttribPointer(shader.getTextureAttributeHandle(),
                    elementsPerTexel,
                    GL_FLOAT,
                    true,
                    totalStrideBytes,
                    VERTEX_BUFFER);
            VERTEX_BUFFER.position(0);
        }
    }

    private void attribColourData()
    {
        if(COLOUR_DIMENSIONS != ColourDimensions_t.NONE)
        {
            // Enable attrib colour data
            VERTEX_BUFFER.position(colourDataOffset);
            glEnableVertexAttribArray(shader.getColourAttributeHandle());
            glVertexAttribPointer(shader.getColourAttributeHandle(),
                    elementsPerColour,
                    GL_FLOAT,
                    true,
                    totalStrideBytes,
                    VERTEX_BUFFER);
            VERTEX_BUFFER.position(0);
        }
    }

    private void enableAttribs()
    {
        switch (ATTRIBUTE_ORDER)
        {
            case POSITION:
                attribPositionData();
                break;
            case POSITION_THEN_TEXEL:
                attribPositionData();
                attribTexelData();
                break;
            case POSITION_THEN_COLOUR:
                attribPositionData();
                attribColourData();
                break;
            case POSITION_THEN_TEXEL_THEN_COLOUR:
                attribPositionData();
                attribTexelData();
                attribColourData();
                break;
            case POSITION_THEN_COLOUR_THEN_TEXEL:
                attribPositionData();
                attribColourData();
                attribTexelData();
                break;
        }
    }

    private void disableAttribs()
    {
        switch (ATTRIBUTE_ORDER)
        {
            case POSITION:
                glDisableVertexAttribArray(shader.getPositionAttributeHandle());
                break;
            case POSITION_THEN_TEXEL:
                glDisableVertexAttribArray(shader.getPositionAttributeHandle());
                glDisableVertexAttribArray(shader.getTextureAttributeHandle());
                break;
            case POSITION_THEN_COLOUR:
                glDisableVertexAttribArray(shader.getPositionAttributeHandle());
                glDisableVertexAttribArray(shader.getColourAttributeHandle());
                break;
            case POSITION_THEN_TEXEL_THEN_COLOUR:
                glDisableVertexAttribArray(shader.getPositionAttributeHandle());
                glDisableVertexAttribArray(shader.getTextureAttributeHandle());
                glDisableVertexAttribArray(shader.getColourAttributeHandle());
                break;
            case POSITION_THEN_COLOUR_THEN_TEXEL:
                glDisableVertexAttribArray(shader.getPositionAttributeHandle());
                glDisableVertexAttribArray(shader.getColourAttributeHandle());
                glDisableVertexAttribArray(shader.getTextureAttributeHandle());
                break;
        }
    }

    public void draw(Camera3D camera)
    {
        updateModelMatrix();

        shader.enableIt();

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, modelMatrix, 0);

        float[] modelViewProjectionMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, camera.getPerspectiveMatrix(), 0, modelViewMatrix, 0);

        glUniformMatrix4fv(shader.getMatrixUniformHandle(), 1, false, modelViewProjectionMatrix, 0);

        if(shader.getColourUniformHandle() != -1)
        {
            glUniform4fv(shader.getColourUniformHandle(), 1, colourData, 0);
        }

        if(shader.getTextureUniformHandle() != -1 && material.hasTexture())
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
            glUniform1i(shader.getTextureUniformHandle(), 0);
        }

        enableAttribs();
        glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);
        disableAttribs();

        shader.disableIt();
    }

    private int resolvePositionDimensions(PositionDimensions_t positionDimensions)
    {
        switch (positionDimensions)
        {
            case XYZ:
                return 3;
            case XY:
                return 2;
            default:
                return 3;
        }
    }

    private int resolveTexelDimensions(TexelDimensions_t texelDimensions)
    {
        switch (texelDimensions)
        {
            case ST:
                return 2;
            case NONE:
                return 0;
            default:
                return 2;
        }
    }

    private int resolveColourDimensions(ColourDimensions_t colourDimensions)
    {
        switch (colourDimensions)
        {
            case RGB:
                return 3;
            case RGBA:
                return 4;
            case NONE:
                return 0;
            default:
                return 3;
        }
    }

    private void resolveStride(PositionDimensions_t positionDimensions,
                               TexelDimensions_t texelDimensions,
                               ColourDimensions_t colourDimensions)
    {
        elementsPerPosition = resolvePositionDimensions(positionDimensions);
        elementsPerTexel = resolveTexelDimensions(texelDimensions);
        elementsPerColour = resolveColourDimensions(colourDimensions);
        positionStrideBytes = elementsPerPosition * BYTES_PER_FLOAT;
        texelStrideBytes = elementsPerTexel * BYTES_PER_FLOAT;
        colourStrideBytes = elementsPerColour * BYTES_PER_FLOAT;
        totalStrideBytes = positionStrideBytes +
                texelStrideBytes +
                colourStrideBytes;
    }

    private void resolveAttributeOffsets(AttributeOrder_t attributeOrder)
    {
        switch (attributeOrder)
        {
            case POSITION:
                positionDataOffset = 0;
                break;
            case POSITION_THEN_TEXEL:
                positionDataOffset = 0;
                texelDataOffset = elementsPerPosition;
                break;
            case POSITION_THEN_COLOUR:
                positionDataOffset = 0;
                colourDataOffset = elementsPerPosition;
                break;
            case POSITION_THEN_TEXEL_THEN_COLOUR:
                positionDataOffset = 0;
                texelDataOffset = elementsPerPosition;
                colourDataOffset = texelDataOffset + elementsPerTexel;
                break;
            case POSITION_THEN_COLOUR_THEN_TEXEL:
                positionDataOffset = 0;
                colourDataOffset = elementsPerPosition;
                texelDataOffset = colourDataOffset + elementsPerColour;
                break;
        }
    }
}
