package com.cgvsu.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class ObjWriterTest {
    private ObjWriter objWriter = new ObjWriter();

    @Test
    public void testWriteVerticesToObjFile() throws IOException {
        ArrayList<Vector3f> vertices = new ArrayList<>();
        vertices.add(new Vector3f(1.0f, 1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("ObjWriterTestsResults\\Test writeVerticesToObjFile.obj")) {
            objWriter.writeVerticesOfModel(printWriter, vertices);
        }

        String fileContent = Files.readString(Path.of("ObjWriterTestsResults\\Test writeVerticesToObjFile.obj"));

        Assertions.assertTrue(fileContent.contains("v 1.0 1.0 1.0"));
    }

    @Test
    public void testWriteTextureVerticesToObjFile() throws IOException {
        ArrayList<Vector2f> textureVertices = new ArrayList<>();
        textureVertices.add(new Vector2f(1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("ObjWriterTestsResults\\Test writeTextureVerticesToObjFile.obj")) {
            objWriter.writeTextureVerticesOfModel(printWriter, textureVertices);
        }

        String fileContent = Files.readString(Path.of("ObjWriterTestsResults\\Test writeTextureVerticesToObjFile.obj"));

        Assertions.assertTrue(fileContent.contains("vt 1.0 1.0"));
    }

    @Test
    public void testWriteNormalsToObjFile() throws IOException {
        ArrayList<Vector3f> normals = new ArrayList<>();
        normals.add(new Vector3f(1.0f, 1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("ObjWriterTestsResults\\Test writeNormalsToObjFile.obj")) {
            objWriter.writeNormalsOfModel(printWriter, normals);
        }

        String fileContent = Files.readString(Path.of("ObjWriterTestsResults\\Test writeNormalsToObjFile.obj"));

        Assertions.assertTrue(fileContent.contains("vn 1.0 1.0 1.0"));
    }

    @Test
    public void testWritePolygonsToObjFile() throws IOException {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(1, 2, 3)));
        polygon.setTextureVertexIndices(new ArrayList<>(Arrays.asList(4, 5, 6)));
        polygon.setNormalIndices(new ArrayList<>(Arrays.asList(7, 8, 9)));
        polygons.add(polygon);
        try (PrintWriter printWriter = new PrintWriter("ObjWriterTestsResults\\Test writePolygonsToObjFile.obg")) {
            objWriter.writePolygonsOfModel(printWriter, polygons);
        }

        String fileContent = Files.readString(Path.of("ObjWriterTestsResults\\Test writePolygonsToObjFile.obg"));
        Assertions.assertTrue(fileContent.contains("f 2/5/8 3/6/9 4/7/10"));
    }
}
