package com.tutorial.glsltutorials.tutorials.Blender;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

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

    public void ReadFile(InputStream filename)
    {
        String nextLine;
        blenderObjects = new ArrayList<BlenderObject>();
        try
        {
            BufferedReader sr = new BufferedReader(new InputStreamReader(filename));
            short vertexcount = 1;
            short previousObjectVertexCount = 1;  // change from 1 to zero based
            nextLine = sr.readLine();
            while (nextLine != null)
            {
                if (nextLine.substring(0, 0) == "o")
                {
                    BlenderObject bo = new BlenderObject(nextLine);
                    while (nextLine != null)
                    {
                        nextLine = sr.readLine();
                        if (nextLine.substring(0, 0) == "o") break;
                        if (nextLine.substring(0, 0) == "v")
                        {
                            bo.AddVertex(nextLine);
                            vertexcount++;
                        }
                        if (nextLine.substring(0, 0) == "f") bo.AddTriangle(nextLine, previousObjectVertexCount);
                    }
                    previousObjectVertexCount = vertexcount;
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
}
