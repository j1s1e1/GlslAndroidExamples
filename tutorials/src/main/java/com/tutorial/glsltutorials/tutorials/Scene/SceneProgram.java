package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

/**
 * Created by jamie on 1/3/15.
 */
public class SceneProgram {
    public SceneProgram(int programObj, int matrixLoc, int normalMatLoc)
    {
        m_programObj = programObj;
        m_matrixLoc = matrixLoc;
        m_normalMatLoc = normalMatLoc;
    }

    public void finalize()
    {
        GLES20.glDeleteProgram(m_programObj);
    }

    public int GetMatrixLoc()
    {
        return m_matrixLoc;
    }

    public int GetNormalMatLoc()
    {
        return m_normalMatLoc;
    }

    public void UseProgram()
    {
        GLES20.glUseProgram(m_programObj);
    }

    public int GetProgram()
    {
        return m_programObj;
    }

    private int m_programObj;
    private int m_matrixLoc;
    private int m_normalMatLoc;
}
