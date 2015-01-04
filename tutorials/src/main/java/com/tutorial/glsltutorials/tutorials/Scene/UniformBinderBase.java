package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

import java.util.HashMap;

/**
 * Created by jamie on 1/3/15.
 */
public abstract class UniformBinderBase implements IStateBinder, IUnbindBinder {
    public void AssociateWithProgram(Integer prog, String unifName)
    {
        m_progUnifLoc.put(prog, GLES20.glGetUniformLocation(prog, unifName));
    }

    protected int GetUniformLoc(int prog)
    {
        return m_progUnifLoc.get(prog);
    }

    private HashMap<Integer, Integer> m_progUnifLoc = new HashMap<Integer, Integer>();
}
