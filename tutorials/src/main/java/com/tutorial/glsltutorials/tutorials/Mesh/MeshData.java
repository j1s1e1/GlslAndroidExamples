package com.tutorial.glsltutorials.tutorials.Mesh;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.VAOMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jamie on 6/7/14.
 */
public class MeshData {
    public int[] oAttribArraysBuffer;  // switched to array
    public int[] oIndexBuffer; // switched to array

    public int 		positionAttribute = -1;
    public int 		colorAttribute = -1;
    public int 		normalAttribute = -1;

    public int 		positionSize = -1;
    public int 		colorSize = -1;
    public int 		normalSize = -1;

    public int 		positionOffset = -1;
    public int 		colorOffset = -1;
    public int 		normalOffset = -1;

    public int 		positionStride = -1;
    public int 		colorStride = -1;
    public int 		normalStride  = -1;

    public int 		vertexCount = -1;
    public Vector3f	positionMin;
    public Vector3f positionMax;

    public VAOMap namedVAOs;
    public HashMap<String, NamedVaoData> namedVaoData = new HashMap<String, NamedVaoData>();

    public ArrayList<RenderCmd> primatives;
    public MeshData()
    {
        primatives = new ArrayList<RenderCmd>();
        namedVAOs = new VAOMap();
        oAttribArraysBuffer = new int[1];
        oIndexBuffer = new int[1];
        //oVAO = new int[1];
    }
}
