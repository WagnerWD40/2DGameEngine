package org.example.components;

import com.sun.security.jgss.GSSUtil;
import org.example.jade.Component;
import org.example.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Sprite sprite;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite();
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color= new Vector4f(1, 1, 1 ,1);
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

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTextCoords() {
        return sprite.getTextureCoords();
    }
}
