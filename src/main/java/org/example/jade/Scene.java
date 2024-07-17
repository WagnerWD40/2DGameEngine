package org.example.jade;

public abstract class Scene {

    protected Camera camera;

    public Scene() {

    }

    public abstract void update(double dt);

    public abstract void init();
}
