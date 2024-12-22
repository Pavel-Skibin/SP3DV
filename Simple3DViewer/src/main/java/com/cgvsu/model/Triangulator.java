package com.cgvsu.model;

import java.util.ArrayList;
import java.util.List;

public class Triangulator {

    public static ArrayList<Polygon> triangulate(Model model) {
        ArrayList<Polygon> triangulatedPolygons = new ArrayList<>();
        ArrayList<Polygon> polygons = model.getPolygons();

        for (Polygon polygon : polygons) {
            if (polygon.isTriangle()) {
                triangulatedPolygons.add(polygon);
            } else {
                List<Integer> vertexIndices = polygon.getVertexIndices();
                List<Integer> textureIndices = polygon.getTextureVertexIndices();
                List<Integer> normalIndices = polygon.getNormalIndices();

                for (int i = 1; i < vertexIndices.size() - 1; i++) {
                    Polygon triangle = new Polygon();

                    triangle.addVertexIndex(vertexIndices.get(0));
                    triangle.addVertexIndex(vertexIndices.get(i));
                    triangle.addVertexIndex(vertexIndices.get(i + 1));

                    if (!textureIndices.isEmpty()) {
                        if (i + 1 < textureIndices.size()) {
                            triangle.addTextureVertexIndex(textureIndices.get(0));
                            triangle.addTextureVertexIndex(textureIndices.get(i));
                            triangle.addTextureVertexIndex(textureIndices.get(i + 1));
                        } else {
                            triangle.addTextureVertexIndex(textureIndices.get(0));
                            triangle.addTextureVertexIndex(textureIndices.get(i % textureIndices.size()));
                            triangle.addTextureVertexIndex(textureIndices.get((i + 1) % textureIndices.size()));
                        }
                    }

                    if (!normalIndices.isEmpty()) {
                        if (i + 1 < normalIndices.size()) {
                            triangle.addNormalIndex(normalIndices.get(0));
                            triangle.addNormalIndex(normalIndices.get(i));
                            triangle.addNormalIndex(normalIndices.get(i + 1));
                        } else {
                            triangle.addNormalIndex(normalIndices.get(0));
                            triangle.addNormalIndex(normalIndices.get(i % normalIndices.size()));
                            triangle.addNormalIndex(normalIndices.get((i + 1) % normalIndices.size()));
                        }
                    }

                    triangulatedPolygons.add(triangle);
                }
            }
        }

        return triangulatedPolygons;
    }
}
