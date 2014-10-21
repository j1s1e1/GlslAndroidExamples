package com.tutorial.glsltutorials.tutorials.Text;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jamie on 1/2/14.
 */
public class TextClass extends Shape {
    String VertexShader = VertexShaders.PosOnlyWorldTransform_vert;
    String FragmentShader = FragmentShaders.ColorUniform_frag;

    int progarmNumber;

    float scaleFactor;
    float letter_offset;
    int current_letter;

    boolean staticText = false;
    boolean reverseRotation = true;

    boolean updateLock = false;
    boolean waitingForUpdate = false;

    private Float[] SwapX(Float[] input)
    {
        for (int i = 0; i < input.length; i = i + 3)
        {
            input[i] = -input[i];
        }
        return input;
    }

    private Float[] SwapY(Float[] input)
    {
        for (int i = 1; i < input.length; i = i + 3)
        {
            input[i] = -input[i];
        }
        return input;
    }

    private float[] ReverseRotation(float[] input)
    {
        float[] output = new float[input.length];
        for (int i = 0; i < output.length; i++)
        {
            switch (i % 9)
            {
                case 0:
                case 1:
                case 2:
                    output[i] = input[i];
                    break;
                case 3:
                case 4:
                case 5:
                    output[i] = input[i+3];
                    break;
                case 6:
                case 7:
                case 8:
                    output[i] = input[i-3];
                    break;
            }
        }
        return output;
    }

    private Float[] MoveX(Float[] input, float distance)
    {
        for (int i = 0; i < input.length; i = i + 3)
        {
            input[i] = input[i] + distance;
        }
        return input;
    }

    private Float[] MoveY(Float[] input, float distance)
    {
        for (int i = 1; i < input.length; i = i + 3)
        {
            input[i] = input[i] + distance;
        }
        return input;
    }

    private Float[] MoveXY(Float[] input, float distanceX, float distanceY)
    {
        input = MoveX(input, distanceX);
        input = MoveY(input, distanceY);
        return input;
    }

    private Float[] GetChar(char next_char)
    {
        Float[] result;
        switch (next_char)
        {
            case (char)'A': result = Letters.A; break;
            case (char)'B': result = Letters.B; break;
            case (char)'C': result = Letters.C; break;
            case (char)'D': result = Letters.D; break;
            case (char)'E': result = Letters.E; break;
            case (char)'F': result = Letters.F; break;
            case (char)'G': result = Letters.G; break;
            case (char)'H': result = Letters.H; break;
            case (char)'I': result = Letters.I; break;
            case (char)'J': result = Letters.J; break;
            case (char)'K': result = Letters.K; break;
            case (char)'L': result = Letters.L; break;
            case (char)'M': result = Letters.M; break;
            case (char)'N': result = Letters.N; break;
            case (char)'O': result = Letters.O; break;
            case (char)'P': result = Letters.P; break;
            case (char)'Q': result = Letters.Q; break;
            case (char)'R': result = Letters.R; break;
            case (char)'S': result = Letters.S; break;
            case (char)'T': result = Letters.T; break;
            case (char)'U': result = Letters.U; break;
            case (char)'V': result = Letters.V; break;
            case (char)'W': result = Letters.W; break;
            case (char)'X': result = Letters.X; break;
            case (char)'Y': result = Letters.Y; break;
            case (char)'Z': result = Letters.Z; break;

            case (char)'0': result = Numbers.Zero; break;
            case (char)'1': result = Numbers.One; break;
            case (char)'2': result = Numbers.Two; break;
            case (char)'3': result = Numbers.Three; break;
            case (char)'4': result = Numbers.Four; break;
            case (char)'5': result = Numbers.Five; break;
            case (char)'6': result = Numbers.Six; break;
            case (char)'7': result = Numbers.Seven; break;
            case (char)'8': result = Numbers.Eight; break;
            case (char)'9': result = Numbers.Nine; break;

            case (char)' ': result = Symbols.Space; break;
            case (char)'.': result = Symbols.Period; break;
            case (char)'=': result = Symbols.Equals; break;

            default:  result = Symbols.Dash; break;
        }
        result = ScaleChar(result);
        result = ShiftChar(result);
        return result;
    }

    private Float[] ScaleChar(Float[] unscaled_char)
    {
        Float[] scaled_char = new Float[unscaled_char.length];
        for (int i = 0; i < unscaled_char.length; i++)
        {
            scaled_char[i] = unscaled_char[i] / 100f * scaleFactor;
        }
        return scaled_char;
    }

    private Float[] ShiftChar(Float[] unshifted_char)
    {
        Float[] shifted_char = new Float[unshifted_char.length];
        for (int i = 0; i < unshifted_char.length; i++)
        {
            if (i % 3 == 0)
            {
                shifted_char[i] = unshifted_char[i] + letter_offset * current_letter;
            }
            else
            {
                shifted_char[i] = unshifted_char[i];
            }
        }
        return shifted_char;
    }

    private float[] GetCoordsFromText(String text)
    {
        ArrayList<Float> new_text = new ArrayList<Float>();
        for (int i = 0; i < text.length(); i++)
        {
            Collections.addAll(new_text, GetChar(text.charAt(i)));
            current_letter++;
        }
        float[] result = new float[new_text.size()];
        int i = 0;
        for (Float f : new_text) {
            result[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return result;
    }

    public TextClass(String text, float scaleFactorIn, float letterOffsetIn)
    {
        this(text, scaleFactorIn, letterOffsetIn, false, true);
    }

    public TextClass(String text, float scaleFactorIn, float letterOffsetIn, boolean staticText)
    {
        this(text, scaleFactorIn, letterOffsetIn, staticText, true);
    }

    public TextClass(String text, float scaleFactorIn, float letterOffsetIn,
                         boolean staticTextIn, boolean reverseRotationIn)
    {
        staticText = staticTextIn;
        reverseRotation = reverseRotationIn;
        text = text.toUpperCase();
        current_letter = 0;
        scaleFactor = scaleFactorIn;
        letter_offset = letterOffsetIn;
        vertexStride = 3 * 4; // bytes per vertex, no color
        vertexData = GetCoordsFromText(text);
        if (reverseRotation)
        {
            vertexData = ReverseRotation(vertexData);
        }
        vertexCount = vertexData.length / COORDS_PER_VERTEX;
        SetupSimpleIndexBuffer();
        InitializeVertexBuffer();

        progarmNumber = Programs.AddProgram(VertexShader, FragmentShader);
    }

    public void UpdateText(String text)
    {
        updateLock = true;
        text = text.toUpperCase();
        current_letter = 0;
        vertexStride = 3 * 4; // bytes per vertex, no color
        vertexData = GetCoordsFromText(text);
        if (reverseRotation)
        {
            vertexData = ReverseRotation(vertexData);
        }
        vertexCount = vertexData.length / COORDS_PER_VERTEX;
        SetupSimpleIndexBuffer();
        InitializeVertexBuffer();
        updateLock = false;
    }

    float[] rotationMat;

    public void rotate(float angle, float[] axis)
    {
        rotationMat = new float[16];
        Matrix.setIdentityM(rotationMat,0);
        Matrix.rotateM(rotationMat, 0, angle, axis[0], axis[1], axis[2]);
    }

    public void draw() {
        Matrix4f mm = Rotate(modelToWorld, axis, angle);
        mm.M41 = offset.x;
        mm.M42 = offset.y;
        mm.M43 = offset.z;

        Matrix4f wtc = worldToCamera;
        if (staticText)
        {
            wtc = Matrix4f.Identity();
        }

        if (updateLock == false)
        {
            Programs.Draw(progarmNumber, vertexBufferObject, indexBufferObject, cameraToClip, wtc, mm,
                    indexData.length, color, COORDS_PER_VERTEX, vertexStride);
            waitingForUpdate = false;
        }
        else
        {
            waitingForUpdate = true;
        }
    }
}
