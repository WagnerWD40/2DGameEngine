package org.example.renderer;

import org.example.components.SpriteRenderer;
import org.example.jade.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);

        if (spriteRenderer != null) {
            add(spriteRenderer);
        }
    }

    public void add(SpriteRenderer sprite) {
        boolean added = false;

        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.getzIndex() == sprite.gameObject.getZIndex()) {
                Texture texture = sprite.getTexture();

                if (texture == null && (batch.hasTexture(texture) || batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.getZIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }
}
