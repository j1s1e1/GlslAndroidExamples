package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jamie on 1/2/14.
 */
public class TextClass {
    public static final String textVertexShader =
            "uniform mat4 uRotation;" +
            "attribute vec4 vPosition;" +
            "uniform vec3 uTranslate;" +

            "void main(void)" +
            "{" +
            "gl_Position = uRotation * vPosition + vec4(uTranslate,0.0);" +
            "}";

    public static final String textFragmentShader =
            "precision mediump float;" +
            "uniform vec4 uColor;" +
            "void main() {" +
            "  gl_FragColor = uColor;" +
            "}";


    private FloatBuffer vertexBuffer;
    private static int mProgram = -1;
    private static int mPositionHandle;
    private static int uColorHandle;
    private static int uRotationHandle;
    private static int uTranslateHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float[] textCoords;
    private int vertexCount; // = textCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0f, 0f, 1f, 1.0f};

    public void SetColor(float red, float green, float blue) {
        color[0] = red;
        color[1] = green;
        color[2] = blue;
    }

    float[] offset = new float[]{0,0,0};
    public void Move(float x_move, float y_move, float z_move) {
        offset[0] = offset[0] + x_move;
        offset[1] = offset[1] + y_move;
        offset[2] = offset[2] + z_move;
    }

    private Float[] AddDash()
    {
        Float[] dash = new Float [18];
        dash[0] = -3f;
        dash[1] = 1f;
        dash[2] = 0f;
        dash[3] = -3f;
        dash[4] = -1f;
        dash[5] = 0f;
        dash[6] = 3f;
        dash[7] = -1f;
        dash[8] = 0f;
        dash[9] = -3f;
        dash[10] = 1f;
        dash[11] = 0f;
        dash[12] = 3f;
        dash[13] = -1f;
        dash[14] = 0f;
        dash[15] = 3f;
        dash[16] = 1f;
        dash[17] = 0f;

        return dash;
    }

    float scaleFactor;
    float letter_offset;
    int current_letter;

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

    private Float[] ReverseRotation(Float[] input)
    {
        Float[] output = new Float[input.length];
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

    private Float[] Rectangle(float width, float height)
    {
        Float[] rectangle = new Float[18];
        rectangle[0] = -width/2;
        rectangle[1] = height/2;
        rectangle[2] = 0f;
        rectangle[3] = rectangle[0];
        rectangle[4] = -rectangle[1];
        rectangle[5] = 0f;
        rectangle[6] = -rectangle[0];
        rectangle[7] = rectangle[1];
        rectangle[8] = 0f;

        rectangle[9] = -rectangle[0];
        rectangle[10] = rectangle[1];
        rectangle[11] = 0f;
        rectangle[12] = rectangle[0];
        rectangle[13] = -rectangle[1];
        rectangle[14] = 0f;
        rectangle[15] = -rectangle[0];
        rectangle[16] = -rectangle[1];
        rectangle[17] = 0f;
        return rectangle;
    }

    private Float[] AddA()
    {
        Float[] A = new Float [54];
        A[0] = -0.3f;
        A[1] = 6f;
        A[2] = 0f;
        A[3] = -3f;
        A[4] = -6f;
        A[5] = 0f;
        A[6] = -2.4f;
        A[7] = -6f;
        A[8] = 0f;

        A[9] = A[0];
        A[10] = A[1];
        A[11] = A[2];
        A[12] = A[6];
        A[13] = A[7];
        A[14] = A[8];
        A[15] = A[0] + 0.6f;
        A[16] = A[1];
        A[17] = A[2];

        A[18] = A[0];
        A[19] = A[1];
        A[20] = A[2];
        A[21] = -A[6];
        A[22] = A[7];
        A[23] = A[8];
        A[24] = A[0] + 0.6f;
        A[25] = A[1];
        A[26] = A[2];

        A[27] = A[24];
        A[28] = A[25];
        A[29] = A[26];
        A[30] = A[21];
        A[31] = A[22];
        A[32] = A[23];
        A[33] = A[21] + 0.6f;
        A[34] = A[22];
        A[35] = A[23];

        A[36] = -1.5f;
        A[37] = 0.5f;
        A[38] = 0f;
        A[39] = A[36];
        A[40] = -A[37];
        A[41] = 0f;
        A[42] = -A[36];
        A[43] = A[37];
        A[44] = 0f;

        A[45] = A[36];
        A[46] = -A[37];
        A[47] = 0f;
        A[48] = -A[36];
        A[49] = -A[37];
        A[50] = 0f;
        A[51] = -A[36];
        A[52] = A[37];
        A[53] = 0f;
        return A;
    }

    private Float[] GetChar(char next_char)
    {
        Float[] result;
        switch (next_char)
        {
            case (char)'A': result = AddA(); break;
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
            default:  result = AddDash(); break;
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

    public TextClass(String text, float scaleFactorIn, float letterOffsetIn) {
        current_letter = 0;
        scaleFactor = scaleFactorIn;
        letter_offset = letterOffsetIn;
        textCoords = GetCoordsFromText(text);
        vertexCount = textCoords.length / COORDS_PER_VERTEX;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                textCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(textCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // prepare shaders and OpenGL program
        if (mProgram == -1) {
            int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER,
                    textVertexShader);
            int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    textFragmentShader);
            mProgram = Shader.createAndLinkProgram(vertexShader, fragmentShader);

            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            uColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
            uRotationHandle = GLES20.glGetUniformLocation(mProgram, "uRotation");
            uTranslateHandle = GLES20.glGetUniformLocation(mProgram, "uTranslate");
        }
        rotate(0, new float[]{ 0.0f,0.0f,1.0f});
    }

    float[] rotationMat;

    public void rotate(float angle, float[] axis)
    {
        rotationMat = new float[16];
        Matrix.setIdentityM(rotationMat,0);
        Matrix.rotateM(rotationMat, 0, angle, axis[0], axis[1], axis[2]);
    }

    public void draw() {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(uRotationHandle, 1, false, rotationMat, 0);
        GLES20.glUniform3fv(uTranslateHandle, 1, offset, 0);
        GLES20.glUniform4fv(uColorHandle, 1, color, 0);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        vertexBuffer.position(0);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glUseProgram(0);
    }


}
