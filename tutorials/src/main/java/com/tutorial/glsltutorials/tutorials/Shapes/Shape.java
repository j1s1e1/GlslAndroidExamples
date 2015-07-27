package com.tutorial.glsltutorials.tutorials.Shapes;
import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Jamie on 4/30/14.
 */
public class Shape
{
    protected static int BYTES_PER_FLOAT = 4;
    protected static int BYTES_PER_SHORT = 2;
    public static float global_x_rotate = 0;
    public static float global_y_rotate = 0;
    public static float global_z_rotate = 0;

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
    public static Matrix4f cameraToClip = Matrix4f.Identity();
    public Matrix4f modelToWorld = Matrix4f.Identity();

    protected int[] vertexBufferObject = new int[1];
    protected int[] indexBufferObject = new int[1];

    protected String VertexShader = VertexShaders.PosOnlyWorldTransform_vert;
    protected String FragmentShader = FragmentShaders.ColorUniform_frag;
    protected int programNumber;

    public void setProgram(int newProgram)
    {
        programNumber = newProgram;
    }

    public int getProgram()
    {
        return programNumber;
    }


    public static void resetCameraToClipMatrix()
    {
        cameraToClip = Matrix4f.Identity();
    }

    public static void resetWorldToCameraMatrix()
    {
        worldToCamera = Matrix4f.Identity();
    }

    public static void setWorldToCameraRotation(float xRotation, float yRotation, float zRotation)
    {
        resetWorldToCameraMatrix();
        rotateWorld(Vector3f.UnitX, xRotation);
        rotateWorld(Vector3f.UnitY, yRotation);
        rotateWorld(Vector3f.UnitZ, zRotation);
    }

    public static void scaleWorldToCameraMatrix(float scaleFactor)
    {
        worldToCamera.Scale(new Vector3f(scaleFactor, scaleFactor, scaleFactor));
    }

    protected void setupSimpleIndexBuffer()
    {
        indexData = new short[vertexData.length/COORDS_PER_VERTEX];
        for (short i = 0; i < indexData.length; i++)
        {
            indexData[i] = i;
        }
    }

    protected void setupVertexBuffer()
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

    protected void initializeVertexBuffer()
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
                // (number of coordinate values * 2 bytes per short)
                indexData.length * 2);
        // use the device hardware's native byte order
        sb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        indexBuffer = sb.asShortBuffer();
        // add the coordinates to the FloatBuffer
        indexBuffer.put(indexData);
        // set the buffer to read the first coordinate
        indexBuffer.position(0);

        GLES20.glGenBuffers(1, vertexBufferObject, 0);
        GLES20.glGenBuffers(1, indexBufferObject, 0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * BYTES_PER_SHORT,
                indexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * BYTES_PER_FLOAT,
                vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public void move (float x_add, float y_add, float z_add)
    {
        move(new Vector3f(x_add, y_add, z_add));
    }

    public void move (Vector3f v)
    {
        modelToWorld.SetRow3(modelToWorld.GetRow3().add(new Vector4f(v, 0f)));
    }

    public void setOffset(float x, float y, float z)
    {
        setOffset(new Vector3f(x, y, z));
    }

    public void setOffset (Vector3f offsetIn)
    {
        modelToWorld.SetRow3(new Vector4f(offsetIn, 1.0f));
    }

    public void setXOffset(float x_in)
    {
        modelToWorld.M41 = x_in;
    }
    public void setYOffset(float y_in)
    {
        modelToWorld.M42 = y_in;
    }
    public void setZOffset(float z_in)
    {
        modelToWorld.M43 = z_in;
    }

    public Vector3f getOffset()
    {
        return new Vector3f(modelToWorld.M41, modelToWorld.M42, modelToWorld.M43);
    }

    protected void setupIndexBuffer()
    {
        indicesVboData = new int[vertexCount];
        for (int i = 0; i < indicesVboData.length; i++) {
            indicesVboData[i] = i;
        }
        // Element Array Buffer
        indiciesBufferID = VBO_Tools.SetupIntBuffer(indicesVboData);
    }

    public void setAxis(Vector3f axisIn)
    {
        axis = axisIn;
    }

    public void updateAngle(float degrees)
    {
        angle = degrees;
    }

    // Set color with red, green, blue and alpha (opacity) values
    public void setColor(float red, float green, float blue) {
        color[0] = red;
        color[1] = green;
        color[2] = blue;
    }

    public void setColor(float[] new_color)
    {
        color = new_color;
    }

    public void solidColor(int color)
    {
        colorData = new int[vertexCount];
        for (int i = 0; i < colorData.length; i++) {
            colorData[i] = VBO_Tools.ColorToRgba32(color);
        }
        // Color Array Buffer
        colorBufferID = VBO_Tools.SetupIntBuffer(colorData);
    }

    public void multiColor(int[] colors)
    {
        colorData = new int[vertexCount];
        for (int i = 0; i < colorData.length; i++) {
            colorData[i] = VBO_Tools.ColorToRgba32(colors[(i % colors.length)]);
        }
        // Color Array Buffer
        colorBufferID = VBO_Tools.SetupIntBuffer(colorData);
    }

    public void setRainbowColors()
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

    public void setAngles(float a1, float a2, float a3)
    {
        angle1 = a1;
        angle2 = a2;
        angle3 = a3;
    }

    public Vector3f getAngles()
    {
        return new Vector3f(angle1, angle2, angle3);
    }

    public void setRotations(float r1, float r2, float r3)
    {
        rotation1 = r1;
        rotation2 = r2;
        rotation3 = r3;
    }

    ///Applies a rotation matrix about the given axis, with the given angle in degrees.
    public Matrix4f rotate(Matrix4f input, Vector3f axis, float angDegCCW)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(axis, (float) Math.PI / 180.0f * angDegCCW);
        return Matrix4f.mul(rotation, input);
    }

    public static void rotateWorld(Vector3f axis, float angDegCCW)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(axis, (float) Math.PI / 180.0f * angDegCCW);
        worldToCamera = Matrix4f.mul(worldToCamera, rotation);
    }

    public static void rotateWorld(Vector3f offset, Vector3f rotationAxis, float angleDeg)
    {
        Matrix4f rotation = Matrix4f.createFromAxisAngle(rotationAxis, (float) Math.PI / 180.0f * angleDeg);
        worldToCamera.SetRow3(worldToCamera.GetRow3().add(new Vector4f(offset, 0)));
        worldToCamera = Matrix4f.mul(worldToCamera, rotation);
        worldToCamera.SetRow3(worldToCamera.GetRow3().sub(new Vector4f(offset, 0)));
    }

    public void rotateShape(Vector3f rotationAxis, float angleDeg)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, (float)Math.PI / 180.0f * angleDeg);
        modelToWorld = Matrix4f.mul(modelToWorld, rotation);
    }

    public void rotateShape(Vector3f offset, Vector3f rotationAxis, float angleDeg)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, (float)Math.PI / 180.0f * angleDeg);
        modelToWorld.SetRow3(modelToWorld.GetRow3().sub(new Vector4f(offset, 0)));
        modelToWorld = Matrix4f.mul(modelToWorld, rotation);
        modelToWorld.SetRow3(modelToWorld.GetRow3().add(new Vector4f(offset, 0)));
    }

    public void setRotation(Matrix3f rotation)
    {
        modelToWorld.SetRow0(new Vector4f(rotation.GetRow0(), modelToWorld.M14));
        modelToWorld.SetRow1(new Vector4f(rotation.GetRow1(), modelToWorld.M24));
        modelToWorld.SetRow2(new Vector4f(rotation.GetRow2(), modelToWorld.M34));
    }

    public void scale(Vector3f scale)
    {
        modelToWorld = Matrix4f.mul(modelToWorld, Matrix4f.createScale(scale));
    }

    public void draw()
    {

    }

    public static void setCameraToClipMatrix(Matrix4f m)
    {
        cameraToClip = m;
    }

    public static void setWorldToCameraMatrix(Matrix4f m)
    {
        worldToCamera = m;
    }

    public static void moveWorld(Vector3f v)
    {
        worldToCamera.M41 += v.x;
        worldToCamera.M42 += v.y;
        worldToCamera.M43 += v.z;
    }
}
