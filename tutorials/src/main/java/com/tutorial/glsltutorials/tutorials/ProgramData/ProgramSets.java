package com.tutorial.glsltutorials.tutorials.ProgramData;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;

import java.util.ArrayList;

/**
 * Created by jamie on 1/1/15.
 */
public class ProgramSets {
    public static ArrayList<ProgramSet> programs;
    static
    {
        programs = new ArrayList<ProgramSet>();
        programs.add(new ProgramSet("PosOnlyWorldTransform_vert ColorUniform_frag",
                VertexShaders.PosOnlyWorldTransform_vert, FragmentShaders.ColorUniform_frag));

        programs.add(new ProgramSet("ObjectPositionColor",
                VertexShaders.worldTransformObjectPositionColor, FragmentShaders.ObjectPositionColor));

        programs.add(new ProgramSet("PosColorWorldTransform_vert ColorMultUniform_frag",
                VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorMultUniform_frag));

        programs.add(new ProgramSet("DirAmbVertexLighting_PN_vert ColorPassthrough_frag",
                VertexShaders.DirAmbVertexLighting_PN_vert, FragmentShaders.ColorPassthrough_frag));

        programs.add(new ProgramSet("DirVertexLighting_PCN ColorPassthrough_frag",
                VertexShaders.DirVertexLighting_PCN, FragmentShaders.ColorPassthrough_frag));

        programs.add(new ProgramSet("unlit", VertexShaders.unlit, FragmentShaders.unlit));

        programs.add(new ProgramSet("BasicTexture_PN ShaderGaussian", VertexShaders.BasicTexture_PN,
                FragmentShaders.ShaderGaussian));

        programs.add(new ProgramSet("PosColorWorldTransform_vert ColorPassthrough_frag",
                VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorPassthrough_frag));

        programs.add(new ProgramSet("PosColorLocalTransform_vert ColorPassthrough_frag",
                VertexShaders.PosColorLocalTransform_vert, FragmentShaders.ColorPassthrough_frag));

        programs.add(new ProgramSet("HDR_PCN DiffuseSpecularHDR",
                VertexShaders.HDR_PCN, FragmentShaders.DiffuseSpecularHDR));

        programs.add(new ProgramSet("HDR_PCN DiffuseOnlyHDR",
                VertexShaders.HDR_PCN, FragmentShaders.DiffuseOnlyHDR));

        programs.add(new ProgramSet("HDR_PCN DiffuseSpecularMtlHDR",
                VertexShaders.HDR_PCN, FragmentShaders.DiffuseSpecularMtlHDR));

        programs.add(new ProgramSet("HDR_PCN DiffuseOnlyMtlHDR",
                VertexShaders.HDR_PCN, FragmentShaders.DiffuseOnlyMtlHDR));

        programs.add(new ProgramSet("projlight", VertexShaders.projlight, FragmentShaders.projlight));
    }

    public static ProgramSet Find(String name)
    {
        for (ProgramSet ps : programs)
        {
            if (ps.name.equals(name))
            {
                return ps;
            }
        }
        return null;
    }
}
