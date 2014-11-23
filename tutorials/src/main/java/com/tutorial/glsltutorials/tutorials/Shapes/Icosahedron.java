package com.tutorial.glsltutorials.tutorials.Shapes;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.ArrayList;

/**
 * Created by Jamie on 1/5/14.
 */
public class Icosahedron {
    static {Create();}
    static float[][] vertices;
    
    public static float[] triangles;

    public static ArrayList<Vector3f> vertexList;
    public static ArrayList<Triangle2> triangleList;

    static float[] CalculateVertex(float theta, float phi)
    {
        float[] result = new float[3];

        result[0] = (float)(Math.cos(theta) * Math.cos(phi));
        result[1] = (float)(Math.sin(theta) * Math.cos(phi));
        result[2] = (float)(Math.sin(phi));
        return result;
    }

    static void CreateVertices()
    {
            /*
        The locations of the vertices of a regular icosahedron can be described using spherical coordinates,
         for instance as latitude and longitude. If two vertices are taken to be at the north and south poles
          (latitude ±90°), then the other ten vertices are at latitude ±arctan(1/2) ≈ ±26.57°. These ten vertices
           are at evenly spaced longitudes (36° apart), alternating between north and south latitudes.
         */
        float latitude_angle = (float)Math.atan(0.5);
        float longitude_angle = (float)(Math.PI / 5.0);
        vertices = new float[12][3];
        vertices[0] = CalculateVertex(0, (float)(Math.PI/2.0f));
        vertices[1] = CalculateVertex(0, (float)(-Math.PI/2.0f));
        vertices[2] = CalculateVertex(longitude_angle * 0, latitude_angle);
        vertices[3] = CalculateVertex(longitude_angle * 2, latitude_angle);
        vertices[4] = CalculateVertex(longitude_angle * 4, latitude_angle);
        vertices[5] = CalculateVertex(longitude_angle * 6, latitude_angle);
        vertices[6] = CalculateVertex(longitude_angle * 8, latitude_angle);
        vertices[7] = CalculateVertex(longitude_angle * 1, -latitude_angle);
        vertices[8] = CalculateVertex(longitude_angle * 3, -latitude_angle);
        vertices[9] = CalculateVertex(longitude_angle * 5, -latitude_angle);
        vertices[10] = CalculateVertex(longitude_angle * 7, -latitude_angle);
        vertices[11] = CalculateVertex(longitude_angle * 9, -latitude_angle);
    }

    static int triangle_count;

    static void AddTriangle(float[] vertex1, float[] vertex2, float[] vertex3)
    {
        float[] vertex_temp = new float[3];
        int point_count = triangle_count * 9;
        for (int vertex = 0; vertex < 3; vertex++)
        {
            switch (vertex)
            {
                case 0: vertex_temp = vertex1; break;
                case 1: vertex_temp = vertex2; break;
                case 2: vertex_temp = vertex3; break;
            }
            for (int point = 0; point < 3; point++)
            {
                triangles[point_count++] = vertex_temp[point];
            }
        }
        triangle_count++;
    }

    static void CreateTriangles()
    {
        triangle_count = 0;
        triangles = new float[20 * 3 * 3];
        AddTriangle(vertices[0], vertices[2], vertices[3]);
        AddTriangle(vertices[0], vertices[3], vertices[4]);
        AddTriangle(vertices[0], vertices[4], vertices[5]);
        AddTriangle(vertices[0], vertices[5], vertices[6]);
        AddTriangle(vertices[0], vertices[6], vertices[2]);

        AddTriangle(vertices[2], vertices[7], vertices[3]);
        AddTriangle(vertices[3], vertices[8], vertices[4]);
        AddTriangle(vertices[4], vertices[9], vertices[5]);
        AddTriangle(vertices[5], vertices[10], vertices[6]);
        AddTriangle(vertices[6], vertices[11], vertices[2]);

        AddTriangle(vertices[2], vertices[11], vertices[7]);
        AddTriangle(vertices[3], vertices[7], vertices[8]);
        AddTriangle(vertices[4], vertices[8], vertices[9]);
        AddTriangle(vertices[5], vertices[9], vertices[10]);
        AddTriangle(vertices[6], vertices[10], vertices[11]);

        AddTriangle(vertices[7], vertices[1], vertices[8]);
        AddTriangle(vertices[8], vertices[1], vertices[9]);
        AddTriangle(vertices[9], vertices[1], vertices[10]);
        AddTriangle(vertices[10], vertices[1], vertices[11]);
        AddTriangle(vertices[11], vertices[1], vertices[7]);

    }
    static void Create()
    {
        CreateVertices();
        CreateTriangles();
        CreateVertexList();
        CreateTriangleList();
    }

    public static void CreateVertexList()
    {
        vertexList = new ArrayList<Vector3f>();
        for (int i = 0; i < triangles.length; i = i + 3)
        {
            vertexList.add(new Vector3f(triangles[i], triangles[i+1], triangles[i+2]));
        }
    }

    public static void CreateTriangleList()
    {
        triangleList = new ArrayList<Triangle2>();
        for (int i = 0; i < vertexList.size(); i = i + 3)
        {
            triangleList.add(new Triangle2(vertexList.get(i), vertexList.get(i+1), vertexList.get(i+2)));
        }
    }


    public static ArrayList<Triangle2> DivideTriangles(ArrayList<Triangle2> triangles)
    {
        ArrayList<Triangle2> result = new ArrayList<Triangle2>();
        for (int i = 0; i < triangles.size(); i++)
        {
            result.addAll(triangles.get(i).Divide());
        }
        return result;
    }

    public static ArrayList<Vector3f> GetVertices(ArrayList<Triangle2> triangles)
    {
        ArrayList<Vector3f> result = new ArrayList<Vector3f>();
        for (int i = 0; i < triangles.size(); i++)
        {
            result.addAll(triangles.get(i).GetVertices());
        }
        return result;
    }

    public static ArrayList<Vector3f> NormalizeVertices(ArrayList<Vector3f> vertices)
    {
        ArrayList<Vector3f> result = new ArrayList<Vector3f>();
        for (Vector3f v : vertices)
        {
            result.add(v.normalize());
        }
        return result;
    }

    public static float[] GetFloatsFromVertices(ArrayList<Vector3f> vertices)
    {
        float[] result = new float[vertices.size() * 3];
        for (int i = 0; i < vertices.size(); i++)
        {
            System.arraycopy(vertices.get(i).toArray(), 0, result, i * 3, 3);
        }
        return result;
    }

    public static float[] GetFloats(ArrayList<Triangle2> triangles)
    {
        float[] result = new float[triangles.size() * 9];
        for (int i = 0; i < triangles.size(); i++)
        {
            System.arraycopy(triangles.get(i).GetFloats(), 0, result, i * 9, 9);
        }
        return result;
    }

    public static float[] GetDividedTriangles(int divideCount)
    {
        ArrayList<Triangle2> result = new ArrayList<Triangle2>();
        for (Triangle2 t : triangleList)
        {
            result.add(t.Clone());
        }
        while (divideCount-- > 0)
        {
            result = DivideTriangles(result);
        }
        ArrayList<Vector3f> vertices = GetVertices(result);
        vertices = NormalizeVertices(vertices);
        return GetFloatsFromVertices(vertices);
    }
}
