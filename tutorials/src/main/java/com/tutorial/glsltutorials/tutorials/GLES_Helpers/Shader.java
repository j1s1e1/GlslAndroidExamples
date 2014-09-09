package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLES20;
import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by Jamie on 1/5/14.
 */
public class Shader {

    // Per Vertex Shader
    String strVShaderPV =
            "attribute vec4 a_position;" +
            "attribute vec3 a_normals;" +
            "attribute vec2 a_texCoords;" +
            "uniform mat4 u_ModelViewMatrix;" +
            "uniform mat3 u_MVNormalsMatrix;" +
            "uniform vec3 u_LightDir;" +
            "uniform vec3 u_LightColor;" +
            "varying vec3 v_colorWeight;" +
            "varying vec2 v_texCoords;" +
            "void main()" +
            "{" +
                "gl_Position = u_ModelViewMatrix * a_position;" +
                "v_texCoords = a_texCoords;" +
                "vec3 normal = normalize(u_MVNormalsMatrix * a_normals);" +
                "vec3 lightNorm = normalize(u_LightDir);" +
                "float lightWeight = max(dot(normal,lightNorm),0.0);" +
                "v_colorWeight = vec3(0.2,0.2,0.2) + (lightWeight * u_LightColor);" +
            "}";

    // Per pixel shader
    String strVShaderPP =
            "attribute vec4 a_position;" +
            "attribute vec3 a_normals;" +
            "attribute vec2 a_texCoords;" +
            "uniform mat4 u_ModelViewMatrix;" +
            "uniform mat3 u_MVNormalsMatrix;" +
            "varying vec3 u_Normals;" +
            "varying vec2 v_texCoords;" +
            "void main()" +
            "{" +
                "v_texCoords = a_texCoords;" +
                "u_Normals = u_MVNormalsMatrix * a_normals;" +
                "gl_Position = u_ModelViewMatrix * a_position;" +
            "}";

    // Per vertex
    String strFShaderPV =
            "precision mediump float;" +
            "varying vec3 v_colorWeight;" +
            "varying vec2 v_texCoords;" +
            "uniform sampler2D u_texId;" +
            "void main()" +
            "{" +
                "vec4 texColor = texture2D(u_texId, v_texCoords);" +
                "gl_FragColor =  vec4(texColor.rgb * v_colorWeight, texColor.a);" +
            "}";

    // Per pixel
    String strFShaderPP =
            "precision mediump float;" +
            "uniform vec3 u_LightDir;" +
            "uniform vec3 u_LightColor;" +
            "uniform sampler2D u_texId;" +
            "varying vec2 v_texCoords;" +
            "varying vec3 u_Normals;" +
            "void main()" +
            "{" +
                "vec3 LNorm = normalize(u_LightDir);" +
                "vec3 normal = normalize(u_Normals);" +
                "float intensity = max(dot(LNorm, normal),0.0);" +
                "vec4 texColor = texture2D(u_texId, v_texCoords);" +
                "vec3 calcColor = vec3(0.2,0.2,0.2) + u_LightColor * intensity;" +
                "gl_FragColor = vec4(texColor.rgb * calcColor, texColor.a);" +
            "}";

    String vertexShader3 =
    "attribute vec4 Position;" +
    "attribute vec2 TexCoordIn;" +
    "attribute vec3 Normal;" +

    "uniform mat4 modelViewProjectionMatrix;" +
    "uniform mat4 modelViewMatrix;" +
    "uniform mat3 normalMatrix;" +

    "uniform vec3 lightPosition;" +

    "varying vec2 TexCoordOut;" +
    "varying vec3 n, PointToLight;" +

    "void main(void) {" +
        "gl_Position =  modelViewProjectionMatrix * Position;" +
        "n = normalMatrix * Normal;" +

        "PointToLight = ((modelViewMatrix * vec4(lightPosition,1.0)) - (modelViewMatrix * Position)).xyz;" +

        "PointToLight = ((viewMatrix * vec4(lightPosition,1.0)) - (viewMatrix * modelMatrix * Position)).xyz;" +

        "// Pass texCoord" +
        "TexCoordOut = TexCoordIn;" +
    "}";

    String fragmentShader3 =
    "varying lowp vec2 TexCoordOut;" +
    "varying highp vec3 n, PointToLight;" +

    "uniform sampler2D Texture;" +

    "void main(void) {" +
        "gl_FragColor = texture2D(Texture, TexCoordOut);" +

        "highp vec3 nn = normalize(n);" +
        "highp vec3 L = normalize(PointToLight);" +

        "lowp float NdotL = clamp(dot(n, L), -0.8, 1.0);" +
        "gl_FragColor *= (NdotL+1.)/2.;" +
    "}";

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    public static int loadShader30(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private static final String TAG = "ShaderHelper";

    public static int compileShader(final int shaderType, final String shaderSource)
    {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    public static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
    {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null)
            {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }

    public static Context context;

    private static void MessageBox(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(message)
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //do some thing here which you need
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle)
    {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            MessageBox("Error creating program");
        }
        else
        {
            //MessageBox("Success creating program");
        }

        return programHandle;
    }

    public static int createAndLinkProgram30(final int vertexShaderHandle, final int fragmentShaderHandle)
    {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            MessageBox("Error creating program");
        }
        else
        {
            //MessageBox("Success creating program");
        }

        return programHandle;
    }
}
