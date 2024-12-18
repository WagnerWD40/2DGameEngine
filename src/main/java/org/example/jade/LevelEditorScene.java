package org.example.jade;

import org.example.components.Sprite;
import org.example.components.SpriteRenderer;
import org.example.components.Spritesheet;
import org.example.renderer.Shader;
import org.example.renderer.Texture;
import org.example.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private GameObject obj1;

    public LevelEditorScene() {
//        System.out.println("Inside " + this.getClass().getName());

    }

    @Override
    public void update(double dt) {
        showFps(dt);
        for (GameObject go : this.gameObjects) {
            obj1.transform.position.x += (float) (10 * dt);
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        Spritesheet sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        //loadTestColors();
        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(64, 64)), 4);
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.gameObjects.add(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(200, 100), new Vector2f(64, 64)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(1)));
        this.gameObjects.add(obj2);

        GameObject obj3 = new GameObject("Object 3", new Transform(new Vector2f(300, 100), new Vector2f(64, 64)), 10);
        obj3.addComponent(new SpriteRenderer(sprites.getSprite(2)));
        this.gameObjects.add(obj3);

        GameObject obj4 = new GameObject("Object 4", new Transform(new Vector2f(400, 100), new Vector2f(64, 64)));
        obj4.addComponent(new SpriteRenderer(sprites.getSprite(10)));
        this.gameObjects.add(obj4);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheet.png", 16, 16, 26, 0);
    }

    private void loadTestColors() {
        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float) (600 - xOffset * 2);
        float totalHeight = (float) (300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject go = new GameObject("Obj" + x + "_" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go);

            }

        }
    }

}
