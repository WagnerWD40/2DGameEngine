package org.example.renderer;

public enum ShaderType {
    VERTEX("vertex"),
    FRAGMENT("fragment");

    public final String label;

    ShaderType(String label) {
        this.label = label;
    }
}
