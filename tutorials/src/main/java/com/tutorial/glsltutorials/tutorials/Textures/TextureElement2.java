package com.tutorial.glsltutorials.tutorials.Textures;

import android.graphics.Bitmap;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

/**
 * Created by jamie on 12/25/14.
 */
public class TextureElement2 extends Shape
{
   int texture;
   int texUnit = 0;

   float scale = 1.0f;
   Vector3f lightPosition = new Vector3f(0f, 0f, 0f);

    public TextureElement2(Bitmap bitmap)
    {
        texture = Textures.createFromBitmap(bitmap);
        setup();
    }

    public TextureElement2(int resourceID)
    {
        texture = Textures.loadTexture(Shader.context, resourceID, false);
        setup();
    }

    private void setup()
    {
        vertexData = new float[]{
            // x y z xn yn zn tx ty
            -1f, -1f, 0f, 0f, 0f, 1f, 0f, 0f,
            -1f, 1f, 0f, 0f, 0f, 1f, 0f, 1f,
            1f, 1f, 0f, 0f, 0f, 1f, 1f, 1f,

            -1f, -1f, 0f, 0f, 0f, 1f, 0f, 0f,
            1f, 1f, 0f, 0f, 0f, 1f, 1f, 1f,
            1f, -1f, 0f, 0f, 0f, 1f, 1f, 0f
        };
        COORDS_PER_VERTEX = 8;
        setupSimpleIndexBuffer();
        initializeVertexBuffer();

        programNumber = Programs.addProgram(VertexShaders.MatrixTexture, FragmentShaders.MatrixTextureScale2);
        Programs.setUniformTexture(programNumber, texUnit);
        Programs.setTexture(programNumber, texture);
        Programs.setUniformScale(programNumber, scale);
    }

    public void replace(int resourceID)
    {
        int texture2 = Textures.loadTexture(Shader.context, resourceID, false);
        texture = texture2;
    }

    public void replace(Bitmap bitmap)
    {
        int texture2 = Textures.createFromBitmap(bitmap);
        texture = texture2;
    }

    public void move (Vector3f v)
    {
        modelToWorld.SetRow3(modelToWorld.GetRow3().add(new Vector4f(v, 0.0f)));
        lightPosition = lightPosition.add(v);
    }

    public void scale(float scaleIn)
    {
        super.scale(new Vector3f(scaleIn, scaleIn, scaleIn));
        scale = scale * scaleIn;
        lightPosition = lightPosition.mul(scaleIn);
    }

    public void setLightPosition(Vector3f v)
    {
        lightPosition = v;
    }


    public void draw()
    {
        Vector3f light = Vector3f.transform(lightPosition, modelToWorld);
        Programs.setLightPosition(programNumber, light);
        Programs.setUniformScale(programNumber, scale);
        Programs.setTexture(programNumber, texture);
        Programs.draw(programNumber, vertexBufferObject[0], indexBufferObject[0], modelToWorld, indexData.length, color);
    }

}
