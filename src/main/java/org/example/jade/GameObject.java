package org.example.jade;

import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class GameObject implements Updatable {

    private String name;
    private List<Component> components;

    public Transform transform;
    private int zIndex = 0;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {

                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false: "Error: Casting component.";
                }

            }
        }

        return null;
    }

    public <T extends  Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);

            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.setGameObject(this);
    }

    @Override
    public void update(double dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public int getZIndex() {
        return zIndex;
    }
}
