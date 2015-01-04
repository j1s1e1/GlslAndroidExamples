package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

/**
 * Created by jamie on 1/3/15.
 */
public class UniformIntBinder extends UniformBinderBase {
    public UniformIntBinder()
    {
        m_val = 0;
    }

    public void SetValue(int val)
    {
        m_val = val;
    }

    public void bindState(int prog)
    {
        GLES20.glUniform1i(GetUniformLoc(prog), m_val);
    }

    public void unbindState(int prog)
    {
        GLES20.glUniform1i(GetUniformLoc(prog), 0);
    }

    public void UnbindBinder(int prog)
    {

    }

    private int m_val;
}
