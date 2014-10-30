package com.tutorial.glsltutorials.tutorials.ProgramData;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

import java.util.ArrayList;

/**
 * Created by jamie on 10/12/14.
 */
public class Programs {
    static ArrayList<ProgramData> ActivePrograms = new ArrayList<ProgramData>();

    public static void Reset()
    {
        ActivePrograms = new ArrayList<ProgramData>();
    }

    public static int AddProgram(String vertexShader, String fragmentShader)
    {
        int program_number = -1;
        boolean new_program = true;
        for (int i = 0; i < ActivePrograms.size(); i++)
        {
            ProgramData pd = ActivePrograms.get(i);

            if (pd.CompareShaders(vertexShader, fragmentShader))
            {
                new_program = false;
                program_number = i;
                break;
            }
        }

        if (new_program == true)
        {
            ProgramData pd = new ProgramData(vertexShader, fragmentShader);
            ActivePrograms.add(pd);
            program_number = ActivePrograms.size() - 1;
        }
        return program_number;
    }

    public static void Draw(int program, int[] vertexBufferObject, int[] indexBufferObject,
                            Matrix4f cameraToClip, Matrix4f worldToCamera, Matrix4f mm,
                            int indexDataLength, float[] color)
    {
        ActivePrograms.get(program).Draw (vertexBufferObject, indexBufferObject,
                cameraToClip, worldToCamera, mm,
                indexDataLength, color);
    }

    public static void SetUniformColor(int program, Vector4f color)
    {
        ActivePrograms.get(program).SetUniformColor(color);
    }

    public static void SetLightPosition(int program, Vector3f lightPos)
    {
        ActivePrograms.get(program).SetLightPosition(lightPos);
    }

    public static void SetModelSpaceLightPosition(int program, Vector3f modelSpaceLightPosition)
    {
        ActivePrograms.get(program).SetModelSpaceLightPosition(modelSpaceLightPosition);
    }

    public static void SetDirectionToLight(int program, Vector3f dirToLight)
    {
        ActivePrograms.get(program).SetDirectionToLight(dirToLight);
    }

    public static void SetLightIntensity(int program, Vector4f lightIntensity)
    {
        ActivePrograms.get(program).SetLightIntensity(lightIntensity);
    }

    public static void SetAmbientIntensity(int program, Vector4f ambientIntensity)
    {
        ActivePrograms.get(program).SetAmbientIntensity(ambientIntensity);
    }

    public static void SetNormalModelToCameraMatrix(int program, Matrix3f normalModelToCameraMatrix)
    {
        ActivePrograms.get(program).SetNormalModelToCameraMatrix(normalModelToCameraMatrix);
    }

    public static void SetModelToCameraMatrix(int program, Matrix4f modelToCameraMatrix)
    {
        ActivePrograms.get(program).SetModelToCameraMatrix(modelToCameraMatrix);
    }

    public static void SetCameraToClipMatrixUnif(int program, Matrix4f cameraToClipMatrix)
    {
        ActivePrograms.get(program).SetCameraToClipMatrixUnif(cameraToClipMatrix);
    }


    public static void SetWorldToCameraMatrixUnif(int program, Matrix4f worldToCameraMatrix)
    {
        ActivePrograms.get(program).SetWorldToCameraMatrixUnif(worldToCameraMatrix);
    }

    public static void SetModelToWorldMatrix(int program, Matrix4f modelToWorldMatrix)
    {
        ActivePrograms.get(program).SetModelToWorldMatrix(modelToWorldMatrix);
    }



    public static void Use(int program)
    {
        ActivePrograms.get(program).Use();
    }
}
