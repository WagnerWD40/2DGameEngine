package org.example.renderer;

import org.example.components.SpriteRenderer;
import org.example.jade.Window;
import org.example.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    // Vertex
    // ======
    // Pos                  Color                        TextCoords    textid
    // float, float,        float, float, float, float   float, float   float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXT_COORDS_SIZE = 2;
    private final int TEXT_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXT_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXT_ID_OFFSET = TEXT_COORDS_OFFSET + TEXT_COORDS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEXT_COORDS_SIZE + TEXT_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = { 0, 1, 2, 3, 4, 5, 6, 7 };

    private List<Texture> textures = new ArrayList<>();
    private int vaoID;
    private int vboID;
    private int maxBatchSize;
    private Shader shader;

    private record VertexAttribute(int size, int offset) {}

    public RenderBatch(int maxBatchSize) {
        this.shader = AssetPool.getShader("assets/shaders/default.glsl");
        shader.compile();

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        //4 vertices quads
        int QUAD_VERTICE_QUANTITY = 4;
        vertices = new float[maxBatchSize * QUAD_VERTICE_QUANTITY * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void start() {
        // Generate and bind Vertex Array;
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        final VertexAttribute[] vertexAttributes = {
                new VertexAttribute(POS_SIZE, POS_OFFSET),
                new VertexAttribute(COLOR_SIZE, COLOR_OFFSET),
                new VertexAttribute(TEXT_COORDS_SIZE, TEXT_COORDS_OFFSET),
                new VertexAttribute(TEXT_ID_SIZE, TEXT_ID_OFFSET)
        };

        // Enable the buffer attribute pointers

        int index = 0;
        for (VertexAttribute attribute : vertexAttributes) {
            glVertexAttribPointer(index, attribute.size(), GL_FLOAT, false, VERTEX_SIZE_BYTES, attribute.offset());
            glEnableVertexAttribArray(index);
            index++;
        }
    }

    public void render() {
        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }

        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        textures.forEach(Texture::unbind);

        shader.detach();
    }

    public void addSprite(SpriteRenderer sprite) {
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        final Texture spriteTexture = sprite.getTexture();
        if (spriteTexture != null) {
            if (!textures.contains(spriteTexture)) {
                textures.add(spriteTexture);
            }
        }

        // Add properties to local vertex array
        loadVertexProperties(index);

        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        // find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        int textId = 0;
        Vector2f[] textCoords = sprite.getTextCoords();

        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == sprite.getTexture()) {
                    textId = i + 1;
                    break;
                }
            }
        }


        //Add vertice with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;

        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            // Load position
            float spritePositionX = sprite.gameObject.transform.position.x;
            float spriteScaleX = sprite.gameObject.transform.scale.x;
            vertices[offset] = spritePositionX + (xAdd * spriteScaleX);

            float spritePositionY = sprite.gameObject.transform.position.y;
            float spriteScaleY = sprite.gameObject.transform.scale.y;
            vertices[offset + 1] = spritePositionY + (yAdd * spriteScaleY);

            // Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // Load texture coordinates
            vertices[offset + 6] = textCoords[i].x;
            vertices[offset + 7] = textCoords[i].y;

            // Load texture id
            vertices[offset + 8] = textId;

            offset += VERTEX_SIZE;
        }


    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];

        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //
        //  2, 3, 0,  // Top right triangle
        //  0, 1, 2   // bottom left triangle

        // Triangle 1
        elements[offsetArrayIndex] = offset + 2;
        elements[offsetArrayIndex + 1] = offset + 3;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 1;
        elements[offsetArrayIndex + 5] = offset + 2;
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }

    public boolean hasTextureRoom() {
        return textures.size() < 8;
    }

    public boolean hasTexture(Texture texture) {
        return textures.contains(texture);
    }


}
