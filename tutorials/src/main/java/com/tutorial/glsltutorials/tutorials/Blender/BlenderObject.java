package com.tutorial.glsltutorials.tutorials.Blender;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.util.ArrayList;

/**
 * Created by jamie on 10/12/14.
 */
public class BlenderObject extends Shape {
    public String Name;
    ArrayList<Float> vertexes;
    ArrayList<Short> indexes;

    String VertexShader = VertexShaders.PosOnlyWorldTransform_vert;
    String FragmentShader = FragmentShaders.ColorUniform_frag;
    int progarmNumber;

    public BlenderObject (String nameIn)
    {
        Name = nameIn;
        vertexes = new ArrayList<Float>();
        indexes = new ArrayList<Short>();
    }

    // v -1.458010 -3.046922 2.461986
    public void AddVertex(String vertexInfo)
    {
        ArrayList<Float> newVertexes = new ArrayList<Float>();
        String[] vertexData = vertexInfo.substring(2).split(" ");
        for (String s : vertexData)
        {
            newVertexes.add(Float.parseFloat(s));
        }
        vertexes.addAll(newVertexes);
    }

    public void AddTriangle(String triangleInfo, short offset)
    {
        ArrayList<Short> newIndexes = new ArrayList<Short>();
        String[] indexData = triangleInfo.substring(2).split(" ");
        for (String s : indexData)
        {
            newIndexes.add(((short)(Short.parseShort(s) - offset)));
        }
        indexes.addAll(newIndexes);
    }

    public void Setup()
    {
        int i;
        progarmNumber = Programs.AddProgram(VertexShader, FragmentShader);

        vertexCount = indexes.size();
        vertexStride = 3 * 4; // no color for now
        // fill in index data
        indexData = new short[indexes.size()];
        i = 0;
        for (Short s : indexes) {
            indexData[i++] = (s != null ? s : 0);
        }

        // fill in vertex data
        vertexData = new float[vertexes.size()];
        i = 0;
        for (Float f : vertexes) {
            vertexData[i++] = (f != null ? f :  0);
        }

        InitializeVertexBuffer();
    }

    public void Scale(Vector3f size)
    {
        modelToWorld.M11 = size.x;
        modelToWorld.M22 = size.y;
        modelToWorld.M33 = size.z;
    }

    public void Draw()
    {
        Matrix4f mm = Rotate(modelToWorld, axis, angle);
        mm.M41 = offset.x;
        mm.M42 = offset.y;
        mm.M43 = offset.z;

        Programs.Draw(progarmNumber, vertexBufferObject, indexBufferObject, cameraToClip, worldToCamera, mm,
                indexData.length, color, COORDS_PER_VERTEX, vertexStride);
    }
}
