package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;

/**
 * Created by jamie on 1/3/15.
 */
public class UniformMat4Binder extends  UniformBinderBase {
    public UniformMat4Binder()
    {
        m_val = Matrix4f.Identity();
    }

    public void setValue(Matrix4f val)
    {
        m_val = val;
    }

    public void bindState(int prog)
    {
        GLES20.glUniformMatrix4fv(GetUniformLoc(prog), 1, false, m_val.toArray(), 0);
    }

    public void unbindState(int prog)
    {

    }

    public void UnbindBinder(int prog)
    {

    }

    private Matrix4f  m_val;
}
