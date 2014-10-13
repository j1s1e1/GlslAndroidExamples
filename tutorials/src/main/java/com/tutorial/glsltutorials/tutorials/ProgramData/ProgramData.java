package com.tutorial.glsltutorials.tutorials.ProgramData;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;

import java.nio.IntBuffer;

/**
 * Created by jamie on 10/12/14.
 */
public class ProgramData {
    public int theProgram;
    public int positionAttribute;
    public int colorAttribute;
    public int modelToCameraMatrixUnif;
    public int modelToWorldMatrixUnif;
    public int worldToCameraMatrixUnif;
    public int cameraToClipMatrixUnif;
    public int baseColorUnif;

    public int normalModelToCameraMatrixUnif;
    public int dirToLightUnif;
    public int lightIntensityUnif;
    public int ambientIntensityUnif;
    public int normalAttribute;
    String vertexShader;
    String fragmentShader;


    public ProgramData(String vertexShaderIn, String fragmentShaderIn)
    {
        vertexShader = vertexShaderIn;
        fragmentShader = fragmentShaderIn;
        int vertex_shader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragment_shader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");
        if (positionAttribute != -1)
        {
            if (positionAttribute != 0)
            {
                //FIXME MessageBox.Show("These meshes only work with position at location 0 " + vertexShader);
            }
        }
        if (colorAttribute != -1)
        {
            if (colorAttribute != 1)
            {
                // FIXME MessageBox.Show("These meshes only work with color at location 1" + vertexShader);
            }
        }

        modelToWorldMatrixUnif = GLES20.glGetUniformLocation(theProgram, "modelToWorldMatrix");
        worldToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "worldToCameraMatrix");
        cameraToClipMatrixUnif = GLES20.glGetUniformLocation(theProgram, "cameraToClipMatrix");
        if (cameraToClipMatrixUnif == -1)
        {
            cameraToClipMatrixUnif = GLES20.glGetUniformLocation(theProgram, "Projection.cameraToClipMatrix");
        }
        baseColorUnif = GLES20.glGetUniformLocation(theProgram, "baseColor");

        modelToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "modelToCameraMatrix");

        normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "normalModelToCameraMatrix");
        dirToLightUnif =  GLES20.glGetUniformLocation(theProgram, "dirToLight");
        lightIntensityUnif = GLES20.glGetUniformLocation(theProgram, "lightIntensity");
        ambientIntensityUnif = GLES20.glGetUniformLocation(theProgram, "ambientIntensity");
        normalAttribute = GLES20.glGetAttribLocation(theProgram, "normal");
    }

    public boolean CompareShaders(String vertexShaderIn, String fragmentShaderIn)
    {
        return ((vertexShaderIn == vertexShader) & (fragmentShader == fragmentShaderIn));
    }

    public void Draw(int[] vertexBufferObject, int[] indexBufferObject,
                     Matrix4f cameraToClip, Matrix4f worldToCamera, Matrix4f mm,
                     int indexDataLength, float[] color, int COORDS_PER_VERTEX, int vertexStride)
    {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, cameraToClip.toArray(), 0);
        GLES20.glUniformMatrix4fv(worldToCameraMatrixUnif, 1, false, worldToCamera.toArray(), 0);
        GLES20.glUniformMatrix4fv(modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
        GLES20.glUniform4fv(baseColorUnif, 1, color, 0);


        GLES20.glEnableVertexAttribArray(positionAttribute);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionAttribute, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, vertexStride, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexDataLength, GLES20.GL_UNSIGNED_SHORT, 0);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionAttribute);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(0);
    }

    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("theProgram = " + String.valueOf(theProgram));
        result.append("positionAttribute = " + String.valueOf(positionAttribute));
        result.append("colorAttribute = " + String.valueOf(colorAttribute));
        result.append("modelToCameraMatrixUnif = " + String.valueOf(modelToCameraMatrixUnif));
        result.append("modelToWorldMatrixUnif = " + String.valueOf(modelToWorldMatrixUnif));
        result.append("worldToCameraMatrixUnif = " + String.valueOf(worldToCameraMatrixUnif));
        result.append("cameraToClipMatrixUnif = " + String.valueOf(cameraToClipMatrixUnif));
        result.append("baseColorUnif = " + String.valueOf(baseColorUnif));
        result.append("normalModelToCameraMatrixUnif = " + String.valueOf(normalModelToCameraMatrixUnif));
        result.append("dirToLightUnif = " + String.valueOf(dirToLightUnif));
        result.append("lightIntensityUnif = " + String.valueOf(lightIntensityUnif));
        result.append("ambientIntensityUnif = " + String.valueOf(ambientIntensityUnif));
        result.append("normalAttribute = " + String.valueOf(normalAttribute));
        return result.toString();
    }
}
