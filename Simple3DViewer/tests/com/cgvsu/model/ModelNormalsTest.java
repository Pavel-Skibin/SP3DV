package com.cgvsu.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.cgvsu.math.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

class ModelNormalsTest {

    @Test
    void calculateNormals() {
        Model model = new Model();

        model.getVertices().add(new Vector3f(-1.0f, -1.0f, 1.0f)); // 0
        model.getVertices().add(new Vector3f(-1.0f, 1.0f, 1.0f));  // 1
        model.getVertices().add(new Vector3f(-1.0f, -1.0f, -1.0f)); // 2
        model.getVertices().add(new Vector3f(-1.0f, 1.0f, -1.0f));  // 3
        model.getVertices().add(new Vector3f(1.0f, -1.0f, 1.0f));  // 4
        model.getVertices().add(new Vector3f(1.0f, 1.0f, 1.0f));   // 5
        model.getVertices().add(new Vector3f(1.0f, -1.0f, -1.0f));  // 6
        model.getVertices().add(new Vector3f(1.0f, 1.0f, -1.0f));   // 7





        // Составляем полигоны (грани куба)
        ArrayList<Integer> face1 = new ArrayList<>(Arrays.asList(0, 1, 3, 2));
        ArrayList<Integer> face2 = new ArrayList<>(Arrays.asList(2, 3, 7, 6));
        ArrayList<Integer> face3 = new ArrayList<>(Arrays.asList(6, 7, 5, 4));
        ArrayList<Integer> face4 = new ArrayList<>(Arrays.asList(4, 5, 1, 0));
        ArrayList<Integer> face5 = new ArrayList<>(Arrays.asList(2, 6, 4, 0));
        ArrayList<Integer> face6 = new ArrayList<>(Arrays.asList(7, 3, 1, 5));

        // Создаем полигоны и устанавливаем индексы вершин для каждой грани
        Polygon polygon1 = new Polygon();
        polygon1.setVertexIndices(face1);

        Polygon polygon2 = new Polygon();
        polygon2.setVertexIndices(face2);

        Polygon polygon3 = new Polygon();
        polygon3.setVertexIndices(face3);

        Polygon polygon4 = new Polygon();
        polygon4.setVertexIndices(face4);

        Polygon polygon5 = new Polygon();
        polygon5.setVertexIndices(face5);

        Polygon polygon6 = new Polygon();
        polygon6.setVertexIndices(face6);

        model.getPolygons().add(polygon1);
        model.getPolygons().add(polygon2);
        model.getPolygons().add(polygon3);
        model.getPolygons().add(polygon4);
        model.getPolygons().add(polygon5);
        model.getPolygons().add(polygon6);


        model.setNormals(NormalCalculator.calculateNormals(model));


        // Нормали для каждой вершины, используя корень из 3
        float sqrt3 = (float) Math.sqrt(3);

        // Ожидаемые нормали для каждой вершины (рассчитаны вручную)
        Vector3f expectedResult1 = new Vector3f(-sqrt3 / 3, -sqrt3 / 3, sqrt3 / 3); // Вершина 0
        Vector3f expectedResult2 = new Vector3f(-sqrt3 / 3, sqrt3 / 3, sqrt3 / 3); // Вершина 1
        Vector3f expectedResult3 = new Vector3f(-sqrt3 / 3, -sqrt3 / 3, -sqrt3 / 3); // Вершина 2
        Vector3f expectedResult4 = new Vector3f(-sqrt3 / 3, sqrt3 / 3, -sqrt3 / 3); // Вершина 3
        Vector3f expectedResult5 = new Vector3f(sqrt3 / 3, -sqrt3 / 3, sqrt3 / 3); // Вершина 4
        Vector3f expectedResult6 = new Vector3f(sqrt3 / 3, sqrt3 / 3, sqrt3 / 3); // Вершина 5
        Vector3f expectedResult7 = new Vector3f(sqrt3 / 3, -sqrt3 / 3, -sqrt3 / 3); // Вершина 6
        Vector3f expectedResult8 = new Vector3f(sqrt3 / 3, sqrt3 / 3, -sqrt3 / 3); // Вершина 7

        ArrayList<Vector3f> expectedResult = new ArrayList<>(Arrays.asList(
                expectedResult1, expectedResult2, expectedResult3, expectedResult4,
                expectedResult5, expectedResult6, expectedResult7, expectedResult8
        ));

        // Проверяем, что нормали вершин совпадают с ожидаемыми значениями
        for (int i = 0; i < model.getNormals().size(); i++) {

            assertEquals(expectedResult.get(i).getX(), model.getNormals().get(i).getX(), 0.0001);
            assertEquals(expectedResult.get(i).getY(), model.getNormals().get(i).getY(), 0.0001);
            assertEquals(expectedResult.get(i).getZ(), model.getNormals().get(i).getZ(), 0.0001);
        }
    }
}
