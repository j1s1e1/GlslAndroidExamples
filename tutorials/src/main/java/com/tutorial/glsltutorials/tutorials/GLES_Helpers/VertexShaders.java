package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

/**
 * Created by Jamie on 6/7/14.
 */
public class VertexShaders {

    public static final String PosColorLocalTransform_vert =
    "attribute vec4 color;" +
    "attribute vec4 position;" +

    "uniform mat4 cameraToClipMatrix;" +
    "uniform mat4 modelToCameraMatrix;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "vec4 cameraPos = modelToCameraMatrix * position;" +
        "gl_Position = cameraToClipMatrix * cameraPos;" +
        "theColor = color;" +
    "}";

    public static final String PosOnlyWorldTransform_vert =
    "attribute vec4 position;" +

    "uniform mat4 cameraToClipMatrix;" +
    "uniform mat4 worldToCameraMatrix;" +
    "uniform mat4 modelToWorldMatrix;" +

    "void main()" +
    "{" +
        "vec4 temp = modelToWorldMatrix *  position;" +
        "temp = worldToCameraMatrix * temp;" +
        "gl_Position = cameraToClipMatrix * temp;" +
    " }";

    public static final String PosColorWorldTransform_vert =
    "attribute vec4 color;" +
    "attribute vec4 position;" +

    "uniform	mat4 cameraToClipMatrix;" +
    "uniform	mat4 worldToCameraMatrix;" +
    "uniform mat4 modelToWorldMatrix;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "vec4 temp = modelToWorldMatrix * position;" +
        "temp = worldToCameraMatrix * temp;" +
        "gl_Position = cameraToClipMatrix * temp;" +
        "theColor = color;" +
    "}";

    public static final String DirVertexLighting_PN_vert =
    "attribute vec3 normal;" +
    "attribute vec4 color;" + // dummy to hold positions
    "attribute vec3 position;" +

    "uniform vec3 dirToLight;" +
    "uniform vec4 lightIntensity;" +

    "uniform mat4 modelToCameraMatrix;" +
    "uniform mat3 normalModelToCameraMatrix;" +

    "struct UniformBlock" +
    "{" +
        "mat4 cameraToClipMatrix;" +
    "};" +

    "uniform UniformBlock Projection;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_Position = Projection.cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +
        "theColor = 0.001 * color;" + //"theColor = lightIntensity * cosAngIncidence;" +
        "vec3 normCamSpace = normalize(normalModelToCameraMatrix * normal);" +

        "float cosAngIncidence = dot(normCamSpace, dirToLight);" +
        "cosAngIncidence = clamp(cosAngIncidence, 0.0, 1.0);" +


        "theColor = vec4(1.0, 0.0, 0.0, 1.0) + 0.001 * color;" + //"theColor = lightIntensity * cosAngIncidence;" +
    "}";

    public static final String DirVertexLighting_PCN_vert =
    "attribute vec3 normal;" +
    "attribute vec4 diffuseColor;" +
    "attribute vec3 position;" +

    "uniform vec3 dirToLight;" +
    "uniform vec4 lightIntensity;" +

    "uniform mat4 modelToCameraMatrix;" +
    "uniform mat3 normalModelToCameraMatrix;" +

    "struct UniformBlock" +
    "{" +
        "mat4 cameraToClipMatrix;" +
    "};" +

    "uniform UniformBlock Projection;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_Position = Projection.cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +

        "vec3 normCamSpace = normalize(normalModelToCameraMatrix * normal);" +

        "float cosAngIncidence = dot(normCamSpace, dirToLight);" +
        "cosAngIncidence = clamp(cosAngIncidence, 0.0, 1.0);" +

        "theColor = lightIntensity * diffuseColor * cosAngIncidence;" +
    "}";

    public static final String DirAmbVertexLighting_PN_vert =
    "attribute vec3 normal;" +
    "attribute vec3 position;" +

    "uniform vec3 dirToLight;" +
    "uniform vec4 lightIntensity;" +
    "uniform vec4 ambientIntensity;" +

    "uniform mat4 modelToCameraMatrix;" +
    "uniform mat3 normalModelToCameraMatrix;" +

    "struct UniformBlock" +
    "{" +
        "mat4 cameraToClipMatrix;" +
    "};" +

    "uniform UniformBlock Projection;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_Position = Projection.cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +

        "vec3 normCamSpace = normalize(normalModelToCameraMatrix * normal);" +

        "float cosAngIncidence = dot(normCamSpace, dirToLight);" +
        "cosAngIncidence = clamp(cosAngIncidence, 0.0, 1.0);" +

        "theColor = (lightIntensity * cosAngIncidence) + ambientIntensity;" +
    "}";

    public static final String DirAmbVertexLighting_PCN_vert =
    "attribute vec3 normal;" +
    "attribute vec4 diffuseColor;" +
    "attribute vec3 position;" +

    "uniform vec3 dirToLight;" +
    "uniform vec4 lightIntensity;" +

    "uniform mat4 modelToCameraMatrix;" +
    "uniform mat3 normalModelToCameraMatrix;" +

    "uniform mat4 cameraToClipMatrix;" +

    "uniform UniformBlock Projection;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_Position = cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +
        "gl_Position = vec4(position, 1.0));" +

        "vec3 normCamSpace = normalize(normalModelToCameraMatrix * normal);" +

        "float cosAngIncidence = dot(normCamSpace, dirToLight);" +
        "cosAngIncidence = clamp(cosAngIncidence, 0.0, 1.0);" +

        "theColor = lightIntensity * diffuseColor * cosAngIncidence;" +
        "theColor = vec4(0.0, 1.0, 0.0, 1.0);" +
    "}";

    public static final String lms_vertexShaderCode =
    "attribute vec4 position;" +
    "attribute vec3 normal;" +		// Per-vertex normal information we will pass in.

    "uniform mat4 cameraToClipMatrix;" +
    "uniform mat4 worldToCameraMatrix;" +
    "uniform mat4 modelToWorldMatrix;" +

    "varying vec3 v_Normal;" +		// This will be passed into the fragment shader.
    "varying vec3 v_Position;" +	// This will be passed into the fragment shader.
    "void main()" +
    "{" +
        "vec4 temp = modelToWorldMatrix * position;" +
        "temp = worldToCameraMatrix * temp;" +
        "temp = cameraToClipMatrix * temp;" +
        "v_Position = vec3(temp);" +
        "v_Normal = normal;" +
        "gl_Position = temp;" +
    "}";

    public static final String FragmentLighting_PN =

    "attribute vec3 normal;" +
    "attribute vec4 color;" + // dummy to hold positions
    "attribute vec3 position;" +

    "uniform mat4 modelToCameraMatrix;" +

    "uniform mat4 cameraToClipMatrix;" +

    "varying vec4 diffuseColor;" +
    "varying vec3 vertexNormal;" +
    "varying vec3 modelSpacePosition;" +

    "void main()" +
    "{" +
        "gl_Position = cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +
        //"gl_Position =  vec4(position, 1.0);" + //TEST
        "vertexNormal = normal;" +
        "modelSpacePosition = position;" +
        "diffuseColor = vec4(1.0);" +
    "}";

    public static final String FragmentLighting_PCN =
    "attribute vec3 normal;" +
    "attribute vec4 inDiffuseColor;" +
    "attribute vec3 position;" +

    "uniform mat4 modelToCameraMatrix;" +

    "uniform mat4 cameraToClipMatrix;" +

    "varying vec4 diffuseColor;" +
    "varying vec3 vertexNormal;" +
    "varying vec3 modelSpacePosition;" +

    "void main()" +
    "{" +
        "gl_Position =  cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +

        "modelSpacePosition = position;" +
        "vertexNormal = normal;" +
        "diffuseColor = inDiffuseColor;" +
    "}";

    public static final String PosTransform =

    "attribute vec3 position;" +

    "uniform mat4 modelToCameraMatrix;" +

    "uniform mat4 cameraToClipMatrix;" +

    "void main()" +
    "{" +
        "gl_Position = cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +
    "}";

    public static final String DirVertexLighting_PCN =

    "attribute vec3 position;" +
    "attribute vec4 color;" +
    "attribute vec3 normal;" +

    "uniform vec3 dirToLight;" +
    "uniform vec4 lightIntensity;" +

    "uniform mat4 modelToCameraMatrix;" +
    "uniform mat3 normalModelToCameraMatrix;" +

    "uniform mat4 cameraToClipMatrix;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
    "gl_Position = cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +

    "vec3 normCamSpace = normalize(normalModelToCameraMatrix * normal);" +

    "float cosAngIncidence = dot(normCamSpace, dirToLight);" +
    "cosAngIncidence = clamp(cosAngIncidence, 0.0, 1.0);" +

    //"theColor = lightIntensity * color;" + // works
    //"theColor = vec4(normal, 1.0f);" + // works
    "theColor = vec4(normCamSpace, 1.0f);" + // works\
    "theColor = cosAngIncidence * vec4(normCamSpace, 1.0f);" +  // works for some faces

    "theColor = lightIntensity * cosAngIncidence * vec4(normCamSpace, 1.0f);" +  // works for some faces

    //"theColor = lightIntensity  * cosAngIncidence * color;" +
    "}";

    public static final String SimpleTexture =

    "attribute vec4 position;" +
    "attribute vec2 texCoord;" +

    "varying vec2 colorCoord;" +

    "void main()" +
    "{" +
        "gl_Position = position;" +
        "colorCoord = texCoord;" +
     "}";

    public static final String MatrixTexture =
    "attribute vec4 position;" +
    "attribute vec3 normal;" +
    "attribute vec2 texCoord;" +

    "uniform mat4 cameraToClipMatrix;" +
    "uniform mat4 worldToCameraMatrix;" +
    "uniform mat4 modelToWorldMatrix;" +

    "varying vec3 v_Normal;" +
    "varying vec3 v_Position;" +
    "varying vec2 colorCoord;" +

    "void main()" +
    "{" +
        "vec4 temp = modelToWorldMatrix * position;" +
        "temp = worldToCameraMatrix * temp;" +
        "temp = cameraToClipMatrix * temp;" +
        "v_Position = vec3(temp);" +
        "v_Normal = normal;" +
        "gl_Position = temp;" +
        "colorCoord = texCoord;" +
    "}";

    public static String unlit =
    "attribute vec3 position;" +

    "uniform mat4 cameraToClipMatrix;" +

    "uniform mat4 modelToCameraMatrix;" +

    "void main()" +
    "{" +
        "gl_Position = cameraToClipMatrix * (modelToCameraMatrix * vec4(position, 1.0));" +
        "gl_Position = vec4(position, 1.0);" +
    "}";

    public static String BasicTexture_PN =

    "attribute vec3 position;" +
    "attribute vec4 color;" +		//added for spacing
    "attribute vec3 normal;" +

    "varying vec3 vertexNormal;" +
    "varying vec3 cameraSpacePosition;" +

    "uniform mat4 cameraToClipMatrix;" +

    "uniform mat4 modelToCameraMatrix;" +
    "uniform mat3 normalModelToCameraMatrix;" +

    "void main()" +
    "{" +
        "vec4 tempCamPosition = (modelToCameraMatrix * vec4(position, 1.0 + color * 0.0001));" +
        "gl_Position = cameraToClipMatrix * tempCamPosition;" +

        "vertexNormal = normalize(normalModelToCameraMatrix * normal);" +
        "cameraSpacePosition = vec3(tempCamPosition);" +
        "gl_Position = vec4(position, 1.0);" +
    "}";

}
