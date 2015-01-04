package com.tutorial.glsltutorials.tutorials.Scene;

import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 1/3/15.
 */
public class NodeRef {
    public void NodeSetScale(Vector3f scale)
    {
        m_pNode.NodeSetScale(scale);
    }

    public void NodeSetScale(float scale)
    {
        m_pNode.NodeSetScale(new Vector3f(scale));
    }

    //Right-multiplies the given orientation to the current one.
    public void NodeRotate(Quaternion orient)
    {
        m_pNode.NodeRotate(orient);
    }

    //Sets the current orientation to the given one.
    public void NodeSetOrient(Quaternion orient)
    {
        m_pNode.NodeSetOrient(orient);
    }

    public Quaternion NodeGetOrient()
    {
        return m_pNode.NodeGetOrient();
    }

    //Adds the offset to the current translation.
    public void NodeOffset(Vector3f offset)
    {
        m_pNode.NodeOffset(offset);
    }

    //Sets the current translation to the given one.
    public void NodeSetTrans(Vector3f offset)
    {
        m_pNode.NodeSetTrans(offset);
    }

    //This object does *NOT* claim ownership of the pointer.
    //You must ensure that it stays around so long as this Scene exists.
    public void setStateBinder(UniformBinderBase pBinder)
    {
        m_pNode.SetStateBinder(pBinder);
    }

    public int getProgram()
    {
        return m_pNode.GetProgram();
    }

    public NodeRef(SceneNode pNode)
    {
        m_pNode = pNode;
    }
    SceneNode m_pNode;
}
