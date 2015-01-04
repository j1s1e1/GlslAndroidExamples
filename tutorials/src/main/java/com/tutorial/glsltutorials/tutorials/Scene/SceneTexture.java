package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;
import android.util.Log;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.R;

/**
 * Created by jamie on 1/3/15.
 */
public class SceneTexture {
    public SceneTexture(String fileName, int creationFlags) {
        int resourceId = -1;
        switch (fileName)
        {
            case "seamless_rock1_small.png": resourceId = R.drawable.seamless_rock1_small; break;
            case "concrete649_small.png": resourceId = R.drawable.concrete649_small; break;
            case "dsc_1621_small.png": resourceId = R.drawable.dsc_1621_small; break;
            case "rough645_small.png": resourceId = R.drawable.rough645_small; break;
            case "wood4_rotate.png": resourceId = R.drawable.wood4_rotate; break;
        }
        if (resourceId == -1)
        {
            Log.e("Missing texture resource", fileName);
        }
        else {
            setup(resourceId, creationFlags);
        }
    }

    public SceneTexture(int resourceId, int creationFlags)
    {
       setup(resourceId, creationFlags);
    }

    public void setup(int resourceId, int creationFlags)
    {
        m_texObj = Textures.createMipMapTexture(Shader.context, resourceId);  //FIXME add options
        m_texType = GLES20.GL_TEXTURE_2D;
    }

    public void finalize()
    {
        // FIXME GLES20.glDeleteTextures(1, m_texObj);
    }

    public int GetTexture()
    {
        return m_texObj;
    }
    public int GetTextureType()
    {
        return m_texType;
    }

    private int m_texObj;
    private int m_texType;
}
