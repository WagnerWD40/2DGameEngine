package org.example.jade;

import org.example.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene implements Updatable {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {

    }

    public abstract void update(double dt);

    public void addGameObjectToScene(GameObject go) {
        gameObjects.add(go);
        if (isRunning) {
            go.start();
            this.renderer.add(go);
        }
    }

    public void start() {
        for (GameObject go: gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public abstract void init();

    public Camera getCamera() {
        return camera;
    }

    protected void showFps(double dt) {
        System.out.println("" + (1.0f / dt) + " FPS");
    }
}
