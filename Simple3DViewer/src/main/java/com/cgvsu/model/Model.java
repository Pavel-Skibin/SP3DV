package com.cgvsu.model;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;


public class Model {

    private Texture texture;

    private final ArrayList<Vector2f> textureVertices = new ArrayList<>();
    private final ArrayList<Vector3f> vertices = new ArrayList<>();
    private final ArrayList<Vector3f> normals = new ArrayList<>();
    private final ArrayList<Polygon> polygons = new ArrayList<>();

    private final Vector3f rotate = new Vector3f(0.0f, 0.0f, 0.0f);
    private final Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    private final Vector3f translate = new Vector3f(0.0f, 0.0f, 0.0f);


    public void loadTexture(String texturePath) {
        this.texture = new Texture(texturePath);
    }


    public Texture getTexture() {
        return texture;
    }


    public ArrayList<Vector2f> getTextureVertices() {
        return textureVertices;
    }


    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }


    public ArrayList<Vector3f> getNormals() {
        return normals;
    }


    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }


    public Vector3f getRotate() {
        return rotate;
    }


    public Vector3f getScale() {
        return scale;
    }


    public Vector3f getTranslate() {
        return translate;
    }

    public void setNormals(ArrayList<Vector3f> normals) {
        this.normals.clear();
        this.normals.addAll(normals);
    }

    public void setPolygons(ArrayList<Polygon> polygons) {
        this.polygons.clear();
        this.polygons.addAll(polygons);
    }


    public void setTexture(Texture texture) {
        this.texture = texture;
    }


    public void clearTexture() {
        this.texture = null;
    }
}