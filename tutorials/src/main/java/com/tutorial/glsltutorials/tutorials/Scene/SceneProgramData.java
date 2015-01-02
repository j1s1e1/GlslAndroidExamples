package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.ShadersNames;
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.Material.MaterialBlock;

/**
 * Created by jamie on 1/2/15.
 */
public class SceneProgramData {
    public int theProgram;

    public int modelToCameraMatrixUnif;
    public int normalModelToCameraMatrixUnif;
    public LightBlock lightBlock;
    public MaterialBlock materialBlock;

    public int cameraToClipMatrixUnif;

    public static SceneProgramData LoadLitProgram(ShadersNames shaders)
    {
        SceneProgramData data = new SceneProgramData();
        int vertexShaderInt = Shader.compileShader(GLES20.GL_VERTEX_SHADER, shaders.vertexShader);
        int fragmentShaderInt = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, shaders.fragmentShader);

        data.theProgram = Shader.createAndLinkProgram(vertexShaderInt, fragmentShaderInt);
        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");

        data.lightBlock = new LightBlock(4);
        data.lightBlock.setUniforms(data.theProgram);

        data.normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "normalModelToCameraMatrix");

        data.materialBlock = new MaterialBlock();
        data.materialBlock.setUniforms(data.theProgram);

        data.cameraToClipMatrixUnif =  GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");

        return data;
    }
}
