package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.LightingProgramTypes;
import com.tutorial.glsltutorials.tutorials.Material.MaterialBlock;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.PushStack;

import java.util.ArrayList;

/**
 * Created by jamie on 1/2/15.
 */
public class Scene {
    //One for the ground, and one for each of the 5 objects.
    int MATERIAL_COUNT = 6;

    Mesh m_pTerrainMesh;
    Mesh m_pCubeMesh;
    Mesh m_pTetraMesh;
    Mesh m_pCylMesh;
    Mesh m_pSphereMesh;

    static public SceneProgramData[] TutorialPrograms;

    ArrayList<MaterialBlock> materials;

    SceneProgramData GetProgram(int eType)
    {
        return TutorialPrograms[eType];
    }

    static void GetMaterials( ArrayList<MaterialBlock> materials )
    {
        Vector4f One = new Vector4f(1f, 1f, 1f, 1f);
        //Ground
        materials.add(new MaterialBlock(One, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 0.6f));

        //Tetrahedron
        materials.add(new MaterialBlock(One.Mult(0.5f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 0.05f));

        //Monolith
        materials.add(new MaterialBlock(One.Mult(0.05f), new Vector4f(0.95f, 0.95f, 0.95f, 1.0f), 0.4f));

        //Cube
        materials.add(new MaterialBlock(One.Mult(0.05f), new Vector4f(0.3f, 0.3f, 0.3f, 1.0f), 0.1f));

        //Cylinder
        materials.add(new MaterialBlock(One.Mult(0.05f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), 0.6f));

        //Sphere
        materials.add(new MaterialBlock(new Vector4f(0.63f, 0.60f, 0.02f, 1.0f),
                new Vector4f(0.22f, 0.20f, 0.0f, 1.0f), 0.3f));
    }

    public Scene() throws Exception
    {
        m_pTerrainMesh = new Mesh("ground.xml");
        m_pCubeMesh = new Mesh("unitcubehdr.xml");
        m_pTetraMesh = new Mesh("unittetrahedron.xml");
        m_pCylMesh = new Mesh("unitcylinder.xml");
        m_pSphereMesh = new Mesh("unitsphere12.xml");

        materials = new ArrayList<MaterialBlock>();
        GetMaterials(materials);
        //assert(materials.size() == MATERIAL_COUNT);

        //ArrayList<byte> mtlBuffer;
        //mtlBuffer.resize(sizeMaterialUniformBuffer, 0);

        //GLubyte *bufferPtr = &mtlBuffer[0];

        //for(size_t mtl = 0; mtl < materials.size(); ++mtl)
        //	memcpy(bufferPtr + (mtl * m_sizeMaterialBlock), &materials[mtl], sizeof(MaterialBlock));

        //glGenBuffers(1, &m_materialUniformBuffer);
        //glBindBuffer(GL_UNIFORM_BUFFER, m_materialUniformBuffer);
        //glBufferData(GL_UNIFORM_BUFFER, sizeMaterialUniformBuffer, bufferPtr, GL_STATIC_DRAW);
        //glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    public void Draw(MatrixStack modelMatrix, int materialBlockIndex, float alphaTetra ) throws Exception
    {
        //Render the ground plane.
        try ( PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.RotateX(-90);

            DrawObject(m_pTerrainMesh, GetProgram(LightingProgramTypes.LP_VERT_COLOR_DIFFUSE), materialBlockIndex, 0,
                    modelMatrix);
        }

        //Render the tetrahedron object.
        try ( PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(75.0f, 5.0f, 75.0f);
            modelMatrix.RotateY(360.0f * alphaTetra);
            modelMatrix.scale(10.0f, 10.0f, 10.0f);
            modelMatrix.Translate(0.0f, (float)Math.sqrt(2.0f), 0.0f);
            modelMatrix.rotate(new Vector3f(-0.707f, 0.0f, -0.707f), 54.735f);

            DrawObject(m_pTetraMesh, "lit-color",
                    GetProgram(LightingProgramTypes.LP_VERT_COLOR_DIFFUSE_SPECULAR),
                    materialBlockIndex, 1, modelMatrix);
        }

        //Render the monolith object.
        try ( PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(88.0f, 5.0f, -80.0f);
            modelMatrix.scale(4.0f, 4.0f, 4.0f);
            modelMatrix.scale(4.0f, 9.0f, 1.0f);
            modelMatrix.Translate(0.0f, 0.5f, 0.0f);

            DrawObject(m_pCubeMesh, "lit", GetProgram(LightingProgramTypes.LP_MTL_COLOR_DIFFUSE_SPECULAR),
                    materialBlockIndex, 2, modelMatrix);
        }

        //Render the cube object.
        try ( PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(-52.5f, 14.0f, 65.0f);
            modelMatrix.RotateZ(50.0f);
            modelMatrix.RotateY(-10.0f);
            modelMatrix.scale(20.0f, 20.0f, 20.0f);

            DrawObject(m_pCubeMesh, "lit-color", GetProgram(LightingProgramTypes.LP_VERT_COLOR_DIFFUSE_SPECULAR),
                    materialBlockIndex, 3, modelMatrix);
        }

        //Render the cylinder.
        try ( PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(-7.0f, 30.0f, -14.0f);
            modelMatrix.scale(15.0f, 55.0f, 15.0f);
            modelMatrix.Translate(0.0f, 0.5f, 0.0f);

            DrawObject(m_pCylMesh, "lit-color", GetProgram(LightingProgramTypes.LP_VERT_COLOR_DIFFUSE_SPECULAR),
                    materialBlockIndex, 4, modelMatrix);
        }

        //Render the sphere.
        try ( PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(-83.0f, 14.0f, -77.0f);
            modelMatrix.scale(20.0f, 20.0f, 20.0f);

            DrawObject(m_pSphereMesh, "lit", GetProgram(LightingProgramTypes.LP_MTL_COLOR_DIFFUSE_SPECULAR),
                    materialBlockIndex, 5, modelMatrix);
        }
    }

    public void DrawObject(Mesh pMesh, SceneProgramData prog, int materialBlockIndex, int mtlIx,
                           MatrixStack modelMatrix ) throws Exception
    {
        Matrix3f normMatrix = new Matrix3f(modelMatrix.Top());
        normMatrix.invert();
        normMatrix.transposed();

        GLES20.glUseProgram(prog.theProgram);
        Matrix4f mm =  modelMatrix.Top();

        GLES20.glUniformMatrix4fv(prog.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);

        GLES20.glUniformMatrix3fv(prog.normalModelToCameraMatrixUnif, 1, false, normMatrix.toArray(), 0);

        // Apply Material
        materials.get(mtlIx).setUniforms(prog.theProgram);
        materials.get(mtlIx).updateInternal();

        pMesh.render();
        GLES20.glUseProgram(0);
    }

    public void DrawObject(Mesh pMesh, String meshName, SceneProgramData prog, int materialBlockIndex, int mtlIx,
                           MatrixStack modelMatrix) throws Exception
    {
        Matrix3f normMatrix = new Matrix3f(modelMatrix.Top());
        normMatrix.invert();
        normMatrix.transposed();

        GLES20.glUseProgram(prog.theProgram);
        // Apply Material
        materials.get(mtlIx).setUniforms(prog.theProgram);
        materials.get(mtlIx).updateInternal();

        Matrix4f mm = modelMatrix.Top();
        GLES20.glUniformMatrix4fv(prog.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);

        GLES20.glUniformMatrix3fv(prog.normalModelToCameraMatrixUnif, 1, false, normMatrix.toArray(), 0);
        pMesh.render(meshName);
        GLES20.glUseProgram(0);
    }

    public Mesh GetSphereMesh()
    {
        return m_pSphereMesh;
    }

    public Mesh GetCubeMesh()
    {
        return m_pCubeMesh;
    }
}
