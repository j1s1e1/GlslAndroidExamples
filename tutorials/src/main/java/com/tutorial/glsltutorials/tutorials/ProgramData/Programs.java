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

    public static void reset()
    {
        ActivePrograms = new ArrayList<ProgramData>();
    }

    public static int addProgram(String vertexShader, String fragmentShader)
    {
        int program_number = -1;
        boolean new_program = true;
        for (int i = 0; i < ActivePrograms.size(); i++)
        {
            ProgramData pd = ActivePrograms.get(i);

            if (pd.compareShaders(vertexShader, fragmentShader))
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

    public static void draw(int program, int[] vertexBufferObject, int[] indexBufferObject,
                            Matrix4f mm, int indexDataLength, float[] color)
    {
        ActivePrograms.get(program).draw(vertexBufferObject, indexBufferObject, mm, indexDataLength, color);
    }

    public static void drawWireFrame(int program, int[] vertexBufferObject, int[] indexBufferObject,
                            Matrix4f mm, int indexDataLength, float[] color)
    {
        ActivePrograms.get(program).drawWireFrame(vertexBufferObject, indexBufferObject, mm, indexDataLength, color);
    }

    public static void setUniformColor(int program, Vector4f color)
    {
        ActivePrograms.get(program).setUniformColor(color);
    }

    public static void setUniformTexture(int program, int colorTexUnit)
    {
        ActivePrograms.get(program).setUniformTexture(colorTexUnit);
    }

    public static void setUniformScale(int program, float scale)
    {
        ActivePrograms.get(program).setUniformScale(scale);
    }

    public static void loadTexture(int program, int texture, boolean oneTwenty)
    {
        ActivePrograms.get(program).loadTexture(texture, oneTwenty);
    }

    public static void setTexture(int program, int texture)
    {
        ActivePrograms.get(program).setTexture(texture);
    }


    public static void setLightPosition(int program, Vector3f lightPos)
    {
        ActivePrograms.get(program).setLightPosition(lightPos);
    }

    public static void setModelSpaceLightPosition(int program, Vector3f modelSpaceLightPosition)
    {
        ActivePrograms.get(program).setModelSpaceLightPosition(modelSpaceLightPosition);
    }

    public static void setDirectionToLight(int program, Vector3f dirToLight)
    {
        ActivePrograms.get(program).setDirectionToLight(dirToLight);
    }

    public static void setLightIntensity(int program, Vector4f lightIntensity)
    {
        ActivePrograms.get(program).setLightIntensity(lightIntensity);
    }

    public static void setAmbientIntensity(int program, Vector4f ambientIntensity)
    {
        ActivePrograms.get(program).setAmbientIntensity(ambientIntensity);
    }

    public static void setNormalModelToCameraMatrix(int program, Matrix3f normalModelToCameraMatrix)
    {
        ActivePrograms.get(program).setNormalModelToCameraMatrix(normalModelToCameraMatrix);
    }

    public static void setModelToCameraMatrix(int program, Matrix4f modelToCameraMatrix)
    {
        ActivePrograms.get(program).setModelToCameraMatrix(modelToCameraMatrix);
    }

    public static void setCameraToClipMatrixUnif(int program, Matrix4f cameraToClipMatrix)
    {
        ActivePrograms.get(program).setCameraToClipMatrixUnif(cameraToClipMatrix);
    }


    public static void setWorldToCameraMatrixUnif(int program, Matrix4f worldToCameraMatrix)
    {
        ActivePrograms.get(program).setWorldToCameraMatrixUnif(worldToCameraMatrix);
    }

    public static void setModelToWorldMatrix(int program, Matrix4f modelToWorldMatrix)
    {
        ActivePrograms.get(program).setModelToWorldMatrix(modelToWorldMatrix);
    }

    public static void use(int program)
    {
        ActivePrograms.get(program).use();
    }
}
