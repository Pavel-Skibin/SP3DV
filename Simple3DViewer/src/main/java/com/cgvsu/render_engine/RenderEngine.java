package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.model.Texture;

import com.cgvsu.model.Triangle;
import javafx.scene.paint.Color;


import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static com.cgvsu.render_engine.GraphicConveyor.calculateModelViewProjectionMatrix;
import static com.cgvsu.render_engine.GraphicConveyor.vertexToPoint;

/**
 * Рендер-энжин для отображения 3D моделей с использованием JavaFX.
 */
public class RenderEngine {


    public static void render(Camera camera, Model model, RenderContext context, RenderParameters renderParameters) {


        Vector3f[] transformedVertices = transformAllVertices(model, calculateModelViewProjectionMatrix(camera, model.getRotate(), model.getScale(), model.getTranslate()));

        Texture texture = model.getTexture();

        if (renderParameters.isEnableParallelization()) {
            ForkJoinPool.commonPool().submit(() ->
                    model.getPolygons().parallelStream()
                            .filter(polygon -> isPolygonInFrustum(polygon, transformedVertices))
                            .forEach(polygon -> renderPolygonOptimized(polygon, model, texture, context, renderParameters, transformedVertices))
            ).join();
        } else {
            for (Polygon polygon : model.getPolygons()) {
                if (isPolygonInFrustum(polygon, transformedVertices)) {
                    renderPolygonOptimized(polygon, model, texture, context, renderParameters, transformedVertices);
                }
            }
        }
    }


    private static Vector3f[] transformAllVertices(Model model, Matrix4f mvpMatrix) {
        Vector3f[] transformedVertices = new Vector3f[model.getVertices().size()];
        for (int i = 0; i < model.getVertices().size(); i++) {
            transformedVertices[i] = GraphicConveyor.multiplyMatrix4ByVector3(mvpMatrix, model.getVertices().get(i));
        }
        return transformedVertices;
    }


    private static boolean isPolygonInFrustum(Polygon polygon, Vector3f[] transformedVertices) {
        boolean allXLess = true, allXGreater = true;
        boolean allYLess = true, allYGreater = true;
        boolean allZLess = true, allZGreater = true;

        for (int vertexIndex : polygon.getVertexIndices()) {
            Vector3f vertex = transformedVertices[vertexIndex];
            float x = vertex.getX();
            float y = vertex.getY();
            float z = vertex.getZ();

            if (x >= -1) allXLess = false;
            if (x <= 1) allXGreater = false;
            if (y >= -1) allYLess = false;
            if (y <= 1) allYGreater = false;
            if (z >= -1) allZLess = false;
            if (z <= 1) allZGreater = false;
        }

        return !(allXLess || allXGreater || allYLess || allYGreater || allZLess || allZGreater);
    }

    private static void renderPolygonOptimized(Polygon polygon, Model model, Texture texture, RenderContext context, RenderParameters renderParameters, Vector3f[] transformedVertices) {
        int[] vertexIndices = polygon.getVertexIndices().stream().mapToInt(Integer::intValue).toArray();
        int[] textureIndices = polygon.getTextureVertexIndices().stream().mapToInt(Integer::intValue).toArray();

        if (vertexIndices.length < 3) return;

        for (int i = 1; i < vertexIndices.length - 1; i++) {
            int idx0 = vertexIndices[0];
            int idx1 = vertexIndices[i];
            int idx2 = vertexIndices[i + 1];

            Vector3f v0 = transformedVertices[idx0];
            Vector3f v1 = transformedVertices[idx1];
            Vector3f v2 = transformedVertices[idx2];

            Point2f p0 = vertexToPoint(v0, context.getWidth(), context.getHeight());
            Point2f p1 = vertexToPoint(v1, context.getWidth(), context.getHeight());
            Point2f p2 = vertexToPoint(v2, context.getWidth(), context.getHeight());

            Vector2f tex0 = null, tex1 = null, tex2 = null;
            if (renderParameters.isEnableTextureMapping() && texture != null && textureIndices.length >= 3) {
                tex0 = model.getTextureVertices().get(textureIndices[0]);
                tex1 = model.getTextureVertices().get(textureIndices[i]);
                tex2 = model.getTextureVertices().get(textureIndices[i + 1]);
            }

            Vector3f n0 = null, n1 = null, n2 = null;
            if (renderParameters.isEnableInterpolatedLighting() || renderParameters.isEnableFlatLighting()) {
                n0 = model.getNormals().get(idx0);
                n1 = model.getNormals().get(idx1);
                n2 = model.getNormals().get(idx2);
            }

            Triangle triangle = new Triangle(p0, p1, p2, v0.getZ(), v1.getZ(), v2.getZ(), n0, n1, n2, tex0, tex1, tex2);
            rasterizeTriangleOptimized(triangle, texture, context, renderParameters);
        }

        if (renderParameters.isEnablePolygonalGrid()) {
            drawModelPolygonalGrid(polygon, 1, context, transformedVertices, renderParameters);
        }
    }




    private static void rasterizeTriangleOptimized(Triangle triangle, Texture texture, RenderContext context, RenderParameters renderParameters) {
        Point2f p0 = triangle.p0, p1 = triangle.p1, p2 = triangle.p2;
        float z0 = triangle.z0, z1 = triangle.z1, z2 = triangle.z2;
        Vector3f n0 = triangle.n0, n1 = triangle.n1, n2 = triangle.n2;


        int minX = (int) Math.max(0, Math.floor(Math.min(p0.getX(), Math.min(p1.getX(), p2.getX()))));
        int maxX = (int) Math.min(context.getWidth() - 1, Math.ceil(Math.max(p0.getX(), Math.max(p1.getX(), p2.getX()))));
        int minY = (int) Math.max(0, Math.floor(Math.min(p0.getY(), Math.min(p1.getY(), p2.getY()))));
        int maxY = (int) Math.min(context.getHeight() - 1, Math.ceil(Math.max(p0.getY(), Math.max(p1.getY(), p2.getY()))));

        float denom = ((p1.getY() - p2.getY()) * (p0.getX() - p2.getX()) +
                (p2.getX() - p1.getX()) * (p0.getY() - p2.getY()));
        if (denom == 0) return;
        float invDenom = 1.0f / denom;

        Vector3f faceNormal = null;
        if (renderParameters.isEnableFlatLighting() && n0 != null && n1 != null && n2 != null) {
            faceNormal = calculateFaceNormal(n0, n1, n2);
        }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                float[] lambdas = computeBarycentricCoordinates(p0, p1, p2, x, y, invDenom);

                if (lambdas[0] >= 0 && lambdas[1] >= 0 && lambdas[2] >= 0) {
                    int index = y * context.getWidth() + x;
                    float depth = lambdas[0] * z0 + lambdas[1] * z1 + lambdas[2] * z2;

                    if (depth < context.getZBuffer()[index]) {
                        int color = 0;

                        if (renderParameters.isEnableFillPolygon()) {
                            color = computePixelColor(lambdas, triangle,faceNormal, texture, renderParameters);
                        }

                        if (renderParameters.isEnableFillPolygon() && color != 0) {
                            context.updateZBuffer(x, y, depth);
                            context.updateColorBuffer(x, y, color);
                        }
                    }
                }
            }
        }
    }

    private static int computePixelColor(float[] lambdas, Triangle triangle, Vector3f faceNormal,Texture texture, RenderParameters renderParameters) {
        float lambda0 = lambdas[0];
        float lambda1 = lambdas[1];
        float lambda2 = lambdas[2];

        Vector3f n0 = triangle.n0, n1 = triangle.n1, n2 = triangle.n2;
        Vector2f tex0 = triangle.tex0, tex1 = triangle.tex1, tex2 = triangle.tex2;

        float baseR, baseG, baseB;

        if (renderParameters.isEnableTextureMapping() && texture != null && tex0 != null && tex1 != null && tex2 != null) {
            float u = lambda0 * tex0.getX() + lambda1 * tex1.getX() + lambda2 * tex2.getX();
            float v = lambda0 * tex0.getY() + lambda1 * tex1.getY() + lambda2 * tex2.getY();
            Color texColor = texture.getColor(u, v);
            baseR = (float) texColor.getRed();
            baseG = (float) texColor.getGreen();
            baseB = (float) texColor.getBlue();
        } else {

            baseR = (float) renderParameters.getDefaultFillColor().getRed();
            baseG = (float) renderParameters.getDefaultFillColor().getGreen();
            baseB = (float) renderParameters.getDefaultFillColor().getBlue();
        }

        if (renderParameters.isEnableInterpolatedLighting() || renderParameters.isEnableFlatLighting()) {
            Vector3f normal;

            if (renderParameters.isEnableInterpolatedLighting() && n0 != null && n1 != null && n2 != null) {
                normal = interpolateNormal(lambda0, lambda1, lambda2, n0, n1, n2);
                normal.normalize();
            }

            else if (renderParameters.isEnableFlatLighting() && faceNormal != null) {
                normal = faceNormal;
            } else {

                normal = new Vector3f(0, 0, 1);
            }


            float totalR = 0, totalG = 0, totalB = 0;
            List<Light> lights = renderParameters.getLightingManager().getActiveLights();
            for (Light currentLight : lights) {
                float diffuse = Math.max(0, normal.dot(currentLight.getDirection()));
                float intensity = currentLight.getIntensity() * diffuse;


                Color lightColor = currentLight.getColor();
                totalR += (float) (intensity * lightColor.getRed());
                totalG += (float) (intensity * lightColor.getGreen());
                totalB += (float) (intensity * lightColor.getBlue());
            }


            baseR *= totalR;
            baseG *= totalG;
            baseB *= totalB;
        }

        return ((0xFF) << 24) |
                ((int) (clamp(baseR) * 255) << 16) |
                ((int) (clamp(baseG) * 255) << 8) |
                ((int) (clamp(baseB) * 255));
    }




    private static int getRgbaFromColor(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        int alpha = (int) (color.getOpacity() * 255);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }


    private static Color getColorFromRgba(int rgba) {
        int alpha = (rgba >> 24) & 0xFF;
        int red = (rgba >> 16) & 0xFF;
        int green = (rgba >> 8) & 0xFF;
        int blue = rgba & 0xFF;

        return new Color(red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0);
    }


    private static float[] computeBarycentricCoordinates(Point2f p0, Point2f p1, Point2f p2, int x, int y, float invDenom) {
        float lambda0 = ((p1.getY() - p2.getY()) * (x - p2.getX()) +
                (p2.getX() - p1.getX()) * (y - p2.getY())) * invDenom;
        float lambda1 = ((p2.getY() - p0.getY()) * (x - p2.getX()) +
                (p0.getX() - p2.getX()) * (y - p2.getY())) * invDenom;
        float lambda2 = 1.0f - lambda0 - lambda1;

        return new float[]{lambda0, lambda1, lambda2};
    }


    private static Vector3f interpolateNormal(float lambda0, float lambda1, float lambda2,
                                              Vector3f n0, Vector3f n1, Vector3f n2) {
        Vector3f interpolated = new Vector3f(
                lambda0 * n0.getX() + lambda1 * n1.getX() + lambda2 * n2.getX(),
                lambda0 * n0.getY() + lambda1 * n1.getY() + lambda2 * n2.getY(),
                lambda0 * n0.getZ() + lambda1 * n1.getZ() + lambda2 * n2.getZ()
        );
        interpolated.normalize();
        return interpolated;
    }


    private static Vector3f calculateFaceNormal(Vector3f n0, Vector3f n1, Vector3f n2) {
        Vector3f averageNormal = new Vector3f(
                (n0.getX() + n1.getX() + n2.getX()) / 3,
                (n0.getY() + n1.getY() + n2.getY()) / 3,
                (n0.getZ() + n1.getZ() + n2.getZ()) / 3
        );
        averageNormal.normalize();
        return averageNormal;
    }


    private static void drawModelPolygonalGrid(Polygon polygon, int lineThickness, RenderContext context, Vector3f[] transformedVertices, RenderParameters renderParameters) {
        int[] vertexIndices = polygon.getVertexIndices().stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < vertexIndices.length; i++) {
            int idx0 = vertexIndices[i];
            int idx1 = vertexIndices[(i + 1) % vertexIndices.length];  // Следующая вершина (с цикличностью)

            Vector3f v0 = transformedVertices[idx0];
            Vector3f v1 = transformedVertices[idx1];

            Point2f p0 = vertexToPoint(v0, context.getWidth(), context.getHeight());
            Point2f p1 = vertexToPoint(v1, context.getWidth(), context.getHeight());

            updateBuffersForEdge(p0, p1, v0.getZ(), v1.getZ(), lineThickness, context, renderParameters);
        }
    }

    private static void updateBuffersForEdge(Point2f p0, Point2f p1, float z0, float z1, int lineThickness, RenderContext context, RenderParameters renderParameters) {
        int x0 = (int) p0.getX();
        int y0 = (int) p0.getY();
        int x1 = (int) p1.getX();
        int y1 = (int) p1.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            for (int dxOffset = -lineThickness / 2; dxOffset <= lineThickness / 2; dxOffset++) {
                for (int dyOffset = -lineThickness / 2; dyOffset <= lineThickness / 2; dyOffset++) {
                    int nx = x0 + dxOffset;
                    int ny = y0 + dyOffset;

                    if (nx >= 0 && nx < context.getWidth() && ny >= 0 && ny < context.getHeight()) {
                        float t = Math.max(0, Math.min(1, (float) (nx - p0.getX()) / (p1.getX() - p0.getX())));
                        float z = (1 - t) * z0 + t * z1;

                        int index = ny * context.getWidth() + nx;
                        if (z < context.getZBuffer()[index]) {
                            context.updateZBuffer(nx, ny, z);

                            int rgba = getRgbaFromColor(renderParameters.getPolygonalGridColor());

                            context.updateColorBuffer(nx, ny, rgba);
                        }
                    }
                }
            }


            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    private static float clamp(float value) {
        return Math.max(0.0f, Math.min(1.0f, value));
    }




}

