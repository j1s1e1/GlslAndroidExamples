package com.tutorial.glsltutorials.tutorials.Attributes;

import android.opengl.GLES20;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * Created by Jamie on 6/6/14.
 */
public class AttributesClass {

    static
    {
        InitializeG_All_AttribeTypes();
    }

    static AttribType[] g_allAttributeTypes;

    public static void InitializeG_All_AttribeTypes()
    {
        g_allAttributeTypes = new AttribType[5];
        g_allAttributeTypes[0] = new AttribType("float", false,  GLES20.GL_FLOAT, 4);
        g_allAttributeTypes[1] = new AttribType("int", false, 	 GLES20.GL_INT, 4);
        g_allAttributeTypes[2] = new AttribType("short", false,  GLES20.GL_SHORT, 2);
        g_allAttributeTypes[3] = new AttribType("ushort", false, GLES20.GL_UNSIGNED_SHORT, 2);
        g_allAttributeTypes[4] = new AttribType("uint", false, 	 GLES20.GL_UNSIGNED_INT, 4);
    }
    /*
        {"uint",		false,	GL_UNSIGNED_INT,	sizeof(GLuint),		ParseUInts,		WriteUInts},
        {"norm-int",	true,	GL_INT,				sizeof(GLint),		ParseInts,		WriteInts},
        {"norm-uint",	true,	GL_UNSIGNED_INT,	sizeof(GLuint),		ParseUInts,		WriteUInts},
        {"short",		false,	GL_SHORT,			sizeof(GLshort),	ParseShorts,	WriteShorts},
        {"ushort",		false,	GL_UNSIGNED_SHORT,	sizeof(GLushort),	ParseUShorts,	WriteUShorts},
        {"norm-short",	true,	GL_SHORT,			sizeof(GLshort),	ParseShorts,	WriteShorts},
        {"norm-ushort",	true,	GL_UNSIGNED_SHORT,	sizeof(GLushort),	ParseUShorts,	WriteUShorts},
        {"byte",		false,	GL_BYTE,			sizeof(GLbyte),		ParseBytes,		WriteBytes},
        {"ubyte",		false,	GL_UNSIGNED_BYTE,	sizeof(GLubyte),	ParseUBytes,	WriteUBytes},
        {"norm-byte",	true,	GL_BYTE,			sizeof(GLbyte),		ParseBytes,		WriteBytes},
        {"norm-ubyte",	true,	GL_UNSIGNED_BYTE,	sizeof(GLubyte),	ParseUBytes,	WriteUBytes},
    };
    */

    public static AttribType GetAttribType(String strType) throws Exception
    {
        int iArrayCount = g_allAttributeTypes.length;
        int pAttrib = -1;
        for (int i = 0; i < iArrayCount; i++) {
            if (strType.equals(g_allAttributeTypes[i].strNameFromFile)) {
                pAttrib = i;
                break;
            }
        }

        if (pAttrib == -1)
            throw new Exception("Unknown 'type' field.");

        return g_allAttributeTypes[pAttrib];
    }

}
