package com.tutorial.glsltutorials.tutorials.Shapes;
import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Jamie on 4/30/14.
 */
public class Shape
{
    protected static int BYTES_PER_FLOAT = 4;
    protected static int BYTES_PER_SHORT = 2;
    public static float global_x_offset = 0;
    public static float global_y_offset = 0;
    public static float global_z_offset = 0;
    public static float global_x_rotate = 0;
    public static float global_y_rotate = 0;
    public static float global_z_rotate = 0;

    protected float x_offset = 0;
    protected float y_offset = 0;
    protected float z_offset = 0;

    protected Vector3f offset = new Vector3f(0);

    protected float x = 0;
    protected float y = 0;
    protected float z = 0;

    protected boolean global_move = true;
    protected float angle = 0;

    protected Vector3f axis = new Vector3f(0f, 0f, 1f);

    protected float angle1 = 0;
    protected float angle2 = 0;
    protected float angle3 = 0;
    protected float rotation1 = 0;
    protected float rotation2 = 0;
    protected float rotation3 = 0;

    protected int vertexNormalBufferID;
    protected int indiciesBufferID;

    protected int[] indicesVboData;

    protected float[] color = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    protected int[] colorData = null;
    protected int colorBufferID;
    protected int bufferSize;

    public static boolean allGreen = false;

    // From Working Sphere
    // number of coordinates per vertex in this array
    protected int vertexCount;
    protected int COORDS_PER_VERTEX = 3;
    protected int vertexStride = COORDS_PER_VERTEX * 4 * 2; // bytes per vertex
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer indexBuffer;
    protected float[] vertexCoords;

    protected float[] vertexData;
    protected short[] indexData;

    public static Matrix4f worldToCamera = Matrix4f.Identity();

    protected IntBuffer vertexBufferObject;
    protected IntBuffer indexBufferObject;

    protected void SetupVertexBuffer()
    {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertexCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertexCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    protected void InitializeVertexBuffer()
    {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer vb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertexData.length * 4);
        // use the device hardware's native byte order
        vb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = vb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertexData);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // initialize index byte buffer for vertex indexes
        ByteBuffer sb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                indexData.length * 2);
        // use the device hardware's native byte order
        sb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        indexBuffer = sb.asShortBuffer();
        // add the coordinates to the FloatBuffer
        indexBuffer.put(indexData);
        // set the buffer to read the first coordinate
        indexBuffer.position(0);

        GLES20.glGenBuffers(1, vertexBufferObject);
        GLES20.glGenBuffers(1, indexBufferObject);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject.get(0));
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity(),
                indexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject.get(0));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity(),
                vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public Shape()
    {
    }

    public void Move (float x_add, float y_add, float z_add)
    {
        x = x + x_add;
        y = y + y_add;
        z = z + z_add;
    }

    public void SetOffset (float x_in, float y_in, float z_in)
    {
        x_offset = x_in;
        y_offset = y_in;
        z_offset = z_in;
    }
    public void SetXOffset(float x_in)
    {
        x_offset = x_in;
    }
    public void SetYOffset(float y_in)
    {
        y_offset = y_in;
    }
    public void SetZOffset(float z_in)
    {
        z_offset = z_in;
    }

    public void SetOffset(Vector3f offsetIn)
    {
        offset = offsetIn;
    }
    protected void SetupIndexBuffer()
    {
        indicesVboData = new int[vertexCount];
        for (int i = 0; i < indicesVboData.length; i++) {
            indicesVboData[i] = i;
        }
        // Element Array Buffer
        indiciesBufferID = VBO_Tools.SetupIntBuffer(indicesVboData);
    }

    // Set color with red, green, blue and alpha (opacity) values
    public void SetColor(float red, float green, float blue) {
        color[0] = red;
        color[1] = green;
        color[2] = blue;
    }

    public void SolidColor(int color)
    {
        colorData = new int[vertexCount];
        for (int i = 0; i < colorData.length; i++) {
            colorData[i] = VBO_Tools.ColorToRgba32(color);
        }
        // Color Array Buffer
        colorBufferID = VBO_Tools.SetupIntBuffer(colorData);
    }

    public void MultiColor(int[] colors)
    {
        colorData = new int[vertexCount];
        for (int i = 0; i < colorData.length; i++) {
            colorData[i] = VBO_Tools.ColorToRgba32(colors[(i % colors.length)]);
        }
        // Color Array Buffer
        colorBufferID = VBO_Tools.SetupIntBuffer(colorData);
    }

    public void SetRainbowColors()
    {
        // Color Data for the Verticies
        colorData = new int[vertexCount];
        for (int i = 0; i < colorData.length; i++) {
            if (allGreen) {
                switch (i % 8) {
                    case 0:
                        colorData[i] = 32 << 8;
                        break;
                    case 1:
                        colorData[i] = 64 << 8;
                        break;
                    case 2:
                        colorData[i] = 96 << 8;
                        break;
                    case 3:
                        colorData[i] = 128 << 8;
                        break;
                    case 4:
                        colorData[i] = 160 << 8;
                        break;
                    case 5:
                        colorData[i] = 192 << 8;
                        break;
                    case 6:
                        colorData[i] = 224 << 8;
                        ;
                        break;
                    case 7:
                        colorData[i] = 255 << 8;
                        break;
                }
            } else {
                switch (i % 8) {
                    case 0:
                        colorData[i] = VBO_Tools.ColorToRgba32(Colors.Red);
                        break;
                    case 1:
                        colorData[i] = VBO_Tools.ColorToRgba32(Colors.White);
                        break;
                    case 2:
                        colorData[i] = VBO_Tools.ColorToRgba32(Colors.Blue);
                        break;
                    case 3:
                        colorData[i] = VBO_Tools.ColorToRgba32(Colors.Green);
                        break;
                    case 4:
                        colorData[i] = VBO_Tools.ColorToRgba32(Colors.Yellow);
                        break;
                    case 5:
                        colorData[i] = VBO_Tools.ColorToRgba32(Colors.Orange);
                        break;
                    case 6:
                        colorData[i] = VBO_Tools.ColorToRgba32(Colors.Pink);
                        break;
                    case 7:
                        colorData[i] = VBO_Tools.ColorToRgba32(Colors.Purple);
                        break;
                }
            }
        }
        // Color Array Buffer
        colorBufferID = VBO_Tools.SetupIntBuffer(colorData);
    }

    public void SetAngles(float a1, float a2, float a3)
    {
        angle1 = a1;
        angle2 = a2;
        angle3 = a3;
    }

    public Vector3f GetAngles()
    {
        return new Vector3f(angle1, angle2, angle3);
    }

    public void SetRotations(float r1, float r2, float r3)
    {
        rotation1 = r1;
        rotation2 = r2;
        rotation3 = r3;
    }

    ///Applies a rotation matrix about the given axis, with the given angle in degrees.
    public Matrix4f Rotate(Matrix4f input, Vector3f axis, float angDegCCW)
    {
        Matrix4f rotation = Matrix4f.Rotate(axis, (float)Math.PI / 180.0f * angDegCCW);
        return Matrix4f.Mult(rotation, input);
    }

    public void Draw()
    {
        /*
        GLES20.glPushMatrix();
        {
            if (global_move) {
                GLES20.glRotate(global_x_rotate, 1, 0, 0);
                GLES20.glRotate(global_y_rotate, 0, 1, 0);
                GLES20.glRotate(global_z_rotate, 0, 0, 1);
                GLES20.glTranslate(global_x_offset, global_y_offset, global_z_offset);
            }
            GLES20.glTranslate(x + x_offset, y + y_offset, z + z_offset);
            GLES20.glRotate(angle1, 1, 0, 0);
            GLES20.glRotate(angle2, 0, 1, 0);
            GLES20.glRotate(angle3, 0, 1, 1);
            angle1 = (float)(angle1 + rotation1);
            angle2 = (float)(angle2 + rotation2);
            angle3 = (float)(angle3 + rotation3);
            // Color Array Buffer (Colors not used when lighting is enabled)
            VBO_Tools.BindColorBuffer(colorBufferID);
            // Vertex Normal Buffer
            VBO_Tools.BindVertexNormalBuffer(vertexNormalBufferID);
            // Element Array Buffer
            VBO_Tools.BindElementBuffer(indiciesBufferID, 3, 0);
        }
        GLES20.glPopMatrix();
        */
    }
}
