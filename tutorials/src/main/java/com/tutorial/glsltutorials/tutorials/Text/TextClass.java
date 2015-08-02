package com.tutorial.glsltutorials.tutorials.Text;

import android.opengl.Matrix;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jamie on 1/2/14.
 */
public class TextClass extends Shape {
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
            case 'A': result = Letters.A; break;
            case 'B': result = Letters.B; break;
            case 'C': result = Letters.C; break;
            case 'D': result = Letters.D; break;
            case 'E': result = Letters.E; break;
            case 'F': result = Letters.F; break;
            case 'G': result = Letters.G; break;
            case 'H': result = Letters.H; break;
            case 'I': result = Letters.I; break;
            case 'J': result = Letters.J; break;
            case 'K': result = Letters.K; break;
            case 'L': result = Letters.L; break;
            case 'M': result = Letters.M; break;
            case 'N': result = Letters.N; break;
            case 'O': result = Letters.O; break;
            case 'P': result = Letters.P; break;
            case 'Q': result = Letters.Q; break;
            case 'R': result = Letters.R; break;
            case 'S': result = Letters.S; break;
            case 'T': result = Letters.T; break;
            case 'U': result = Letters.U; break;
            case 'V': result = Letters.V; break;
            case 'W': result = Letters.W; break;
            case 'X': result = Letters.X; break;
            case 'Y': result = Letters.Y; break;
            case 'Z': result = Letters.Z; break;

            case '0': result = Numbers.Zero; break;
            case '1': result = Numbers.One; break;
            case '2': result = Numbers.Two; break;
            case '3': result = Numbers.Three; break;
            case '4': result = Numbers.Four; break;
            case '5': result = Numbers.Five; break;
            case '6': result = Numbers.Six; break;
            case '7': result = Numbers.Seven; break;
            case '8': result = Numbers.Eight; break;
            case '9': result = Numbers.Nine; break;

            case ' ': result = Symbols.Space; break;
            case '.': result = Symbols.Period; break;
            case '=': result = Symbols.Equals; break;

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
        setupSimpleIndexBuffer();
        initializeVertexBuffer();

        programNumber = Programs.addProgram(VertexShader, FragmentShader);
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
        setupSimpleIndexBuffer();
        initializeVertexBuffer();
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
        Matrix4f mm = rotate(modelToWorld, axis, angle);
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
            Programs.draw(programNumber, vertexBufferObject[0], indexBufferObject[0], mm, indexData.length, color);
            waitingForUpdate = false;
        }
        else
        {
            waitingForUpdate = true;
        }
    }
}
