package org.example.components;

import com.sun.security.jgss.GSSUtil;
import org.example.jade.Component;
import org.example.jade.Transform;
import org.example.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Sprite sprite;
    private boolean dirty = true;

    private Transform lastTransform;

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
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(double dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.lastTransform = gameObject.transform.copy();
            this.dirty = true;
        }
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

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.color.set(color);
            this.dirty = true;
        }
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setClean() {
        this.dirty = false;
    }
}
