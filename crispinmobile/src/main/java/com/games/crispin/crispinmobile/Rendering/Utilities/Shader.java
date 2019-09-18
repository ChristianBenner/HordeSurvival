package com.games.crispin.crispinmobile.Rendering.Utilities;

import com.games.crispin.crispinmobile.Utilities.FileResourceReader;
import com.games.crispin.crispinmobile.Utilities.Logger;
import com.games.crispin.crispinmobile.Utilities.ShaderCache;

import static android.opengl.GLES30.GL_COMPILE_STATUS;
import static android.opengl.GLES30.GL_FRAGMENT_SHADER;
import static android.opengl.GLES30.GL_INVALID_VALUE;
import static android.opengl.GLES30.GL_TRUE;
import static android.opengl.GLES30.GL_VERTEX_SHADER;
import static android.opengl.GLES30.glAttachShader;
import static android.opengl.GLES30.glCompileShader;
import static android.opengl.GLES30.glCreateProgram;
import static android.opengl.GLES30.glCreateShader;
import static android.opengl.GLES30.glDeleteProgram;
import static android.opengl.GLES30.glDeleteShader;
import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetShaderiv;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glLinkProgram;
import static android.opengl.GLES30.glShaderSource;
import static android.opengl.GLES30.glUseProgram;

/**
 * Shader class is used to load and compile and manage GLSL shader programs from a file or
 * string. It can be used to load vertex/fragment shader programs. The class is designed to be
 * derived from/extended.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Shader
{
    // Tag for the logger
    private static final String TAG = "Shader";

    // Undefined handle value
    private static int UNDEFINED_HANDLE = -1;

    // Vertex shader code
    private String vertexShaderCode;

    // Fragment shader code
    private String fragmentShaderCode;

    // The ID of the OpenGL program
    private int programId;

    // Position attribute handle
    protected int positionAttributeHandle;

    // Colour attribute handle
    protected int colourAttributeHandle;

    // Texture attribute handle
    protected int textureAttributeHandle;

    // Colour uniform handle
    protected int colourUniformHandle;

    // Texture uniform handle
    protected int textureUniformHandle;

    // Matrix uniform handle
    protected int matrixUniformHandle;

    // UV Multiplier Uniform handle
    protected int uvMultiplierUniformHandle;

    /**
     * Enable the shader program. Should be used before making render calls to objects that you wish
     * to render using the shader.
     *
     * @since 1.0
     */
    public void enableIt()
    {
        glUseProgram(programId);
    }

    /**
     * Disable the shader program. Should be used when finished drawing objects with the shader.
     *
     * @since 1.0
     */
    public void disableIt()
    {
        glUseProgram(0);
    }

    /**
     * Re-create and compile the shader program. This is used to re-load the shader into OpenGL ES
     * memory if it is destroyed.
     *
     * @since 1.0
     */
    public void reconstruct()
    {
        programId = createProgram(vertexShaderCode, fragmentShaderCode);
    }

    /**
     * Remove the program from OpenGL ES memory
     *
     * @since 1.0
     */
    public void destroy()
    {
        glDeleteProgram(programId);
        programId = GL_INVALID_VALUE;
    }

    /**
     * Get an integer ID for a handle of a specified attribute
     *
     * @param attributeName The name of the attribute in the shader code
     * @return              Integer ID of the attribute handle
     * @since               1.0
     */
    public int getAttribute(String attributeName)
    {
        return glGetAttribLocation(programId, attributeName);
    }

    /**
     * Get an integer ID for a handle of a specified uniform
     *
     * @param uniformName   The name of the uniform in the shader code
     * @return              Integer ID of the uniform handle
     * @since               1.0
     */
    public int getUniform(String uniformName)
    {
        return glGetUniformLocation(programId, uniformName);
    }

    /**
     * Get the position attribute handle
     *
     * @return Integer ID of the position attribute handle
     * @since 1.0
     */
    public int getPositionAttributeHandle()
    {
        return positionAttributeHandle;
    }

    /**
     * Get the colour attribute handle
     *
     * @return Integer ID of the colour attribute handle
     * @since 1.0
     */
    public int getColourAttributeHandle()
    {
        return colourAttributeHandle;
    }

    /**
     * Get the texture attribute handle
     *
     * @return Integer ID of the texture attribute handle
     * @since 1.0
     */
    public int getTextureAttributeHandle()
    {
        return textureAttributeHandle;
    }

    /**
     * Get the colour uniform handle
     *
     * @return Integer ID of the colour uniform handle
     * @since 1.0
     */
    public int getColourUniformHandle()
    {
        return colourUniformHandle;
    }

    /**
     * Get the texture uniform handle
     *
     * @return Integer ID of the texture uniform handle
     * @since 1.0
     */
    public int getTextureUniformHandle()
    {
        return textureUniformHandle;
    }

    /**
     * Get the matrix uniform handle
     *
     * @return  Integer ID of the matrix uniform handle
     * @since   1.0
     */
    public int getMatrixUniformHandle()
    {
        return matrixUniformHandle;
    }

    /**
     * Get the uv multiplier uniform handle
     *
     * @return  Integer ID of the uv multipliers uniform handle
     * @since   1.0
     */
    public int getUvMultiplierUniformHandle()
    {
        return uvMultiplierUniformHandle;
    }

    /**
     * Shader constructor. Create and compile the GLSL shader in Open GL ES memory and then register
     * it in the shader cache.
     *
     * @param vertexShaderCode      The vertex shader code as a string. Code must be written in GLSL
     *                              shader programming language
     * @param fragmentShaderCode    The fragment shader code as a string. Code must be written in
     *                              GLSL shader programming language.
     * @since                       1.0
     */
    protected Shader(String vertexShaderCode, String fragmentShaderCode)
    {
        this.vertexShaderCode = vertexShaderCode;
        this.fragmentShaderCode = fragmentShaderCode;

        // Create a new shader program using the vertex shader code and fragment shader code
        programId = createProgram(vertexShaderCode, fragmentShaderCode);

        // Register the shader to the cache so that the engine can handle the re-init call.
        // It is important that the engine handles this call, because the shader memory (all
        // OpenGL ES memory) is cleared when the Android activity 'onSurfaceCreated' is called.
        // This happens on screen rotation or when re-opening the application. The re-init call
        // will re-create the shader's OpenGL ES memory parts. This means that the user doesn't need
        // to worry about this and can just create new shader's in the constructor of their scenes.
        ShaderCache.registerShader(this);

        // Set the default values of all of the different handles to undefined
        positionAttributeHandle = UNDEFINED_HANDLE;
        colourAttributeHandle = UNDEFINED_HANDLE;
        textureAttributeHandle = UNDEFINED_HANDLE;
        colourUniformHandle = UNDEFINED_HANDLE;
        textureUniformHandle = UNDEFINED_HANDLE;
        matrixUniformHandle = UNDEFINED_HANDLE;
        uvMultiplierUniformHandle = UNDEFINED_HANDLE;
    }

    /**
     * Shader constructor. Create and compile the GLSL shader in Open GL ES memory and then register
     * it in the shader cache.
     *
     * @param vertexShaderCode      The vertex shader code as a byte array. Code must be written in
     *                              GLSL shader programming language
     * @param fragmentShaderCode    The fragment shader code as a byte arrray. Code must be written
     *                              in GLSL shader programming language.
     * @since                       1.0
     */
    protected Shader(byte[] vertexShaderCode, byte[] fragmentShaderCode)
    {
        this(new String(vertexShaderCode), new String(fragmentShaderCode));
    }

    /**
     * Shader constructor. Create and compile the GLSL shader in Open GL ES memory and then register
     * it in the shader cache. Reads in the shader files for the given resource IDs
     *
     * @param vertexShaderResourceId    The vertex shader file resource ID. Code must be written in
     *                                  GLSL shader programming language
     * @param fragmentShaderResourceId  The fragment shader file resource ID. Code must be written
     *                                  in GLSL shader programming language.
     * @since                           1.0
     */
    protected Shader(int vertexShaderResourceId, int fragmentShaderResourceId)
    {
        this(FileResourceReader.readRawResource(vertexShaderResourceId),
                FileResourceReader.readRawResource(fragmentShaderResourceId));
    }

    /**
     * Create the shader program. This includes all steps of GLSL shader program creation. The steps
     * are: create and compile vertex and fragment shaders, create program ID, attach vertex and
     * fragment shaders to program, then link the program.
     *
     * @param vertexShaderCode      String containing vertex shader code. Must be written in GLSL
     *                              shader programming language.
     * @param fragmentShaderCode    String containing fragment shader code. Must be written in GLSL
     *                              shader programming language.
     * @return                      Program ID integer
     * @since                       1.0
     */
    private static int createProgram(String vertexShaderCode, String fragmentShaderCode)
    {
        // Create a vertex shader
        final int VERTEX_SHADER_ID = createShader(GL_VERTEX_SHADER, vertexShaderCode);

        // Create a fragment shader
        final int FRAGMENT_SHADER_ID = createShader(GL_FRAGMENT_SHADER, fragmentShaderCode);

        // Create the shader program
        final int PROGRAM_ID = glCreateProgram();

        // Throw exception on failure
        if (PROGRAM_ID == 0)
        {
            Logger.error(TAG, "OpenGLES failed to generate a program object");
        }

        // Attach the vertex and fragment shaders to the program
        glAttachShader(PROGRAM_ID, VERTEX_SHADER_ID);
        glAttachShader(PROGRAM_ID, FRAGMENT_SHADER_ID);

        // Link the shaders
        glLinkProgram(PROGRAM_ID);

        // Delete the vertex and fragment shaders from OpenGL ES memory
        glDeleteShader(VERTEX_SHADER_ID);
        glDeleteShader(FRAGMENT_SHADER_ID);

        return PROGRAM_ID;
    }

    /**
     * Create a shader of specified type. This created a shader ID, uploads the source code and then
     * compiles it. The shader source code must be written in the GLSL programming language. This
     * method should not be mistaken for the 'createProgram' method. This method doesn't create a
     * shader program, it creates an individual shader such as a vertex shader or fragment shader.
     * To create a shader program, use the 'createProgram' method.
     *
     * @param type          The type of shader as specified in OpenGL ES. For example:
     *                      GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
     * @param shaderCode    String containing the shader code. Must be written in GLSL shader
     *                      programming language.
     * @return              Return
     * @see                 #createProgram(String, String)
     * @see                 android.opengl.GLES20
     * @since               1.0
     */
    private static int createShader(int type, String shaderCode)
    {
        // Create a shader of the specified type and get the ID
        final int SHADER = glCreateShader(type);

        // Check if the shader ID is invalid
        if (SHADER == 0)
        {
            // Failed to generate shader object
            Logger.error(TAG, "OpenGLES failed to generate " +
                    typeToString(type) +
                    " shader object");
        }

        // Upload the source to the shader
        glShaderSource(SHADER, shaderCode);

        // Compile that shader object
        glCompileShader(SHADER);

        // Check compilation
        if(!isCompiled(SHADER))
        {
            Logger.error(TAG, "OpenGLES failed to compile " +
                    typeToString(type) +
                    "shader program");
        }

        return SHADER;
    }

    /**
     * Get the compilation status of a shader via its ID.
     *
     * @param shaderID  ID of the shader
     * @return          True if the shader is compiled, false if not.
     * @since           1.0
     */
    private static boolean isCompiled(int shaderID)
    {
        // Place to store the vertex shader compilation status
        final int[] SHADER_COMPILATION_STATUS = new int[1];

        // Get the vertex shader compilation status
        glGetShaderiv(shaderID,
                GL_COMPILE_STATUS,
                SHADER_COMPILATION_STATUS,
                0);

        // Return true if shader compilation successful
        return SHADER_COMPILATION_STATUS[0] == GL_TRUE;
    }

    /**
     * Resolve the name of a type of shader
     *
     * @param type  The type of shader as specified in OpenGL ES. For example:
     *              GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
     * @see         android.opengl.GLES20
     * @return      Returns string of shader type specified
     * @since       1.0
     */
    private static String typeToString(int type)
    {
        // Determine the type and return the associated string
        switch (type)
        {
            case GL_VERTEX_SHADER:
                return "GL_VERTEX_SHADER";
            case GL_FRAGMENT_SHADER:
                return "GL_FRAGMENT_SHADER";
            default:
                return "UNDEFINED";
        }
    }
}