package org.example.jade;

public abstract class Component implements Updatable{

    public GameObject gameObject;

    public abstract void update(double dt);

    public void start() {};

    public GameObject getGameObject() {
        return this.gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
