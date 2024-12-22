package com.cgvsu.render_engine;


import com.cgvsu.math.Vector3f;
import javafx.scene.paint.Color;

import java.util.*;
public class LightingManager {
    private Map<Integer, Light> lights;
    private Set<Integer> activeLightIds;
    private int nextLightId;
    private boolean isBoundToCamera;
    private Camera boundCamera;
    private Integer boundLightId;

    public LightingManager() {
        lights = new HashMap<>();
        activeLightIds = new HashSet<>();
        nextLightId = 0;
        isBoundToCamera = false;
        boundLightId = null;
    }

    public int addLight(Vector3f direction, Color color) {
        Light light = new Light(direction, color, 1.0f);
        int id = nextLightId++;
        lights.put(id, light);
        return id;
    }

    public void removeLight(int id) {
        lights.remove(id);
        activeLightIds.remove(id);

        if (boundLightId != null && id == boundLightId) {
            unbindFromCamera();
        }
    }

    public List<Light> getActiveLights() {
        List<Light> activeLights = new ArrayList<>();
        for (int id : activeLightIds) {
            Light light = lights.get(id);
            if (light != null) {

                if (isBoundToCamera && boundCamera != null && id == boundLightId) {
                    Vector3f direction = boundCamera.getPosition().sub(new Vector3f(0, 0, 0));
                    direction.normalize();
                    light.setDirection(direction);
                }
                activeLights.add(light);
            }
        }
        return activeLights;
    }

    public void bindToCamera(Camera camera) {
        if (activeLightIds.size() < 1) {
            throw new IllegalStateException("Выберите хотя бы один источник освещения.");
        }

        boundLightId = activeLightIds.iterator().next();
        this.boundCamera = camera;
        this.isBoundToCamera = true;
    }

    public void unbindFromCamera() {
        this.boundCamera = null;
        this.isBoundToCamera = false;
        this.boundLightId = null;
    }

    public Integer getBoundLightId() {
        return boundLightId;
    }

    public void setActiveLights(Set<Integer> activeLightIds) {
        this.activeLightIds = activeLightIds;

        if (!activeLightIds.contains(boundLightId) && isBoundToCamera) {
            unbindFromCamera();
        }
    }

    public boolean isBoundToCamera() {
        return isBoundToCamera;
    }

    public Map<Integer, Light> getLights() {
        return lights;
    }

    public Set<Integer> getActiveLightIds() {
        return activeLightIds;
    }
}