package org.example.components;

import com.sun.security.jgss.GSSUtil;
import org.example.jade.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    @Override
    public void start() {
        System.out.println(this.getClass().getName() + " is starting...");
    }

    @Override
    public void update(double dt) {

    }

    public Vector4f getColor() {
        return color;
    }
}
