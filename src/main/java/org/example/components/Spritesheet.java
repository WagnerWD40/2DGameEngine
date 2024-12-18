package org.example.components;

import org.example.renderer.Texture;
import org.example.util.AssetPool;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {

    private Texture texture;
    private List<Sprite> sprites;

    public Spritesheet(String path, int spriteWidth, int spriteHeight, int spriteCount, int spacing) {
        this(AssetPool.getTexture(path), spriteWidth, spriteHeight, spriteCount, spacing);
    }

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int spriteCount, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;

        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for (int i = 0; i < spriteCount; i++) {
            float topY = (currentY +spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] texCoords = Sprite.createCoordinates(rightX, topY, leftX, bottomY);

            Sprite sprite = new Sprite(texture, texCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;

            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }
}
