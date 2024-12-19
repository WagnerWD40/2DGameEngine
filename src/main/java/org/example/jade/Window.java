package org.example.jade;

import org.example.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {

    private int width;
    private int height;
    private String title;
    private long glfwWindow;

    private float r;
    private float g;
    private float b;
    private float a;

    public Scene currentScene = null;

    private static Window window = null;

    private ImGUILayer imGUILayer;

    private Window(ImGUILayer imGUILayer) {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
        this.a = 1.0f;

        this.imGUILayer = imGUILayer;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window(new ImGUILayer());
        }

        return Window.window;
    }

    public static void changeScene(Scene newScene) {
        get().currentScene = newScene;
        get().currentScene.init();
        get().currentScene.start();
    }

    public static void changeWindowColor(float r, float g, float b) {
        get().r = r;
        get().g = g;
        get().b = b;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion());

        init();
        loop();

        // Free Memory
        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Termnate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (glfwWindow == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL Context
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

    }

    public void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        double beginTime = glfwGetTime();
        double endTime;
        double dt = -1.0f;

        imGUILayer.init(glfwWindow);

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Pool events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);



            if (currentScene == null) {
                Window.changeScene(new LevelEditorScene());
            }

            if (dt > 0 && currentScene != null) {
                currentScene.update(dt);
            }

            imGUILayer.run();

            glfwSwapBuffers(glfwWindow);

            endTime = glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
