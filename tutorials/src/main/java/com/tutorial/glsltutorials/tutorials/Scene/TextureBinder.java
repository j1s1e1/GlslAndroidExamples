package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;

/**
 * Created by jamie on 1/3/15.
 */
public class TextureBinder extends UniformBinderBase {
    public TextureBinder()
    {
        m_texUnit = 0;
        m_texType = GLES20.GL_TEXTURE_2D;
        m_texObj = 0;
        m_samplerObj = 0;
    }

    void SetTexture(int texUnit, int texType, int texObj, int samplerObj)
    {
        m_texUnit = texUnit;
        m_texType = texType;
        m_texObj = texObj;
        m_samplerObj = samplerObj;
    }

    public void bindState(int prog)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + m_texUnit);
        GLES20.glBindTexture(m_texType, m_texObj);
        // FIXME GLES20.glBindSampler(m_texUnit, m_samplerObj);
        applySampler(m_samplerObj);
    }

    public void unbindState(int prog)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + m_texUnit);
        GLES20.glBindTexture(m_texType, 0);
        // FIXME GLES20.glBindSampler(m_texUnit, 0);
    }

    private void applySampler(int sampler) {
        //Always
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        switch (sampler) {
            case 0:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                break;
            case 1:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                break;
            case 2:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
                break;
            case 3:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
                break;
            case 4:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
                //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.TextureMaxAnisotropyExt, 4f);
                break;
            case 5:
                //Max anisotropic
                float maxAniso = 0.0f;
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
                //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.TextureMaxAnisotropyExt, maxAniso);
                break;
        }
    }

    public void UnbindBinder(int prog)
    {

    }

    private int m_texUnit;
    private int m_texType;
    private int m_texObj;
    private int m_samplerObj;
}
