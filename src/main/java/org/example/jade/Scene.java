package org.example.jade;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene implements Updatable {

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
        }
    }

    public void start() {
        for (GameObject go: gameObjects) {
            go.start();
        }
        isRunning = true;
    }

    public abstract void init();
}
