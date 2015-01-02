package com.tutorial.glsltutorials.tutorials.ProgramData;

/**
 * Created by jamie on 1/1/15.
 */
public class ProgramSet {
    public String name;
    public String vertexShader;
    public String fragmentShader;
    public ProgramSet (String nameIn, String vertexShaderIn, String fragmentShaderIn)
    {
        name = nameIn;
        vertexShader = vertexShaderIn;
        fragmentShader = fragmentShaderIn;
    }
}
