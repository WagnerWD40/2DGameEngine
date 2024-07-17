package org.example.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private final String filepath;
    private boolean used = false;

    public Shader(String filepath) {
        this.filepath = filepath;

        parseShaders(filepath);

        int vertexID = compileVertex();
        int fragmentID = compileFragment();
        linkShaders(vertexID, fragmentID);

    }

    public void parseShaders(String filepath) {
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            List<String> patterns = new ArrayList<>();

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);

            patterns.add(source.substring(index, eol).trim());

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);

            patterns.add(source.substring(index, eol).trim());

            for (String pattern : patterns) {
                if (pattern.equals(ShaderType.VERTEX.label)) {
                    vertexSource = splitString[1];
                }

                if (pattern.equals(ShaderType.FRAGMENT.label)) {
                    fragmentSource = splitString[2];
                }
            }

//            System.out.println(vertexSource);
//            System.out.println(fragmentSource);

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }

    }

    public int compileVertex() {

        //First load and compile the vertex shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);

        //Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: (\" + filepath + \")\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false: "";
        }

        return vertexID;
    }

    public int compileFragment() {
        // First load and compile the vertex shader
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        int success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: (\" + filepath + \")\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        return fragmentID;
    }

    public void linkShaders(int vertexID, int fragmentID) {
        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);

        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: (" + filepath + ")\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        if (!used) {
            glUseProgram(shaderProgramID);
            used = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        used = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation,false, matBuffer);
    }

    public int getUniLocation(String varName) {
        return glGetUniformLocation(shaderProgramID, varName);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        use();
        glUniform4f(getUniLocation(varName), vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadFloat(String varName, float val) {
        use();
        glUniform1f(getUniLocation(varName), val);
    }

    public void uploadInt(String varName, int val) {
        use();
        glUniform1i(getUniLocation(varName), val);
    }

    public void uploadVec2(String varName, Vector2f val) {
        use();
        glUniform2f(getUniLocation(varName), val.x, val.y);
    }

    public void uploadMat3(String varName, Matrix3f mat3) {
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        use();
        glUniformMatrix3fv(getUniLocation(varName), false, matBuffer);
    }

    public void uploadTexture(String varName, int slot) {
        use();
        glUniform1i(getUniLocation(varName), slot);
    }
}
