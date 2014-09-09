package com.tutorial.glsltutorials.tutorials.Mesh;

import android.content.res.Resources;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Attributes.AttribIndexMap;
import com.tutorial.glsltutorials.tutorials.Attributes.Attribute;
import com.tutorial.glsltutorials.tutorials.Attributes.AttributeCollection;
import com.tutorial.glsltutorials.tutorials.Attributes.AttributesClass;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.VAOMapData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Jamie on 6/6/14.
 */
public class Mesh {
    static
    {
        InitializeGallPrimativeTypes();
    }

    private MeshData m_pData;

    static PrimitiveType[] g_allPrimitiveTypes;

    static private void InitializeGallPrimativeTypes()
    {
        g_allPrimitiveTypes = new PrimitiveType[7];
        g_allPrimitiveTypes[0] = new PrimitiveType("triangles", GLES20.GL_TRIANGLES);
        g_allPrimitiveTypes[1] = new PrimitiveType("tri-strip", GLES20.GL_TRIANGLE_STRIP);
        g_allPrimitiveTypes[2] = new PrimitiveType("tri-fan", GLES20.GL_TRIANGLE_FAN);
        g_allPrimitiveTypes[3] = new PrimitiveType("lines", GLES20.GL_LINES);
        g_allPrimitiveTypes[4] = new PrimitiveType("line-strip", GLES20.GL_LINE_STRIP);
        g_allPrimitiveTypes[5] = new PrimitiveType("line-loop", GLES20.GL_LINE_LOOP);
        g_allPrimitiveTypes[6] = new PrimitiveType("points", GLES20.GL_POINTS);
    }

    RenderCmd ProcessRenderCmd(Element cmdElem) throws Exception
    {
        RenderCmd cmd = new RenderCmd();

        String strCmdName = cmdElem.getAttribute("cmd");
        int iArrayCount = g_allPrimitiveTypes.length;
        PrimitiveType pPrim = new PrimitiveType();
        boolean match = false;
        for (PrimitiveType pt : g_allPrimitiveTypes)
        {
            if (pt.strPrimitiveName.equals(strCmdName))
            {
                match = true;
                pPrim = pt;
            }
        }

        if(match == false)
            throw new Exception("Unknown 'cmd' field.");

        cmd.ePrimType = pPrim.eGLPrimType;

        String strElemName = cmdElem.getTagName();
        if(strElemName.equals("indices"))
        {
            cmd.bIsIndexedCmd = true;
            try
            {
                String test = cmdElem.getAttribute("prim-restart");
                cmd.primRestart = Integer.parseInt(cmdElem.getAttribute("prim-restart"));
            }
            catch (Exception ex)
            {
                cmd.primRestart = -1;
            }
        }
        else if(strElemName.equals("arrays"))
        {
            cmd.bIsIndexedCmd = false;
            cmd.start = Integer.parseInt(cmdElem.getAttribute("start"));
            if(cmd.start < 0)
                throw new Exception("`array` 'start' index must be between 0 or greater.");

            cmd.elemCount = Integer.parseInt(cmdElem.getAttribute("count"));
            if(cmd.elemCount <= 0)
                throw new Exception("`array` 'count' must be between 0 or greater.");
        }
        else
            throw new Exception("Bad command element " + strElemName + ". Must be 'indices' or 'arrays'.");

        return cmd;
    }

    public Mesh(InputStream is) throws Exception {
        int i;
        m_pData = new MeshData();
        ArrayList<Attribute> attribs = new ArrayList<Attribute>();
        AttribIndexMap attribIndexMap = new AttribIndexMap();

        ArrayList<IndexData> indexData = new ArrayList<IndexData>();

        AttributeCollection namedVaoList = new AttributeCollection();

        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;
            try {

                doc = dBuilder.parse(is);
            } catch (Exception ex) {
                throw new Exception("Error loading xml mesh: " + ex.toString());
            }

            Element root = doc.getDocumentElement();

            NodeList attributeNodes = root.getElementsByTagName("attribute");
            if (attributeNodes.getLength() > 0) {
                for (i = 0; i < attributeNodes.getLength(); i++) {
                    {
                        Node vaoNode = attributeNodes.item(i);
                        attribs.add(new Attribute((Element) vaoNode));
                        attribIndexMap.add(attribs.get(attribs.size() - 1).iAttribIx, attribs.size() - 1);
                    }
                }

                NodeList vaoNodes = root.getElementsByTagName("vao");
                if (vaoNodes.getLength() > 0) {
                    for (i=0;  i < vaoNodes.getLength(); i++) {
                        Node vaoNode = vaoNodes.item(i);
                        ProcessVAO((Element) vaoNode, namedVaoList);
                    }
                }

                NodeList iNodes = root.getElementsByTagName("indices");
                if (iNodes.getLength() > 0) {
                    for (i = 0; i < iNodes.getLength();i++ ) {
                        Node iNode = iNodes.item(i);
                        m_pData.primatives.add(ProcessRenderCmd((Element) iNode));
                        indexData.add(new IndexData((Element) iNode));
                    }
                }

                NodeList aNodes = root.getElementsByTagName("arrays");
                if (aNodes.getLength() > 0) {
                    for (i = 0; i < aNodes.getLength();i++ ) {
                        Node iNode = aNodes.item(i);
                        m_pData.primatives.add(ProcessRenderCmd((Element) iNode));
                    }
                }




                //Figure out how big of a buffer object for the attribute data we need.
                int iAttrbBufferSize = 0;
                ArrayList<Integer> attribStartLocs = new ArrayList<Integer>();
                int iNumElements = 0;
                for (int iLoop = 0; iLoop < attribs.size(); iLoop++) {
                    iAttrbBufferSize = ((iAttrbBufferSize % 16) != 0) ?
                            (iAttrbBufferSize + (16 - iAttrbBufferSize % 16)) : iAttrbBufferSize;

                    attribStartLocs.add(iAttrbBufferSize);

                    iAttrbBufferSize += attribs.get(iLoop).CalcByteSize();

                    if (iNumElements != 0) {
                        if (iNumElements != attribs.get(iLoop).NumElements()) {
                            throw new Exception("Some of the attribute arrays have different element counts. "
                                    + String.valueOf(iNumElements) + "  " + String.valueOf(attribs.get(iLoop).NumElements()));
                        }
                    } else
                        iNumElements = attribs.get(iLoop).NumElements();
                }

                //Create the "Everything" VAO.
                //GLES20.glGenVertexArrays(1, m_pData.oVAO, 0);
                //GLES20.glBindVertexArray(m_pData.oVAO[0]);

                //Create the buffer object.
                GLES20.glGenBuffers(1, m_pData.oAttribArraysBuffer, 0);
                // Changing these only works if other group changes as well.
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_pData.oAttribArraysBuffer[0]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, iAttrbBufferSize,
                        null, GLES20.GL_STATIC_DRAW);

                //Fill in our data and set up the attribute arrays.
                for (int iLoop = 0; iLoop < attribs.size(); iLoop++) {
                    switch (iLoop)
                    {
                        case 0:
                            m_pData.positionAttribute = 0;
                            m_pData.positionSize = attribs.get(iLoop).iSize;
                            m_pData.positionOffset = attribStartLocs.get(iLoop);
                            m_pData.positionStride = m_pData.positionSize * attribs.get(iLoop).pAttribType.iNumBytes;
                            break;
                        case 1:
                            m_pData.colorAttribute = 1;
                            m_pData.colorSize = attribs.get(iLoop).iSize;
                            m_pData.colorOffset = attribStartLocs.get(iLoop);
                            m_pData.colorStride = m_pData.colorSize * attribs.get(iLoop).pAttribType.iNumBytes;
                            break;
                        case 2:
                            m_pData.normalAttribute = 2;
                            m_pData.normalSize = attribs.get(iLoop).iSize;
                            m_pData.normalOffset = attribStartLocs.get(iLoop);
                            m_pData.normalStride = m_pData.normalSize * attribs.get(iLoop).pAttribType.iNumBytes;
                            break;
                    }
                    attribs.get(iLoop).FillBoundBufferObject(attribStartLocs.get(iLoop));
                    attribs.get(iLoop).SetupAttributeArray(attribStartLocs.get(iLoop));
                }

                //GLES20.glBindVertexArray(0);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

                //Fill the named VAOs.
                Set<String> keys = namedVaoList.collection.keySet();



                for (String key : keys)
                {
                    NamedVaoData namedVaoData = new NamedVaoData();
                    ArrayList<Integer> namedVao = namedVaoList.collection.get(key);
                    int[] vao = new int[1];
                    vao[0] = 1; // using array buffer, just selecting items
                    //GLES20.glGenVertexArrays(1, vao, 0);
                    //GLES20.glBindVertexArray(vao[0]);
                    int bufferSize = 0;

                    for (int iAttribIx = 0; iAttribIx < namedVao.size(); iAttribIx++) {
                        int iAttrib = namedVao.get(iAttribIx);
                        int iAttribOffset = -1;
                        for (int iCount = 0; iCount < attribs.size(); iCount++) {
                            bufferSize = bufferSize + attribs.get(iCount).iSize * attribs.get(iCount).pAttribType.iNumBytes;
                            switch (iCount)
                            {
                                case 0:
                                    namedVaoData.positionAttribute = 0;
                                    namedVaoData.positionSize = attribs.get(iCount).iSize;
                                    namedVaoData.positionOffset = attribStartLocs.get(iCount);
                                    namedVaoData.positionStride = m_pData.positionSize * attribs.get(iCount).pAttribType.iNumBytes;
                                    break;
                                case 1:
                                    namedVaoData.colorAttribute = 1;
                                    namedVaoData.colorSize = attribs.get(iCount).iSize;
                                    namedVaoData.colorOffset = attribStartLocs.get(iCount);
                                    namedVaoData.colorStride = m_pData.colorSize * attribs.get(iCount).pAttribType.iNumBytes;
                                    break;
                                case 2:
                                    namedVaoData.normalAttribute = 2;
                                    namedVaoData.normalSize = attribs.get(iCount).iSize;
                                    namedVaoData.normalOffset = attribStartLocs.get(iCount);
                                    namedVaoData.normalStride = m_pData.normalSize * attribs.get(iCount).pAttribType.iNumBytes;
                                    break;
                            }
                            if (attribs.get(iCount).iAttribIx == iAttrib) {
                                iAttribOffset = iCount;
                                break;
                            }
                        }
                        // skip this use other buffer attribs.get(iAttribOffset).SetupAttributeArray(attribStartLocs.get(iAttribOffset));
                    }


                    m_pData.namedVAOs.add(key, vao[0]);
                    m_pData.namedVaoData.put(key, namedVaoData);
                    //GLES20.glBindVertexArray(0);
                    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
                }

                //Get the size of our index buffer data.
                int iIndexBufferSize = 0;
                ArrayList<Integer> indexStartLocs = new ArrayList<Integer>();
                for (int iLoop = 0; iLoop < indexData.size(); iLoop++) {
                    iIndexBufferSize = ((iIndexBufferSize % 16) != 0) ?
                            (iIndexBufferSize + (16 - iIndexBufferSize % 16)) : iIndexBufferSize;

                    indexStartLocs.add(iIndexBufferSize);
                    IndexData currData = indexData.get(iLoop);

                    iIndexBufferSize += currData.CalcByteSize();
                }

                //Create the index buffer object.
                if (iIndexBufferSize != 0) {
                    //GLES20.glBindVertexArray(m_pData.oVAO[0]);

                    GLES20.glGenBuffers(1, m_pData.oIndexBuffer, 0);
                    // Changing these only works if other group changes as well.
                    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_pData.oIndexBuffer[0]);
                    GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, iIndexBufferSize,
                            VBO_Tools.MakeFloatBuffer(new float[iIndexBufferSize]), GLES20.GL_STATIC_DRAW);

                    //Fill with data.
                    for (int iLoop = 0; iLoop < indexData.size(); iLoop++) {
                        IndexData currData = indexData.get(iLoop);
                        currData.FillBoundBufferObject(indexStartLocs.get(iLoop));
                    }

                    //Fill in indexed rendering commands.
                    int iCurrIndexed = 0;
                    for (int iLoop = 0; iLoop < m_pData.primatives.size(); iLoop++) {
                        RenderCmd prim = m_pData.primatives.get(iLoop);
                        if (prim.bIsIndexedCmd) {
                            prim.start = indexStartLocs.get(iCurrIndexed);
                            prim.elemCount = indexData.get(iCurrIndexed).dataArray.size();
                            prim.eIndexDataType = indexData.get(iCurrIndexed).pAttribType.eGLType;
                            iCurrIndexed++;
                            m_pData.primatives.set(iLoop, prim);
                        }
                    }

                    ArrayList<String> vaoKeys = new ArrayList<String>(m_pData.namedVAOs.getKeys());
                    for (String key : vaoKeys) {
                        //GLES20.glBindVertexArray(m_pData.namedVAOs.Value(key));
                        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_pData.oIndexBuffer[0]);
                    }

                    //GLES20.glBindVertexArray(0);
                    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
                } else {
                    //throw new Exception("Empty Index Buffer for VAO");
                }
            }
        }
    }

    void ProcessVAO(Element vaoElem, AttributeCollection attributes) throws Exception
    {
        ArrayList<Integer> source_attributes = new ArrayList<Integer>();
        boolean sources_found = false;

        NodeList nodeList = vaoElem.getElementsByTagName("source");
        if (nodeList.getLength() > 0)
        {
            sources_found = true;
            for (int i = 0; i < nodeList.getLength(); i++ )
            {
                Node pSource = nodeList.item(i);
                NamedNodeMap  element_attributes = pSource.getAttributes();
                int j = 0;
                for (Node attrib = element_attributes.item(j); j < element_attributes.getLength(); j++) {
                    source_attributes.add(Integer.parseInt(attrib.getNodeValue()));
                }
            }
            if (sources_found) {
                String vao_name = vaoElem.getAttribute("name");
                attributes.add(vaoElem.getAttribute("name"), source_attributes);
            } else {
                throw new Exception("No sources found for vao");
            }
        }
    }



    public void Render() throws Exception
    {
        //if (m_pData.oVAO[0] == 0)
        if (m_pData.oIndexBuffer[0] == 0)
            return;

        //GLES20.glBindVertexArray(m_pData.oVAO[0]);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_pData.oAttribArraysBuffer[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_pData.oIndexBuffer[0]);
        if (m_pData.positionAttribute != -1)
        {
            GLES20.glEnableVertexAttribArray(m_pData.positionAttribute);
            GLES20.glVertexAttribPointer(m_pData.positionAttribute, m_pData.positionSize,
                    GLES20.GL_FLOAT, false, m_pData.positionStride, m_pData.positionOffset);
        }
        if (m_pData.colorAttribute != -1)
        {
            GLES20.glEnableVertexAttribArray(m_pData.colorAttribute);
            GLES20.glVertexAttribPointer(m_pData.colorAttribute, m_pData.colorSize,
                    GLES20.GL_FLOAT, false, m_pData.colorStride, m_pData.colorOffset);

        }
        if (m_pData.normalAttribute != -1)
        {
            GLES20.glEnableVertexAttribArray(m_pData.normalAttribute);
            GLES20.glVertexAttribPointer(m_pData.normalAttribute, m_pData.normalSize,
                    GLES20.GL_FLOAT, false, m_pData.normalStride, m_pData.normalOffset);
        }


        for (RenderCmd renderCmd : m_pData.primatives)
        {
            renderCmd.Render();
        }
        if (m_pData.positionAttribute != -1) GLES20.glDisableVertexAttribArray(m_pData.positionAttribute);
        if (m_pData.colorAttribute != -1) GLES20.glDisableVertexAttribArray(m_pData.colorAttribute);
        if (m_pData.normalAttribute != -1) GLES20.glDisableVertexAttribArray(m_pData.normalAttribute);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        //GLES20.glBindVertexArray(0);
    }

    public void Render(String strMeshName) throws Exception
    {
        int theIt = m_pData.namedVAOs.Value(strMeshName);
        if (theIt == 0)
            return;

        if (m_pData.oAttribArraysBuffer[0] == 0)
            return;

        //GLES20.glBindVertexArray(theIt);

        NamedVaoData namedVaoData = m_pData.namedVaoData.get(strMeshName);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_pData.oAttribArraysBuffer[0]);
        if ( m_pData.oIndexBuffer[0] != 0) GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_pData.oIndexBuffer[0]);
        //GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, namedVaoData.oIndexBuffer[0]);
        if (namedVaoData.positionAttribute != -1)
        {
            GLES20.glEnableVertexAttribArray(namedVaoData.positionAttribute);
            GLES20.glVertexAttribPointer(namedVaoData.positionAttribute, namedVaoData.positionSize,
                    GLES20.GL_FLOAT, false, namedVaoData.positionStride, namedVaoData.positionOffset);
        }
        if (namedVaoData.colorAttribute != -1)
        {
            GLES20.glEnableVertexAttribArray(namedVaoData.colorAttribute);
            GLES20.glVertexAttribPointer(namedVaoData.colorAttribute, namedVaoData.colorSize,
                    GLES20.GL_FLOAT, false, namedVaoData.colorStride, namedVaoData.colorOffset);

        }
        if (namedVaoData.normalAttribute != -1)
        {
            GLES20.glEnableVertexAttribArray(namedVaoData.normalAttribute);
            GLES20.glVertexAttribPointer(namedVaoData.normalAttribute, namedVaoData.normalSize,
                    GLES20.GL_FLOAT, false, namedVaoData.normalStride, namedVaoData.normalOffset);
        }

        for(RenderCmd renderCmd : m_pData.primatives)
        {
            renderCmd.Render();
        }
        //GLES20.glBindVertexArray(0);
        if (namedVaoData.positionAttribute != -1) GLES20.glDisableVertexAttribArray(namedVaoData.positionAttribute);
        if (namedVaoData.colorAttribute != -1) GLES20.glDisableVertexAttribArray(namedVaoData.colorAttribute);
        if (namedVaoData.normalAttribute != -1) GLES20.glDisableVertexAttribArray(namedVaoData.normalAttribute);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

public void DeleteObjects()
        {
        GLES20.glDeleteBuffers(1, m_pData.oAttribArraysBuffer, 0);
        m_pData.oAttribArraysBuffer[0] = 0;
        GLES20.glDeleteBuffers(1, m_pData.oIndexBuffer, 0);
        m_pData.oIndexBuffer[0] = 0;
        //GLES20.glDeleteBuffers(1, m_pData.oVAO, 0);
        //m_pData.oVAO[0] = 0;

        ArrayList<String> keys = new ArrayList<String>(m_pData.namedVAOs.getKeys());
        int[] current_value  = new int[1];
       for (String key : keys)
        {
            current_value[0]  =  m_pData.namedVAOs.Value(key);
            //GLES20.glDeleteVertexArrays(1, current_value, 0);
            m_pData.namedVAOs.Remove(key);
        }
    }
}
