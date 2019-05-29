package com.games.crispin.crispinmobile;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

public class RenderObject
{
    static final int ELEMENTS_PER_VERTEX = 3;
    static final int BYTES_PER_FLOAT = 4;
    static final int VERTEX_STRIDE = ELEMENTS_PER_VERTEX * BYTES_PER_FLOAT;

    static final float TRIANGLE_COORDS[] =
            {
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,

                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,

                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,

                    -1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, -1.0f,

                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,

                    1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f
            };

    static final int VERTEX_COUNT = TRIANGLE_COORDS.length / ELEMENTS_PER_VERTEX;

    static final float COLOUR[] = {0.64f, 0.77f, 0.22f, 1.0f};

    // Float buffer that holds all the triangle co-ordinate data
    private FloatBuffer VERTEX_BUFFER;

    private float scaleX;
    private float scaleY;
    private float scaleZ;
    private Geometry.Point position;
    private float angle;
    private float rotationX;
    private float rotationY;
    private float rotationZ;

    float[] modelMatrix = new float[16];

    private Material material;

    public RenderObject()
    {
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

        material = new Material(false);
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

    public void setPosition(Geometry.Point position)
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
        Matrix.rotateM(modelMatrix, 0, angle, rotationX, rotationY, rotationZ);
    }

    public void draw(int matrixUniformHandle, int positionAttributeHandle, int colourUniformHandle, Camera camera)
    {
        updateModelMatrix();

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0, modelMatrix, 0);

        float[] modelViewProjectionMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, camera.getFustrumMatrix(), 0, modelViewMatrix, 0);

        glUniformMatrix4fv(matrixUniformHandle, 1, false, modelViewProjectionMatrix, 0);

        glEnableVertexAttribArray(positionAttributeHandle);
        glVertexAttribPointer(positionAttributeHandle,
                ELEMENTS_PER_VERTEX,
                GL_FLOAT,
                false,
                VERTEX_STRIDE,
                VERTEX_BUFFER);

        glUniform4fv(colourUniformHandle, 1, COLOUR, 0);
        glDrawArrays(GL_TRIANGLES, 0, VERTEX_COUNT);
        glDisableVertexAttribArray(positionAttributeHandle);
    }
}