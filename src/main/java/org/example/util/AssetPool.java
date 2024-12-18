package org.example.util;

import org.example.components.Spritesheet;
import org.example.renderer.Shader;
import org.example.renderer.Texture;
import org.lwjgl.system.CallbackI;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);

        String filePath = file.getAbsolutePath();

        if (shaders.containsKey(filePath)) {
            return shaders.get(filePath);
        }
        Shader shader = new Shader(resourceName);
        shader.compile();

        AssetPool.shaders.put(filePath, shader);

        return shader;
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);

        String filePath = file.getAbsolutePath();

        if (textures.containsKey(filePath)) {
            return textures.get(filePath);
        }

        Texture texture = new Texture(filePath);
        AssetPool.textures.put(filePath, texture);

        return  texture;
    }

    public static void addSpriteSheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);

        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static void addSpriteSheet(String resourceName,  int spriteWidth, int spriteHeight, int spriteCount, int spacing) {
        Spritesheet spritesheet = new Spritesheet(resourceName, spriteWidth, spriteHeight, spriteCount, spacing);
        AssetPool.addSpriteSheet(resourceName, spritesheet);
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);

        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            assert false: "Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to the AssetPool'";
        }

        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
