package com.tutorial.glsltutorials.tutorials.Scene;

import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;

/**
 * Created by jamie on 1/3/15.
 */
public class SceneMesh {
    public SceneMesh(String filename) throws Exception
    {
        m_pMesh = new Mesh(filename);
    }

    public void finalize()
    {
        m_pMesh = null;
    }

    public void render() throws Exception
    {
        m_pMesh.render();
    }

    public Mesh getMesh()
    {
        return m_pMesh;
    }

    private Mesh m_pMesh;
}
