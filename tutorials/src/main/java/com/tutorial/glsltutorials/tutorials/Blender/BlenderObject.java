package com.tutorial.glsltutorials.tutorials.Blender;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Portability.BitConverter;
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
    ArrayList<Float> normals;
    ArrayList<String> vertexNormalIndexes;
    ArrayList<Float> vertexNormals;


    public BlenderObject (String nameIn)
    {
        Name = nameIn;
        vertexes = new ArrayList<Float>();
        indexes = new ArrayList<Short>();
        normals = new ArrayList<Float>();
        vertexNormalIndexes = new ArrayList<String>();
        vertexNormals = new ArrayList<Float>();
    }

    // v -1.458010 -3.046922 2.461986
    public void addVertex(String vertexInfo)
    {
        ArrayList<Float> newVertexes = new ArrayList<Float>();
        String[] vertexData = vertexInfo.substring(2).split(" ");
        for (String s : vertexData)
        {
            newVertexes.add(Float.parseFloat(s));
        }
        vertexes.addAll(newVertexes);
    }

    public void addNormal(String normalInfo)
    {
        ArrayList<Float> newNormal =  new   ArrayList<Float>();
        String[] normalData = normalInfo.substring(3).split(" ");
        for (String s : normalData)
        {
            newNormal.add(Float.parseFloat(s));
        }
        normals.addAll(newNormal);
    }

    public void addTriangle(String triangleInfo,  short vertexOffset, short normalOffset)
    {
        ArrayList<Short> newIndexes = new ArrayList<Short>();
        if (triangleInfo.contains("/"))
        {
            String[] selections = triangleInfo.substring(2).split(" ");
            for (int i = 0; i < selections.length; i++)
            {
                if (vertexNormalIndexes.contains(selections[i]))
                {
                    newIndexes.add((short) vertexNormalIndexes.indexOf(selections[i]));
                }
                else
                {
                    vertexNormalIndexes.add(selections[i]);
                    String[] selections_parts = selections[i].split ("/");
                    ArrayList<Float> newVertexNormal = new ArrayList<Float>();
                    int vertexIndex = Integer.parseInt(selections_parts[0]) - vertexOffset;
                    int normalIndex = Integer.parseInt(selections_parts[2]) - normalOffset;
                    newVertexNormal.addAll(vertexes.subList(vertexIndex * 3, vertexIndex * 3 + 3));
                    newVertexNormal.addAll(normals.subList(normalIndex * 3, normalIndex * 3 + 3));
                    vertexNormals.addAll(newVertexNormal);
                    newIndexes.add((short) vertexNormalIndexes.indexOf(selections[i]));
                }
            }
        }
        else
        {
            String[] indexData = triangleInfo.substring(2).split(" ");
            for (String s : indexData)
            {
                newIndexes.add(((short)(Short.parseShort(s) - vertexOffset)));
            }

        }
        indexes.addAll(newIndexes);
    }

    public void setup()
    {
        int i;
        vertexCount = indexes.size();
        // fill in index data
        indexData = new short[indexes.size()];
        i = 0;
        for (Short s : indexes) {
            indexData[i++] = (s != null ? s : 0);
        }

        if (vertexNormals.size() > 0)
        {
            VertexShader = VertexShaders.lms_vertexShaderCode;
            FragmentShader = FragmentShaders.lms_fragmentShaderCode;

            vertexData = new float[vertexNormals.size()];
            i = 0;
            for (Float f : vertexNormals) {
                vertexData[i++] = (f != null ? f :  0);
            }
        }
        else
        {
            vertexStride = 3 * 4; // position only
            // fill in vertex data
            vertexData = new float[vertexes.size()];
            i = 0;
            for (Float f : vertexes) {
                vertexData[i++] = (f != null ? f :  0);
            }
        }
        initializeVertexBuffer();
        programNumber = Programs.addProgram(VertexShader, FragmentShader);
    }

    public void scale(Vector3f size)
    {
        modelToWorld.M11 = size.x;
        modelToWorld.M22 = size.y;
        modelToWorld.M33 = size.z;
    }

    public void draw()
    {
        Programs.draw(programNumber, vertexBufferObject[0], indexBufferObject[0], modelToWorld, indexData.length, color);
    }

    public ArrayList<Byte> getBinaryBlenderObject()
    {
        ArrayList<Byte> binaryBlenderObjectBytes = new ArrayList<Byte>();
        Integer vertexBytes = vertexes.size() * 4;
        Integer indexBytes = indexes.size() * 2;
        Integer normalBytes = normals.size() * 4;
        Integer vertexNormalBytes = vertexNormals.size() * 4;


        binaryBlenderObjectBytes.addAll(BitConverter.GetByteList(vertexBytes));
        binaryBlenderObjectBytes.addAll(BitConverter.GetByteList(indexBytes));
        binaryBlenderObjectBytes.addAll(BitConverter.GetByteList(normalBytes));
        binaryBlenderObjectBytes.addAll(BitConverter.GetByteList(vertexNormalBytes));

        if (vertexBytes > 0)
        {
            binaryBlenderObjectBytes.addAll(BitConverter.GetByteListFromFloatList(vertexes));
        }
        if (indexBytes > 0)
        {
            binaryBlenderObjectBytes.addAll(BitConverter.GetByteListFromShortList(indexes));
        }
        if (normalBytes > 0)
        {
            binaryBlenderObjectBytes.addAll(BitConverter.GetByteListFromFloatList(normals));
        }
        if (vertexNormalBytes > 0)
        {
            binaryBlenderObjectBytes.addAll(BitConverter.GetByteListFromFloatList(vertexNormals));
        }
        return binaryBlenderObjectBytes;
    }

    public int createFromBinaryData(byte[] binaryBlenderObjects, int offset)
    {
        int blenderHeaderBytes = 16;
        int vertexBytes = BitConverter.ToInt32(binaryBlenderObjects, offset);
        offset = offset + 4;
        int indexBytes = BitConverter.ToInt32(binaryBlenderObjects, offset);
        offset = offset + 4;
        int normalBytes = BitConverter.ToInt32(binaryBlenderObjects, offset);
        offset = offset + 4;
        int vertexNormalBytes = BitConverter.ToInt32(binaryBlenderObjects, offset);
        offset = offset + 4;
        if (vertexBytes > 0)
        {
            for (int i = 0; i < vertexBytes; i = i + 4)
            {
                vertexes.add(BitConverter.ToSingle(binaryBlenderObjects, offset));
                offset = offset + 4;
            }
        }
        if (indexBytes > 0)
        {
            for (int i = 0; i < indexBytes; i = i + 2)
            {
                indexes.add(BitConverter.ToInt16(binaryBlenderObjects, offset));
                offset = offset + 2;
            }
        }
        if (normalBytes > 0)
        {
            for (int i = 0; i < normalBytes; i = i + 4)
            {
                normals.add(BitConverter.ToSingle(binaryBlenderObjects, offset));
                offset = offset + 4;
            }
        }
        if (vertexNormalBytes > 0)
        {
            for (int i = 0; i < vertexNormalBytes; i = i + 4)
            {
                vertexNormals.add(BitConverter.ToSingle(binaryBlenderObjects, offset));
                offset = offset + 4;
            }
        }
        return blenderHeaderBytes + vertexBytes + indexBytes + normalBytes + vertexNormalBytes;
    }
}
