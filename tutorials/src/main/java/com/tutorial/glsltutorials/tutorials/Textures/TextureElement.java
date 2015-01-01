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

   float scale = 1.0f;
   Vector3f lightPosition = new Vector3f(0f, 0f, 1f);

    public TextureElement(Bitmap bitmap)
    {
        texture = Textures.createFromBitmap(bitmap);
        setup();
    }

    public TextureElement(int resourceID)
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

        programNumber = Programs.addProgram(VertexShaders.MatrixTexture, FragmentShaders.MatrixTexture);
        Programs.setUniformTexture(programNumber, texUnit);
        Programs.setTexture(programNumber, texture);
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
        lightPosition = lightPosition.add(v.mul(scale));
    }

    public void scale(float scaleIn)
    {
        scale = scale * scaleIn;
    }

    public void draw()
    {
        Programs.setLightPosition(programNumber, lightPosition);
        Matrix4f mm = rotate(modelToWorld, axis, angle);
        mm.M41 = offset.x;
        mm.M42 = offset.y;
        mm.M43 = offset.z;
        mm.Scale(new Vector3f(scale, scale, scale) );
        Programs.setTexture(programNumber, texture);
        Programs.draw(programNumber, vertexBufferObject, indexBufferObject, mm, indexData.length, color);
    }

}
