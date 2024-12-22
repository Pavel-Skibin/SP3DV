package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import javafx.scene.paint.Color;

public class RenderParameters {
    private boolean enableParallelization = true;
    private boolean enablePolygonalGrid = false;
    private boolean enableFillPolygon = false;
    private boolean enableInterpolatedLighting = false;
    private boolean enableFlatLighting = false;
    private boolean enableTextureMapping = false;

    private LightingManager lightingManager;

    private Color polygonalGridColor = Color.BLUE;
    private Color defaultFillColor = Color.LIGHTGREY;
    private float lightingCoefficient = 1.6f;


    public RenderParameters() {
        lightingManager = new LightingManager();


    }

    public boolean isEnableParallelization() {
        return enableParallelization;
    }

    public void setEnableParallelization(boolean enableParallelization) {
        this.enableParallelization = enableParallelization;
    }

    public boolean isEnablePolygonalGrid() {
        return enablePolygonalGrid;
    }

    public void setEnablePolygonalGrid(boolean enablePolygonalGrid) {
        this.enablePolygonalGrid = enablePolygonalGrid;
    }

    public boolean isEnableFillPolygon() {
        return enableFillPolygon;
    }

    public void setEnableFillPolygon(boolean enableFillPolygon) {
        this.enableFillPolygon = enableFillPolygon;
    }

    public boolean isEnableInterpolatedLighting() {
        return enableInterpolatedLighting;
    }

    public void setEnableInterpolatedLighting(boolean enableInterpolatedLighting) {
        this.enableInterpolatedLighting = enableInterpolatedLighting;
    }

    public boolean isEnableFlatLighting() {
        return enableFlatLighting;
    }

    public void setEnableFlatLighting(boolean enableFlatLighting) {
        this.enableFlatLighting = enableFlatLighting;
    }

    public boolean isEnableTextureMapping() {
        return enableTextureMapping;
    }

    public void setEnableTextureMapping(boolean enableTextureMapping) {
        this.enableTextureMapping = enableTextureMapping;
    }

    public Color getPolygonalGridColor() {
        return polygonalGridColor;
    }

    public void setPolygonalGridColor(Color polygonalGridColor) {
        this.polygonalGridColor = polygonalGridColor;
    }

    public float getLightingCoefficient() {
        return lightingCoefficient;
    }

    public void setLightingCoefficient(float lightingCoefficient) {
        this.lightingCoefficient = lightingCoefficient;
    }

    public LightingManager getLightingManager() {
        return lightingManager;
    }

    public void setLightingManager(LightingManager lightingManager) {
        this.lightingManager = lightingManager;
    }



    public Color getDefaultFillColor() {
        return defaultFillColor;
    }

    public void setDefaultFillColor(Color defaultFillColor) {
        this.defaultFillColor = defaultFillColor;
    }
}
