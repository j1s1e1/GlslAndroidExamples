package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

/**
 * Created by Jamie on 6/7/14.
 */
public class FragmentShaders {
    public static final String ColorPassthrough_frag =
    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    public static  final String ColorUniform_frag =
    "uniform vec4 baseColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = baseColor;" +
    "}";

    public static String ObjectPositionColor =
    "varying vec4 objectPosition;" +

    "void main()" +
    "{" +
        "gl_FragColor = vec4(objectPosition.xyz, 1.0);" +
    "}";

    public static final String ColorMultUniform_frag =
    "uniform vec4 baseColor;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor * baseColor;" +
    "}";

    public static final String lms_fragmentShaderCode =
    "uniform vec4 baseColor;" +
    "uniform vec3 lightPos;" +       	// The position of the light in eye space.
    "varying vec3 v_Position;" +		// This will be passed into the fragment shader.
    "varying vec3 v_Normal;" +         	// Interpolated normal for this fragment.
    "void main()" +
    "{" +
        // Will be used for attenuation.
        "float distance = length(lightPos - v_Position);" +

        // Get a lighting direction vector from the light to the vertex.
        "vec3 lightVector = normalize(lightPos - v_Position);" +

        // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
        // pointing in the same direction then it will get max illumination.
        "float diffuse = max(dot(v_Normal, lightVector), 0.0);" +

        // Add attenuation." +
        "diffuse = diffuse * (1.0 / distance);" +

        // Add ambient lighting"
        "diffuse = diffuse + 0.2;" +

        // Multiply the color by the diffuse illumination level and texture value to get final output color."
        "gl_FragColor = (diffuse * baseColor);" +
    "}";

    public static final String solid_green_with_normals_frag =
    "uniform vec3 u_LightPos;" +       	// The position of the light in eye space.
    "varying vec3 v_Position;" +		// This will be passed into the fragment shader.
    "varying vec3 v_Normal;" +         	// Interpolated normal for this fragment.
    "void main()" +
    "{" +
        // Will be used for attenuation.
        "float distance = length(u_LightPos - v_Position);" +

        // Get a lighting direction vector from the light to the vertex.
        "vec3 lightVector = normalize(u_LightPos - v_Position);" +

        // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
        // pointing in the same direction then it will get max illumination.
        "float diffuse = max(dot(v_Normal, lightVector), 0.0);" +

        // Add attenuation." +
        "diffuse = diffuse * (1.0 / distance);" +

        // Add ambient lighting"
        "diffuse = diffuse + 0.2;" +

        // Multiply the color by the diffuse illumination level to get final output color."
        "gl_FragColor = (diffuse * vec4(0.0, 1.0, 0.0, 1.0));" +
    "}";

    public static String FragmentLighting =

    "uniform vec3 modelSpaceLightPos;" +

    "uniform vec4 lightIntensity;" +
    "uniform vec4 ambientIntensity;" +

    "varying vec4 diffuseColor;" +
    "varying vec3 vertexNormal;" +
    "varying vec3 modelSpacePosition;" +

    "void main()" +
    "{" +
        "vec3 lightDir = normalize(modelSpaceLightPos - modelSpacePosition);" +

        "float cosAngIncidence = dot(normalize(vertexNormal), lightDir);" +
        "cosAngIncidence = clamp(cosAngIncidence, 0.0, 1.0);" +

        "gl_FragColor = (diffuseColor * lightIntensity * cosAngIncidence) + (diffuseColor * ambientIntensity);" +
    "}";

    public static final String SimpleTexture =

    "varying vec2 colorCoord;" +

    "uniform sampler2D diffuseColorTex;" +

    "void main()" +
    "{" +
        "gl_FragColor = texture2D(diffuseColorTex, colorCoord);" +
    "}";

    public static final String MatrixTexture =
    "uniform vec3 lightPos;" +
    "uniform sampler2D diffuseColorTex;" +

    "varying vec3 v_Position;" +
    "varying vec3 v_Normal;" +
    "varying vec2 colorCoord;" +

    "void main()" +
    "{" +
        "float distance = length(lightPos - v_Position);" +
        "vec3 lightVector = normalize(lightPos - v_Position);" +
        "float diffuse = max(dot(v_Normal, lightVector), 0.0);" +

        "diffuse = diffuse * (1.0 / distance);" +

        "vec4 textureColor = texture2D(diffuseColorTex, colorCoord);" +

        "if (textureColor.w == 0.0) discard;" +

        "gl_FragColor = vec4(diffuse * textureColor.xyz, textureColor.w);" +
    "}";

    public static String unlit =

    "uniform vec4 objectColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = objectColor;" +
    "}";

    public static String ShaderGaussian =
    "varying vec3 vertexNormal;" +
    "varying vec3 cameraSpacePosition;" +

    "struct Material" +
    "{" +
        "vec4 diffuseColor;" +
        "vec4 specularColor;" +
        "float specularShininess;" +
    "};" +

    "uniform Material Mtl;" +

    "struct PerLight" +
    "{" +
        "vec4 cameraSpaceLightPos;" +
        "vec4 lightIntensity;" +
    "};" +

    "const int numberOfLights = 2;" +

    "struct Light" +
    "{" +
        "vec4 ambientIntensity;" +
        "float lightAttenuation;" +
        "PerLight lights[numberOfLights];" +
    "};" +

    "uniform Light Lgt;" +

    "vec3 lightDirection;" +

    "float CalcAttenuation(vec3 cameraSpacePosition, vec3 cameraSpaceLightPos)" +
    "{" +
        "vec3 lightDifference =  cameraSpaceLightPos - cameraSpacePosition;" +
        "float lightDistanceSqr = dot(lightDifference, lightDifference);" +
        "lightDirection = lightDifference * inversesqrt(lightDistanceSqr);" +

        "return (1.0 / ( 1.0 + Lgt.lightAttenuation * lightDistanceSqr));" +
    "}" +

    "vec4 ComputeLighting(PerLight lightData, vec3 cameraSpacePosition, vec3 cameraSpaceNormal)" +
    "{" +
        "vec4 lightIntensity;" +
        "if(lightData.cameraSpaceLightPos.w == 0.0)" +
         "{" +
            "lightDirection = vec3(lightData.cameraSpaceLightPos);" +
            "lightIntensity = lightData.lightIntensity;" +
        "}" +
        "else" +
        "{" +
            "float atten = CalcAttenuation(cameraSpacePosition, lightData.cameraSpaceLightPos.xyz);" +
            "lightIntensity = atten * lightData.lightIntensity;" +
        "}" +

        "vec3 surfaceNormal = normalize(cameraSpaceNormal);" +
        "float cosAngIncidence = dot(surfaceNormal, lightDirection);" +
        "cosAngIncidence = cosAngIncidence < 0.0001 ? 0.0 : cosAngIncidence;" +

        "vec3 viewDirection = normalize(-cameraSpacePosition);" +

        "vec3 halfAngle = normalize(lightDirection + viewDirection);" +

        "float angleNormalHalf = acos(dot(halfAngle, surfaceNormal));" +
        "float exponent = angleNormalHalf / Mtl.specularShininess;" +
        "exponent = -(exponent * exponent);" +
        "float gaussianTerm = exp(exponent);" +

        "gaussianTerm = cosAngIncidence != 0.0 ? gaussianTerm : 0.0;" +

        "vec4 lighting = Mtl.diffuseColor * lightIntensity * cosAngIncidence;" +
        "lighting += Mtl.specularColor * lightIntensity * gaussianTerm;" +

        "return lighting;" +
    "}" +

    "void main()" +
    "{" +
        "vec4 accumLighting = Mtl.diffuseColor * Lgt.ambientIntensity;" +
        "for(int light = 0; light < numberOfLights; light++)" +
        "{" +
            "accumLighting += ComputeLighting(Lgt.lights[light]," +
            "cameraSpacePosition, vertexNormal);" +
        "}" +

        "gl_FragColor = sqrt(accumLighting);" + //2.0 gamma correction / OK
    "}";

    public static String TextureGaussian =

    "varying vec3 vertexNormal;" +
    "varying vec3 cameraSpacePosition;" +

    "struct Material" +
    "{" +
        "vec4 diffuseColor;" +
        "vec4 specularColor;" +
        "float specularShininess;" +	//Not used in this shader
    "};" +

    "uniform Material Mtl;" +

    "struct PerLight" +
    "{" +
        "vec4 cameraSpaceLightPos;" +
        "vec4 lightIntensity;" +
    "};" +

    "const int numberOfLights = 2;" +

    "struct Light" +
    "{" +
        "vec4 ambientIntensity;" +
        "float lightAttenuation;" +
        "PerLight lights[numberOfLights];" +
    "};" +

    "uniform Light Lgt;" +

    "uniform sampler2D gaussianTexture;" +

    "float CalcAttenuation(in vec3 cameraSpacePosition," +
    "in vec3 cameraSpaceLightPos," +
    "out vec3 lightDirection)" +
    "{" +
        "vec3 lightDifference =  cameraSpaceLightPos - cameraSpacePosition;" +
        "float lightDistanceSqr = dot(lightDifference, lightDifference);" +
        "lightDirection = lightDifference * inversesqrt(lightDistanceSqr);" +

        "return (1.0 / ( 1.0 + Lgt.lightAttenuation * lightDistanceSqr));" +
    "}" +

    "vec4 ComputeLighting(in PerLight lightData, in vec3 cameraSpacePosition," +
    "in vec3 cameraSpaceNormal)" +
    "{" +
        "vec3 lightDir;" +
        "vec4 lightIntensity;" +
        "if(lightData.cameraSpaceLightPos.w == 0.0)" +
        "{" +
            "lightDir = vec3(lightData.cameraSpaceLightPos);" +
            "lightIntensity = lightData.lightIntensity;" +
        "}" +
        "else" +
        "{" +
            "float atten = CalcAttenuation(cameraSpacePosition," +
            "lightData.cameraSpaceLightPos.xyz, lightDir);" +
            "lightIntensity = atten * lightData.lightIntensity;" +
        "}" +

        "vec3 surfaceNormal = normalize(cameraSpaceNormal);" +
        "float cosAngIncidence = dot(surfaceNormal, lightDir);" +
        "cosAngIncidence = cosAngIncidence < 0.0001 ? 0.0 : cosAngIncidence;" +

        "vec3 viewDirection = normalize(-cameraSpacePosition);" +

        "vec3 halfAngle = normalize(lightDir + viewDirection);" +
        "float texCoord = dot(halfAngle, surfaceNormal);" +
        "float gaussianTerm = texture2D(gaussianTexture, vec2(texCoord, 0.0)).r;" +

        "gaussianTerm = cosAngIncidence != 0.0 ? gaussianTerm : 0.0;" +

        "vec4 lighting = Mtl.diffuseColor * lightIntensity * cosAngIncidence;" +
        "lighting += Mtl.specularColor * lightIntensity * gaussianTerm;" +

        "return lighting;" +
    "}" +

    "void main()" +
    "{" +
        "vec4 accumLighting = Mtl.diffuseColor * Lgt.ambientIntensity;" +
        "for(int light = 0; light < numberOfLights; light++)" +
        "{" +
            "accumLighting += ComputeLighting(Lgt.lights[light]," +
            "cameraSpacePosition, vertexNormal);" +
        "}" +

        "gl_FragColor = sqrt(accumLighting);" + //2.0 gamma correction
    "}";


    private static String MaterialStructureUniform =
    "struct Material" +
    "{" +
        "vec4 diffuseColor;" +
        "vec4 specularColor;" +
        "float specularShininess;" +
    "};" +

    "uniform Material Mtl;";

    private static String LightStructureUniform =
    "struct PerLight" +
    "{" +
        "vec4 cameraSpaceLightPos;" +
        "vec4 lightIntensity;" +
    "};" +

    "const int numberOfLights = 4;" +

    "struct Light" +
    "{" +
        "vec4 ambientIntensity;" +
        "float lightAttenuation;" +
        "float maxIntensity;" +
        "PerLight lights[numberOfLights];" +
    "};" +

    "uniform Light Lgt;";

    private static String CalcAttenuation =
    "float CalcAttenuation(vec3 cameraSpacePosition, vec3 cameraSpaceLightPos, out vec3 lightDirection)" +
    "{" +
        "vec3 lightDifference =  cameraSpaceLightPos - cameraSpacePosition;" +
        "float lightDistanceSqr = dot(lightDifference, lightDifference);" +
        "lightDirection = lightDifference * inversesqrt(lightDistanceSqr);" +

        "return (1.0 / ( 1.0 + Lgt.lightAttenuation * lightDistanceSqr));" +
    "}";


    public static String DiffuseSpecularHDR =
    "varying vec4 diffuseColor;" +
    "varying vec3 vertexNormal;" +
    "varying vec3 cameraSpacePosition;" +

    MaterialStructureUniform +

    LightStructureUniform +

    CalcAttenuation +

    "vec4 ComputeLighting(PerLight lightData)" +
    "{" +
        "vec3 lightDir;" +
        "vec4 lightIntensity;" +
        "if(lightData.cameraSpaceLightPos.w == 0.0)" +
        "{" +
            "lightDir = vec3(lightData.cameraSpaceLightPos);" +
            "lightIntensity = lightData.lightIntensity;" +
        "}" +
        "else" +
        "{" +
            "float atten = CalcAttenuation(cameraSpacePosition," +
            "lightData.cameraSpaceLightPos.xyz, lightDir);" +
            "lightIntensity = atten * lightData.lightIntensity;" +
        "}" +

        "vec3 surfaceNormal = normalize(vertexNormal);" +
        "float cosAngIncidence = dot(surfaceNormal, lightDir);" +
        "cosAngIncidence = cosAngIncidence < 0.0001 ? 0.0 : cosAngIncidence;" +

        "vec3 viewDirection = normalize(-cameraSpacePosition);" +

        "vec3 halfAngle = normalize(lightDir + viewDirection);" +
        "float angleNormalHalf = acos(dot(halfAngle, surfaceNormal));" +
        "float exponent = angleNormalHalf / Mtl.specularShininess;" +
        "exponent = -(exponent * exponent);" +
        "float gaussianTerm = exp(exponent);" +

        "gaussianTerm = cosAngIncidence != 0.0 ? gaussianTerm : 0.0;" +

        "vec4 lighting = diffuseColor * lightIntensity * cosAngIncidence;" +
        "lighting += Mtl.specularColor * lightIntensity * gaussianTerm;" +

        "return lighting;" +
    "}" +

    "void main()" +
    "{" +
        "vec4 accumLighting = diffuseColor * Lgt.ambientIntensity;" +
        "for(int light = 0; light < numberOfLights; light++)" +
        "{" +
            "accumLighting += ComputeLighting(Lgt.lights[light]);" +
        "}" +

        "gl_FragColor = accumLighting / Lgt.maxIntensity;" +
    "}";

    public static String DiffuseOnlyHDR =
    "varying vec4 diffuseColor;" +
    "varying vec3 vertexNormal;" +
    "varying vec3 cameraSpacePosition;" +

    MaterialStructureUniform +

    LightStructureUniform +

    CalcAttenuation +

    "vec4 ComputeLighting(PerLight lightData)" +
    "{" +
        "vec3 lightDir;" +
        "vec4 lightIntensity;" +
        "if(lightData.cameraSpaceLightPos.w == 0.0)" +
        "{" +
            "lightDir = vec3(lightData.cameraSpaceLightPos);" +
            "lightIntensity = lightData.lightIntensity;" +
        "}" +
        "else" +
        "{" +
            "float atten = CalcAttenuation(cameraSpacePosition," +
            "lightData.cameraSpaceLightPos.xyz, lightDir);" +
            "lightIntensity = atten * lightData.lightIntensity;" +
        "}" +

        "vec3 surfaceNormal = normalize(vertexNormal);" +
        "float cosAngIncidence = dot(surfaceNormal, lightDir);" +
        "cosAngIncidence = cosAngIncidence < 0.0001 ? 0.0 : cosAngIncidence;" +

        "vec4 lighting = diffuseColor * lightIntensity * cosAngIncidence;" +

        "return lighting;" +
    "}" +

    "void main()" +
    "{" +
        "vec4 accumLighting = diffuseColor * Lgt.ambientIntensity;" +
        "for(int light = 0; light < numberOfLights; light++)" +
        "{" +
            "accumLighting += ComputeLighting(Lgt.lights[light]);" +
        "}" +

        "gl_FragColor = accumLighting / Lgt.maxIntensity;" +
    "}"; // DiffuseOnlyHDR

    public static String DiffuseSpecularMtlHDR =
    "varying vec3 vertexNormal;" +
    "varying vec3 cameraSpacePosition;" +

    MaterialStructureUniform +

    LightStructureUniform +

    CalcAttenuation +

    "vec4 ComputeLighting(PerLight lightData)" +
    "{" +
        "vec3 lightDir;" +
        "vec4 lightIntensity;" +
        "if(lightData.cameraSpaceLightPos.w == 0.0)" +
        "{" +
            "lightDir = vec3(lightData.cameraSpaceLightPos);" +
            "lightIntensity = lightData.lightIntensity;" +
        "}" +
        "else" +
        "{" +
            "float atten = CalcAttenuation(cameraSpacePosition," +
            "lightData.cameraSpaceLightPos.xyz, lightDir);" +
            "lightIntensity = atten * lightData.lightIntensity;" +
        "}" +

        "vec3 surfaceNormal = normalize(vertexNormal);" +
        "float cosAngIncidence = dot(surfaceNormal, lightDir);" +
        "cosAngIncidence = cosAngIncidence < 0.0001 ? 0.0 : cosAngIncidence;" +

        "vec3 viewDirection = normalize(-cameraSpacePosition);" +

        "vec3 halfAngle = normalize(lightDir + viewDirection);" +
        "float angleNormalHalf = acos(dot(halfAngle, surfaceNormal));" +
        "float exponent = angleNormalHalf / Mtl.specularShininess;" +
        "exponent = -(exponent * exponent);" +
        "float gaussianTerm = exp(exponent);" +

        "gaussianTerm = cosAngIncidence != 0.0 ? gaussianTerm : 0.0;" +

        "vec4 lighting = Mtl.diffuseColor * lightIntensity * cosAngIncidence;" +
        "lighting += Mtl.specularColor * lightIntensity * gaussianTerm;" +

        "return lighting;" +
    "}" +

    "void main()" +
    "{" +
        "vec4 accumLighting = Mtl.diffuseColor * Lgt.ambientIntensity;" +
        "for(int light = 0; light < numberOfLights; light++)" +
        "{" +
            "accumLighting += ComputeLighting(Lgt.lights[light]);" +
        "}" +

        "gl_FragColor = accumLighting / Lgt.maxIntensity;" +
    "}";

    public static String DiffuseOnlyMtlHDR =
    "varying vec3 vertexNormal;" +
    "varying vec3 cameraSpacePosition;" +

    MaterialStructureUniform +

    LightStructureUniform +

    CalcAttenuation +

    "vec4 ComputeLighting(PerLight lightData)" +
    "{" +
        "vec3 lightDir;" +
        "vec4 lightIntensity;" +
        "if(lightData.cameraSpaceLightPos.w == 0.0)" +
        "{" +
            "lightDir = vec3(lightData.cameraSpaceLightPos);" +
            "lightIntensity = lightData.lightIntensity;" +
        "}" +
        "else" +
        "{" +
            "float atten = CalcAttenuation(cameraSpacePosition," +
            "lightData.cameraSpaceLightPos.xyz, lightDir);" +
            "lightIntensity = atten * lightData.lightIntensity;" +
        "}" +

        "vec3 surfaceNormal = normalize(vertexNormal);" +
        "float cosAngIncidence = dot(surfaceNormal, lightDir);" +
        "cosAngIncidence = cosAngIncidence < 0.0001 ? 0.0 : cosAngIncidence;" +

        "vec4 lighting = Mtl.diffuseColor * lightIntensity * cosAngIncidence;" +

        "return lighting;" +
    "}" +

    "void main()" +
    "{" +
        "vec4 accumLighting = Mtl.diffuseColor * Lgt.ambientIntensity;" +
        "for(int light = 0; light < numberOfLights; light++)" +
        "{" +
          "accumLighting += ComputeLighting(Lgt.lights[light]);" +
        "}" +

        "gl_FragColor = accumLighting / Lgt.maxIntensity;" +
    "}";

    private static String VariableLightStructureUniform =
    "struct PerLight" +
    "{" +
        "vec4 cameraSpaceLightPos;" +
        "vec4 lightIntensity;" +
    "};" +

    "uniform int numberOfLights;" +

    "struct Light" +
    "{" +
        "vec4 ambientIntensity;" +
        "float lightAttenuation;" +
        "float maxIntensity;" +
        "PerLight lights[4];" +
    "};" +

    "uniform Light Lgt;";

    public static String projlight =

    "varying vec2 colorCoord;" +
    "varying  vec3 cameraSpacePosition;" +
    "varying  vec3 cameraSpaceNormal;" +
    "varying  vec4 lightProjPosition;" +

    VariableLightStructureUniform +

    CalcAttenuation +

    "vec4 ComputeLighting(in vec4 diffuseColor, in PerLight lightData)" +
    "{" +
        "vec3 lightDir;" +
        "vec4 lightIntensity;" +
        "if(lightData.cameraSpaceLightPos.w == 0.0)" +
        "{" +
            "lightDir = vec3(lightData.cameraSpaceLightPos);" +
            "lightIntensity = lightData.lightIntensity;" +
        "}" +
        "else" +
        "{" +
            "float atten = CalcAttenuation(cameraSpacePosition, lightData.cameraSpaceLightPos.xyz, lightDir);" +
            "lightIntensity = atten * lightData.lightIntensity;" +
        "}" +

        "vec3 surfaceNormal = normalize(cameraSpaceNormal);" +
        "float cosAngIncidence = dot(surfaceNormal, lightDir);" +
        "cosAngIncidence = cosAngIncidence < 0.0001 ? 0.0 : cosAngIncidence;" +

        "vec4 lighting = diffuseColor * lightIntensity * cosAngIncidence;" +

        "return lighting;" +
    "}" +

    "uniform sampler2D diffuseColorTex;" +
    "uniform sampler2D lightProjTex;" +

    "uniform vec3 cameraSpaceProjLightPos;" +

    "void main()" +
    "{" +
        "vec4 diffuseColor = texture2D(diffuseColorTex, colorCoord);" +

        "PerLight currLight;" +
        "currLight.cameraSpaceLightPos = vec4(cameraSpaceProjLightPos, 1.0);" +
        "currLight.lightIntensity = texture2DProj(lightProjTex, lightProjPosition.xyw) * 4.0;" +

        "currLight.lightIntensity = lightProjPosition.w > 0.0 ? currLight.lightIntensity : vec4(0.0);" +

        "vec4 accumLighting = diffuseColor * Lgt.ambientIntensity;" +
        "for(int light = 0; light < numberOfLights; light++)" +
        "{" +
            "accumLighting += ComputeLighting(diffuseColor, Lgt.lights[light]);" +
        "}" +

        "accumLighting += ComputeLighting(diffuseColor, currLight);" +
        "if ((accumLighting.x + accumLighting.y + accumLighting.z) == 0.0)" +
        "{" +
            "discard;" +
        "}" +
        "gl_FragColor = accumLighting / Lgt.maxIntensity;" + // ok
    "}"; // projlight

    public static String Tex =
    "varying vec2 colorCoord;" +
    "uniform sampler2D colorTexture;" +

    "void main()" +
    "{" +
        "gl_FragColor = texture2D(colorTexture, colorCoord);" +
    "}";

}

