package com.cgvsu.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

class ModelTriangulationTest {

    @Test
    void triangulate() {
        Model model = new Model();

        // Добавляем вершины куба
        model.getVertices().add(new Vector3f(-1.0f, -1.0f, 1.0f)); // 0
        model.getVertices().add(new Vector3f(-1.0f, 1.0f, 1.0f));  // 1
        model.getVertices().add(new Vector3f(-1.0f, -1.0f, -1.0f)); // 2
        model.getVertices().add(new Vector3f(-1.0f, 1.0f, -1.0f));  // 3
        model.getVertices().add(new Vector3f(1.0f, -1.0f, 1.0f));  // 4
        model.getVertices().add(new Vector3f(1.0f, 1.0f, 1.0f));   // 5
        model.getVertices().add(new Vector3f(1.0f, -1.0f, -1.0f));  // 6
        model.getVertices().add(new Vector3f(1.0f, 1.0f, -1.0f));   // 7

        // Составляем полигоны (грани куба)
        ArrayList<Integer> face1 = new ArrayList<>(Arrays.asList(0, 1, 2, 3)); // Лицевая грань
        ArrayList<Integer> face2 = new ArrayList<>(Arrays.asList(4, 5, 1, 0)); // Нижняя грань
        ArrayList<Integer> face3 = new ArrayList<>(Arrays.asList(7, 6, 4, 5)); // Верхняя грань
        ArrayList<Integer> face4 = new ArrayList<>(Arrays.asList(3, 2, 6, 7)); // Задняя грань
        ArrayList<Integer> face5 = new ArrayList<>(Arrays.asList(1, 7, 3, 5)); // Правая грань
        ArrayList<Integer> face6 = new ArrayList<>(Arrays.asList(0, 4, 6, 2)); // Левая грань

        // Создаем полигоны
        Polygon polygon1 = new Polygon();
        polygon1.setVertexIndices(face1); // Лицевая грань

        Polygon polygon2 = new Polygon();
        polygon2.setVertexIndices(face2); // Нижняя грань

        Polygon polygon3 = new Polygon();
        polygon3.setVertexIndices(face3); // Верхняя грань

        Polygon polygon4 = new Polygon();
        polygon4.setVertexIndices(face4); // Задняя грань

        Polygon polygon5 = new Polygon();
        polygon5.setVertexIndices(face5); // Правая грань

        Polygon polygon6 = new Polygon();
        polygon6.setVertexIndices(face6); // Левая грань

        model.getPolygons().add(polygon1);
        model.getPolygons().add(polygon2);
        model.getPolygons().add(polygon3);
        model.getPolygons().add(polygon4);
        model.getPolygons().add(polygon5);
        model.getPolygons().add(polygon6);

        // Выполним триангуляцию через Triangulator
        ArrayList<Polygon> triangulatedPolygons = Triangulator.triangulate(model);

        // Ожидаем, что каждая грань будет разделена на два треугольника.
        // Куб имеет 6 граней, каждая из которых изначально является четырехугольником,
        // и каждая будет разделена на два треугольника.

        assertEquals(12, triangulatedPolygons.size()); // 6 граней * 2 треугольника на грань

        // Проверяем, что треугольники создаются правильно
        for (Polygon polygon : triangulatedPolygons) {
            assertTrue(polygon.isTriangle(), "Polygon is not a triangle");
            assertEquals(3, polygon.getVertexIndices().size(), "Polygon doesn't have 3 vertices");
        }
    }
}
