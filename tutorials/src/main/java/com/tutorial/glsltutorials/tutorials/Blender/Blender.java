package com.tutorial.glsltutorials.tutorials.Blender;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Portability.BitConverter;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by jamie on 10/12/14.
 */
public class Blender extends Shape {
    public Blender ()
    {
    }

    ArrayList<BlenderObject> blenderObjects;
    Vector3f currentOffset = new Vector3f();

    // These files were created from blender objects using C# version
    public String readBinaryFile(InputStream filename)
    {
        byte[] binaryBlenderObjects = new byte[4];
        blenderObjects = new ArrayList<BlenderObject>();
        int offset = 0;
        StringBuilder result = new StringBuilder();
        try {
            binaryBlenderObjects = new byte[filename.available()];
            filename.read(binaryBlenderObjects);

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
            int blenderObjectSize = bo.createFromBinaryData(binaryBlenderObjects, offset);
            offset = offset + blenderObjectSize;
            result.append("Object " + String.valueOf(i) + " size = " + String.valueOf(blenderObjectSize));
            bo.setup();
            blenderObjects.add(bo);
        }
        return result.toString();
    }

    public void readFromResource(int resource) {
        InputStream resourceData = Shader.context.getResources().openRawResource(resource);
        readBinaryFile(resourceData);
    }

    public void readFile(InputStream filename)
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
                                bo.addVertex(nextLine);
                                vertexCount++;
                            }
                            if (nextLine.substring(1, 2).equals("n"))
                            {
                                bo.addNormal(nextLine);
                                normalCount++;
                            }
                        }
                        if (nextLine.substring(0, 1).equals("f"))
                        {
                            bo.addTriangle(nextLine, previousObjectVertexCount, previousObjectNormalCount);
                        }
                    }
                    previousObjectVertexCount = vertexCount;
                    previousObjectNormalCount = normalCount;
                    bo.setup();
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

    public int objectCount()
    {
        return blenderObjects.size();
    }

    public void draw()
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.draw();
        }
    }

    public void scale(Vector3f scale)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.scale(scale);
        }
    }

    public void setOffset(Vector3f offset)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.setOffset(offset);
        }
    }

    public Vector3f getOffset()
    {
        return currentOffset;
    }

    public void setColor(float[] color)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.setColor(color);
        }
    }

    public void rotateShapes(Vector3f rotationAxis, float angle)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.rotateShape(rotationAxis, angle);
        }
    }

    public void setProgram(int program)
    {
        for (BlenderObject bo : blenderObjects)
        {
            bo.setProgram(program);
        }
    }

    public void face(Vector3f direction)
    {
        Vector3f axis = new Vector3f();
        float angle = 0;
        rotateShapes(direction, 10f);
    }
}
