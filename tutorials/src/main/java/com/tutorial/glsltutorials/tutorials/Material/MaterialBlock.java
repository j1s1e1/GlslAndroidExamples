package com.tutorial.glsltutorials.tutorials.Material;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

/**
 * Created by jamie on 12/26/14.
 */
public class MaterialBlock
{
    public Vector4f diffuseColor;
    public Vector4f specularColor;
    public float specularShininess;
    public float[] padding = new float[3];

    int programNumber;
    int diffuseColorUnif;
    int specularColorUnif;
    int specularShininessUnif;

    public MaterialBlock(Vector4f diffuseColorIn, Vector4f specularColorIn, float specularShininessIn)
    {
        diffuseColor = diffuseColorIn;
        specularColor = specularColorIn;
        specularShininess = specularShininessIn;
    }

    public MaterialBlock()
    {
    }

    public static int Size()
    {
        int size = 0;
        size += 2 * Vector4f.sizeInBytes();
        size += 4 * 4;  // 4 floats
        return size;
    }

    public float[] toArray()
    {
        float[] result = new float[Size()/4];
        int position = 0;
        System.arraycopy(diffuseColor.toArray(), 0, result, position, 4);
        position += 4;
        System.arraycopy(specularColor.toArray(), 0, result, position, 4);
        position += 4;
        result[position] = specularShininess;
        return result;
    }

    public void setUniforms(int program)
    {
        programNumber = program;
        diffuseColorUnif = GLES20.glGetUniformLocation(program, "Mtl.diffuseColor");
        specularColorUnif = GLES20.glGetUniformLocation(program, "Mtl.specularColor");
        specularShininessUnif = GLES20.glGetUniformLocation(program, "Mtl.specularShininess");
    }

    public void update(MaterialBlock materialBlock)
    {
        GLES20.glUseProgram(programNumber);
        GLES20.glUniform4fv(diffuseColorUnif, 1, materialBlock.diffuseColor.toArray(), 0);
        GLES20.glUniform4fv(specularColorUnif, 1, materialBlock.specularColor.toArray(), 0);
        GLES20.glUniform1f(specularShininessUnif, materialBlock.specularShininess);
        GLES20.glUseProgram(programNumber);
    }

    public void updateInternal()
    {
        GLES20.glUseProgram(programNumber);
        GLES20.glUniform4fv(diffuseColorUnif, 1, diffuseColor.toArray(), 0);
        GLES20.glUniform4fv(specularColorUnif, 1, specularColor.toArray(), 0);
        GLES20.glUniform1f(specularShininessUnif, specularShininess);
        GLES20.glUseProgram(programNumber);
    }
}
