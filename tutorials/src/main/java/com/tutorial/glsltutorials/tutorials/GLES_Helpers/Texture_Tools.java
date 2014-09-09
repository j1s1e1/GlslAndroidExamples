package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.tutorial.glsltutorials.tutorials.Geometry.*;

/**
 * Created by Jamie on 5/1/14.
 */
public class Texture_Tools {


    public static int SetupTexture (final Context context, final int resourceId)
    {
        return loadTexture(context, resourceId);
    }

    public static Vector2f[] DivideShaderTriangle(Vector2f[] shader_triangles, int index)
    {
        Vector2f[] shader_triangle = new Vector2f[3];
        shader_triangle[0] = shader_triangles[index++];
        shader_triangle[1] = shader_triangles[index++];
        shader_triangle[2] = shader_triangles[index++];
        Vector2f[] four_shader_triangles = new Vector2f[12];
        Vector2f[] new_shader_points = new Vector2f[3];
        new_shader_points[0] = Vector2f.Midpoint(shader_triangle[0], shader_triangle[1]);
        new_shader_points[1] = Vector2f.Midpoint (shader_triangle[1], shader_triangle[2]);
        new_shader_points[2] = Vector2f.Midpoint (shader_triangle[2], shader_triangle[0]);
        four_shader_triangles[0] = shader_triangle[0];
        four_shader_triangles[1] = new_shader_points[0];
        four_shader_triangles[2] = new_shader_points[2];

        four_shader_triangles[3] = new_shader_points[2];
        four_shader_triangles[4] = new_shader_points[0];
        four_shader_triangles[5] = new_shader_points[1];

        four_shader_triangles[6] = new_shader_points[0];
        four_shader_triangles[7] = shader_triangle[1];
        four_shader_triangles[8] = new_shader_points[1];

        four_shader_triangles[9] = new_shader_points[2];
        four_shader_triangles[10] = new_shader_points[1];
        four_shader_triangles[11] = shader_triangle[2];

        return four_shader_triangles;
    }

        public static Vector2f[] DivideShaderTriangles(Vector2f[] shader_triangles, int repeat)
        {
            Vector2f[] result = new Vector2f[shader_triangles.length * 4];
            for (int i = 0; i < shader_triangles.length; i = i + 3)
            {
                System.arraycopy(DivideShaderTriangle(shader_triangles, i), 0, result, i * 4, 12);
            }
            return result;
        }

    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }
}
