package com.tutorial.glsltutorials.tutorials.Scene;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;

/**
 * Created by jamie on 1/3/15.
 */
public class FrameworkScene {
    public FrameworkScene(String filename) throws Exception
    {
        m_pImpl = new SceneImpl(filename);
    }

    public void render(Matrix4f cameraMatrix) throws Exception
    {
        m_pImpl.Render(cameraMatrix);
    }

    public NodeRef findNode(String nodeName)
    {
        return m_pImpl.findNode(nodeName);
    }

    public int findProgram(String progName)
    {
        return m_pImpl.findProgram(progName);
    }

    public Mesh FindMesh(String meshName)
    {
        return m_pImpl.findMesh(meshName);
    }

    private SceneImpl m_pImpl;
}
