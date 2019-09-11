package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;
import android.util.Pair;

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

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
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
import static android.opengl.GLES32.GL_QUADS;

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
    public static final int BYTES_PER_FLOAT = 4;

    // The number of vertices in the model data
    private final int VERTEX_COUNT;

    // The number of elements that are in the position data
    private int elementsPerPosition;

    // The position index of the position data in the vertex data buffer
    private int positionDataOffset;

    // The number of elements that are in the texel data
    private int elementsPerTexel;

    // The position index of the texel data in the vertex data buffer
    private int texelDataOffset;

    // The number of elements that are in the colour data
    private int elementsPerColour;

    // The position index of the colour data in the vertex data buffer
    private int colourDataOffset;

    // The number of elements that are in the direction data
    private int elementsPerNormal;

    // The position index of the direction data in the vertex data buffer
    private int normalDataOffset;

    // The stride between each set of data (only if the data format is ungrouped)
    private int totalStrideBytes;

    // Float buffer that holds all the triangle co-ordinate data
    private FloatBuffer vertexBuffer;

    // The format of the render data
    private final RenderObjectDataFormat DATA_FORMAT;

    // If the model has a custom shader
    private boolean hasCustomShader;

    // Model matrix containing positional data
    private float[] modelMatrix = new float[16];

    // Scale of the object
    private Scale3D scale;

    // Position of the object
    private Point3D position;

    // Objects rotation
    private Rotation3D rotation;

    // Material to apply to the object
    protected Material material;

    // Shader applied to the object
    protected Shader shader;

    // Method of rendering the object. Points renders one vertex at a time and places a point. Lines
    // renders two vertex at a time and render a line between them. Triangles renders three vertices
    // at a time and uses the fragment shader to fill the middle.
    public enum RenderMethod
    {
        POINTS,
        LINES,
        TRIANGLES,
        NONE
    }

    /**
     * Create an object with vertex data comprised of multiple buffers containing different forms of
     * vertex data. For the buffers that you are not providing data for, put <code>null</code>.
     *
     * @param positionBuffer            Float buffer containing the position data
     * @param texelBuffer               Float buffer containing the texel data, or <code>null</code>
     *                                  if no texel data is being provided
     * @param colourBuffer              Float buffer containing the colour data, or
     *                                  <code>null</code> if no colour data is being provided
     * @param normalBuffer              Float buffer containing the normal data, or
     *                                  <code>null</code> if no normal data is being provided
     * @param renderObjectDataFormat    The format of the vertex data, such as what data is provided
     *                                  and in what order.
     * @param material                  The material to apply to the object
     * @since   1.0
     */
    public RenderObject(float[] positionBuffer,
                        float[] texelBuffer,
                        float[] colourBuffer,
                        float[] normalBuffer,
                        RenderObjectDataFormat renderObjectDataFormat,
                        Material material)
    {
        this.DATA_FORMAT = renderObjectDataFormat;
        this.scale = new Scale3D();
        this.position = new Point3D();
        this.rotation = new Rotation3D();
        this.totalStrideBytes = 0;

        // Figure out the stride
        resolveStride();
        resolveAttributeOffsets();


        final int POS_BUFFER_LENGTH = positionBuffer == null ? 0 : positionBuffer.length;
        final int COL_BUFFER_LENGTH = texelBuffer == null ? 0 : texelBuffer.length;
        final int TEX_BUFFER_LENGTH = colourBuffer == null ? 0 : colourBuffer.length;
        final int NOR_BUFFER_LENGTH = normalBuffer == null ? 0 : normalBuffer.length;

        // Vertex data length
        final int VERTEX_DATA_LENGTH = POS_BUFFER_LENGTH +
                COL_BUFFER_LENGTH +
                TEX_BUFFER_LENGTH +
                NOR_BUFFER_LENGTH;

        float[] vertexData = new float[VERTEX_DATA_LENGTH];

        if(positionBuffer != null)
        {
            System.arraycopy(positionBuffer,
                    0,
                    vertexData,
                    0,
                    positionBuffer.length);
        }

        if(texelBuffer != null)
        {
            System.arraycopy(texelBuffer,
                    0,
                    vertexData,
                    POS_BUFFER_LENGTH,
                    texelBuffer.length);
        }

        if(colourBuffer != null)
        {
            System.arraycopy(colourBuffer,
                    0, vertexData,
                    POS_BUFFER_LENGTH + COL_BUFFER_LENGTH,
                    colourBuffer.length);
        }

        if(normalBuffer != null)
        {
            System.arraycopy(normalBuffer,
                    0,
                    vertexData,
                    POS_BUFFER_LENGTH + COL_BUFFER_LENGTH +
                            TEX_BUFFER_LENGTH + NOR_BUFFER_LENGTH,
                    normalBuffer.length);
        }

        // Initialise a vertex byte buffer for the shape float array
        final ByteBuffer VERTICES_BYTE_BUFFER = ByteBuffer.allocateDirect(
                vertexData.length * BYTES_PER_FLOAT);

        // Use the devices hardware's native byte order
        VERTICES_BYTE_BUFFER.order(ByteOrder.nativeOrder());

        // Create a Float buffer from the ByteBuffer
        vertexBuffer = VERTICES_BYTE_BUFFER.asFloatBuffer();

        // Add the array of floats to the buffer
        vertexBuffer.put(vertexData);

        // Set buffer to read the first co-ordinate
        vertexBuffer.position(0);

        // Calculate the number of vertices in the data
        VERTEX_COUNT = vertexData.length /
                (elementsPerPosition +
                        elementsPerTexel +
                        elementsPerColour +
                        elementsPerNormal);

        setMaterial(material);

        hasCustomShader = false;
    }

    /**
     * Create an object with vertex data comprised of multiple buffers containing different forms of
     * vertex data. For the buffers that you are not providing data for, put <code>null</code>. A
     * default material will be applied.
     *
     * @param positionBuffer            Float buffer containing the position data
     * @param texelBuffer               Float buffer containing the texel data, or <code>null</code>
     *                                  if no texel data is being provided
     * @param colourBuffer              Float buffer containing the colour data, or
     *                                  <code>null</code> if no colour data is being provided
     * @param normalBuffer              Float buffer containing the normal data, or
     *                                  <code>null</code> if no normal data is being provided
     * @param renderObjectDataFormat    The format of the vertex data, such as what data is provided
     *                                  and in what order
     * @since   1.0
     */
    public RenderObject(float[] positionBuffer,
                        float[] texelBuffer,
                        float[] colourBuffer,
                        float[] normalBuffer,
                        RenderObjectDataFormat renderObjectDataFormat)
    {
        this(positionBuffer,
                texelBuffer,
                colourBuffer,
                normalBuffer,
                renderObjectDataFormat,
                new Material());
    }

    /**
     * Create an object using a single vertex data buffer. The format of the data must be specified
     * in order for the data to be correctly interpreted. Data stride and attribute offsets are
     * calculated based on the format.
     *
     * @param vertexData                Float buffer containing the vertex data
     * @param renderObjectDataFormat    The format of the vertex data, such as what data is provided
     *                                  and in what order
     * @param material                  Material to apply to the object
     * @since   1.0
     */
    public RenderObject(float[] vertexData,
                        RenderObjectDataFormat renderObjectDataFormat,
                        Material material)
    {
        this.DATA_FORMAT = renderObjectDataFormat;
        this.scale = new Scale3D();
        this.position = new Point3D();
        this.rotation = new Rotation3D();
        this.totalStrideBytes = 0;

        // Figure out the stride
        resolveStride();
        resolveAttributeOffsets();

        // Initialise a vertex byte buffer for the shape float array
        final ByteBuffer VERTICES_BYTE_BUFFER = ByteBuffer.allocateDirect(
                vertexData.length * BYTES_PER_FLOAT);

        // Use the devices hardware's native byte order
        VERTICES_BYTE_BUFFER.order(ByteOrder.nativeOrder());

        // Create a Float buffer from the ByteBuffer
        vertexBuffer = VERTICES_BYTE_BUFFER.asFloatBuffer();

        // Add the array of floats to the buffer
        vertexBuffer.put(vertexData);

        // Set buffer to read the first co-ordinate
        vertexBuffer.position(0);

        // Calculate the number of vertices in the data
        VERTEX_COUNT = vertexData.length /
                (elementsPerPosition +
                        elementsPerTexel +
                        elementsPerColour +
                        elementsPerNormal);

        setMaterial(material);

        hasCustomShader = false;
    }

    /**
     * Create an object using a single vertex data buffer. The format of the data must be specified
     * in order for the data to be correctly interpreted. Data stride and attribute offsets are
     * calculated based on the format. A default material will be applied to the object as one has
     * not been provided.
     *
     * @param vertexData                Float buffer containing the vertex data
     * @param renderObjectDataFormat    The format of the vertex data, such as what data is provided
     *                                  and in what order
     * @since 1.0
     */
    public RenderObject(float[] vertexData,
                        RenderObjectDataFormat renderObjectDataFormat)
    {
        this(vertexData, renderObjectDataFormat, new Material());
    }

    /**
     * Set the material. Materials can contain multiple pieces of information such as texture and
     * colour. Your model must contain texel data in order to support material textures.
     *
     * @param material  The material to apply to the render object
     * @since 1.0
     */
    public void setMaterial(Material material)
    {
        this.material = material;
    }

    /**
     * Get the material applied to the render object
     *
     * @return The material attached to the render object
     * @since 1.0
     */
    public Material getMaterial()
    {
        return this.material;
    }

    /**
     * Set the position
     *
     * @param position  The new position
     * @since 1.0
     */
    public void setPosition(Point3D position)
    {
        this.position = position;
    }

    /**
     * Set the position
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @param z The new z-coordinate
     * @since 1.0
     */
    public void setPosition(float x,
                            float y,
                            float z)
    {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    /**
     * Set the position
     *
     * @param position  The new position
     * @since 1.0
     */
    public void setPosition(Point2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    /**
     * Set the position
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @since 1.0
     */
    public void setPosition(float x, float y)
    {
        this.position.x = x;
        this.position.y = y;
    }

    /**
     * Get the position
     *
     * @return The position of the object
     * @since 1.0
     */
    public Point3D getPosition()
    {
        return position;
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object
     * @since 1.0
     */
    public void setScale(Scale3D scale)
    {
        this.scale = scale;
    }

    /**
     * Set the scale
     *
     * @param w Multiplier for the x-axis
     * @param h Multiplier for the y-axis
     * @param l Multiplier for the z-axis
     * @since 1.0
     */
    public void setScale(float w,
                         float h,
                         float l)
    {
        this.scale.x = w;
        this.scale.y = h;
        this.scale.z = l;
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object
     * @since 1.0
     */
    public void setScale(Scale2D scale)
    {
        this.scale.x = scale.x;
        this.scale.y = scale.y;
    }

    /**
     * Set the scale
     *
     * @param w Multiplier for the x-axis
     * @param h Multiplier for the y-axis
     * @since 1.0
     */
    public void setScale(float w, float h)
    {
        this.scale.x = w;
        this.scale.y = h;
    }

    /**
     * Translate the render objects position
     *
     * @param point The point to translate by
     * @since 1.0
     */
    public void translate(Point3D point)
    {
        this.position.translate(point);
    }

    /**
     * Translate the render objects position
     *
     * @param x The x-coordinate to translate by
     * @param y The y-coordinate to translate by
     * @param z The z-coordinate to translate by
     * @since 1.0
     */
    public void translate(float x,
                          float y,
                          float z)
    {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    /**
     * Translate the render objects position
     *
     * @param point The point to translate by
     * @since 1.0
     */
    public void translate(Point2D point)
    {
        this.position.translate(point);
    }

    /**
     * Translate the render objects position
     *
     * @param x The x-coordinate to translate by
     * @param y The y-coordinate to translate by
     * @since 1.0
     */
    public void translate(float x, float y)
    {
        this.position.x += x;
        this.position.y += y;
    }

    /**
     * Set the rotation of the object
     *
     * @param rotation  The new rotation for the object
     * @since 1.0
     */
    public void setRotation(Rotation3D rotation)
    {
        this.rotation = rotation;
    }

    /**
     * Set the rotation of the object
     *
     * @param x Degrees to rotate around the x-axis
     * @param y Degrees to rotate around the y-axis
     * @param z Degrees to rotate around the z-axis
     * @since 1.0
     */
    public void setRotation(float x,
                            float y,
                            float z)
    {
        setRotation(x, y);
        this.rotation.z = z;
    }

    /**
     * Set the rotation of the object
     *
     * @param rotation  The new rotation for the object
     * @since 1.0
     */
    public void setRotation(Rotation2D rotation)
    {
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
    }

    /**
     * Set the rotation of the object
     *
     * @param x Degrees to rotate around the x-axis
     * @param y Degrees to rotate around the y-axis
     * @since 1.0
     */
    public void setRotation(float x, float y)
    {
        this.rotation.x = x;
        this.rotation.y = y;
    }

    /**
     * Get the rotation of the object
     *
     * @return The rotation on the object
     * @since 1.0
     */
    public Rotation3D getRotation()
    {
        return this.rotation;
    }

    /**
     * Use a custom shader with the object. This means that the object will be rendered using your
     * own or a different built in GLSL program. You must make sure the shader you are setting
     * supports the data attributes and uniforms of the object or the object may not render
     * correctly or worse the program will crash.
     *
     * @since 1.0
     */
    public void useCustomShader(Shader customShader)
    {
        // Check if the shader being assigned has been initialised
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

    /**
     * Render the object to the display using a 2D camera object
     *
     * @param camera    2-Dimensional camera object. Render the object in the cameras view
     * @since 1.0
     */
    public void render(Camera2D camera)
    {
        if(shader == null)
        {
            updateShader();
        }

        updateModelMatrix();

        shader.enableIt();

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix,
                0,
                camera.getOrthoMatrix(),
                0,
                modelMatrix,
                0);

        glUniformMatrix4fv(shader.getMatrixUniformHandle(),
                1,
                false,
                modelViewMatrix,
                0);

        if(shader.getColourUniformHandle() != -1)
        {
            glUniform4fv(shader.getColourUniformHandle(),
                    1,
                    material.getColourData(),
                    0);
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

        switch (DATA_FORMAT.getRenderMethod())
        {
            case POINTS:
                glDrawArrays(GL_POINTS, 0, VERTEX_COUNT);
                break;
            case LINES:
                glDrawArrays(GL_LINES, 0, VERTEX_COUNT);
                break;
            case TRIANGLES:
                glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);
                break;
        }

        handleAttributes(false);

        glBindTexture(GL_TEXTURE_2D, 0);

        shader.disableIt();
    }

    /**
     * Render the object to the display using a 3D camera object
     *
     * @param camera    3-Dimensional camera object. Render the object in the cameras view
     * @since 1.0
     */
    public void render(Camera3D camera)
    {
        if(shader == null)
        {
            updateShader();
        }

        updateModelMatrix();

        shader.enableIt();

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix,
                0,
                camera.getViewMatrix(),
                0,
                modelMatrix,
                0);

        float[] modelViewProjectionMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjectionMatrix,
                0,
                camera.getPerspectiveMatrix(),
                0,
                modelViewMatrix,
                0);

        glUniformMatrix4fv(shader.getMatrixUniformHandle(),
                1,
                false,
                modelViewProjectionMatrix,
                0);

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

        switch (DATA_FORMAT.getRenderMethod())
        {
            case POINTS:
                glDrawArrays(GL_POINTS, 0, VERTEX_COUNT);
                break;
            case LINES:
                glDrawArrays(GL_LINES, 0, VERTEX_COUNT);
                break;
            case TRIANGLES:
                glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);
                break;
        }

        handleAttributes(false);

        glBindTexture(GL_TEXTURE_2D, 0);

        shader.disableIt();
    }

    /**
     * Update the model matrix. This can update the model matrix if a transformation property of the
     * object such as a position, scale or rotation has changed.
     *
     * @since 1.0
     */
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

    /**
     * Update the shader by automatically deciding what built in GLSL program to use depending on
     * the data that is present on the render object. For example, if the object has position data,
     * texel data and a texture applied, a texture shader will be assigned.
     *
     * @since 1.0
     */
    protected void updateShader()
    {
        if(!hasCustomShader)
        {
            boolean supportsTexture = false;
            boolean supportsColourPerAttrib = false;

            // Check that the object has all of the components required to render a texture
            supportsTexture = material.hasTexture() &&
                    DATA_FORMAT.supportsTexelData() &&
                    !material.isIgnoringTexelData();

            // Check if the object has all of the components required to render per vertex colour
            supportsColourPerAttrib =
                    DATA_FORMAT.supportsColourData() &&
                            !material.isIgnoringColourData();

//                // Determine the best shader to used depending on the material
//                if(material.isLightingEnabled() && material.hasTexture() && material.hasNormalMap())
//                {
//                    // Use lighting, texture/direction map supporting shader
//                }
//                else if(material.isLightingEnabled() && material.hasTexture())
//                {
//                    // Use lighting, texture supporting shader
//                }
//                else if(material.isLightingEnabled())
//                {
//                    // Use lighting supporting shader
//                }
//                else


            if(supportsColourPerAttrib && supportsTexture)
            {
                // A colour attribute and texture shader
                shader = new TextureAttributeColourShader();
            }
            else if(supportsTexture)
            {
                // Just a texture shader
                shader = new TextureShader();
            }
            else if(supportsColourPerAttrib)
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

    /**
     * Resolve the stride between the vertex data types. The stride is the number of bytes between
     * data of the same type in the vertex data.
     *
     * @since 1.0
     */
    private void resolveStride()
    {
        elementsPerPosition = DATA_FORMAT.getNumberPositionDimensions();
        elementsPerTexel = DATA_FORMAT.getNumberTexelDimensions();
        elementsPerColour = DATA_FORMAT.getNumberColourDimensions();
        elementsPerNormal = DATA_FORMAT.getNumberNormalDimensions();

        if(DATA_FORMAT.getNumberVerticesPerGroup() == RenderObjectDataFormat.UNGROUPED)
        {
            totalStrideBytes = (elementsPerPosition +
                    elementsPerTexel +
                    elementsPerColour +
                    elementsPerNormal) *
                    BYTES_PER_FLOAT;
        }
    }

    /**
     * Resolve the attribute offsets. This is where each attribute starts from the beginning of the
     * vertex data.
     *
     * @since 1.0
     */
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

    /**
     * Handle the attributes that are supported by the render model. For example, if texel data, a
     * texture applied and the user has not chosen to ignore texel data, the texel data attribute
     * will be enabled.
     *
     * @param enable    True will enable the attributes, false will disable the attributes
     * @since 1.0
     */
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

    /**
     * Enable or disable the position attribute. This will upload the vertex buffer and tell OpenGL
     * SL how to read the position data from it.
     *
     * @param enable    True will enable the attribute, false will disable the attribute
     * @since 1.0
     */
    private void handlePositionDataAttribute(boolean enable)
    {
        if(enable)
        {
            vertexBuffer.position(positionDataOffset);
            glEnableVertexAttribArray(shader.getPositionAttributeHandle());
            glVertexAttribPointer(shader.getPositionAttributeHandle(),
                    elementsPerPosition,
                    GL_FLOAT,
                    true,
                    totalStrideBytes,
                    vertexBuffer);
            vertexBuffer.position(0);
        }
        else
        {
            glDisableVertexAttribArray(shader.getPositionAttributeHandle());
        }
    }

    /**
     * Enable or disable the texel attribute. This will upload the vertex buffer and tell OpenGL SL
     * how to read the texel data from it.
     *
     * @param enable    True will enable the attribute, false will disable the attribute
     * @since 1.0
     */
    private void handleTexelDataAttribute(boolean enable)
    {
        if(enable)
        {
            // Enable attrib texture data
            vertexBuffer.position(texelDataOffset);
            glEnableVertexAttribArray(shader.getTextureAttributeHandle());
            glVertexAttribPointer(shader.getTextureAttributeHandle(),
                    elementsPerTexel,
                    GL_FLOAT,
                    true,
                    totalStrideBytes,
                    vertexBuffer);
            vertexBuffer.position(0);
        }
        else
        {
            glDisableVertexAttribArray(shader.getTextureAttributeHandle());
        }
    }

    /**
     * Enable or disable the colour attribute. This will upload the vertex buffer and tell OpenGL SL
     * how to read the colour data from it.
     *
     * @param enable    True will enable the attribute, false will disable the attribute
     * @since 1.0
     */
    private void handleColourDataAttribute(boolean enable)
    {
        if(enable)
        {
            // Enable attrib colour data
            vertexBuffer.position(colourDataOffset);
            glEnableVertexAttribArray(shader.getColourAttributeHandle());
            glVertexAttribPointer(shader.getColourAttributeHandle(),
                    elementsPerColour,
                    GL_FLOAT,
                    true,
                    totalStrideBytes,
                    vertexBuffer);
            vertexBuffer.position(0);
        }
        else
        {
            glDisableVertexAttribArray(shader.getColourAttributeHandle());
        }
    }
}
