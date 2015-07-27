package com.tutorial.glsltutorials.tutorials.Shapes;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;

/**
 * Created by jamie on 10/12/14.
 */
public class LitMatrixSphere2 extends Shape {
    float radius;
    int divideCount = 1;

    public LitMatrixSphere2 (float radius_in, int divideCountIn) {
        radius = radius_in;
        divideCount = divideCountIn;
        setup();
    }

    public LitMatrixSphere2 (float radius_in) {
        radius = radius_in;
        setup();
    }

    void setup()
    {
        vertexCoords = getCircleCoords();
        vertexCount = vertexCoords.length / COORDS_PER_VERTEX / 2;

        vertexData = vertexCoords;
        setupSimpleIndexBuffer();

        initializeVertexBuffer();

        programNumber = Programs.addProgram(VertexShaders.lms_vertexShaderCode,
                FragmentShaders.lms_fragmentShaderCode);
    }

    private float[] getCircleCoords()
    {
        float[] coords = Icosahedron.GetDividedTriangles(divideCount);
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
        int newVertexCount = indexData.length / 20 * (last_triangle - first_triangle + 1);

        Programs.draw(programNumber, vertexBufferObject, indexBufferObject, modelToWorld, newVertexCount, color);
    }

    public void setLightPosition(Vector3f lightPosition)
    {
        Programs.setLightPosition(programNumber, lightPosition);
    }

    public void draw() {
        drawSub(0, 19);
    }

    public void drawSemi(int first_triangle, int last_triangle)
    {
        drawSub(first_triangle, last_triangle);
    }
}
