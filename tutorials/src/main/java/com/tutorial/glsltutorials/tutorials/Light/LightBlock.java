package com.tutorial.glsltutorials.tutorials.Light;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

/**
 * Created by jamie on 12/26/14.
 */
public class LightBlock
{
    public LightBlock(int NUMBER_OF_LIGHTS_IN)
    {
        NUMBER_OF_LIGHTS = NUMBER_OF_LIGHTS_IN;
        lights = new PerLight[NUMBER_OF_LIGHTS];
        for (int i = 0; i < NUMBER_OF_LIGHTS; i++)
        {
            lights[i] = new PerLight();
        }
    }

    int NUMBER_OF_LIGHTS;
    public Vector4f ambientIntensity;
    public float lightAttenuation;
    public float maxIntensity;
    public float gamma;
    public float padding;
    public PerLight[] lights;


    public static int size(int numberOfLights)
    {
        int size = 0;
        size += Vector4f.sizeInBytes();
        size += 4 * 4;  // 4 floats
        size += numberOfLights * PerLight.size();
        return size;
    }

    public float[] toFloat()
    {
        float[] result = new float[size(NUMBER_OF_LIGHTS)/4];
        int position = 0;
        System.arraycopy(ambientIntensity.toArray(), 0, result, position, 4);
        position += 4;
        result[position++] = lightAttenuation;
        result[position++] = maxIntensity;
        result[position++] = gamma;
        position += 1;
        for (int i = 0; i < NUMBER_OF_LIGHTS; i++)
        {
            System.arraycopy(lights[i].toFloat(), 0, result, position, PerLight.size()/4);
            position += PerLight.size()/4;
        }
        return result;
    }

    int programNumber;
    int ambientIntensityUnif;
    int lightAttenuationUnif;
    int maxIntensityUnif;
    int[] cameraSpaceLightPosUnif;
    int[] lightIntensityUnif;
    int gammaUnif;
    int numberOfLightsUnif;

    public void setUniforms(int program)
    {
        programNumber = program;
        ambientIntensityUnif = GLES20.glGetUniformLocation(program, "Lgt.ambientIntensity");
        lightAttenuationUnif = GLES20.glGetUniformLocation(program, "Lgt.lightAttenuation");
        maxIntensityUnif = GLES20.glGetUniformLocation(program, "Lgt.maxIntensity");
        gammaUnif = GLES20.glGetUniformLocation(program, "Lgt.gamma");
        cameraSpaceLightPosUnif = new int[NUMBER_OF_LIGHTS];
        lightIntensityUnif = new int[NUMBER_OF_LIGHTS];
        for (int i = 0 ; i < NUMBER_OF_LIGHTS; i++)
        {
            cameraSpaceLightPosUnif[i] = GLES20.glGetUniformLocation(program, "Lgt.lights[" + String.valueOf(i) + "].cameraSpaceLightPos");
            lightIntensityUnif[i] =  GLES20.glGetUniformLocation(program, "Lgt.lights[" + String.valueOf(i) + "].lightIntensity");
        }
        numberOfLightsUnif = GLES20.glGetUniformLocation(program, "numberOfLights");
    }

    public void update(LightBlock lightblock)
    {
        GLES20.glUseProgram(programNumber);
        GLES20.glUniform4fv(ambientIntensityUnif, 1, lightblock.ambientIntensity.toArray(), 0);
        GLES20.glUniform1f(lightAttenuationUnif, lightblock.lightAttenuation);
        if (maxIntensityUnif != -1) GLES20.glUniform1f(maxIntensityUnif, lightblock.maxIntensity);
        if (gammaUnif != -1) GLES20.glUniform1f(gammaUnif, lightblock.gamma);
        for (int i = 0; i < NUMBER_OF_LIGHTS; i++)
        {
            GLES20.glUniform4fv(cameraSpaceLightPosUnif[i], 1, lightblock.lights[i].cameraSpaceLightPos.toArray(), 0);
            GLES20.glUniform4fv(lightIntensityUnif[i], 1, lightblock.lights[i].lightIntensity.toArray(), 0);
        }
        if (numberOfLightsUnif != -1) GLES20.glUniform1f(numberOfLightsUnif, lightblock.NUMBER_OF_LIGHTS);
        GLES20.glUseProgram(programNumber);
    }

    public void updateInternal()
    {
        GLES20.glUseProgram(programNumber);
        GLES20.glUniform4fv(ambientIntensityUnif, 1, ambientIntensity.toArray(), 0);
        GLES20.glUniform1f(lightAttenuationUnif, lightAttenuation);
        GLES20.glUniform1f(maxIntensityUnif, maxIntensity);
        if (gammaUnif != -1) GLES20.glUniform1f(gammaUnif, gamma);
        for (int i = 0; i < NUMBER_OF_LIGHTS; i++)
        {
            GLES20.glUniform4fv(cameraSpaceLightPosUnif[i], 1, lights[i].cameraSpaceLightPos.toArray(), 0);
            GLES20.glUniform4fv(lightIntensityUnif[i], 1, lights[i].lightIntensity.toArray(), 0);
        }
        if (numberOfLightsUnif != -1) GLES20.glUniform1i(numberOfLightsUnif, NUMBER_OF_LIGHTS);
        GLES20.glUseProgram(programNumber);
    }
};
