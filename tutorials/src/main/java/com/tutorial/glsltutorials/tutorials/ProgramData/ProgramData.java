package com.tutorial.glsltutorials.tutorials.ProgramData;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.Material.MaterialBlock;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.io.UnsupportedEncodingException;

/**
 * Created by jamie on 10/12/14.
 */
public class ProgramData {
    private int theProgram;
    private int vertex_shader;
    private int fragment_shader;
    private int positionAttribute;
    private int colorAttribute;
    private int modelToCameraMatrixUnif;
    private int modelToWorldMatrixUnif;
    private int worldToCameraMatrixUnif;
    private int cameraToClipMatrixUnif;
    private int baseColorUnif;
    private int texCoordAttribute;
    private int colorTextureUnif;
    private int scaleUniform;

    private int sampler = 0;
    private int texUnit = 0;
    private int current_texture;

    public int normalModelToCameraMatrixUnif;
    public int dirToLightUnif;
    public int lightPosUnif;
    public int modelSpaceLightPosUnif;
    public int lightIntensityUnif;
    public int ambientIntensityUnif;
    public int normalAttribute;
    String vertexShader;
    String fragmentShader;


    int BYTES_PER_SHORT = 2;
    int BYTES_PER_FLOAT = 4;
    int COORDS_PER_VERTEX = 3;
    int POSITION_DATA_SIZE_IN_ELEMENTS = 3;
    int COLOR_DATA_SIZE_IN_ELEMENTS = 4;
    int NORMAL_DATA_SIZE_IN_ELEMENTS = 3;
    int NORMAL_START = 3 * 4; // POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;
    int TEXTURE_DATA_SIZE_IN_ELEMENTS = 2;
    int TEXTURE_START = 3 * 4 + 3 * 4;
    protected int vertexStride = 3 * 4; // bytes per vertex default to only 3 position floats

    LightBlock lightBlock;
    MaterialBlock materialBlock;


    public ProgramData(String vertexShaderIn, String fragmentShaderIn) {
        vertexShader = vertexShaderIn;
        fragmentShader = fragmentShaderIn;
        vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        theProgram = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");
        if (positionAttribute != -1) {
            if (positionAttribute != 0) {
                //FIXME MessageBox.Show("These meshes only work with position at location 0 " + vertexShader);
            }
        }
        if (colorAttribute != -1) {
            if (colorAttribute != 1) {
                // FIXME MessageBox.Show("These meshes only work with color at location 1" + vertexShader);
            }
        }

        modelToWorldMatrixUnif = GLES20.glGetUniformLocation(theProgram, "modelToWorldMatrix");
        worldToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "worldToCameraMatrix");
        cameraToClipMatrixUnif = GLES20.glGetUniformLocation(theProgram, "cameraToClipMatrix");
        if (cameraToClipMatrixUnif == -1) {
            cameraToClipMatrixUnif = GLES20.glGetUniformLocation(theProgram, "Projection.cameraToClipMatrix");
        }
        baseColorUnif = GLES20.glGetUniformLocation(theProgram, "baseColor");

        modelToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "modelToCameraMatrix");

        normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "normalModelToCameraMatrix");
        dirToLightUnif = GLES20.glGetUniformLocation(theProgram, "dirToLight");
        lightPosUnif = GLES20.glGetUniformLocation(theProgram, "lightPos");
        modelSpaceLightPosUnif = GLES20.glGetUniformLocation(theProgram, "modelSpaceLightPos");
        lightIntensityUnif = GLES20.glGetUniformLocation(theProgram, "lightIntensity");
        ambientIntensityUnif = GLES20.glGetUniformLocation(theProgram, "ambientIntensity");
        normalAttribute = GLES20.glGetAttribLocation(theProgram, "normal");
        if (normalAttribute != -1) {
            vertexStride = 3 * 4 * 2;
        }
        texCoordAttribute = GLES20.glGetAttribLocation(theProgram, "texCoord");
        if (texCoordAttribute != -1) {
            //createSampler();
            vertexStride = 3 * 4 * 2 + 2 * 4;
        }
        colorTextureUnif = GLES20.glGetUniformLocation(theProgram, "diffuseColorTex");
        scaleUniform = GLES20.glGetUniformLocation(theProgram, "scaleFactor");
    }

    void createSampler() {
        // not used?? applied with setup
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    public boolean compareShaders(String vertexShaderIn, String fragmentShaderIn) {
        return ((vertexShaderIn == vertexShader) & (fragmentShader == fragmentShaderIn));
    }

    public void draw(int[] vertexBufferObject, int[] indexBufferObject, Matrix4f mm,
                     int indexDataLength, float[] color) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, Shape.cameraToClip.toArray(), 0);
        GLES20.glUniformMatrix4fv(worldToCameraMatrixUnif, 1, false, Shape.worldToCamera.toArray(), 0);
        GLES20.glUniformMatrix4fv(modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
        if (modelToWorldMatrixUnif != -1) {
            GLES20.glUniformMatrix4fv(modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
        }
        if (modelToCameraMatrixUnif != -1) {
            GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
        }
        if (baseColorUnif != -1) GLES20.glUniform4fv(baseColorUnif, 1, color, 0);


        GLES20.glEnableVertexAttribArray(positionAttribute);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS,
                GLES20.GL_FLOAT, false, vertexStride, 0);

        if (normalAttribute != -1) {
            GLES20.glEnableVertexAttribArray(normalAttribute);
            GLES20.glVertexAttribPointer(normalAttribute, NORMAL_DATA_SIZE_IN_ELEMENTS,
                    GLES20.GL_FLOAT, false, vertexStride, NORMAL_START);
        }

        if (texCoordAttribute != -1) {
            // throws error gl2mERROR: result=0x0500 GLES20.glEnable(GLES20.GL_TEXTURE_2D);
            GLES20.glEnableVertexAttribArray(texCoordAttribute);
            GLES20.glVertexAttribPointer(texCoordAttribute, TEXTURE_DATA_SIZE_IN_ELEMENTS,
                    GLES20.GL_FLOAT, false, vertexStride, TEXTURE_START);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, current_texture);
        }

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexDataLength, GLES20.GL_UNSIGNED_SHORT, 0);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionAttribute);
        if (normalAttribute != -1) GLES20.glDisableVertexAttribArray(normalAttribute);
        if (texCoordAttribute != -1) {
            GLES20.glDisableVertexAttribArray(texCoordAttribute);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(0);
    }

    public void drawWireFrame(int[] vertexBufferObject, int[] indexBufferObject, Matrix4f mm,
                              int indexDataLength, float[] color) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, Shape.cameraToClip.toArray(), 0);
        GLES20.glUniformMatrix4fv(worldToCameraMatrixUnif, 1, false, Shape.worldToCamera.toArray(), 0);
        GLES20.glUniformMatrix4fv(modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
        if (modelToWorldMatrixUnif != -1) {
            GLES20.glUniformMatrix4fv(modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
        }
        if (modelToCameraMatrixUnif != -1) {
            GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
        }
        if (baseColorUnif != -1) GLES20.glUniform4fv(baseColorUnif, 1, color, 0);


        GLES20.glEnableVertexAttribArray(positionAttribute);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS,
                GLES20.GL_FLOAT, false, vertexStride, 0);

        if (normalAttribute != -1) {
            GLES20.glEnableVertexAttribArray(normalAttribute);
            GLES20.glVertexAttribPointer(normalAttribute, NORMAL_DATA_SIZE_IN_ELEMENTS,
                    GLES20.GL_FLOAT, false, vertexStride, NORMAL_START);
        }

        if (texCoordAttribute != -1) {
            // throws error gl2mERROR: result=0x0500 GLES20.glEnable(GLES20.GL_TEXTURE_2D);
            GLES20.glEnableVertexAttribArray(texCoordAttribute);
            GLES20.glVertexAttribPointer(texCoordAttribute, TEXTURE_DATA_SIZE_IN_ELEMENTS,
                    GLES20.GL_FLOAT, false, vertexStride, TEXTURE_START);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, current_texture);
        }

        // Draw the wireframes
        for (int i = 0; i < indexDataLength; i += 3) {
            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, GLES20.GL_UNSIGNED_SHORT, i * BYTES_PER_SHORT);
        }

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionAttribute);
        if (normalAttribute != -1) GLES20.glDisableVertexAttribArray(normalAttribute);
        if (texCoordAttribute != -1) {
            GLES20.glDisableVertexAttribArray(texCoordAttribute);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(0);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("theProgram = " + String.valueOf(theProgram));
        result.append("positionAttribute = " + String.valueOf(positionAttribute));
        result.append("colorAttribute = " + String.valueOf(colorAttribute));
        result.append("modelToCameraMatrixUnif = " + String.valueOf(modelToCameraMatrixUnif));
        result.append("modelToWorldMatrixUnif = " + String.valueOf(modelToWorldMatrixUnif));
        result.append("worldToCameraMatrixUnif = " + String.valueOf(worldToCameraMatrixUnif));
        result.append("cameraToClipMatrixUnif = " + String.valueOf(cameraToClipMatrixUnif));
        result.append("baseColorUnif = " + String.valueOf(baseColorUnif));
        result.append("normalModelToCameraMatrixUnif = " + String.valueOf(normalModelToCameraMatrixUnif));
        result.append("dirToLightUnif = " + String.valueOf(dirToLightUnif));
        result.append("lightIntensityUnif = " + String.valueOf(lightIntensityUnif));
        result.append("ambientIntensityUnif = " + String.valueOf(ambientIntensityUnif));
        result.append("normalAttribute = " + String.valueOf(normalAttribute));
        return result.toString();
    }

    public void setUniformColor(Vector4f color) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform4fv(baseColorUnif, 1, color.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setUniformTexture(int colorTexUnit) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform1i(colorTextureUnif, colorTexUnit);
        GLES20.glUseProgram(0);
    }

    public void setUniformScale(float scale) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform1f(scaleUniform, scale);
        GLES20.glUseProgram(0);
    }

    public void loadTexture(int texture, boolean oneTwenty) {
        current_texture = Textures.loadTexture(Shader.context, texture, oneTwenty);
    }

    public void setTexture(int texture) {
        current_texture = texture;
    }

    public void setLightPosition(Vector3f lightPosition) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform3fv(lightPosUnif, 1, lightPosition.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setModelSpaceLightPosition(Vector3f modelSpaceLightPos) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform3fv(modelSpaceLightPosUnif, 1, modelSpaceLightPos.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setDirectionToLight(Vector3f dirToLight) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform3fv(dirToLightUnif, 1, dirToLight.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setLightIntensity(Vector4f lightIntensity) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform4fv(lightIntensityUnif, 1, lightIntensity.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setAmbientIntensity(Vector4f ambientIntensity) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform4fv(ambientIntensityUnif, 1, ambientIntensity.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setNormalModelToCameraMatrix(Matrix3f normalModelToCameraMatrix) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix3fv(normalModelToCameraMatrixUnif, 1, false, normalModelToCameraMatrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setModelToCameraMatrix(Matrix4f modelToCameraMatrix) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, modelToCameraMatrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setUpLightBlock(int numberOfLights) {
        lightBlock = new LightBlock(numberOfLights);
        lightBlock.setUniforms(theProgram);
    }

    public void setUpMaterialBlock() {
        materialBlock = new MaterialBlock();
        materialBlock.setUniforms(theProgram);
    }

    public void updateLightBlock(LightBlock lb) {
        lightBlock.update(lb);
    }

    public void updateMaterialBlock(MaterialBlock mb) {
        materialBlock.update(mb);
    }

    public void setModelToWorldMatrix(Matrix4f modelToWorldMatrix) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(modelToWorldMatrixUnif, 1, false, modelToWorldMatrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setCameraToClipMatrixUnif(Matrix4f cameraToClipMatrix) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, cameraToClipMatrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }


    public void setWorldToCameraMatrixUnif(Matrix4f worldToCameraMatrix) {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(worldToCameraMatrixUnif, 1, false, worldToCameraMatrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void use() {
        GLES20.glUseProgram(theProgram);
    }

    public int getVertexShader() {
        return vertex_shader;
    }

    public String getVertexShaderSource() {
        return getShaderSource(vertex_shader);
    }

    public String getFragmentShaderSource() {
        return getShaderSource(fragment_shader);
    }

    public String getVertexShaderInfoLog() {
        return getShaderInfoLog(vertex_shader);
    }

    public String getFragmentShaderInfoLog() {
        return getShaderInfoLog(fragment_shader);
    }

    private String getShaderInfoLog(int shader)
    {
        return GLES20.glGetShaderInfoLog(shader);
    }

    public String getProgramInfoLog()
    {
        GLES20.glValidateProgram(theProgram);
        return GLES20.glGetProgramInfoLog(theProgram);
    }

    private String getShaderSource(int shader)
    {
        String result = "";
        int bufsize = 4096;
        int[] length = new int[1];
        int lengthOffset = 0;
        byte[] source = new byte[bufsize];
        int sourceOffset = 0;
        GLES20.glGetShaderSource(shader, bufsize, length, lengthOffset, source, sourceOffset);
        byte[] sourceBytes = new byte[length[0]];
        System.arraycopy(source, 0, sourceBytes, 0, sourceBytes.length);
        try
        {
            result = new String(sourceBytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            result = "Error converting string";
        }
        return result;
    }

    public String getVertexAttributes()
    {
        StringBuilder result = new StringBuilder();
        int[] attribCount = new int[1];
        int[] length = new int[1];
        int sizeOffset = 0;
        int[] type = new int[1];
        int typeOffset = 0;
        GLES20.glGetProgramiv(theProgram, GLES20.GL_ACTIVE_ATTRIBUTES, attribCount, 0);
        result.append("\nattributes " + String.valueOf(attribCount[0]));
        for (int i = 0; i < attribCount[0]; i++) {
            result.append("\n" + GLES20.glGetActiveAttrib(theProgram, i, length, sizeOffset, type, typeOffset));
        }
        return result.toString();
    }

    private String getUniformFloats(String name, int length)
    {
        int location;
        location = GLES20.glGetUniformLocation(theProgram, name);
        StringBuilder result = new StringBuilder();
        float[] values = new float[length];
        GLES20.glGetUniformfv(theProgram, location, values, 0);
        for (int i = 0; i < values.length; i++)
        {
            result.append(String.valueOf(values[i]) + " ");
        }
        return result.toString();
    }

    public String getUniforms()
    {
        StringBuilder result = new StringBuilder();
        int[] uniformCount = new int[1];
        int[] length = new int[1];
        int sizeOffset = 0;
        int[] type = new int[1];
        int typeOffset = 0;
        GLES20.glGetProgramiv(theProgram, GLES20.GL_ACTIVE_UNIFORMS, uniformCount, 0);
        result.append("\nuniforms " + String.valueOf(uniformCount[0]));
        for (int i = 0; i < uniformCount[0]; i++) {
            String name = GLES20.glGetActiveUniform(theProgram, i, length, sizeOffset, type, typeOffset);
            result.append("\n" + name);
            String typeString = "Unknown " + String.valueOf(type[0]);
            switch (type[0]) {
                case GLES20.GL_FLOAT_VEC2 : typeString = "GL_FLOAT_VEC2 " + getUniformFloats(name, 2); break;
                case GLES20.GL_FLOAT_VEC3: typeString = "GL_FLOAT_VEC3" + getUniformFloats(name, 3); break;
                case GLES20.GL_FLOAT_VEC4: typeString = "GL_FLOAT_VEC4 " + getUniformFloats(name, 4); break;
                case GLES20.GL_INT_VEC2: typeString = "GL_INT_VEC2"; break;
                case GLES20.GL_INT_VEC3: typeString = "GL_INT_VEC3"; break;
                case GLES20.GL_INT_VEC4: typeString = "GL_INT_VEC4"; break;
                case GLES20.GL_BOOL: typeString = "GL_BOOL"; break;
                case GLES20.GL_BOOL_VEC2: typeString = "GL_BOOL_VEC2"; break;
                case GLES20. GL_BOOL_VEC3: typeString = "GL_BOOL_VEC3  "; break;
                case GLES20.GL_BOOL_VEC4 : typeString = "GL_BOOL_VEC4 "; break;
                case GLES20.GL_FLOAT_MAT2 : typeString = "GL_FLOAT_MAT2 " + getUniformFloats(name, 4); break;
                case GLES20.GL_FLOAT_MAT3 : typeString = "GL_FLOAT_MAT3 " + getUniformFloats(name, 9); break;
                case GLES20.GL_FLOAT_MAT4 : typeString = "GL_FLOAT_MAT4 " + getUniformFloats(name, 16); break;
                case GLES20.GL_SAMPLER_2D : typeString = "GL_SAMPLER_2D "; break;
                case GLES20.GL_SAMPLER_CUBE : typeString = "GL_SAMPLER_CUBE "; break;
            }
            result.append(" type = " + typeString);
        }
        return result.toString();
    }

    public int getFragmentShader()
    {
        return fragment_shader;
    }
}
