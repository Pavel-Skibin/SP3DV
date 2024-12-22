package com.cgvsu.scene;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cgvsu.render_engine.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import static com.cgvsu.render_engine.RenderEngine.render;

public class Scene {
    private List<Model> models;
    private List<Model> selectedModels;
    private float[] zBuffer;
    private int[] colorBuffer;

    public Scene() {
        models = new ArrayList<>();
        selectedModels = new ArrayList<>();
    }

    public List<Model> getModels() {
        return models;
    }

    public List<Model> getSelectedModels() {
        return selectedModels;
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public void removeModel(Model model) {
        models.remove(model);
    }

    public void selectModel(Model model) {
        if (!selectedModels.contains(model)) {
            selectedModels.add(model);
        }
    }

    public void deselectModel(Model model) {
        selectedModels.remove(model);
    }


    public void renderScene(GraphicsContext graphicsContext, Camera camera,  RenderParameters renderParameters, double width, double height) {

        int w = (int) width;
        int h = (int) height;
        if (zBuffer == null || zBuffer.length != w * h) {
            zBuffer = new float[w * h];
            colorBuffer = new int[w * h];
        }
        Arrays.fill(zBuffer, Float.MAX_VALUE);
        Arrays.fill(colorBuffer, 0);
         RenderContext renderContext = new RenderContext( w, h, zBuffer, colorBuffer);



        for (Model model : models) {
            render(camera,model,renderContext,renderParameters);

        }




        WritableImage writableImage = new WritableImage(w, h);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        pixelWriter.setPixels(0, 0, w, h, PixelFormat.getIntArgbInstance(), colorBuffer, 0, w);
        graphicsContext.drawImage(writableImage, 0, 0);

    }
}
