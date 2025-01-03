package org.example.jade;

import org.lwjgl.glfw.GLFW;

public class KeyListener {
    private static KeyListener instance;
    private boolean[] keyPressed = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            get().keyPressed[key] = true;
        }

        if (action == GLFW.GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
