package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import javafx.scene.paint.Color;

public class Light {
    private Vector3f direction;
    private Color color;
    private float intensity;
    private boolean isEnabled;


    public Light(Vector3f direction, Color color, float intensity) {
        direction.normalize();
        this.direction = direction;
        this.color = color;
        this.intensity = intensity;

        this.isEnabled = true;
    }
    public Vector3f getDirection() {
        return direction;
    }

    public Color getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}