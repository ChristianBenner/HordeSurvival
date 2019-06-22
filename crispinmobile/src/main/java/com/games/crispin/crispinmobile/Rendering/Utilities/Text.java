package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Shaders.TextShader;

import static android.opengl.GLES20.GL_TEXTURE;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class Text
{
    private Font font;
    private String textString;

    private TextShader textShader;

    public Text(Font font, String textString)
    {
        this.font = font;
        this.textString = textString;
        textShader = new TextShader();
    }

    public void renderText(float x, float y, float scale, Colour colour)
    {
        for(int i = 0; i < textString.length(); i++)
        {
          //  Font.FreeTypeCharacter character = font.getCharacterTexture()
        }

        textShader.enableIt();
        glUniform4f(textShader.getColourUniformHandle(),
                colour.getRed(),
                colour.getGreen(),
                colour.getBlue(),
                colour.getAlpha());

        glActiveTexture(GL_TEXTURE0);


        glBindVertexArray()

        textShader.enableIt();

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

        textShader.disableIt();
    }
}
