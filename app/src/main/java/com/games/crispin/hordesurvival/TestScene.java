package com.games.crispin.hordesurvival;

import android.content.Context;
import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Scene;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = (context) -> new TestScene(context);

    static final float TRIANGLE_COORDS[] =
    {
            -1.0f,-1.0f,-1.0f,
            -1.0f,-1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,
            -1.0f, 1.0f,-1.0f,

            1.0f,-1.0f, 1.0f,
            -1.0f,-1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            1.0f, 1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,

            -1.0f,-1.0f,-1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f,-1.0f,
            1.0f,-1.0f, 1.0f,
            -1.0f,-1.0f, 1.0f,
            -1.0f,-1.0f,-1.0f,

            -1.0f, 1.0f, 1.0f,
            -1.0f,-1.0f, 1.0f,
            1.0f,-1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f,-1.0f,-1.0f,
            1.0f, 1.0f,-1.0f,

            1.0f,-1.0f,-1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f,-1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f,-1.0f,
            -1.0f, 1.0f,-1.0f,

            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f,-1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f,-1.0f, 1.0f
    };

    static final int ELEMENTS_PER_VERTEX = 3;
    static final int BYTES_PER_FLOAT = 4;
    static final int VERTEX_STRIDE = ELEMENTS_PER_VERTEX * BYTES_PER_FLOAT;

    static final int VERTEX_COUNT = TRIANGLE_COORDS.length / ELEMENTS_PER_VERTEX;

    static final float COLOUR[] = { 0.64f, 0.77f, 0.22f, 1.0f };

    // The vertex shader program
    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;" +
                    "uniform mat4 uMatrix;" +
                    "void main() {" +
                    "gl_Position = uMatrix * vPosition;" +
                    "}";

    // The fragment shader code
    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 uColour;" +
                    "void main() {" +
                    "vec4 colour = uColour;" +
                    "gl_FragColor = colour;" +
                    "}";

    public static int loadShader(int type, String shaderCode)
    {
        // Create a shader of the specified type and get the ID
        final int SHADER = glCreateShader(type);

        // Upload the source to the shader
        glShaderSource(SHADER, shaderCode);

        // Compile that shader object
        glCompileShader(SHADER);

        return SHADER;
    }

    int program;
    int vertexShader;
    int fragmentShader;
    int posAttribHandle;
    int matrixHandle;
    int colourHandle;

    // Float buffer that holds all the triangle co-ordinate data
    private FloatBuffer VERTEX_BUFFER;

    int width;
    int height;

    public TestScene(Context context)
    {
        program = glCreateProgram();
        vertexShader = loadShader(GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        fragmentShader = loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        posAttribHandle = glGetAttribLocation(program, "vPosition");
        matrixHandle = glGetUniformLocation(program, "uMatrix");
        colourHandle = glGetUniformLocation(program, "uColour");


        // Initialise a vertex byte buffer for the shape float array
        final ByteBuffer VERTICES_BYTE_BUFFER = ByteBuffer.allocateDirect(
                TRIANGLE_COORDS.length * BYTES_PER_FLOAT);

        // Use the devices hardware's native byte order
        VERTICES_BYTE_BUFFER.order(ByteOrder.nativeOrder());

        // Create a Float buffer from the ByteBuffer
        VERTEX_BUFFER = VERTICES_BYTE_BUFFER.asFloatBuffer();

        // Add the array of floats to the buffer
        VERTEX_BUFFER.put(TRIANGLE_COORDS);

        // Set buffer to read the first co-ordinate
        VERTEX_BUFFER.position(0);

        width = Crispin.getSurfaceWidth();
        height = Crispin.getSurfaceHeight();

        System.out.println("Width: " + width + ", Height: " + height);

        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.YELLOW);
    }

    @Override
    public void update(float deltaTime) {

    }

    public float[] crossProduct(float x, float y, float z, float x2, float y2, float z2) {
        return new float[]
        {
                (y * z2) - (z * y2),
                (z * x2) - (x * z2),
                (x * y2) - (y * x2)
        };
    }

    float cameraAngle = 0.0f;
    float[] viewMatrix = new float[16];
    float[] fustrumMatrix = new float[16];
    @Override
    public void render() {
        glUseProgram(program);

        float px = 0.0f;
        float py = 0.0f;
        float pz = 1.0f;
        float rx = 1.0f;
        float ry = 0.0f;
        float rz = 0.0f;
        float dx = 0.0f;
        float dy = 0.0f;
        float dz = -1.0f;
        float[] crossProduct = crossProduct(rx, ry, rz, dx, dy, dz);
        float cx = crossProduct[0];
        float cy = crossProduct[1];
        float cz = crossProduct[2];
        float distance = 1.0f;
        cameraAngle += 0.005f;

        // calculate x and z values for a constant radius
        float x = distance * (float)Math.sin((double)cameraAngle);
        float z = distance * (float)Math.cos((double)cameraAngle);

        Matrix.setLookAtM(viewMatrix, 0,
                px, py, pz,
                px + dx, py + dy, pz + dz,
                cx, cy, cz);

        Matrix.perspectiveM(fustrumMatrix, 0, 90f, (float)width/(float)height, 0.1f, 5.0f);


        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);

       // Matrix.scaleM(modelMatrix, 0, size, size, size);
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, 2.0f);
      //  Matrix.rotateM(modelMatrix, 0, particle.angle, 0.0f, 1.0f, 0.0f);

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);

        float[] modelViewProjectionMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, fustrumMatrix, 0, modelViewMatrix, 0);

        glUniformMatrix4fv(matrixHandle, 1, false, modelViewProjectionMatrix, 0);

        glUniform4fv(colourHandle, 1, COLOUR, 0);

        glEnableVertexAttribArray(posAttribHandle);
        glVertexAttribPointer(posAttribHandle,
                ELEMENTS_PER_VERTEX,
                GL_FLOAT,
                false,
                VERTEX_STRIDE,
                VERTEX_BUFFER);

        glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);

        glDisableVertexAttribArray(posAttribHandle);
    }
}
