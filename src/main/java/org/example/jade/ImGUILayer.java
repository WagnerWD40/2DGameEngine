package org.example.jade;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class ImGUILayer {
    protected ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    protected ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;
    private long windowPointer;

    private boolean showText = false;

    public ImGUILayer() {

    }

    public void init(long windowPointer) {
        initWindow();
        this.windowPointer = windowPointer;
        imGuiGlfw.init(windowPointer, true);
        imGuiGl3.init(glslVersion);
    }

    public void destroy() {
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(windowPointer);
        glfwDestroyWindow(windowPointer);
        glfwTerminate();
    }

    private void initWindow() {
        ImGui.createContext();
//        ImGuiIO io = ImGui.getIO();
//        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    private void startFrame() {
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    private void imgui() {

        ImGui.begin("Cool Window");

        if (ImGui.button("I am a button")) {
            this.showText = true;
        }

        if (this.showText) {
            ImGui.text("You clocked a button");
            ImGui.sameLine();

            if (ImGui.button("Stop showing text")) {
                this.showText = false;
            }
        }

        ImGui.end();
    }

    private void endFrame() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        // Update and Render additional Platform Windows
        // (Platform functions may change the current OpenGL context, so we save/restore it to make it easier to paste this code elsewhere.
        //  For this specific demo app we could also call glfwMakeContextCurrent(window) directly)
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupCurrentContext = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupCurrentContext);
        }
    }

    public void run() {
        this.startFrame();
        this.imgui();
        this.endFrame();
    }
}
