package com.tutorial.glsltutorials.tutorials.Mesh;

/**
 * Created by Jamie on 6/7/14.
 */
public class PrimitiveType {
    public String strPrimitiveName;
    public int eGLPrimType;
    public PrimitiveType()
    {
    }
    public PrimitiveType(String name, int mode)
    {
        strPrimitiveName = name;
        eGLPrimType = mode;
    }
}
