package com.tutorial.glsltutorials.tutorials.Blender;

import android.widget.Toast;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Portability.BitConverter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamie on 10/12/14.
 */
public class Blender {
    public Blender ()
    {
    }

    ArrayList<BlenderObject> blenderObjects;

    // These files were created from blender objects using C# version
    public String ReadBinaryFile(InputStream filename)
    {
        byte[] binaryBlenderObjects = new byte[4];
        blenderObjects = new ArrayList<BlenderObject>();
        int offset = 0;
        StringBuilder result = new StringBuilder();
        try {
            binaryBlenderObjects = new byte[filename.available()];
        }
        catch (Exception ex)
        {

        }

        int objectCount = BitConverter.ToInt32(binaryBlenderObjects, 0);
        result.append("Found " + String.valueOf(objectCount) + " Blender Objects");
        offset = offset + 4;
        for (int i = 0; i < objectCount; i++)
        {
            BlenderObject bo = new BlenderObject("Object" + String.valueOf(i) );
            int blenderObjectSize = bo.CreateFromBinaryData(binaryBlenderObjects, offset);
            offset = offset + blenderObjectSize;
            result.append("Object " + String.valueOf(i) + " size = " + String.valueOf(blenderObjectSize));
            bo.Setup();
            blenderObjects.add(bo);
        }
        return result.toString();
    }

    public void ReadFile(InputStream filename)
    {
        String nextLine;
        blenderObjects = new ArrayList<BlenderObject>();
        try
        {
            BufferedReader sr = new BufferedReader(new InputStreamReader(filename));
            short vertexCount = 1;
            short normalCount = 1;
            short previousObjectVertexCount = 1;  // change from 1 to zero based
            short previousObjectNormalCount = 1;  // change from 1 to zero based
            nextLine = sr.readLine();
            while (nextLine != null)
            {
                if (nextLine.substring(0, 1).equals("o"))
                {
                    BlenderObject bo = new BlenderObject(nextLine);
                    while (nextLine != null)
                    {

                        nextLine = sr.readLine();
                        if (nextLine == null) break;
                        if (nextLine.substring(0, 1).equals("o")) break;
                        if (nextLine.substring(0, 1).equals("v"))
                        {
                            if (nextLine.substring(1, 2).equals(" "))
                            {
                                bo.AddVertex(nextLine);
                                vertexCount++;
                            }
                            if (nextLine.substring(1, 2).equals("n"))
                            {
                                bo.AddNormal(nextLine);
                                normalCount++;
                            }
                        }
                        if (nextLine.substring(0, 1).equals("f"))
                        {
                            bo.AddTriangle(nextLine, previousObjectVertexCount, previousObjectNormalCount);
                        }
                    }
                    previousObjectVertexCount = vertexCount;
                    previousObjectNormalCount = normalCount;
                    bo.Setup();
                    blenderObjects.add(bo);
                }
                else
                {
                    if (nextLine != null) nextLine = sr.readLine();
                }
            }
        }
        catch (Throwable ignore)
        {

        }
    }

    public int ObjectCount()
    {
        return blenderObjects.size();
    }

    public void Draw()
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.Draw();
        }
    }

    public void Scale(Vector3f scale)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.Scale(scale);
        }
    }

    public void SetOffset(Vector3f offset)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.SetOffset(offset);
        }
    }

    public void SetColor(float[] color)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.SetColor(color);
        }
    }

    public void RotateShapes(Vector3f rotationAxis, float angle)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.RotateShape(rotationAxis, angle);
        }
    }

    public void SetProgram(int program)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.SetProgram(program);
        }
    }
}
