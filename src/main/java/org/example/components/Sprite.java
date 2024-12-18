package org.example.components;

import org.example.renderer.Texture;
import org.example.util.AssetPool;
import org.joml.Vector2f;

public class Sprite {
    private Texture texture;
    private Vector2f[] textureCoords;

    public Sprite(Texture texture) {
        this.texture = texture;
        this.textureCoords = createDefaultCoordinates();
    }

    public Sprite(Texture texture, Vector2f[] textureCoords) {
        this.texture = texture;
        this.textureCoords = textureCoords;
    }

    public Sprite(String path) {
        this.texture = AssetPool.getTexture(path);
        this.textureCoords = createDefaultCoordinates();
    }

    public Sprite() {
        this.texture = null;
        this.textureCoords = createDefaultCoordinates();
    }

    static public Vector2f[] createDefaultCoordinates() {
        return new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1),
        };
    }

    static public Vector2f[] createCoordinates(float rightX, float topY, float leftX, float bottomY) {
        return new Vector2f[]{
                new Vector2f(rightX, topY),
                new Vector2f(rightX, bottomY),
                new Vector2f(leftX, bottomY),
                new Vector2f(leftX, topY),
        };
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextureCoords() {
        return textureCoords;
    }
}
