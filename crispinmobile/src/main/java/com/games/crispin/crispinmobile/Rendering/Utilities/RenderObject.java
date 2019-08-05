package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Rotation2D;
import com.games.crispin.crispinmobile.Geometry.Rotation3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;
import com.games.crispin.crispinmobile.Rendering.Data.RenderObjectDataFormat;
import com.games.crispin.crispinmobile.Rendering.Shaders.AttributeColourShader;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextureAttributeColourShader;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextureShader;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.glUniform2f;
import static android.opengl.GLES20.glUniform4f;
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
    private int elementsPerNormal;
    private int normalStrideBytes;
    private int normalDataOffset;
    private int totalStrideBytes;

    private final RenderObjectDataFormat DATA_FORMAT;

    // Float buffer that holds all the triangle co-ordinate data
    private FloatBuffer VERTEX_BUFFER;

    private Scale3D scale;
    private Point3D position;
    private Rotation3D rotation;

    float[] modelMatrix = new float[16];

    protected Material material;

    protected Shader shader;
    private boolean hasCustomShader;

    final int VERTEX_COUNT;

    public RenderObject(float[] vertexData,
                        RenderObjectDataFormat renderObjectDataFormat,
                        Material material)
    {
        this.DATA_FORMAT = renderObjectDataFormat;
        this.scale = new Scale3D();
        this.position = new Point3D();
        this.rotation = new Rotation3D();

        // Figure out the stride
        resolveStride();
        resolveAttributeOffsets();

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
                        elementsPerColour +
                        elementsPerNormal);

        setMaterial(material);

        hasCustomShader = false;
    }

    public RenderObject(float[] vertexData,
                        RenderObjectDataFormat renderObjectDataFormat)
    {
        this(vertexData, renderObjectDataFormat, new Material());
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    protected void updateShader()
    {
        if(!hasCustomShader)
        {
            // Check that the object has all of the components required to render a texture
            final boolean SUPPORTS_TEXTURE = material.hasTexture() &&
                    DATA_FORMAT.supportsTexelData() &&
                    !material.isIgnoringTexelData();

            // Check if the object has all of the components required to render per vertex colour
            final boolean SUPPORTS_COLOUR_PER_ATTRIB =
                    DATA_FORMAT.supportsColourData() &&
                    !material.isIgnoringColourData();

//            // Determine the best shader to used depending on the material
//            if(material.isLightingEnabled() && material.hasTexture() && material.hasNormalMap())
//            {
//                // Use lighting, texture/normal map supporting shader
//            }
//            else if(material.isLightingEnabled() && material.hasTexture())
//            {
//                // Use lighting, texture supporting shader
//            }
//            else if(material.isLightingEnabled())
//            {
//                // Use lighting supporting shader
//            }
//            else

            if(SUPPORTS_COLOUR_PER_ATTRIB && SUPPORTS_TEXTURE)
            {
                // A colour attribute and texture shader
                shader = new TextureAttributeColourShader();
            }
            else if(SUPPORTS_TEXTURE)
            {
                // Just a texture shader
                shader = new TextureShader();
            }
            else if(SUPPORTS_COLOUR_PER_ATTRIB)
            {
                // Just a colour attribute shader
                shader = new AttributeColourShader();
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

    public void setScale(Scale2D scale)
    {
        this.scale.x = scale.x;
        this.scale.y = scale.y;
    }

    public void setScale(float w, float h)
    {
        this.scale.x = w;
        this.scale.y = h;
    }

    public void setScale(Scale3D scale)
    {
        this.scale = scale;
    }

    public void setScale(float w, float h, float l)
    {
        this.scale.x = w;
        this.scale.y = h;
        this.scale.z = l;
    }

    public void setPosition(Point2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public void setPosition(float x, float y)
    {
        this.position.x = x;
        this.position.y = y;
    }

    public void setPosition(Point3D position)
    {
        this.position = position;
    }

    public void setPosition(float x, float y, float z)
    {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public Point3D getPosition()
    {
        return position;
    }

    public void offset(float x, float y)
    {
        this.position.x += x;
        this.position.y += y;
    }

    public void offset(float x, float y, float z)
    {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    public void offset(Point2D point2D)
    {
        this.position.x += point2D.x;
        this.position.y += point2D.y;
    }

    public void offset(Point3D point3D)
    {
        this.position.x += point3D.x;
        this.position.y += point3D.y;
        this.position.z += point3D.z;
    }

    public void setRotation(Rotation3D rotation)
    {
        this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z)
    {
        setRotation(x, y);
        this.rotation.z = z;
    }

    public void setRotation(Rotation2D rotation)
    {
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
    }

    public void setRotation(float x, float y)
    {
        this.rotation.x = x;
        this.rotation.y = y;
    }

    public Rotation3D getRotation()
    {
        return this.rotation;
    }

    protected void updateModelMatrix()
    {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, position.x, position.y, position.z);
        Matrix.scaleM(modelMatrix, 0, scale.x, scale.y, scale.z);
        
        if(rotation.x != 0.0f)
        {
            Matrix.rotateM(modelMatrix, 0, rotation.x, 1.0f, 0.0f, 0.0f);
        }
        if(rotation.y != 0.0f)
        {
            Matrix.rotateM(modelMatrix, 0, rotation.y, 0.0f, 1.0f, 0.0f);
        }
        if(rotation.z != 0.0f)
        {
            Matrix.rotateM(modelMatrix, 0, rotation.z, 0.0f, 0.0f, 1.0f);
        }
    }

    // Enable set to true will enable the vertex attributes, disable will disable the vertex attrib
    private void handlePositionDataAttribute(boolean enable)
    {
        if(enable)
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
        else
        {
            glDisableVertexAttribArray(shader.getPositionAttributeHandle());
        }
    }

    private void handleTexelDataAttribute(boolean enable)
    {
        if(enable)
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
        else
        {
            glDisableVertexAttribArray(shader.getTextureAttributeHandle());
        }
    }

    private void handleColourDataAttribute(boolean enable)
    {
        if(enable)
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
        else
        {
            glDisableVertexAttribArray(shader.getColourAttributeHandle());
        }
    }

    // check if the data provided contains the data necessary for disable or enable an attribute
    protected void handleAttributes(boolean enable)
    {
        if(!material.isIgnoringPositionData())
        {
            handlePositionDataAttribute(enable);
        }

        if(!material.isIgnoringTexelData() && DATA_FORMAT.supportsTexelData())
        {
            handleTexelDataAttribute(enable);
        }

        if(!material.isIgnoringColourData() && DATA_FORMAT.supportsColourData())
        {
            handleColourDataAttribute(enable);
        }
    }

    public void draw(Camera2D camera)
    {
        if(shader == null)
        {
            updateShader();
        }

        updateModelMatrix();

        shader.enableIt();

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, camera.getOrthoMatrix(), 0, modelMatrix, 0);

        glUniformMatrix4fv(shader.getMatrixUniformHandle(), 1, false, modelViewMatrix, 0);

        if(shader.getColourUniformHandle() != -1)
        {
            glUniform4fv(shader.getColourUniformHandle(), 1, material.getColourData(), 0);
        }

        if(shader.getTextureUniformHandle() != -1 && material.hasTexture())
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
            glUniform1i(shader.getTextureUniformHandle(), 0);

            if(shader.getUvMultiplierUniformHandlea() != -1)
            {
                glUniform2f(shader.getUvMultiplierUniformHandlea(),
                        material.getUvMultiplier().x,
                        material.getUvMultiplier().y);
            }
        }

        handleAttributes(true);
        glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);
        handleAttributes(false);

        glBindTexture(GL_TEXTURE_2D, 0);

        shader.disableIt();
    }

    public void draw(Camera3D camera)
    {
        if(shader == null)
        {
            updateShader();
        }

        updateModelMatrix();

        shader.enableIt();

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, modelMatrix, 0);

        float[] modelViewProjectionMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, camera.getPerspectiveMatrix(), 0, modelViewMatrix, 0);

        glUniformMatrix4fv(shader.getMatrixUniformHandle(), 1, false, modelViewProjectionMatrix, 0);

        if(shader.getColourUniformHandle() != -1)
        {
            glUniform4f(shader.getColourUniformHandle(),
                    material.getColour().getRed(),
                    material.getColour().getGreen(),
                    material.getColour().getBlue(),
                    material.getColour().getAlpha());
        }

        if(shader.getTextureUniformHandle() != -1 && material.hasTexture())
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
            glUniform1i(shader.getTextureUniformHandle(), 0);

            if(shader.getUvMultiplierUniformHandlea() != -1)
            {
                glUniform2f(shader.getUvMultiplierUniformHandlea(),
                        material.getUvMultiplier().x,
                        material.getUvMultiplier().y);
            }
        }

        handleAttributes(true);
        glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);
        handleAttributes(false);

        glBindTexture(GL_TEXTURE_2D, 0);

        shader.disableIt();
    }

    private void resolveStride()
    {
        elementsPerPosition = DATA_FORMAT.getNumberPositionDimensions();
        elementsPerTexel = DATA_FORMAT.getNumberTexelDimensions();
        elementsPerColour = DATA_FORMAT.getNumberColourDimensions();
        elementsPerNormal = DATA_FORMAT.getNumberNormalDimensions();
        positionStrideBytes = elementsPerPosition * DATA_FORMAT.getNumberVerticesPerGroup() * BYTES_PER_FLOAT;
        texelStrideBytes = elementsPerTexel * DATA_FORMAT.getNumberVerticesPerGroup() * BYTES_PER_FLOAT;
        colourStrideBytes = elementsPerColour * DATA_FORMAT.getNumberVerticesPerGroup() * BYTES_PER_FLOAT;
        normalStrideBytes = elementsPerNormal * DATA_FORMAT.getNumberVerticesPerGroup() * BYTES_PER_FLOAT;
        totalStrideBytes = positionStrideBytes +
                texelStrideBytes +
                colourStrideBytes +
                normalStrideBytes;
    }

    private void resolveAttributeOffsets()
    {
        final int TOTAL_NUMBER_POSITION_ELEMENTS = elementsPerPosition * DATA_FORMAT.getNumberVerticesPerGroup();
        final int TOTAL_NUMBER_TEXEL_ELEMENTS = elementsPerTexel * DATA_FORMAT.getNumberVerticesPerGroup();
        final int TOTAL_NUMBER_COLOUR_ELEMENTS = elementsPerColour * DATA_FORMAT.getNumberVerticesPerGroup();
        final int TOTAL_NUMBER_NORMAL_ELEMENTS = elementsPerNormal * DATA_FORMAT.getNumberVerticesPerGroup();

        switch (DATA_FORMAT.getAttributeOrder())
        {
            case POSITION:
                positionDataOffset = 0;
                break;
            case POSITION_THEN_TEXEL:
                positionDataOffset = 0;
                texelDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                break;
            case POSITION_THEN_COLOUR:
                positionDataOffset = 0;
                colourDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                break;
            case POSITION_THEN_NORMAL:
                positionDataOffset = 0;
                normalDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                break;
            case POSITION_THEN_TEXEL_THEN_COLOUR:
                positionDataOffset = 0;
                texelDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                colourDataOffset = texelDataOffset + TOTAL_NUMBER_TEXEL_ELEMENTS;
                break;
            case POSITION_THEN_COLOUR_THEN_TEXEL:
                positionDataOffset = 0;
                colourDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                texelDataOffset = colourDataOffset + TOTAL_NUMBER_COLOUR_ELEMENTS;
                break;
            case POSITION_THEN_TEXEL_THEN_NORMAL:
                positionDataOffset = 0;
                texelDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                normalDataOffset = texelDataOffset + TOTAL_NUMBER_TEXEL_ELEMENTS;
                break;
            case POSITION_THEN_NORMAL_THEN_TEXEL:
                positionDataOffset = 0;
                normalDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                texelDataOffset = normalDataOffset + TOTAL_NUMBER_NORMAL_ELEMENTS;
                break;
            case POSITION_THEN_COLOUR_THEN_NORMAL:
                positionDataOffset = 0;
                colourDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                normalDataOffset = colourDataOffset + TOTAL_NUMBER_COLOUR_ELEMENTS;
                break;
            case POSITION_THEN_NORMAL_THEN_COLOUR:
                positionDataOffset = 0;
                normalDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                colourDataOffset = normalDataOffset + TOTAL_NUMBER_NORMAL_ELEMENTS;
                break;
            case POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL:
                positionDataOffset = 0;
                texelDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                colourDataOffset = texelDataOffset + TOTAL_NUMBER_TEXEL_ELEMENTS;
                normalDataOffset = colourDataOffset + TOTAL_NUMBER_COLOUR_ELEMENTS;
                break;
            case POSITION_THEN_COLOUR_THEN_TEXEL_THEN_NORMAL:
                positionDataOffset = 0;
                colourDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                texelDataOffset = colourDataOffset + TOTAL_NUMBER_COLOUR_ELEMENTS;
                normalDataOffset = texelDataOffset + TOTAL_NUMBER_TEXEL_ELEMENTS;
                break;
            case POSITION_THEN_TEXEL_THEN_NORMAL_THEN_COLOUR:
                positionDataOffset = 0;
                texelDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                normalDataOffset = texelDataOffset + TOTAL_NUMBER_TEXEL_ELEMENTS;
                colourDataOffset = normalDataOffset + TOTAL_NUMBER_NORMAL_ELEMENTS;
                break;
            case POSITION_THEN_COLOUR_THEN_NORMAL_THEN_TEXEL:
                positionDataOffset = 0;
                colourDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                normalDataOffset = colourDataOffset + TOTAL_NUMBER_COLOUR_ELEMENTS;
                texelDataOffset = normalDataOffset + TOTAL_NUMBER_NORMAL_ELEMENTS;
                break;
            case POSITION_THEN_NORMAL_THEN_TEXEL_THEN_COLOUR:
                positionDataOffset = 0;
                normalDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                texelDataOffset = normalDataOffset + TOTAL_NUMBER_NORMAL_ELEMENTS;
                colourDataOffset = texelDataOffset + TOTAL_NUMBER_TEXEL_ELEMENTS;
                break;
            case POSITION_THEN_NORMAL_THEN_COLOUR_THEN_TEXEL:
                positionDataOffset = 0;
                normalDataOffset = TOTAL_NUMBER_POSITION_ELEMENTS;
                colourDataOffset = normalDataOffset + TOTAL_NUMBER_NORMAL_ELEMENTS;
                texelDataOffset = colourDataOffset + TOTAL_NUMBER_COLOUR_ELEMENTS;
                break;
        }
    }
}
