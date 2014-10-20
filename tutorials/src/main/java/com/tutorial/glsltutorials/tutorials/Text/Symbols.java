package com.tutorial.glsltutorials.tutorials.Text;

import android.graphics.Rect;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 10/19/14.
 */
public class Symbols 
{
    public static Float[] Dash;
    public static Float[] Space;
    public static Float[] Period;
    public static Float[] Equals;

    static
    {
        AddDash();
        AddSpace();
        AddPeriod();
        AddEquals();
    }

    private static Float[] MoveX(Float[] input, float distance)
    {
        for (int i = 0; i < input.length; i = i + 3)
        {
            input[i] = input[i] + distance;
        }
        return input;
    }

    private static Float[] MoveY(Float[] input, float distance)
    {
        for (int i = 1; i < input.length; i = i + 3)
        {
            input[i] = input[i] + distance;
        }
        return input;
    }

    private static void AddVertex(Float[] symbol, int vertexNumber, Vector3f vertex)
    {
        symbol[vertexNumber * 3 + 0] = vertex.x;
        symbol[vertexNumber * 3 + 1] = vertex.y;
        symbol[vertexNumber * 3 + 2] = vertex.z;
    }

    public static Float[] Rectangle(Float width, Float height)
    {
        Float X0 = -width/2;
        Float X1 = width/2;
        Float Y0 = height/2;
        Float Y1 = -height/2;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y1, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y1, 0f);

        Float[] rectangle = new Float [18];
        AddVertex(rectangle, 0, V0);
        AddVertex(rectangle, 1, V1);
        AddVertex(rectangle, 2, V2);
        AddVertex(rectangle, 3, V2);
        AddVertex(rectangle, 4, V1);
        AddVertex(rectangle, 5, V3);
        return rectangle;
    }

    private static void AddDash()
    {
        Dash = Rectangle(6f, 2f);
    }

    private static void AddSpace()
    {
        Space = new Float[9];
        // Zero Size Triangle
        for (int i = 0; i < Space.length; i++)
        {
            Space[i] = 0f;
        }
    }

    private static void AddPeriod()
    {
        Period = Rectangle(2f, 2f);
        Period = MoveY(Period, -5f);
    }

    private static void AddEquals()
    {
        Equals = new Float[36];
        Float[] rectangle = Rectangle(6f, 1f);
        rectangle = MoveY(rectangle, 3f);
        System.arraycopy(rectangle, 0, Equals, 0, rectangle.length);
        rectangle = MoveY(rectangle, -6f);
        System.arraycopy(rectangle, 0, Equals, 18, rectangle.length);
    }
}
