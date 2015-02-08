package com.tutorial.glsltutorials.tutorials.Tutorials;

import com.tutorial.glsltutorials.tutorials.Creatures.Alien;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

import java.util.ArrayList;

/**
 * Created by jamie on 10/25/14.
 */
public class Tut_MultipleShaders extends TutorialBase {
    public Tut_MultipleShaders ()
    {
    }

    ArrayList<LitMatrixSphere2> spheres = new ArrayList<LitMatrixSphere2>();
    ArrayList<Alien> aliens = new ArrayList<Alien>();
    protected void init()
    {
        int lms_program = Programs.addProgram(VertexShaders.lms_vertexShaderCode,
                FragmentShaders.lms_fragmentShaderCode);

        Programs.setUniformColor(lms_program, new Vector4f(0f, 0.5f, 0.5f, 1f));
        Programs.setLightPosition(lms_program, new Vector3f(0f, 0.5f, -0.5f));

        int greenprog = Programs.addProgram(VertexShaders.lms_vertexShaderCode,
                FragmentShaders.solid_green_with_normals_frag);

        int DirVertexLighting_PN = Programs.addProgram(VertexShaders.DirVertexLighting_PN_vert,
                FragmentShaders.ColorPassthrough_frag);
        Vector3f dirToLight = new Vector3f(0.5f, 0.5f, 0.0f);
        dirToLight = dirToLight.normalize();
        Programs.setDirectionToLight(DirVertexLighting_PN, dirToLight);
        Programs.setLightIntensity(DirVertexLighting_PN, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f));
        Programs.setNormalModelToCameraMatrix(DirVertexLighting_PN, Matrix3f.Identity());
        Programs.setModelToCameraMatrix(DirVertexLighting_PN, Matrix4f.Identity());

        Alien alien;
        LitMatrixSphere2 lms;

        lms = new LitMatrixSphere2(0.1f);
        lms.setOffset(new Vector3f(-0.5f, 0.0f, 0.0f));
        spheres.add(lms);

        lms = new LitMatrixSphere2(0.025f);
        //lms.setOffset(new Vector3f(0.5f, 0.0f, 0.0f));
        lms.setProgram(DirVertexLighting_PN);

        spheres.add(lms);

        lms = new LitMatrixSphere2(0.1f);
        lms.setOffset(new Vector3f(0.0f, 0.5f, 0.0f));
        lms.setProgram(lms_program);
        spheres.add(lms);


        alien = new Alien();
        alien.setProgram(DirVertexLighting_PN);
        aliens.add(alien);

        alien = new Alien();
        alien.setProgram(greenprog);

        aliens.add(alien);


        setupDepthAndCull();
    }

    public  void display()
    {
        clearDisplay();
        for (LitMatrixSphere2 sphere : spheres)
        {
            sphere.draw();
        }

        for (Alien a : aliens)
        {
            a.draw();
        }
    }
}
