package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 1/3/15.
 */
public class UniformVec3Binder extends UniformBinderBase {
    public UniformVec3Binder()
    {
        m_val = new Vector3f();
    }

    public void setValue(Vector3f val)
    {
        m_val = val;
    }

    public void bindState(int prog)
    {
        GLES20.glUniform3fv(GetUniformLoc(prog), 1, m_val.toArray(), 0);
    }

    public void unbindState(int prog)
    {

    }

    public void UnbindBinder(int prog)
    {

    }

    private Vector3f m_val;
}
