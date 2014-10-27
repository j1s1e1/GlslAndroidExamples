package com.tutorial.glsltutorials.tutorials.Portability;

import android.provider.Settings;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Created by jamie on 10/26/14.
 */
public class BitConverter {

    public static byte[] GetBytes(Integer value)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
        buffer.putInt(value);
        return buffer.array();
    }

    public static byte[] GetBytes(Float value)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
        buffer.putFloat(value);
        return buffer.array();
    }

    public static byte[] GetBytes(Short value)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
        buffer.putShort(value);
        return buffer.array();
    }

    public static ArrayList<Byte> GetByteList(Integer value)
    {
        ArrayList<Byte> result = new ArrayList<Byte>();
        byte[] bytes = GetBytes(value);
        for (byte b : bytes)
        {
            result.add(b);
        }
        return result;
    }

    public static ArrayList<Byte> GetByteList(Float value)
    {
        ArrayList<Byte> result = new ArrayList<Byte>();
        byte[] bytes = GetBytes(value);
        for (byte b : bytes)
        {
            result.add(b);
        }
        return result;
    }

    public static ArrayList<Byte> GetByteList(Short value)
    {
        ArrayList<Byte> result = new ArrayList<Byte>();
        byte[] bytes = GetBytes(value);
        for (byte b : bytes)
        {
            result.add(b);
        }
        return result;
    }

    public static int byteArrayToInt(byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }

    public static byte[] intToByteArray(int i) {
        final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(i);
        return bb.array();
    }

    public static ByteBuffer ArrayToBuffer(float[] data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4 * data.length).order(ByteOrder.nativeOrder());
        for (Float f : data) {
            buffer.putFloat(f);
        }
        return buffer;
    }

    public static ArrayList<Byte> GetByteListFromFloatList(ArrayList<Float> data)
    {
        ArrayList<Byte> result = new ArrayList<Byte>();
        for (Float f : data)
        {
            result.addAll(GetByteList(f));
        }
        return result;
    }

    public static ArrayList<Byte> GetByteListFromShortList(ArrayList<Short> data)
    {
        ArrayList<Byte> result = new ArrayList<Byte>();
        for (Short s : data)
        {
            result.addAll(GetByteList(s));
        }
        return result;
    }

    public static IntBuffer GetIntBufferFromByteArray(byte[] byteArray)
    {
        IntBuffer intBuf = ByteBuffer.wrap(byteArray)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return intBuf;
    }

    public static Integer ToInt32(Byte[] byteArray, int offset)
    {
        byte[] selectedBytes = new byte[4];
        System.arraycopy(byteArray, offset, selectedBytes, 0, 4);
        ByteBuffer buffer = ByteBuffer.wrap(selectedBytes);
        return buffer.getInt();
    }

    public static Integer ToInt32(byte[] byteArray, int offset)
    {
        byte[] selectedBytes = new byte[4];
        System.arraycopy(byteArray, offset, selectedBytes, 0, 4);
        ByteBuffer buffer = ByteBuffer.wrap(selectedBytes);
        return buffer.getInt();
    }

    public static Float ToSingle(Byte[] byteArray, int offset)
    {
        byte[] selectedBytes = new byte[4];
        System.arraycopy(byteArray, offset, selectedBytes, 0, 4);
        ByteBuffer buffer = ByteBuffer.wrap(selectedBytes);
        return buffer.getFloat();
    }

    public static Float ToSingle(byte[] byteArray, int offset)
    {
        byte[] selectedBytes = new byte[4];
        System.arraycopy(byteArray, offset, selectedBytes, 0, 4);
        ByteBuffer buffer = ByteBuffer.wrap(selectedBytes);
        return buffer.getFloat();
    }

    public static Short ToInt16(Byte[] byteArray, int offset)
    {
        byte[] selectedBytes = new byte[2];
        System.arraycopy(byteArray, offset, selectedBytes, 0, 2);
        ByteBuffer buffer = ByteBuffer.wrap(selectedBytes);
        return buffer.getShort();
    }

    public static Short ToInt16(byte[] byteArray, int offset)
    {
        byte[] selectedBytes = new byte[2];
        System.arraycopy(byteArray, offset, selectedBytes, 0, 2);
        ByteBuffer buffer = ByteBuffer.wrap(selectedBytes);
        return buffer.getShort();
    }
}
