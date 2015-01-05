package com.tutorial.glsltutorials.tutorials.Scene;

import android.opengl.GLES20;
import android.util.Log;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jamie on 1/3/15.
 */
public class SceneImpl {
    int FORCE_SRGB_COLORSPACE_FMT = 1;
    private MeshMap m_meshes = new MeshMap();
    private TextureMap m_textures = new TextureMap();
    private  ProgramMap m_progs = new ProgramMap();
    private NodeMap m_nodes = new NodeMap();

    ArrayList <SceneNode> m_rootNodes = new ArrayList <SceneNode>();

    ArrayList <Integer> m_samplers = new ArrayList <Integer>();

    // To allow reflectoin
    static Class vertexShaders = new VertexShaders().getClass();
    static Class fragmentShaders = new FragmentShaders().getClass();

    // Test
    static Field[] vertexFields = vertexShaders.getFields();
    static Field[] fragmentFields = fragmentShaders.getFields();

    private String GetVertexShader(String name) throws Exception
    {
        name = name.toLowerCase();
        for(Field field : vertexFields)
        {
            String test = field.getName();
            if (test.equals(name))
            {
                return (String)field.get(vertexShaders);
            }
        }
        return null;
    }

    private String GetFragmentShader(String name) throws Exception
    {
        for(Field field : fragmentFields)
        {
            String test = field.getName();
            if (test.equals(name))
            {
                return (String)field.get(fragmentShaders);
            }
        }
        return null;
    }

    public SceneImpl(String fileName) throws Exception
    {
        int resource = -1;
        switch (fileName) {
            case "dp_scene.xml": resource = R.raw.dp_scene; break;
            case "proj2d_scene.xml": resource = R.raw.proj2d_scene; break;
        }
        if (resource == -1)
        {
            Log.e("Missing scene resource", fileName);
            return;
        }
        InputStream inputStream = Shader.context.getResources().openRawResource(resource);
        setup(inputStream);
    }
    
    public void setup(InputStream inputStream) throws Exception
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc;
        try {

            doc = dBuilder.parse(inputStream);
        } catch (Exception ex) {
            throw new Exception("Error loading xml mesh: " + ex.toString());
        }

        Element pSceneNode = doc.getDocumentElement();

        PARSE_THROW(pSceneNode, " Scene node not found : scene file.");

        try
        {
            ReadMeshes(pSceneNode);
            ReadTextures(pSceneNode);
            ReadPrograms(pSceneNode);
            ReadNodes(null, pSceneNode);
        }
        catch(Exception ex)
        {
            Log.i("SceneImpl", "Error creating scene " + ex.toString());
            m_nodes.clear();
            m_progs.clear();
            m_textures.clear();
            m_meshes.clear();
        }

        // Fixme call when drawing MakeSamplerObjects(m_samplers);
    }

    public void finalize()
    {
        Integer[] samplers = (Integer[])m_samplers.toArray();
        m_samplers.clear();
        m_nodes.clear();
        m_progs.clear();
        m_textures.clear();
        m_meshes.clear();
    }

    public void Render(Matrix4f cameraMatrix) throws Exception
    {
        Iterator<Map.Entry<String, SceneNode>> iterator = m_nodes.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, SceneNode> entry = iterator.next();
            entry.getValue().render(m_samplers, cameraMatrix);
        }
    }

    public NodeRef findNode(String nodeName)
    {
        if (m_nodes.containsKey(nodeName))
        {
            return new NodeRef(m_nodes.get(nodeName));
        }
        Log.i("SceneImpl", "Could not find node for " + nodeName);
        return null;
    }

    public int findProgram(String progName)
    {
        if (m_progs.containsKey(progName))
        {
            return m_progs.get(progName).GetProgram();
        }
        Log.i("SceneImpl", "Could not find program " + progName);
        return -1;
    }

    public Mesh findMesh(String meshName)
    {
        if (m_meshes.containsKey(meshName))
        {
                return m_meshes.get(meshName).getMesh();
        }
        Log.i("SceneImpl", "Could not find mesh " + meshName);
        return null;
    }

    public SceneTexture findTexture(String textureName)
    {
        if (m_textures.containsKey(textureName))
        {
            return m_textures.get(textureName);
        }
        Log.i("SceneImpl", "Could not find texture " + textureName);
        return  null;
    }

    private void ReadMeshes(Element scene) throws Exception
    {
        NodeList pMeshNodes = scene.getElementsByTagName("mesh");
        for (int i = 0; i < pMeshNodes.getLength(); i++)
        {
            ReadMesh((Element)pMeshNodes.item(i));
        }
    }

    private void ReadMesh(Element meshNode) throws Exception
    {
        String pNameNode = meshNode.getAttribute("xml:id");
        String pFilenameNode = meshNode.getAttribute("file");

        PARSE_THROW(pNameNode, "Mesh found with no `xml:id` name specified.");
        PARSE_THROW(pFilenameNode, "Mesh found with no `file` filename specified.");

        if (m_meshes.containsKey(pNameNode))
        {
            Log.i("SceneImpl", "The mesh named " + pNameNode + " already exists.");
        }

        SceneMesh pMesh = new SceneMesh(pFilenameNode);
        m_meshes.put(pNameNode, pMesh);
    }

    private void ReadTextures(Element scene)
    {
        NodeList  xnl = scene.getElementsByTagName("texture");
        for (int i = 0; i < xnl.getLength(); i++)
        {
            ReadTexture((Element)xnl.item(i));
        }
    }

    private void ReadTexture(Element TexNode)
    {
        String pNameNode = TexNode.getAttribute("xml:id");
        String pFilenameNode = TexNode.getAttribute("file");

        if (pNameNode.equals("")) return;

        PARSE_THROW(pNameNode, "Texture found with no `xml:id` name specified.");
        PARSE_THROW(pFilenameNode, "Texture found with no `file` filename specified.");

        String pSrgbNode = TexNode.getAttribute("srgb");

        if(m_textures.containsKey(pNameNode))
        {
            Log.i("SceneImpl", "The texture named \"" + pNameNode + "\" already exists.");
        }

        int creationFlags = 0;
        if(pSrgbNode != null)
            creationFlags |= FORCE_SRGB_COLORSPACE_FMT;

        SceneTexture pTexture = new SceneTexture(pFilenameNode, creationFlags);

        m_textures.put(pNameNode, pTexture);
    }

    private void ReadPrograms(Element scene) throws Exception
    {
        NodeList  attributeNodes = scene.getElementsByTagName("prog");
        for (int i = 0; i < attributeNodes.getLength(); i++)
        {
            ReadProgram((Element)attributeNodes.item(i));
        }
    }

    private void ReadProgram(Element progNode) throws Exception
    {
        String pNameNode = progNode.getAttribute("xml:id");
        String pVertexShaderNode = progNode.getAttribute("vert");
        String pFragmentShaderNode = progNode.getAttribute("frag");
        String pModelMatrixNode = progNode.getAttribute("model-to-camera");

        PARSE_THROW(pNameNode, "Program found with no `xml:id` name specified.");
        PARSE_THROW(pVertexShaderNode, "Program found with no `vert` vertex shader specified.");
        PARSE_THROW(pFragmentShaderNode, "Program found with no `frag` fragment shader specified.");
        PARSE_THROW(pModelMatrixNode, "Program found with no model-to-camera matrix uniform name specified.");

        //Optional.
        String pNormalMatrixNode = progNode.getAttribute("normal-model-to-camera");
        String pGeometryShaderNode = progNode.getAttribute("geom");

        if(m_progs.containsKey(pNameNode))
        {
            Log.i("SceneImpl", "The program named \"" + pNameNode + "\" already exists.");
        }

        ArrayList <Integer> shaders = new ArrayList <Integer>();
        int program = 0;



        String vertexShaderString = pVertexShaderNode.toLowerCase().substring(0, pVertexShaderNode.length() - 5);

        String vertexShader = GetVertexShader(vertexShaderString);

        if (vertexShader == null)
        {
            Log.e("Vertex Shader not found", vertexShaderString);
        }


        String fragmentShaderString = pFragmentShaderNode.toLowerCase().substring(0, pVertexShaderNode.length() - 5);

        String fragmentShader = GetFragmentShader(fragmentShaderString);

        if (fragmentShader == null)
        {
            Log.e("Fragment Shader not found", fragmentShaderString);
        }

        try
        {
            shaders.add(Shader.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader));
            shaders.add(Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader));
            if(pGeometryShaderNode != "") {
                //shaders.add(Shader.compileShader(GLES20.GL_GEOMETRY_SHADER, "GEOMETRY_SHADER"));
                Log.e("Shaders", "Geometry shaders not available.");
            }

            program = Shader.createAndLinkProgram(shaders);

        }
        catch(Exception ex)
        {
            Log.i("SceneImpl", "Error creating program " + ex.toString());
            for(int shader : shaders)
            {
                GLES20.glDeleteShader(shader);
            }
        }

        for (int shader : shaders)
        {
            GLES20.glDeleteShader(shader);
        }

        int matrixLoc = GLES20.glGetUniformLocation(program, pModelMatrixNode);
        if(matrixLoc == -1)
        {
            GLES20.glDeleteProgram(program);
            Log.i("SceneImpl", "Could not find the matrix uniform " + pModelMatrixNode +
                    " : program " + pNameNode);
        }

        int normalMatLoc = -1;
        if(pNormalMatrixNode != "")
        {
            normalMatLoc = GLES20.glGetUniformLocation(program, pNormalMatrixNode);
            if(normalMatLoc == -1)
            {
                GLES20.glDeleteProgram(program);
                Log.i("SceneImpl", "Could not find the normal matrix uniform " + pNormalMatrixNode +
                        " : program " + pNameNode);
            }
        }

        m_progs.put(pNameNode,  new SceneProgram(program, matrixLoc, normalMatLoc));

        ReadProgramContents(program, progNode);
    }

    private void ReadProgramContents(int program, Element progNode)
    {
        ArrayList <String> blockBindings = new ArrayList <String>();
        ArrayList <String> samplerBindings = new ArrayList <String>();

        NodeList  pChildNodes = progNode.getChildNodes();

        for (int i = 0; i < pChildNodes.getLength(); i++)
        {
            Node pChildNode = pChildNodes.item(i);
            String childName = pChildNode.getNodeName();
            if(childName == "block")
            {
                String pNameNode = ((Element)pChildNode).getAttribute("name");
                String pBindingNode = ((Element)pChildNode).getAttribute("binding");

                PARSE_THROW(pNameNode, "Program `block` element with no `name`.");
                PARSE_THROW(pBindingNode, "Program `block` element with no `binding`.");

                if(blockBindings.contains(pNameNode))
                {
                    Log.i("SceneImpl", "The uniform block named " + pNameNode + " is used twice : the same program.");
                }

                blockBindings.add(pNameNode);

                // FIXMEint blockIx = GLES20.glGetUniformBlockIndex(program, name);

                // FIXME if(blockIx == GL_INVALID_INDEX)
                // FIXME {
                // FIXME 	std::cout << "Warning: the uniform block " << name << " could not be found." << std::endl;
                // FIXME 	continue;
                // FIXME }

                // FIXME int bindPoint = rapidxml::attrib_to_int(*pBindingNode, ThrowAttrib);
                // FIXME GLES20.glUniformBlockBinding(program, blockIx, bindPoint);
            }
            else if(childName == "sampler")
            {
                String pNameNode = ((Element)pChildNode).getAttribute("name");
                String pTexunitNode = ((Element)pChildNode).getAttribute("unit");

                PARSE_THROW(pNameNode, "Program `sampler` element with no `name`.");
                PARSE_THROW(pTexunitNode, "Program `sampler` element with no `unit`.");

                if(samplerBindings.contains(pNameNode))
                {
                    Log.i("SceneImpl", "A sampler " + pNameNode + " is used twice : the same program.");
                }

                samplerBindings.add(pNameNode);

                int samplerLoc = GLES20.glGetUniformLocation(program, pNameNode);
                if(samplerLoc == -1)
                {
                    Log.i("SceneImpl", "Warning: the sampler " + pNameNode + " could not be found.");
                }

                int textureUnit = Integer.parseInt(pTexunitNode);
                GLES20.glUseProgram(program);
                GLES20.glUniform1f(samplerLoc, textureUnit);
                GLES20.glUseProgram(0);
            }
            else
            {
                //Bad node. Die.
                Log.i("SceneImpl", "Unknown element found : program.");
            }
        }
    }

    private void ReadNodes(SceneNode pParent, Element scene)
    {
        NodeList  xnl = scene.getElementsByTagName("node");
        for (int i = 0; i < xnl.getLength(); i++)
        {
            try {
                ReadNode(pParent, (Element) xnl.item(i));
            }
            catch (Exception exception)
            {
                Log.e("ReadNodes", xnl.item(i).getNodeName() + " " + exception.toString());
            }
        }
    }

    private void ReadNode(SceneNode pParent, Element nodeNode)
    {
        String pNameNode = nodeNode.getAttribute("name");
        String pMeshNode = nodeNode.getAttribute("mesh");
        String pProgNode = nodeNode.getAttribute("prog");

        PARSE_THROW(pNameNode, "Node found with no `name` name specified.");
        PARSE_THROW(pMeshNode, "Node found with no `mesh` name specified.");
        PARSE_THROW(pProgNode, "Node found with no `prog` name specified.");

        String pPositionNode = nodeNode.getAttribute("pos");
        String pOrientNode = nodeNode.getAttribute("orient");
        String pScaleNode = nodeNode.getAttribute("scale");

        PARSE_THROW(pPositionNode, "Node found with no `pos` specified.");

        if(m_nodes.containsKey(pNameNode))
        {
            Log.i("SceneImpl", "The node named " + pNameNode + " already exist.");
        }

        if(!m_meshes.containsKey(pMeshNode))
        {
            Log.i("SceneImpl", "The node named " + pNameNode + " references the mesh " + pMeshNode + " which does not exist.");
        }

        if(!m_progs.containsKey(pProgNode))
        {
            Log.i("SceneImpl", "The node named " + pNameNode + " references the program " + pProgNode + " which does not exist.");
        }

        Vector3f nodePos = ParseVec3(pPositionNode);

        SceneNode pNode = new SceneNode(m_meshes.get(pMeshNode), m_progs.get(pProgNode), nodePos, ReadNodeTextures(nodeNode));
        m_nodes.put(pNameNode, pNode);

        //parent/child nodes.
        if(pParent == null)
            m_rootNodes.add(pNode);

        if(pOrientNode != "")
            pNode.SetNodeOrient(ParseQuaternion(pOrientNode));

        if(pScaleNode != "")
        {
            try
            {
                Vector3f result = ParseVec3(pScaleNode);
                pNode.setNodeScale(result);
            }
            catch (Exception e)
            {
                float unifScale = Float.parseFloat(pScaleNode);
                pNode.setNodeScale(new Vector3f(unifScale));
            }
        }
        ReadNodeNotes(nodeNode);
    }

    private void ReadNodeNotes(Element nodeNode)
    {
        NodeList  xnl = nodeNode.getElementsByTagName("note");
        for(int i = 0; i < xnl.getLength(); i++)
        {
            NodeList  pNameNode = nodeNode.getElementsByTagName("name");
            PARSE_THROW(pNameNode, "Notations on nodes must have a `name` attribute.");
        }
    }

    private ArrayList <TextureBinding> ReadNodeTextures(Element nodeNode)
    {
        ArrayList <TextureBinding> texBindings = new ArrayList <TextureBinding>();
        ArrayList <Integer> texUnits = new ArrayList <>();

        NodeList  xnl = nodeNode.getElementsByTagName("texture");
        for(int i = 0; i < xnl.getLength(); i++)
        {
            Node texNode = xnl.item(i);
            String pNameNode = ((Element)texNode).getAttribute("name");
            String pUnitName = ((Element)texNode).getAttribute("unit");
            String pSamplerName = ((Element)texNode).getAttribute("sampler");

            PARSE_THROW(pNameNode, "Textures on nodes must have a `name` attribute.");
            PARSE_THROW(pUnitName, "Textures on nodes must have a `unit` attribute.");
            PARSE_THROW(pSamplerName, "Textures on nodes must have a `sampler` attribute.");

            if(!m_textures.containsKey(pNameNode))
            {
                Log.i("SceneImpl", "The node texture named " + pNameNode + "  is a texture which does not exist.");
            }

            TextureBinding binding = new TextureBinding();

            binding.pTex = m_textures.get(pNameNode);
            binding.texUnit = Integer.parseInt(pUnitName);
            binding.sampler = GetTypeFromName(pSamplerName);

            if(texUnits.contains(binding.texUnit))
            {
                Log.i("SceneImpl", "Multiply bound texture unit : node texture " + pNameNode);
            }
            texBindings.add(binding);

            texUnits.add(binding.texUnit);
        }

        return texBindings;
    }

    int GetTypeFromName(String name)
    {
        String[] samplerNames = new String[]
        {
                "nearest",
                "linear",
                "mipmap nearest",
                "mipmap linear",
                "anisotropic",
                "half anisotropic",
        };

        for(int spl = 0; spl < SamplerTypes.MAX_SAMPLERS; ++spl)
        {
            if(name == samplerNames[spl])
                return spl;
        }

        Log.i("SceneImpl", "Unknown sampler name: " + name);
        return -1;
    }


    private void PARSE_THROW(String message)
    {
        Log.i("SceneImpl", message);
    }

    private void PARSE_THROW(Element obj, String message)
    {
        if (obj == null) Log.i("SceneImpl", "Object is null " + message);
    }

    private void PARSE_THROW(Node obj, String message)
    {
        if (obj == null) Log.i("SceneImpl", "Object is null " + message);
    }

    private void PARSE_THROW(NodeList obj, String message)
    {
        if (obj == null) Log.i("SceneImpl", "Object is null " + message);
    }

    private void PARSE_THROW(String obj, String message)
    {
        if (obj == "") Log.i("SceneImpl", "Object is null " + message);
    }

    Vector3f ParseVec3(String strVec3)
    {
        ArrayList<Float> newFloats = new ArrayList<Float>();
        String[] floatData = strVec3.split(" ");
        for (String s : floatData)
        {
            newFloats.add(Float.parseFloat(s));
        }
        Vector3f ret = new Vector3f(newFloats.get(0), newFloats.get(1), newFloats.get(1));
        return ret;
    }

    Quaternion ParseQuaternion(String strQuaternion)
    {
        ArrayList<Float> newFloats = new ArrayList<Float>();
        String[] floatData = strQuaternion.split(" ");
        for (String s : floatData)
        {
            newFloats.add(Float.parseFloat(s));
        }
        // FIXME should this be w, x y, z ??
        Quaternion ret = new Quaternion(newFloats.get(1), newFloats.get(2), newFloats.get(3), newFloats.get(0));
        return ret;
    }
}
