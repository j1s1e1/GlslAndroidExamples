package com.tutorial.glsltutorials.tutorials.Attributes;

import android.opengl.GLES20;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jamie on 6/7/14.
 */
public class Attribute {
    public Attribute()
    {
        iAttribIx = 0xFFFFFFFF;
        iSize = -1;
        bIsIntegral = false;
    }

    public Attribute(Element attribElem) throws Exception
    {
        int iAttributeIndex = -1;
        String attribute = attribElem.getAttribute("index");
        iAttributeIndex = Integer.parseInt(attribute);
        if (!((0 <= iAttributeIndex) && (iAttributeIndex < 16)))
            throw new Exception("Attribute index must be between 0 and 16.");
        iAttribIx = (int)iAttributeIndex;

        int iVectorSize = -1;
        String VectorSize = attribElem.getAttribute("size");
        iVectorSize = Integer.parseInt(VectorSize);
        if (!((1 <= iVectorSize) && (iVectorSize < 5)))
            throw new Exception("Attribute size must be between 1 and 4.");
        iSize = iVectorSize;

        pAttribType = AttributesClass.GetAttribType(attribElem.getAttribute("type"));


        bIsIntegral = false;
        String pIntegralAttrib = attribElem.getAttribute("integral");

        if (!pIntegralAttrib.equals(""))
        {
            String strIntegral = pIntegralAttrib;
            if (strIntegral.equals("true"))
                bIsIntegral = true;
            else if (strIntegral.equals("false"))
                bIsIntegral = false;
            else
                throw new Exception("Incorrect 'integral' value for the 'attribute'.");

            if (pAttribType.bNormalized)
                throw new Exception("Attribute cannot be both 'integral' and a normalized 'type'.");

            if (pAttribType.eGLType == GLES20.GL_FLOAT)
                throw new Exception("Attribute cannot be both 'integral' and a floating-point 'type'.");
        }

        //Parse text
        NodeList nodes = attribElem.getChildNodes();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (Node pChild = nodes.item(i); i < nodes.getLength(); i++ )
        {
            String next_value = pChild.getNodeValue();
            sb.append(next_value);
        }

        dataArray = pAttribType.ParseFunc(sb);

        if (dataArray.size() == 0)
            throw new Exception("The attribute must have an array of values.");
        if (dataArray.size()  % iSize != 0)
            throw new Exception("The attribute's data must be a multiple of its size in elements.");

    }

    public Attribute(Attribute rhs)
    {
        iAttribIx = rhs.iAttribIx;
        pAttribType = rhs.pAttribType;
        iSize = rhs.iSize;
        bIsIntegral = rhs.bIsIntegral;
        dataArray = rhs.dataArray;
    }

    public int NumElements()
    {
        return dataArray.size()  / iSize;
    }

    public int CalcByteSize()
    {
        return dataArray.size()  * pAttribType.iNumBytes;
    }

    public void FillBoundBufferObject(int iOffset) throws Exception
    {
        pAttribType.WriteToBuffer(GLES20.GL_ARRAY_BUFFER, dataArray, iOffset);
    }

    public void SetupAttributeArray(int iOffset)
    {
        switch (iAttribIx)
        {
            case 0: break; // position
            case 1: break; // color
            case 2: break; // normal
        }
        GLES20.glEnableVertexAttribArray(iAttribIx);
        if (bIsIntegral)
        {
            //FIX_THIS GLES20.glVertexAttribIPointer(iAttribIx, iSize, pAttribType.eGLType, 0, iOffset);
        }
        else
        {
            GLES20.glVertexAttribPointer(iAttribIx, iSize,
                    pAttribType.eGLType, pAttribType.bNormalized, 0, iOffset);
        }
    }

    public float getMin(int offset, int step)
    {
        ArrayList<Float> selectedItems = new ArrayList<Float>();

        for (int i = 0; i < dataArray.size(); i += step)
        {
            selectedItems.add(dataArray.get(i+offset).fValue);
        }
        return Collections.min(selectedItems);
    }

    public float getMax(int offset, int step)
    {
        ArrayList<Float> selectedItems = new ArrayList<Float>();
        for (int i = 0; i < dataArray.size(); i += step)
        {
            selectedItems.add(dataArray.get(i+offset).fValue);
        }
        return Collections.max(selectedItems);
    }

    public int iAttribIx;
    public AttribType pAttribType;
    public int iSize;
    boolean bIsIntegral;
    ArrayList<AttribData> dataArray;
}
