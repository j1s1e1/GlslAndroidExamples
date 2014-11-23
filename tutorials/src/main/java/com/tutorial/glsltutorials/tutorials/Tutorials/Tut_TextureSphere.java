package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.Icosahedron;

/**
 * Created by jamie on 11/23/14.
 */
public class Tut_TextureSphere extends TutorialBase {
    private int current_texture;

    class ProgramData
    {
        public int theProgram;
        public int position;
        public int texCoord;
    };

    static ProgramData simpleTextureProgram;
    static int sampler = 0;
    static int texUnit = 0;
    static int g_colorTexUnit = 0;

    static float[] vertexData;
    static float[] textureCoordinates;
    static float[] vertexDataWithTextureCoordinates;

    private static int vertexCount;
    private static int texCoordOffset;

    ProgramData LoadProgram(String strVertexShader, String strFragmentShader)
    {
        ProgramData data = new ProgramData();
        int vertex_shader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.position =  GLES20.glGetAttribLocation(data.theProgram, "position");
        data.texCoord =  GLES20.glGetAttribLocation(data.theProgram, "texCoord");

        int colorTextureUnif = GLES20.glGetUniformLocation(data.theProgram, "diffuseColorTex");
        GLES20.glUseProgram(data.theProgram);
        GLES20.glUniform1f(colorTextureUnif, g_colorTexUnit);
        GLES20.glUseProgram(0);

        return data;
    }

    void InitializePrograms()
    {
        simpleTextureProgram = LoadProgram(VertexShaders.SimpleTexture, FragmentShaders.SimpleTexture);
    }

    void CreateSampler()
    {
        /* FIXME GLES30 only
        GLES20.glGenSamplers(1, sampler);
        GLES20.glSamplerParameter(sampler, SamplerParameterName.TextureMagFilter,  (int)TextureMagFilter.Nearest);
        GLES20.glSamplerParameter(sampler, SamplerParameterName.TextureMinFilter,  (int)TextureMinFilter.Nearest);
        GLES20.glSamplerParameter(sampler, SamplerParameterName.TextureWrapT, (int)TextureWrapMode.Repeat);
        */

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    private void ScaleCoordinates(float scale, float zOffset)
    {
        for (int i = 0; i < vertexData.length; i++)
        {
            vertexData[i] = vertexData[i] * scale;
            if (i % 3 == 2) vertexData[i] = vertexData[i]  + zOffset;
        }
    }

    private void AddFourthCoordinate()
    {
        float[] newVertexData = new float[vertexData.length * 4/3];
        for (int i = 0; i < vertexData.length / 3; i++)
        {
            System.arraycopy(vertexData, i*3, newVertexData, i * 4, 3);
            newVertexData[i*4+3] = 1f;
        }
        vertexData = newVertexData;
    }

    private void CalculateTextureCoordinates()
    {
        textureCoordinates = new float[vertexCount * TEXTURE_DATA_SIZE_IN_ELEMENTS];
        for (int vertex = 0; vertex < vertexCount; vertex++)
        {
            float x = vertexData[vertex * 3];
            float y = vertexData[vertex * 3 + 1];
            float z = vertexData[vertex * 3 + 2];
            float longitude = (float)Math.atan2(y, x);
            float lattitude = (float)Math.asin(z);
            //lattitude = lattitude + (float)Math.PI/4;
            //longitude = longitude + 3 * (float)Math.PI/4;

            textureCoordinates[vertex * 2] = (float)((longitude + Math.PI) / (Math.PI * 2));
            textureCoordinates[vertex * 2 + 1] = (float)((lattitude + Math.PI/2) / Math.PI);
            if (textureCoordinates[vertex * 2] < 0) textureCoordinates[vertex * 2] = 0f;
            if (textureCoordinates[vertex * 2] > 1) textureCoordinates[vertex * 2] = 1f;
            if (textureCoordinates[vertex * 2 + 1] < 0) textureCoordinates[vertex * 2] = 0f;
            if (textureCoordinates[vertex * 2 + 1] > 1) textureCoordinates[vertex * 2] = 1f;
        }
        // center all x coordinates in original 100%
        for (int vertex = 0; vertex < vertexCount; vertex++)
        {
            textureCoordinates[vertex * 2] = 1f/12f + 10f/12f * textureCoordinates[vertex * 2];
        }
        // Check each set of 3 coordinates for crossing edges.  Move some if necessary
        for (int vertex = 0; vertex < vertexCount; vertex = vertex + 3)
        {
            if (textureCoordinates[vertex * 2] < 0.35f)
            {
                if (textureCoordinates[(vertex + 1) * 2] > 0.65f)
                {
                    textureCoordinates[(vertex + 1) * 2] = textureCoordinates[(vertex + 1) * 2] - 10f/12f;
                }
                if (textureCoordinates[(vertex + 2) * 2] > 0.65f)
                {
                    textureCoordinates[(vertex + 2) * 2] = textureCoordinates[(vertex + 2) * 2] - 10f/12f;
                }
            }
            if (textureCoordinates[vertex * 2] > 0.65f)
            {
                if (textureCoordinates[(vertex + 1) * 2] < 0.35f)
                {
                    textureCoordinates[(vertex + 1) * 2] = textureCoordinates[(vertex + 1) * 2] + 10f/12f;
                }
                if (textureCoordinates[(vertex + 2) * 2] < 0.35f)
                {
                    textureCoordinates[(vertex + 2) * 2] = textureCoordinates[(vertex + 2) * 2] + 10f/12f;
                }
            }
        }
    }

    private void AddTextureCoordinates()
    {
        vertexDataWithTextureCoordinates = new float[vertexData.length + textureCoordinates.length];
        System.arraycopy(vertexData, 0, vertexDataWithTextureCoordinates, 0, vertexData.length);
        System.arraycopy(textureCoordinates, 0, vertexDataWithTextureCoordinates, vertexData.length,
                textureCoordinates.length);
    }

    protected void init ()
    {
        vertexData = Icosahedron.GetDividedTriangles(2);
        vertexCount = vertexData.length / 3;  // Icosahedron class only uses 3 floats per vertex
        CalculateTextureCoordinates();
        ScaleCoordinates(0.8f, 0.5f);
        AddFourthCoordinate();
        vertexCount = vertexData.length / COORDS_PER_VERTEX;
        texCoordOffset = 4 * 4 * vertexCount;
        AddTextureCoordinates();
        InitializePrograms();
        initializeVertexBuffer(vertexDataWithTextureCoordinates);
        CreateSampler();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //current_texture = Textures.Load("Mars_MGS_colorhillshade_mola_1024.jpg", 1);
        current_texture = Textures.loadTexture(Shader.context, R.drawable.venus_magellan, true);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);

        setupDepthAndCull();
    }

    public void display()
    {
        clearDisplay();

        GLES20.glUseProgram(simpleTextureProgram.theProgram);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, current_texture);
        //GLES20.glBindSampler(texUnit, sampler);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glEnableVertexAttribArray(simpleTextureProgram.position);
        GLES20.glEnableVertexAttribArray(simpleTextureProgram.texCoord);
        GLES20.glVertexAttribPointer(simpleTextureProgram.position, POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT,
                false, POSITION_STRIDE, 0);
        GLES20.glVertexAttribPointer(simpleTextureProgram.texCoord, TEXTURE_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT,
                false, TEXTURE_STRIDE, texCoordOffset);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(simpleTextureProgram.position);
        GLES20.glDisableVertexAttribArray(simpleTextureProgram.texCoord);

        //GLES20.glBindSampler(texUnit, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glUseProgram(0);
    }
}
