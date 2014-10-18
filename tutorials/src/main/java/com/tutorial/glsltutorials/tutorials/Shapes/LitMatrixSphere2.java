package com.tutorial.glsltutorials.tutorials.Shapes;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;

/**
 * Created by jamie on 10/12/14.
 */
public class LitMatrixSphere2 extends Shape {
    float radius;

    String VertexShader = VertexShaders.PosOnlyWorldTransform_vert;
    String FragmentShader = FragmentShaders.ColorUniform_frag;

    int progarmNumber;

    public LitMatrixSphere2 (float radius_in)
    {
        radius = radius_in;
        vertexCoords = GetCircleCoords(radius);
        vertexCount = vertexCoords.length / COORDS_PER_VERTEX / 2;

        vertexData = vertexCoords;
        SetupSimpleIndexBuffer(COORDS_PER_VERTEX);

        InitializeVertexBuffer();

        progarmNumber = Programs.AddProgram(VertexShader, FragmentShader);
    }

    private float[] GetCircleCoords(float radius)
    {
        float[] coords = Icosahedron.triangles.clone();
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

    private void DrawSub(int first_triangle, int last_triangle)
    {
        int newVertexCount = (last_triangle - first_triangle + 1) * 3 * 3 / COORDS_PER_VERTEX;
        // Add program to OpenGL environment

        Matrix4f mm = Rotate(modelToWorld, axis, angle);
        mm.M41 = offset.x;
        mm.M42 = offset.y;
        mm.M43 = offset.z;

        Programs.Draw(progarmNumber, vertexBufferObject, indexBufferObject, cameraToClip, worldToCamera, mm,
                indexData.length, color, COORDS_PER_VERTEX, vertexStride);
    }

    public void Draw() {
        DrawSub(0, 19);
    }

    public void DrawSemi(int first_triangle, int last_triangle)
    {
        DrawSub(first_triangle, last_triangle);
    }
}
