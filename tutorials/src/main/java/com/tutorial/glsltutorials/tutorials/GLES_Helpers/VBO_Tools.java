package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

import android.opengl.GLES20;
import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Jamie on 4/30/14.
 */
public class VBO_Tools {
    public VBO_Tools ()
    {
    }

    static final int BYTES_PER_SHORT = 2;
    static final int BYTES_PER_INT = 4;
    static final int BYTES_PER_FLOAT = 4;

    // Color Array Buffer
    public static int SetupIntBuffer (int[] data)
    {
        int[] bufferID = new int[1];

        final IntBuffer intBuffer;
        intBuffer =  ByteBuffer.allocateDirect(data.length * BYTES_PER_INT)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        intBuffer.put(data).position(0);

        // Generate Array Buffer Id
        GLES20.glGenBuffers (1, bufferID, 0);

        // Bind current context to Array Buffer ID
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID[0]);

        // Send data to buffer
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, intBuffer.capacity(), intBuffer, GLES20.GL_STATIC_DRAW);

        // Validate that the buffer is the correct size
        /*
        GL.GetBufferParameter (BufferTarget.ArrayBuffer, BufferParameterName.BufferSize, out bufferSize);

        if (data.Length * sizeof(int) != bufferSize)
        {
            throw new ApplicationException ("Vertex array not uploaded correctly");
        }
        */

        // Clear the buffer Binding
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        return bufferID[0];
    }

    // Color Array Buffer
    public static int SetupShortElementBuffer30 (short[] data)
    {
        int[] bufferID = new int[1];

        final ShortBuffer shortBuffer;
        shortBuffer =  ByteBuffer.allocateDirect(data.length * BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        shortBuffer.put(data).position(0);

        // Generate Array Buffer Id
        GLES20.glGenBuffers(1, bufferID, 0);

        // Bind current context to Array Buffer ID
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferID[0]);

        // Send data to buffer
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, shortBuffer.capacity(), shortBuffer, GLES20.GL_STATIC_DRAW);

        // Validate that the buffer is the correct size
        /*
        GL.GetBufferParameter (BufferTarget.ArrayBuffer, BufferParameterName.BufferSize, out bufferSize);

        if (data.Length * sizeof(int) != bufferSize)
        {
            throw new ApplicationException ("Vertex array not uploaded correctly");
        }
        */

        // Clear the buffer Binding
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        return bufferID[0];
    }

    public static ShortBuffer MakeShortElementBuffer30 (short[] data)
    {
        final ShortBuffer shortBuffer;
        shortBuffer =  ByteBuffer.allocateDirect(data.length * BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        shortBuffer.put(data).position(0);

        return shortBuffer;
    }

    public static IntBuffer MakeIntBuffer (int[] data)
    {
        IntBuffer intBuffer;
        intBuffer =  ByteBuffer.allocateDirect(data.length * BYTES_PER_INT)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        intBuffer.put(data).position(0);
        return intBuffer;
    }

    public static ShortBuffer MakeShortBuffer (short[] data)
    {
        ShortBuffer shortBuffer;
        shortBuffer =  ByteBuffer.allocateDirect(data.length * BYTES_PER_INT)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        shortBuffer.put(data).position(0);
        return shortBuffer;
    }

    // TexCoord Array Buffer
    public static int SetupVector2fBuffer (Vector2f[] data)
    {
        int[] bufferID = new int[1];
        float[] float_data = new float[data.length * 2];
        for (int i = 0; i < data.length; i++)
        {
            float_data[i * 2] = data[i].getX();
            float_data[i * 2 + 1] = data[i].getY();
        }

        final FloatBuffer floatBuffer;
        floatBuffer =  ByteBuffer.allocateDirect(float_data.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(float_data).position(0);


        // Generate Array Buffer Id
        GLES20.glGenBuffers(1, bufferID, 0);

        // Bind current context to Array Buffer ID
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID[0]);

        // Send data to buffer
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, floatBuffer.capacity(), floatBuffer, GLES20.GL_STATIC_DRAW);

        // Validate that the buffer is the correct size
        /*
        GL.GetBufferParameter (BufferTarget.ArrayBuffer, BufferParameterName.BufferSize, out bufferSize);
        if (data.Length * 8 != bufferSize)
        {
            throw new ApplicationException ("TexCoord array not uploaded correctly");
        }
        */

        // Clear the buffer Binding
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        return bufferID[0];
    }


    // Normal Array Buffer
    public static int SetupVector3fBuffer (Vector3f[] data)
    {
        int[] bufferID = new int[1];
        float[] float_data = new float[data.length * 3];
        for (int i = 0; i < data.length; i++)
        {
            float_data[i * 3] = data[i].getX();
            float_data[i * 3 + 1] = data[i].getY();
            float_data[i * 3 + 2] = data[i].getZ();
        }

        final FloatBuffer floatBuffer;
        floatBuffer =  ByteBuffer.allocateDirect(float_data.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(float_data).position(0);

        // Generate Array Buffer Id
        GLES20.glGenBuffers(1, bufferID, 0);

        // Bind current context to Array Buffer ID
         GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID[0]);

        // Send data to buffer
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, floatBuffer.capacity(), floatBuffer, GLES20.GL_STATIC_DRAW);

        /*
        // Validate that the buffer is the correct size
        GL.GetBufferParameter (BufferTarget.ArrayBuffer, BufferParameterName.BufferSize, out bufferSize);
        if (data.Length * Vector3f.ByteSize != bufferSize)
        {
            throw new ApplicationException ("Normal array not uploaded correctly");
        }
        */

        // Clear the buffer Binding
        GLES20.glBindBuffer (GLES20.GL_ARRAY_BUFFER, 0);
        return bufferID[0];
    }

    static void createVertexBuffer(int target, float[] vertices, int bufferId) {
        int size = vertices.length * 4;
        FloatBuffer fb = ByteBuffer.allocateDirect(4*vertices.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(vertices);
        fb.position(0);

        createBuffer(target, fb, size, bufferId);
    }

    static void createBuffer(int target, Buffer buf, int size, int bufferId) {
        GLES20.glBindBuffer(target, bufferId);
        GLES20.glBufferData(target, size, buf, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(target, 0);
    }

    // Normal Array Buffer
    public static int SetupFloatBuffer (float[] data)
    {
        int[] bufferID = new int[1];

        FloatBuffer floatBuffer;
        floatBuffer =  ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);

        // Generate Array Buffer Id
        GLES20.glGenBuffers(1, bufferID, 0);

        // Bind current context to Array Buffer ID
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID[0]);

        // Send data to buffer
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, floatBuffer.capacity(), floatBuffer, GLES20.GL_STATIC_DRAW);

        /*
        // Validate that the buffer is the correct size
        GL.GetBufferParameter (BufferTarget.ArrayBuffer, BufferParameterName.BufferSize, out bufferSize);
        if (data.Length * sizeof(float) != bufferSize)
        {
            throw new ApplicationException ("Normal array not uploaded correctly");
        }
        */

        // Clear the buffer Binding
        GLES20.glBindBuffer (GLES20.GL_ARRAY_BUFFER, 0);
        return bufferID[0];
    }

    // Normal Array Buffer
    public static FloatBuffer MakeFloatBuffer (float[] data)
    {
        FloatBuffer floatBuffer;
        floatBuffer =  ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static void ChangeFloatBuffer(float[] data, int bufferID)
    {

        final FloatBuffer floatBuffer;
        floatBuffer =  ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(data).position(0);

        // Bind current context to Array Buffer ID
         GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID);

        // Send data to buffer
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, floatBuffer.capacity(), floatBuffer, GLES20.GL_STATIC_DRAW);

        /*
        // Validate that the buffer is the correct size
        GL.GetBufferParameter(BufferTarget.ArrayBuffer, BufferParameterName.BufferSize, out bufferSize);
        if (data.Length * sizeof(float) != bufferSize) {
        throw new ApplicationException("Normal array not uploaded correctly");

        }
        */

        // Clear the buffer Binding
        GLES20.glBindBuffer (GLES20.GL_ARRAY_BUFFER, 0);
    }


    // Color Array Buffer (Colors not used when lighting is enabled)
    public static void BindColorBuffer (int bufferID)
    {

        // Bind to the Array Buffer ID
         GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID);
        /*
        // Set the Pointer to the current bound array describing how the data ia stored
        GLES20.glColorPointer (4, ColorPointerType.UnsignedByte, sizeof(int), IntPtr.Zero);

        // Enable the client state so it will use this array buffer pointer
        GL.EnableClientState (ArrayCap.ColorArray);
        */
        // ???GLES20.glEnableVertexAttribArray(GLES20.GL_COLOR_ATTACHMENT0);
    }

    // Vertex Array Buffer
    public static void BindVertexBuffer (int bufferID)
    {
        // Bind to the Array Buffer ID
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID);

        // Set the Pointer to the current bound array describing how the data ia stored
        //GL.VertexPointer (3, VertexPointerType.Float, Vector3f.ByteSize, IntPtr.Zero);

        // Enable the client state so it will use this array buffer pointer
        //GL.EnableClientState (ArrayCap.VertexArray);

    }

    // Vertex Array Buffer
    public static void BindVertexNormalBuffer (int bufferID)
    {

        // Bind to the Array Buffer ID
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID);

        /*
        // Set the Pointers to the current bound array describing how the data ia stored
        GL.VertexPointer(3, VertexPointerType.Float, 2 * Vector3f.ByteSize, new IntPtr (0));
        GL.NormalPointer(NormalPointerType.Float, 2 * Vector3f.ByteSize, new IntPtr (Vector3f.ByteSize));

        // Enable the client state so it will use this array buffer pointer
        GL.EnableClientState(ArrayCap.VertexArray);
        GL.EnableClientState(ArrayCap.NormalArray);
        */
    }

    // Element Array Buffer
    public static void BindElementBuffer (int bufferID, int elementCount, int offset)
    {
        // Bind to the Array Buffer ID
         GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferID);

        // Draw the elements in the element array buffer
        // Draws up items in the Color, Vertex, TexCoordinate, and Normal Buffers using indices in the ElementArrayBuffer
        //GLES20.glDrawElements(GLES20.GL_ELEMENT_ARRAY_BUFFER, elementCount, DrawElementsType.UnsignedInt, (IntPtr)offset);
    }

    public static void BindTextureBuffer (int bufferID)
    {

        // Bind to the Array Buffer ID
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferID);

        // Set the Pointer to the current bound array describing how the data ia stored
        //GL.TexCoordPointer (2, TexCoordPointerType.Float, 8, IntPtr.Zero);

        // Enable the client state so it will use this array buffer pointer
        //GL.EnableClientState (ArrayCap.TextureCoordArray);

    }

    /// <summary>
    /// Converts a Color instance into an int representation
    /// </summary>
    /// <param name="c">
    /// A <see cref="Color"/> instance to be converted
    /// </param>
    /// <returns>
    /// A <see cref="System.Int32"/>
    /// </returns>
    public static int ColorToRgba32 (int color)
    {
        return (int) color; //((c.A << 24) | (c.B << 16) | (c.G << 8) | c.R);
    }

    public static int[] Rgba32ToColor4(int  Rgba32)
    {
        int[] result =  new int[]{Rgba32 & 0xFF, (Rgba32 >> 8) & 0xFF,
                (Rgba32 >> 16) & 0xFF, (Rgba32 >> 24) & 0xFF };
        return result;
    }

    public static float[] Rgba32ToColor3(int  Rgba32)
    {
        float[] result = new float[]{
                (Rgba32 & 0xFF)/256f,
                ((Rgba32 >> 8) & 0xFF) / 256f,
                ((Rgba32 >> 16) & 0xFF) / 256f};
        return result;
    }

    public static FloatBuffer InitializeVertexBuffer(float[] data)
    {
        FloatBuffer vertexBuffer;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                data.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(data);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        return vertexBuffer;
    }

    public static ShortBuffer InitializeElementBuffer(short[] data)
    {
        ShortBuffer elementBuffer;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 2 bytes per short)
                data.length * 2);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // create a floating point buffer from the ByteBuffer
        elementBuffer = bb.asShortBuffer();
        // add the coordinates to the FloatBuffer
        elementBuffer.put(data);
        // set the buffer to read the first coordinate
        elementBuffer.position(0);
        return elementBuffer;
    }

}
