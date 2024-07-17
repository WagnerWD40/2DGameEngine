package org.example.jade;

import org.example.renderer.Shader;
import org.example.renderer.Texture;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private int vertexID;
    private int fragmentID;
    private int shaderProgram;

    private Texture testTexture;

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private float[] vertexArray = {
            // position           //color                      //uv coordinates
            100.0f, 100.0f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f,   1.0f, 1.0f, // top right    0
            100.0f, 0.0f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f,   1.0f, 0.0f, // bottom right        1
            0.0f, 0.0f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f,   0.0f, 0.0f, // bottom left       2
            0.0f, 100.0f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f,   0.0f, 1.0f, // top left     3
    };

    // IMPORTANT: Must be in counter-clockwise order
//    private int[] elementArray = {
//        0, 2, 1,  // Top right triangle
//        2, 0, 3   // bottom left triangle
//    };

    private int[] elementArray = {
            2, 3, 0,  // Top right triangle
            0, 1, 2   // bottom left triangle
    };

    private int vaoID;
    private int vboID;
    private int eboID;

    private Shader defaultShader;


    public LevelEditorScene() {
//        System.out.println("Inside " + this.getClass().getName());


    }

    @Override
    public void update(double dt) {
//        showFps(dt);
        //camera.updatePosition((float)dt * -50.f, (float)dt * -50.f);

        // Bind shader program
        defaultShader.use();

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", (float) glfwGetTime());

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute points
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0.0f, 0.0f));
        this.defaultShader = new Shader("assets/shaders/default.glsl");
//        this.testTexture = new Texture("assets/images/mario_sprite.png");
        this.testTexture = new Texture("assets/images/mario_test.png");
//        this.testTexture = new Texture("assets/images/megaman_sprite.png");
        testTexture.bind();

        generateBufferObjectsAndSendToGPU();


    }

    //TODO - refactor to another class
    private void generateBufferObjectsAndSendToGPU() {
        // ==================================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ==================================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    private void showFps(double dt) {
        System.out.println("" + (1.0f / dt) + " FPS");
    }

}
