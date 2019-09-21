package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Rotation2D;
import com.games.crispin.crispinmobile.Geometry.Rotation3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;
import com.games.crispin.crispinmobile.Rendering.Shaders.AttributeColourShader;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextureAttributeColourShader;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextureShader;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
    /**
     * The attribute order type is to store the order that the vertex data elements appear. This
     * means that the data can be submitted to the GPU in the correct order.
     *  POSITION: The position data element is the only one provided
     *  POSITION_THEN_TEXEL: The position data is provided first, then texel data
     *  POSITION_THEN_COLOUR: The position data provided first, then colour
     *  POSITION_THEN_NORMAL: The position data provided first, then normal
     *  POSITION_THEN_TEXEL_THEN_COLOUR: Position data first, then texel, then colour
     *  POSITION_THEN_COLOUR_THEN_TEXEL: Position data first, then colour, then texel
     *  POSITION_THEN_TEXEL_THEN_NORMAL: Position data first, then texel, then normal
     *  POSITION_THEN_NORMAL_THEN_TEXEL: Position data first, then normal, then texel
     *  POSITION_THEN_COLOUR_THEN_NORMAL: Position data first, then colour, then normal
     *  POSITION_THEN_NORMAL_THEN_COLOUR: Position data first, then normal, then colour
     *  POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL: Position data first, then texel, then normal,
     *      then normal
     *  POSITION_THEN_COLOUR_THEN_TEXEL_THEN_NORMAL: Position data first, then colour, then texel,
     *      then normal
     *  POSITION_THEN_TEXEL_THEN_NORMAL_THEN_COLOUR: Position data first, then texel, then normal,
     *      then colour
     *  POSITION_THEN_COLOUR_THEN_NORMAL_THEN_TEXEL: Position data first, then colour, then normal,
     *      then texel
     *  POSITION_THEN_NORMAL_THEN_TEXEL_THEN_COLOUR: Position data first, then normal, then texel,
     *      then colour
     *  POSITION_THEN_NORMAL_THEN_COLOUR_THEN_TEXEL: Position data first, then normal, then colour,
     *      then texel
     *
     * @since       1.0
     */
    public enum AttributeOrder_t
    {
        POSITION,
        POSITION_THEN_TEXEL,
        POSITION_THEN_COLOUR,
        POSITION_THEN_NORMAL,
        POSITION_THEN_TEXEL_THEN_COLOUR,
        POSITION_THEN_COLOUR_THEN_TEXEL,
        POSITION_THEN_TEXEL_THEN_NORMAL,
        POSITION_THEN_NORMAL_THEN_TEXEL,
        POSITION_THEN_COLOUR_THEN_NORMAL,
        POSITION_THEN_NORMAL_THEN_COLOUR,
        POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL,
        POSITION_THEN_COLOUR_THEN_TEXEL_THEN_NORMAL,
        POSITION_THEN_TEXEL_THEN_NORMAL_THEN_COLOUR,
        POSITION_THEN_COLOUR_THEN_NORMAL_THEN_TEXEL,
        POSITION_THEN_NORMAL_THEN_TEXEL_THEN_COLOUR,
        POSITION_THEN_NORMAL_THEN_COLOUR_THEN_TEXEL
    }

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

    // Tag used in logging output
    private static final String TAG = "RenderObject";

    // The 'numVerticesPerGroup' if the vertices are not grouped
    public static final int UNGROUPED = 1;

    // The number of bytes in a float
    public static final int BYTES_PER_FLOAT = 4;

    // Value that represents an invalid OpenGL ES GLSL shader uniform handle
    private static final int INVALID_UNIFORM_HANDLE = -1;

    // Number of uniform elements to upload in a GLSL uniform upload
    private static final int UNIFORM_UPLOAD_COUNT = 1;

    // The number of vertices in the model data
    private final int VERTEX_COUNT;

    // The number of elements in a 4x4 view matrix
    private final int NUM_VALUES_PER_VIEW_MATRIX = 16;

    // The number to multiply the specified rotation on a rotation axis
    private final float ROTATION_AXIS_MULTIPLIER = 1.0f;

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

    // The method to render the data as (e.g. triangles or lines)
    private final RenderMethod renderMethod;

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

    /**
     * Get the attribute order of the model depending on a set of allowed data types
     *
     * @param renderTexels  True if the model is allowed to use texel data, else false
     * @param renderColour  True if the model is allowed to use colour data, else false
     * @param renderNormals True if the model is allowed to use normal data, else false
     * @return  Returns an attribute order based on the set of allowed data types. Position will
     *          always be part of the attribute order. In the attribute order, texel data takes 2nd
     *          priority in the, colour data takes 3rd priority, and normal data takes 4th priority.
     *          For example, if all data types are allowed then the attribute order is
     *          <code>POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL</code>
     * @since   1.0
     */
    private static AttributeOrder_t resolveAttributeOrder(boolean renderTexels,
                                                          boolean renderColour,
                                                          boolean renderNormals)
    {
        // Check what attribute order to use depending on what data types are allowed
        if (renderTexels &&
                renderColour &&
                renderNormals)
        {
            return AttributeOrder_t.POSITION_THEN_TEXEL_THEN_COLOUR_THEN_NORMAL;
        }
        else if (renderTexels && renderColour)
        {
            return AttributeOrder_t.POSITION_THEN_TEXEL_THEN_COLOUR;
        }
        else if (renderTexels && renderNormals)
        {
            return AttributeOrder_t.POSITION_THEN_TEXEL_THEN_NORMAL;
        }
        else if(renderColour && renderNormals)
        {
            return AttributeOrder_t.POSITION_THEN_COLOUR_THEN_NORMAL;
        }
        else if(renderTexels)
        {
            return AttributeOrder_t.POSITION_THEN_TEXEL;
        }
        else if(renderColour)
        {
            return AttributeOrder_t.POSITION_THEN_COLOUR;
        }
        else if(renderNormals)
        {
            return AttributeOrder_t.POSITION_THEN_NORMAL;
        }

        return AttributeOrder_t.POSITION;
    }

    /**
     * Create an object with vertex data comprised of multiple buffers containing different forms of
     * vertex data. For the buffers that you are not providing data for, put <code>null</code>.
     *
     * @param positionBuffer        Float buffer containing the position data
     * @param texelBuffer           Float buffer containing the texel data, or <code>null</code> if
     *                              no texel data is being provided
     * @param colourBuffer          Float buffer containing the colour data, or <code>null</code> if
     *                              no colour data is being provided
     * @param normalBuffer          Float buffer containing the normal data, or <code>null</code> if
     *                              no normal data is being provided
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param elementsPerPosition   The number of components the position data is comprised of
     * @param elementsPerTexel      The number of components that the texel data is comprised of
     * @param elementsPerColour     The number of components that the colour data is comprised of
     * @param elementsPerNormal     The number components that the normal data is comprised of
     * @param material              The material to apply to the object
     * @since 1.0
     */
    public RenderObject(float[] positionBuffer,
                        float[] texelBuffer,
                        float[] colourBuffer,
                        float[] normalBuffer,
                        RenderObject.RenderMethod renderMethod,
                        int numVerticesPerGroup,
                        byte elementsPerPosition,
                        byte elementsPerTexel,
                        byte elementsPerColour,
                        byte elementsPerNormal,
                        Material material)
    {
        this.scale = new Scale3D();
        this.position = new Point3D();
        this.rotation = new Rotation3D();

        this.renderMethod = renderMethod;
        this.elementsPerPosition = positionBuffer == null ? 0 : elementsPerPosition;
        this.elementsPerTexel = texelBuffer == null ? 0 : elementsPerTexel;
        this.elementsPerColour = colourBuffer == null ? 0 : elementsPerColour;
        this.elementsPerNormal = normalBuffer == null ? 0 : elementsPerNormal;

        // Solve the attribute order here
        AttributeOrder_t attributeOrder = resolveAttributeOrder(
                texelBuffer != null,
                colourBuffer != null,
                normalBuffer != null);

        // Solve the data stride
        resolveStride(numVerticesPerGroup);

        // Solve the attribute offsets
        resolveAttributeOffsets(attributeOrder, numVerticesPerGroup);

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

        // If the position buffer has been specified, add it to the vertex data buffer
        if(positionBuffer != null)
        {
            System.arraycopy(positionBuffer,
                    0,
                    vertexData,
                    0,
                    positionBuffer.length);
        }

        // If the texel buffer has been specified, add it to the vertex data buffer
        if(texelBuffer != null)
        {
            System.arraycopy(texelBuffer,
                    0,
                    vertexData,
                    POS_BUFFER_LENGTH,
                    texelBuffer.length);
        }

        // If the colour buffer has been specified, add it to the vertex data buffer
        if(colourBuffer != null)
        {
            System.arraycopy(colourBuffer,
                    0, vertexData,
                    POS_BUFFER_LENGTH + COL_BUFFER_LENGTH,
                    colourBuffer.length);
        }

        // If the normal buffer has been specified, add it to the vertex data buffer
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
        VERTEX_COUNT = vertexData.length / (this.elementsPerPosition + this.elementsPerTexel +
                this.elementsPerColour + this.elementsPerNormal);

        setMaterial(material);

        hasCustomShader = false;
    }

    /**
     * Create an object with vertex data comprised of multiple buffers containing different forms of
     * vertex data. For the buffers that you are not providing data for, put <code>null</code>. A
     * default material will be applied.
     *
     * @param positionBuffer        Float buffer containing the position data
     * @param texelBuffer           Float buffer containing the texel data, or <code>null</code> if
     *                              no texel data is being provided
     * @param colourBuffer          Float buffer containing the colour data, or <code>null</code> if
     *                              no colour data is being provided
     * @param normalBuffer          Float buffer containing the normal data, or <code>null</code> if
     *                              no normal data is being provided
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param elementsPerPosition   The number of components the position data is comprised of
     * @param elementsPerTexel      The number of components that the texel data is comprised of
     * @param elementsPerColour     The number of components that the colour data is comprised of
     * @param elementsPerNormal     The number components that the normal data is comprised of
     * @since   1.0
     */
    public RenderObject(float[] positionBuffer,
                        float[] texelBuffer,
                        float[] colourBuffer,
                        float[] normalBuffer,
                        RenderObject.RenderMethod renderMethod,
                        int numVerticesPerGroup,
                        byte elementsPerPosition,
                        byte elementsPerTexel,
                        byte elementsPerColour,
                        byte elementsPerNormal)
    {
        this(positionBuffer,
                texelBuffer,
                colourBuffer,
                normalBuffer,
                renderMethod,
                numVerticesPerGroup,
                elementsPerPosition,
                elementsPerTexel,
                elementsPerColour,
                elementsPerNormal,
                new Material());
    }

    /**
     * Create an object using a single vertex data buffer. The format of the data must be specified
     * in order for the data to be correctly interpreted. Data stride and attribute offsets are
     * calculated based on the format.
     *
     * @param vertexData            Float buffer containing the vertex data
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param elementsPerPosition   The number of components the position data is comprised of
     * @param elementsPerTexel      The number of components that the texel data is comprised of
     * @param elementsPerColour     The number of components that the colour data is comprised of
     * @param elementsPerNormal     The number components that the normal data is comprised of
     * @param material              Material to apply to the object
     * @since 1.0
     */
    public RenderObject(float[] vertexData,
                        RenderObject.RenderMethod renderMethod,
                        AttributeOrder_t attributeOrder,
                        int numVerticesPerGroup,
                        byte elementsPerPosition,
                        byte elementsPerTexel,
                        byte elementsPerColour,
                        byte elementsPerNormal,
                        Material material)
    {
        this.renderMethod = renderMethod;
        this.scale = new Scale3D();
        this.position = new Point3D();
        this.rotation = new Rotation3D();
        this.totalStrideBytes = 0;
        this.elementsPerPosition = elementsPerPosition;
        this.elementsPerTexel = elementsPerTexel;
        this.elementsPerColour = elementsPerColour;
        this.elementsPerNormal = elementsPerNormal;

        // Resolve the data stride
        resolveStride(numVerticesPerGroup);

        // Solve attribute offset
        resolveAttributeOffsets(attributeOrder, numVerticesPerGroup);

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
        VERTEX_COUNT = vertexData.length / (this.elementsPerPosition + this.elementsPerTexel +
                this.elementsPerColour + this.elementsPerNormal);

        setMaterial(material);

        hasCustomShader = false;
    }

    /**
     * Create an object using a single vertex data buffer. The format of the data must be specified
     * in order for the data to be correctly interpreted. Data stride and attribute offsets are
     * calculated based on the format. A default material will be applied to the object as one has
     * not been provided.
     *
     * @param vertexData            Float buffer containing the vertex data
     * @param renderMethod          The method to render the data (e.g. triangles or quads)
     * @param attributeOrder        The order in which the vertex elements appear
     * @param numVerticesPerGroup   The number of vertices in a group
     * @param elementsPerPosition   The number of components the position data is comprised of
     * @param elementsPerTexel      The number of components that the texel data is comprised of
     * @param elementsPerColour     The number of components that the colour data is comprised of
     * @param elementsPerNormal     The number components that the normal data is comprised of
     * @since 1.0
     */
    public RenderObject(float[] vertexData,
                        RenderObject.RenderMethod renderMethod,
                        AttributeOrder_t attributeOrder,
                        int numVerticesPerGroup,
                        byte elementsPerPosition,
                        byte elementsPerTexel,
                        byte elementsPerColour,
                        byte elementsPerNormal)
    {
        this(vertexData,
                renderMethod,
                attributeOrder,
                numVerticesPerGroup,
                elementsPerPosition,
                elementsPerTexel,
                elementsPerColour,
                elementsPerNormal,
                new Material());
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
     * Set x-axis scale multiplier
     *
     * @param x Multiplier for the x-axis
     * @since 1.0
     */
    public void setScaleX(float x)
    {
        this.scale.x = x;
    }

    /**
     * Set y-axis scale multiplier
     *
     * @param y Multiplier for the y-axis
     * @since 1.0
     */
    public void setScaleY(float y)
    {
        this.scale.y = y;
    }

    /**
     * Set z-axis scale multiplier
     *
     * @param z Multiplier for the z-axis
     * @since 1.0
     */
    public void setScaleZ(float z)
    {
        this.scale.z = z;
    }

    /**
     * Get the scale
     *
     * @return The scale multiplier of the object
     * @since 1.0
     */
    public Scale3D getScale()
    {
        return this.scale;
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
        // Check if the shader being assigned has been freeTypeInitialised
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
        // If the shader is null, create a shader for the object
        if(shader == null)
        {
            updateShader();
        }

        updateModelMatrix();

        shader.enableIt();

        float[] modelViewMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
        Matrix.multiplyMM(modelViewMatrix,
                0,
                camera.getOrthoMatrix(),
                0,
                modelMatrix,
                0);

        glUniformMatrix4fv(shader.getMatrixUniformHandle(),
                UNIFORM_UPLOAD_COUNT,
                false,
                modelViewMatrix,
                0);

        // If the shader colour uniform handle is not invalid, upload the colour data
        if(shader.getColourUniformHandle() != INVALID_UNIFORM_HANDLE)
        {
            glUniform4fv(shader.getColourUniformHandle(),
                    UNIFORM_UPLOAD_COUNT,
                    material.getColourData(),
                    0);
        }

        // If the shader texture uniform handle is not invalid, upload the texture unit
        if(shader.getTextureUniformHandle() !=INVALID_UNIFORM_HANDLE && material.hasTexture())
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
            glUniform1i(shader.getTextureUniformHandle(), 0);

            // Upload the texture UV multiplier handle if it isn't an invalid handle
            if(shader.getUvMultiplierUniformHandle() != INVALID_UNIFORM_HANDLE)
            {
                glUniform2f(shader.getUvMultiplierUniformHandle(),
                        material.getUvMultiplier().x,
                        material.getUvMultiplier().y);
            }
        }

        handleAttributes(true);

        // Draw the vertex data with the specified render method
        switch (renderMethod)
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
        // If the shader is null, create a shader for the object
        if(shader == null)
        {
            updateShader();
        }

        updateModelMatrix();

        shader.enableIt();

        float[] modelViewMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
        Matrix.multiplyMM(modelViewMatrix,
                0,
                camera.getViewMatrix(),
                0,
                modelMatrix,
                0);

        float[] modelViewProjectionMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
        Matrix.multiplyMM(modelViewProjectionMatrix,
                0,
                camera.getPerspectiveMatrix(),
                0,
                modelViewMatrix,
                0);

        glUniformMatrix4fv(shader.getMatrixUniformHandle(),
                UNIFORM_UPLOAD_COUNT,
                false,
                modelViewProjectionMatrix,
                0);

        // If the shader colour uniform handle is not invalid, upload the colour data
        if(shader.getColourUniformHandle() != INVALID_UNIFORM_HANDLE)
        {
            glUniform4f(shader.getColourUniformHandle(),
                    material.getColour().getRed(),
                    material.getColour().getGreen(),
                    material.getColour().getBlue(),
                    material.getColour().getAlpha());
        }

        // If the shader texture uniform handle is not invalid, upload the texture unit
        if(shader.getTextureUniformHandle() != INVALID_UNIFORM_HANDLE && material.hasTexture())
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
            glUniform1i(shader.getTextureUniformHandle(), 0);

            // If the shader UV multiplier uniform handle is not invalid, upload the UV multiplier
            // data
            if(shader.getUvMultiplierUniformHandle() != INVALID_UNIFORM_HANDLE)
            {
                glUniform2f(shader.getUvMultiplierUniformHandle(),
                        material.getUvMultiplier().x,
                        material.getUvMultiplier().y);
            }
        }

        handleAttributes(true);

        // Draw the vertex data with the specified render method
        switch (renderMethod)
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
        Matrix.setIdentityM(modelMatrix,
                0);
        Matrix.translateM(modelMatrix,
                0,
                position.x,
                position.y,
                position.z);
        Matrix.scaleM(modelMatrix,
                0,
                scale.x,
                scale.y,
                scale.z);

        // Check if the rotation hasn't changed in the x-axis
        if(rotation.x != 0.0f)
        {
            Matrix.rotateM(modelMatrix,
                    0,
                    rotation.x,
                    ROTATION_AXIS_MULTIPLIER,
                    0.0f,
                    0.0f);
        }

        // Check if the rotation hasn't changed in the y-axis
        if(rotation.y != 0.0f)
        {
            Matrix.rotateM(modelMatrix,
                    0,
                    rotation.y,
                    0.0f,
                    ROTATION_AXIS_MULTIPLIER,
                    0.0f);
        }

        // Check if the rotation hasn't changed in the z-axis
        if(rotation.z != 0.0f)
        {
            Matrix.rotateM(modelMatrix,
                    0,
                    rotation.z,
                    0.0f,
                    0.0f,
                    ROTATION_AXIS_MULTIPLIER);
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
        // If their has not been a custom shader allocated to the render object, automatically
        // allocate one
        if(!hasCustomShader)
        {
            // Check that the object has all of the components required to render a texture
            boolean supportsTexture = material.hasTexture() &&
                    (elementsPerTexel != 0) &&
                    !material.isIgnoringTexelData();

            // Check if the object has all of the components required to render per vertex colour
            boolean supportsColourPerAttrib = (elementsPerColour != 0) &&
                    !material.isIgnoringColourData();

//            // Determine the best shader to used depending on the material
//            if(material.isLightingEnabled() && material.hasTexture() && material.hasNormalMap())
//            {
//                // Use lighting, texture/direction map supporting shader
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

            // Select a shader based on what data attributes and uniforms the object supports
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
     * Handle the attributes that are supported by the render model. For example, if texel data, a
     * texture applied and the user has not chosen to ignore texel data, the texel data attribute
     * will be enabled.
     *
     * @param enable    True will enable the attributes, false will disable the attributes
     * @since 1.0
     */
    protected void handleAttributes(boolean enable)
    {
        // If the material is not ignoring position data, enable the position data attribute
        if(!material.isIgnoringPositionData())
        {
            handlePositionDataAttribute(enable);
        }

        // If the material is not ignoring texel data, enable the texel data attribute
        if(!material.isIgnoringTexelData() && elementsPerTexel != 0)
        {
            handleTexelDataAttribute(enable);
        }

        // If the material is not ignoring colour data, enable the colour data attribute
        if(!material.isIgnoringColourData() && elementsPerColour != 0)
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
        // Enable or disable the position data attribute
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
        // Enable or disable the texel data attribute
        if(enable)
        {
            // Enable attribute texture data
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
        // Enable or disable the colour data attribute
        if(enable)
        {
            // Enable attribute colour data
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

    /**
     * Resolve the data stride. Data stride is the distance between elements of the same data type
     * in bytes.
     *
     * @param numVerticesPerGroup   The number of vertices in a group
     * @since 1.0
     */
    private void resolveStride(int numVerticesPerGroup)
    {
        // Check if the format of the data is ungrouped before resolving stride
        if(numVerticesPerGroup == UNGROUPED)
        {
            totalStrideBytes = (elementsPerPosition +
                    elementsPerTexel +
                    elementsPerColour +
                    elementsPerNormal) *
                    BYTES_PER_FLOAT;
        }
        else
        {
            totalStrideBytes = 0;
        }
    }

    /**
     * Resolve the data attribute offsets. This is where each data attribute starts looking at the
     * vertex data.
     *
     * @param attributeOrder        The order that the attributes appear in the vertex data
     * @param numVerticesPerGroup   The number of vertices in a group
     * @since 1.0
     */
    private void resolveAttributeOffsets(AttributeOrder_t attributeOrder,
                                         int numVerticesPerGroup)
    {
        // Solve attribute offset
        final int TOTAL_NUMBER_POSITION_ELEMENTS = elementsPerPosition * numVerticesPerGroup;
        final int TOTAL_NUMBER_TEXEL_ELEMENTS = elementsPerTexel * numVerticesPerGroup;
        final int TOTAL_NUMBER_COLOUR_ELEMENTS = elementsPerColour * numVerticesPerGroup;
        final int TOTAL_NUMBER_NORMAL_ELEMENTS = elementsPerNormal * numVerticesPerGroup;

        // Figure out the attribute data offsets depending on the attribute order
        switch (attributeOrder)
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
