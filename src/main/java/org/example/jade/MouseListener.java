package org.example.jade;

import org.lwjgl.glfw.GLFW;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX;
    private double scrollY;

    private double xPos;
    private double yPos;
    private double lastX;
    private double lastY;

    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private  MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }

        return instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;

        get().xPos = xpos;
        get().yPos = ypos;

        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] ||get().mouseButtonPressed[2];
    }

    private static boolean isMouseButtonPressedValid(int button) {
        return !(button > get().mouseButtonPressed.length - 1);
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (!isMouseButtonPressedValid(button)) {
            return;
        }

        if (action == GLFW.GLFW_PRESS) {
            get().mouseButtonPressed[button ] = true;
        }

        if (action == GLFW.GLFW_RELEASE) {
            get().mouseButtonPressed[button ] = false;
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if (isMouseButtonPressedValid(button)) {
            return get().mouseButtonPressed[button];
        }
        return false;
    }
}
