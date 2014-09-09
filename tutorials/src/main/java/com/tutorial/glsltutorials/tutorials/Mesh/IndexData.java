package com.tutorial.glsltutorials.tutorials.Mesh;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Attributes.AttribData;
import com.tutorial.glsltutorials.tutorials.Attributes.AttribType;
import com.tutorial.glsltutorials.tutorials.Attributes.AttributesClass;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by Jamie on 6/7/14.
 */
public class IndexData {
    public IndexData(Element indexElem) throws Exception
    {
        String strType = indexElem.getAttribute("type");

        if(!strType.equals("uint") && !strType.equals("ushort") && !strType.equals("ubyte"))
            throw new Exception("Improper 'type' attribute value on 'index' element.");

        pAttribType = AttributesClass.GetAttribType(strType);

        //Parse text
        NodeList nodes = indexElem.getChildNodes();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodes.getLength(); i++ )
        {
            Node pChild = nodes.item(i);
            String next_value = pChild.getNodeValue();

            sb.append(next_value);
        }

        dataArray = pAttribType.ParseFunc(sb);

        if(dataArray.size() == 0)
            throw new Exception("The index element must have an array of values.");
    }

    public IndexData()
    {
    }

    public IndexData(IndexData rhs)
    {
        pAttribType = rhs.pAttribType;
        dataArray = rhs.dataArray;
    }

    public int CalcByteSize()
    {
        return dataArray.size() * pAttribType.iNumBytes;
    }

    public void FillBoundBufferObject(int iOffset) throws Exception
    {
        pAttribType.WriteToBuffer((int) GLES20.GL_ELEMENT_ARRAY_BUFFER, dataArray, iOffset);
    }

    public AttribType pAttribType;
    public ArrayList<AttribData> dataArray;
}
