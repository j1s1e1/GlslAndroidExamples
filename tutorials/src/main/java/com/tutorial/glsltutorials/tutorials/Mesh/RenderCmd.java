package com.tutorial.glsltutorials.tutorials.Mesh;

import android.opengl.GLES20;

/**
 * Created by Jamie on 6/7/14.
 */
public class RenderCmd {
    public boolean bIsIndexedCmd;
    public int ePrimType;
    public int start;
    public int elemCount;
    public int eIndexDataType;	//Only if bIsIndexedCmd is true.
    public int primRestart;		//Only if bIsIndexedCmd is true.


    public void Render() throws Exception
    {
        try
        {
            if (bIsIndexedCmd) {
                GLES20.glDrawElements(ePrimType, elemCount, eIndexDataType, start);
            }
            else
                GLES20.glDrawArrays(ePrimType, start, elemCount);
        }
        catch (Exception ex)
        {
            throw new Exception("Error rendering mesh: " + ex.toString());
        }
    }

    public void Render(int triangles) throws Exception
    {
        if (triangles > elemCount)
        {
            triangles = elemCount;
        }
        try
        {
            if (bIsIndexedCmd)
                GLES20.glDrawElements(ePrimType, triangles, eIndexDataType, start);
            else
                GLES20.glDrawArrays(ePrimType, start, triangles);
        }
        catch (Exception ex)
        {
            throw new Exception("Error rendering mesh: " + ex.toString());
        }
    }
}
