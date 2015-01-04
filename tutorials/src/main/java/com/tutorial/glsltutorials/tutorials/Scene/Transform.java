package com.tutorial.glsltutorials.tutorials.Scene;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 1/3/15.
 */
public class Transform {
    public Transform()
    {
        m_orient = new Quaternion(1.0f, 0.0f, 0.0f, 0.0f);
        m_scale = new Vector3f(1.0f, 1.0f, 1.0f);
        m_trans = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public Matrix4f GetMatrix()
    {
        // FIXME check matrix orders
        Matrix4f ret;
        ret = Matrix4f.CreateTranslation(m_trans);
        ret = Matrix4f.Mult(ret, Matrix4f.createFromQuaternion(m_orient));
        ret = Matrix4f.Mult(ret, Matrix4f.createScale(m_scale));
        return ret;
    }

    public Quaternion m_orient;
    public Vector3f m_scale;
    public Vector3f m_trans;
}
