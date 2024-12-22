package com.cgvsu.render;


import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.model.Texture;
import com.cgvsu.model.Triangle;
import com.cgvsu.render_engine.RenderContext;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.RenderParameters;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RenderEngineTest {
    private RenderContext context;
    private RenderParameters parameters;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    @BeforeEach
    void setUp() {
        float[] zBuffer = new float[WIDTH * HEIGHT];
        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
        int[] colorBuffer = new int[WIDTH * HEIGHT];
        context = new RenderContext(WIDTH, HEIGHT, zBuffer, colorBuffer);
        parameters = new RenderParameters();
        parameters.setEnableFillPolygon(true);
    }

    @Test
    void testTriangleRasterization() {

        Triangle triangle = new Triangle(
                new Point2f(40, 40),
                new Point2f(60, 40),
                new Point2f(50, 60),
                0.5f, 0.5f, 0.5f,
                new Vector3f(0, 0, 1),
                new Vector3f(0, 0, 1),
                new Vector3f(0, 0, 1),
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(0.5f, 1)
        );


        try {
            java.lang.reflect.Method method = RenderEngine.class.getDeclaredMethod(
                    "rasterizeTriangleOptimized",
                    Triangle.class,
                    Texture.class,
                    RenderContext.class,
                    RenderParameters.class
            );
            method.setAccessible(true);
            method.invoke(null, triangle, null, context, parameters);


            boolean foundColoredPixels = false;
            for (int i = 0; i < context.getColorBuffer().length; i++) {
                if (context.getColorBuffer()[i] != 0) {
                    foundColoredPixels = true;
                    break;
                }
            }
            assertTrue(foundColoredPixels, "Треугольник должен содержать закрашенные пиксели");
        } catch (Exception e) {
            fail("Ошибка при вызове метода рендеринга треугольника: " + e.getMessage());
        }
    }

    @Test
    void testZBufferUpdates() {

        Triangle frontTriangle = new Triangle(
                new Point2f(40, 40),
                new Point2f(60, 40),
                new Point2f(50, 60),
                0.3f, 0.3f, 0.3f,
                null, null, null,
                null, null, null
        );

        Triangle backTriangle = new Triangle(
                new Point2f(40, 40),
                new Point2f(60, 40),
                new Point2f(50, 60),
                0.7f, 0.7f, 0.7f,
                null, null, null,
                null, null, null
        );

        try {
            java.lang.reflect.Method method = RenderEngine.class.getDeclaredMethod(
                    "rasterizeTriangleOptimized",
                    Triangle.class,
                    Texture.class,
                    RenderContext.class,
                    RenderParameters.class
            );
            method.setAccessible(true);


            method.invoke(null, backTriangle, null, context, parameters);


            float[] zBufferAfterBack = context.getZBuffer().clone();


            method.invoke(null, frontTriangle, null, context, parameters);


            boolean zBufferUpdated = false;
            for (int i = 0; i < context.getZBuffer().length; i++) {
                if (context.getZBuffer()[i] != zBufferAfterBack[i] &&
                        context.getZBuffer()[i] < zBufferAfterBack[i]) {
                    zBufferUpdated = true;
                    break;
                }
            }
            assertTrue(zBufferUpdated, "Z-буфер должен обновиться для переднего треугольника");
        } catch (Exception e) {
            fail("Ошибка при тестировании z-буфера: " + e.getMessage());
        }
    }

    @Test
    void testPolygonalGridRendering() {

        List<Integer> vertexIndices = Arrays.asList(0, 1, 2, 3);
        Polygon polygon = new Polygon();
        polygon.setVertexIndices(vertexIndices);

        Vector3f[] transformedVertices = new Vector3f[]{
                new Vector3f(-0.5f, -0.5f, 0.0f),
                new Vector3f(0.5f, -0.5f, 0.0f),
                new Vector3f(0.5f, 0.5f, 0.0f),
                new Vector3f(-0.5f, 0.5f, 0.0f)
        };

        parameters.setEnablePolygonalGrid(true);
        parameters.setPolygonalGridColor(Color.BLUE);

        try {
            java.lang.reflect.Method method = RenderEngine.class.getDeclaredMethod(
                    "drawModelPolygonalGrid",
                    Polygon.class,
                    int.class,
                    RenderContext.class,
                    Vector3f[].class,
                    RenderParameters.class
            );
            method.setAccessible(true);
            method.invoke(null, polygon, 1, context, transformedVertices, parameters);


            boolean gridLinesPresent = false;
            for (int color : context.getColorBuffer()) {
                if (color != 0) {
                    gridLinesPresent = true;
                    break;
                }
            }
            assertTrue(gridLinesPresent, "Должны быть отрисованы линии сетки");
        } catch (Exception e) {
            fail("Ошибка при тестировании отрисовки сетки: " + e.getMessage());
        }
    }

    @Test
    void testColorBufferUpdates() {

        parameters.setDefaultFillColor(Color.RED);
        Triangle triangle = new Triangle(
                new Point2f(40, 40),
                new Point2f(60, 40),
                new Point2f(50, 60),
                0.5f, 0.5f, 0.5f,
                null, null, null,
                null, null, null
        );

        try {
            java.lang.reflect.Method method = RenderEngine.class.getDeclaredMethod(
                    "rasterizeTriangleOptimized",
                    Triangle.class,
                    Texture.class,
                    RenderContext.class,
                    RenderParameters.class
            );
            method.setAccessible(true);
            method.invoke(null, triangle, null, context, parameters);


            boolean correctColorsPresent = false;
            for (int color : context.getColorBuffer()) {
                if (color != 0) {
                    int red = (color >> 16) & 0xFF;
                    correctColorsPresent = red > 0;
                    break;
                }
            }
            assertTrue(correctColorsPresent, "Пиксели должны быть окрашены в красный цвет");
        } catch (Exception e) {
            fail("Ошибка при тестировании буфера цвета: " + e.getMessage());
        }
    }

    @Test
    void testEdgeDrawing() {
        Point2f p0 = new Point2f(30, 30);
        Point2f p1 = new Point2f(70, 70);
        float z0 = 0.5f;
        float z1 = 0.5f;

        try {
            java.lang.reflect.Method method = RenderEngine.class.getDeclaredMethod(
                    "updateBuffersForEdge",
                    Point2f.class,
                    Point2f.class,
                    float.class,
                    float.class,
                    int.class,
                    RenderContext.class,
                    RenderParameters.class
            );
            method.setAccessible(true);
            method.invoke(null, p0, p1, z0, z1, 1, context, parameters);


            boolean linePresent = false;
            for (int i = 0; i < context.getColorBuffer().length; i++) {
                if (context.getColorBuffer()[i] != 0) {
                    linePresent = true;
                    break;
                }
            }
            assertTrue(linePresent, "Линия должна быть отрисована");
        } catch (Exception e) {
            fail("Ошибка при тестировании отрисовки линии: " + e.getMessage());
        }
    }


}