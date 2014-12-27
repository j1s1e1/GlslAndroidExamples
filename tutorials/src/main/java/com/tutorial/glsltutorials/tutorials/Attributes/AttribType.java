package com.tutorial.glsltutorials.tutorials.Attributes;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Attributes.AttribData;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by Jamie on 6/6/14.
 */
public class AttribType{
    public String strNameFromFile;
    public boolean bNormalized;
    public int eGLType;	// this changes, so must be cast
    public int iNumBytes;

    public AttribType(String fn_in, boolean bn_in, int egl_in, int inum_in)
    {
        strNameFromFile = fn_in;
        bNormalized = bn_in;
        eGLType = egl_in;
        iNumBytes = inum_in;
    }

    public ArrayList<AttribData> ParseFunc(StringBuilder sb) throws Exception
    {
        ArrayList<AttribData> attrib_data = new ArrayList<AttribData>();
        String[] items = sb.toString().split("\n");
        for (int i = 0; i < items.length; i++) {
            String[] values = items[i].split(" ");
            for (int j = 0; j < values.length; j++) {
                try {
                    AttribData ad = new AttribData();
                    switch (eGLType)
                    {
                        case GLES20.GL_FLOAT:
                            ad.fValue = Float.parseFloat(values[j]);
                            break;
                        case GLES20.GL_INT:
                            ad.iValue = Integer.parseInt(values[j]);
                            break;
                        case GLES20.GL_SHORT:
                            ad.sValue = Short.parseShort(values[j]);
                            break;
                        case GLES20.GL_UNSIGNED_SHORT:
                            ad.sValue = Short.parseShort(values[j]);
                            break;
                        case GLES20.GL_UNSIGNED_INT:
                            ad.iValue = Integer.parseInt(values[j]);
                            break;
                        default:  throw new Exception("Unidentified type");
                    }
                    attrib_data.add(ad);
                } catch (Exception ex) {
                }
            }

        }
        return attrib_data;
    }


    public void WriteToBuffer(int eBuffer, ArrayList<AttribData> attribData, int iOffset) throws  Exception
    {
        int i = 0;
        switch (eGLType)
        {
            case GLES20.GL_FLOAT:
                float[] floatBuffer = new float[attribData.size()];
                for (AttribData a : attribData)
                {
                    floatBuffer[i++]  = a.fValue;
                }
                FloatBuffer fb = VBO_Tools.MakeFloatBuffer(floatBuffer);
                GLES20.glBufferSubData(eBuffer, iOffset, (floatBuffer.length * 4), fb);
                break;
            case GLES20.GL_INT:
                int[] intBuffer = new int[attribData.size()];
                for (AttribData a : attribData)
                {
                    intBuffer[i++]  = a.iValue;
                }
                IntBuffer ib = VBO_Tools.MakeIntBuffer(intBuffer);
                GLES20.glBufferSubData(eBuffer, iOffset, (intBuffer.length * 4), ib);
                break;
            case GLES20.GL_SHORT:
                short[] shortBuffer = new short[attribData.size()];
                for (AttribData a : attribData)
                {
                    shortBuffer[i++]  = a.sValue;
                }
                ShortBuffer sb = VBO_Tools.MakeShortBuffer(shortBuffer);
                GLES20.glBufferSubData(eBuffer, iOffset, (shortBuffer.length * 2), sb);
                break;
            case GLES20.GL_UNSIGNED_SHORT:
                short[] ushortBuffer = new short[attribData.size()];
                for (AttribData a : attribData)
                {
                    ushortBuffer[i++]  = a.sValue;
                }
                ShortBuffer usb = VBO_Tools.MakeShortBuffer(ushortBuffer);
                GLES20.glBufferSubData(eBuffer, iOffset, (ushortBuffer.length * 2), usb);
                break;
            case GLES20.GL_UNSIGNED_INT:
                int[] uintBuffer = new int[attribData.size()];
                for (AttribData a : attribData)
                {
                    uintBuffer[i++]  = a.iValue;
                }
                IntBuffer uib = VBO_Tools.MakeIntBuffer(uintBuffer);
                GLES20.glBufferSubData(eBuffer, iOffset, (uintBuffer.length * 4), uib);
                break;
            default:  throw new Exception("Unidentified type");
        }
    }
}
