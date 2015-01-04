package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.ArrayList;

/**
 * Created by jamie on 1/3/15.
 */
public class SceneNode {
    public SceneNode(SceneMesh pMesh, SceneProgram pProg, Vector3f nodePos, ArrayList<TextureBinding> texBindings)
    {
        m_pMesh = pMesh;
        m_pProg = pProg;
        m_texBindings = texBindings;
        m_nodeTm = new Transform();
        m_nodeTm.m_trans = nodePos;
    }

    public void NodeSetScale(Vector3f scale )
    {
        m_nodeTm.m_scale = scale;
    }

    public void NodeRotate( Quaternion orient )
    {
        m_nodeTm.m_orient = Quaternion.mult(m_nodeTm.m_orient,  orient);
    }

    public void NodeSetOrient( Quaternion orient )
    {
        m_nodeTm.m_orient = orient;
    }

    public Quaternion NodeGetOrient()
    {
        return m_nodeTm.m_orient;
    }

    public void SetNodeOrient(Quaternion nodeOrient)
    {
        nodeOrient.normalize();
        m_nodeTm.m_orient =  nodeOrient;
    }

    public void setNodeScale(Vector3f nodeScale)
    {
        m_nodeTm.m_scale = nodeScale;
    }

    public void render(ArrayList<Integer> samplers, Matrix4f baseMat) throws Exception
    {
        baseMat = Matrix4f.Mult(baseMat, m_nodeTm.GetMatrix());
        Matrix4f objMat = Matrix4f.Mult(baseMat, m_objTm.GetMatrix());

        m_pProg.UseProgram();
        GLES20.glUniformMatrix4fv(m_pProg.GetMatrixLoc(), 1, false, objMat.toArray(), 0);

        if(m_pProg.GetNormalMatLoc() != -1)
        {
            Matrix3f normMat = new Matrix3f(objMat.inverted().transposed());
            GLES20.glUniformMatrix3fv(m_pProg.GetNormalMatLoc(), 1, false, normMat.toArray(), 0);
        }

        //std::for_each(m_binders.begin(), m_binders.end(), BindBinder(m_pProg->GetProgram()));
        for(UniformBinderBase sb : m_binders)
        {
            sb.bindState(m_pProg.GetProgram());
        }
        for(int texIx = 0; texIx < m_texBindings.size(); ++texIx)
        {
            TextureBinding binding = m_texBindings.get(texIx);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + binding.texUnit);
            GLES20.glBindTexture(binding.pTex.GetTextureType(), binding.pTex.GetTexture());
            // FIXME GLES20.glBindSampler(binding.texUnit, samplers[(int)binding.sampler]);
        }

        m_pMesh.render();

        for(int texIx = 0; texIx < m_texBindings.size(); ++texIx)
        {
            TextureBinding binding = m_texBindings.get(texIx);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0  + binding.texUnit);
            GLES20.glBindTexture(binding.pTex.GetTextureType(), 0);
            // FIXME GLES20.glBindSampler(binding.texUnit, 0);
        }

        for(UniformBinderBase sb : m_binders)
        {
            sb.unbindState(m_pProg.GetProgram());
        }
        GLES20.glUseProgram(0);
    }

    public void NodeOffset(Vector3f offset)
    {
        m_nodeTm.m_trans.add(offset);
    }

    public void NodeSetTrans(Vector3f offset)
    {
        m_nodeTm.m_trans = offset;
    }

    public void SetStateBinder(UniformBinderBase pBinder)
    {
        m_binders.add(pBinder);
    }

    public int GetProgram()
    {
        return m_pProg.GetProgram();
    }

    private SceneMesh m_pMesh;		//Unmanaged. We are deleted first, so these should always be real values.
    private SceneProgram m_pProg;	//Unmanaged. We are deleted first, so these should always be real values.

    private ArrayList<UniformBinderBase> m_binders = new ArrayList<UniformBinderBase>();	//Unmanaged. These live beyond us.
    private ArrayList<TextureBinding> m_texBindings = new ArrayList<TextureBinding>();

    private Transform m_nodeTm = new Transform();
    private Transform m_objTm = new Transform();
}
