package com.tutorial.glsltutorials.tutorials.Shapes;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.R;

/**
 * Created by jamie on 10/12/14.
 */
public class TextureSphere extends Shape {
    float radius;
    int TEXTURE_DATA_SIZE_IN_ELEMENTS = 2;
    float[] textureCoordinates;
    float[] vertexDataWithTextureCoordinates;
    static int g_colorTexUnit = 0;
    int texture = R.drawable.venus_magellan;

    public TextureSphere(float radiusIn, int texture)
    {
        radius = radiusIn;
        vertexCoords = getCircleCoords(1f);
        vertexCount = vertexCoords.length / COORDS_PER_VERTEX / 2;

        vertexData = vertexCoords;
        setupSimpleIndexBuffer();
        calculateTextureCoordinates();
        scaleCoordinates(radius);
        addTextureCoordinates();

        initializeVertexBuffer();

        programNumber = Programs.addProgram(VertexShaders.MatrixTexture,
                FragmentShaders.MatrixTexture);
        Programs.setUniformTexture(programNumber, g_colorTexUnit);
        Programs.loadTexture(programNumber, texture, true);

    }

    private void calculateTextureCoordinates()
    {
        textureCoordinates = new float[vertexCount * TEXTURE_DATA_SIZE_IN_ELEMENTS];
        for (int vertex = 0; vertex < vertexCount; vertex++)
        {
            float x = vertexData[vertex * 6];
            float y = vertexData[vertex * 6 + 1];
            float z = vertexData[vertex * 6 + 2];
            float longitude = (float)Math.atan2(y, x);
            float latitude = (float)Math.asin(z);

            textureCoordinates[vertex * 2] = (float)((longitude + Math.PI) / (Math.PI * 2));
            textureCoordinates[vertex * 2 + 1] = (float)((latitude + Math.PI/2) / Math.PI);
            if (textureCoordinates[vertex * 2] < 0) textureCoordinates[vertex * 2] = 0f;
            if (textureCoordinates[vertex * 2] > 1) textureCoordinates[vertex * 2] = 1f;
            if (textureCoordinates[vertex * 2 + 1] < 0) textureCoordinates[vertex * 2 + 1] = 0f;
            if (textureCoordinates[vertex * 2 + 1] > 1) textureCoordinates[vertex * 2 + 1] = 1f;
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

    private void addTextureCoordinates()
    {
        vertexDataWithTextureCoordinates = new float[vertexData.length + textureCoordinates.length];
        for (int i = 0; i < vertexCount; i++)
        {
            System.arraycopy(vertexData, 6 * i, vertexDataWithTextureCoordinates, 8 * i, 6);
            System.arraycopy(textureCoordinates, 2 * i, vertexDataWithTextureCoordinates, 8 * i + 6,
                    2);
        }
        vertexData = vertexDataWithTextureCoordinates;
    }

    private void scaleCoordinates(float scale)
    {
        for (int i = 0; i < vertexCount; i++)
        {
            vertexData[6 * i + 0] = vertexData[6 * i + 0] * scale;
            vertexData[6 * i + 1] = vertexData[6 * i + 1] * scale;
            vertexData[6 * i + 2] = vertexData[6 * i + 2] * scale;
        }
    }

    private float[] getCircleCoords(float radius)
    {
        float[] coords = Icosahedron.GetDividedTriangles(2);
        float[] coords_with_normals = new float[2*coords.length];
        int j = 0;
        for (int i = 0; i < coords.length * 2; i++)
        {
            switch (i % 6)
            {
                case 0:
                case 1:
                case 2:
                    coords_with_normals[i] = coords[j] * radius;
                    j++;
                    break;
                case 3:  coords_with_normals[i] = coords[j-3]; break;
                case 4:  coords_with_normals[i] = coords[j-2]; break;
                case 5:  coords_with_normals[i] = coords[j-1]; break;

            }

        }
        return coords_with_normals;
    }

    private void drawSub(int first_triangle, int last_triangle)
    {
        int newVertexCount = (last_triangle - first_triangle + 1) * 3 * 3 / COORDS_PER_VERTEX;
        // Add program to OpenGL environment

        Matrix4f mm = rotate(modelToWorld, axis, angle);
        mm.M41 = offset.x;
        mm.M42 = offset.y;
        mm.M43 = offset.z;

        Programs.draw(programNumber, vertexBufferObject[0], indexBufferObject[0], mm, indexData.length, color);
    }

    public void draw() {
        drawSub(0, 19);
    }

    public void DrawSemi(int first_triangle, int last_triangle)
    {
        drawSub(first_triangle, last_triangle);
    }
}
