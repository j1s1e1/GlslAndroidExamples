package com.tutorial.glsltutorials.tutorials.ProgramData;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;

import java.nio.IntBuffer;
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
                            int indexDataLength, float[] color, int COORDS_PER_VERTEX, int vertexStride)
    {
        ActivePrograms.get(program).Draw (vertexBufferObject, indexBufferObject,
                cameraToClip, worldToCamera, mm,
                indexDataLength, color, COORDS_PER_VERTEX, vertexStride);
    }
}
