package com.tutorial.glsltutorials.tutorials.Textures;

import android.graphics.Bitmap;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

/**
 * Created by jamie on 12/25/14.
 */
public class TextureElement extends Shape
{
   int texture;
   int texUnit = 0;
   static boolean texturesEnabled = false;

   float scale = 1.0f;
   Vector3f lightPosition = new Vector3f(0f, 0f, 0f);

    public TextureElement(Bitmap bitmap)
    {
        if (!texturesEnabled)
        {
            Textures.enableTextures();
        }
        texture = Textures.createFromBitmap(bitmap);
        setup();
    }

    public TextureElement(int resourceID)
    {
        if (!texturesEnabled)
        {
            Textures.enableTextures();
        }
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

        programNumber = Programs.addProgram(VertexShaders.MatrixTexture, FragmentShaders.MatrixTextureScale);
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
        super.move(v);
        lightPosition = lightPosition.add(v);
    }

    public void scale(float scaleIn)
    {
        super.scale(new Vector3f(scaleIn, scaleIn, scaleIn));
        scale = scale * scaleIn;
        lightPosition = lightPosition.mul(scaleIn);
    }

    public void rotateShape(Vector3f axis, float angle)
    {
        super.rotateShape(axis, angle);
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(axis, (float)Math.PI / 180.0f * angle);
        //lightPosition = Vector3f.Transform(lightPosition, rotation);
    }

    public void setLightPosition(Vector3f v)
    {
        lightPosition = v;
    }


    public void draw()
    {
        Vector3f light = Vector3f.Transform(lightPosition, modelToWorld);
        Programs.setLightPosition(programNumber, light);
        //Programs.setLightPosition(programNumber, lightPosition);
        Programs.setUniformScale(programNumber, scale);
        Matrix4f mm = rotate(modelToWorld, axis, angle);
        mm.M41 = offset.x;
        mm.M42 = offset.y;
        mm.M43 = offset.z;
        Programs.setTexture(programNumber, texture);
        Programs.draw(programNumber, vertexBufferObject, indexBufferObject, mm, indexData.length, color);
    }

}
