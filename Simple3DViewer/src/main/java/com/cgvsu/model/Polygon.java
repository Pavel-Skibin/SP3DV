package com.cgvsu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет многоугольник в 3D модели, содержащий индексы вершин, текстурных вершин и нормалей.
 */
public class Polygon {
    /**
     * Список индексов вершин многоугольника.
     */
    private List<Integer> vertexIndices;

    /**
     * Список индексов текстурных вершин многоугольника.
     */
    private List<Integer> textureVertexIndices;

    /**
     * Список индексов нормалей многоугольника.
     */
    private List<Integer> normalIndices;

    /**
     * Конструктор для инициализации списков индексов вершин, текстурных вершин и нормалей.
     */
    public Polygon() {
        this.vertexIndices = new ArrayList<>();
        this.textureVertexIndices = new ArrayList<>();
        this.normalIndices = new ArrayList<>();
    }

    /**
     * Устанавливает список индексов вершин многоугольника.
     *
     * @param vertexIndices Список индексов вершин. Должен содержать не менее трех элементов.
     * @throws IllegalArgumentException если количество вершин меньше трех.
     */
    public void setVertexIndices(List<Integer> vertexIndices) {
        if (vertexIndices.size() < 3) {
            throw new IllegalArgumentException("Многоугольник должен содержать не менее трех вершин.");
        }
        this.vertexIndices = new ArrayList<>(vertexIndices);
    }

    /**
     * Устанавливает список индексов текстурных вершин многоугольника.
     *
     * @param textureVertexIndices Список индексов текстурных вершин.
     */
    public void setTextureVertexIndices(List<Integer> textureVertexIndices) {
        this.textureVertexIndices = new ArrayList<>(textureVertexIndices);
    }

    /**
     * Устанавливает список индексов нормалей многоугольника.
     *
     * @param normalIndices Список индексов нормалей.
     */
    public void setNormalIndices(List<Integer> normalIndices) {
        this.normalIndices = new ArrayList<>(normalIndices);
    }

    /**
     * Возвращает список индексов вершин многоугольника.
     *
     * @return Список индексов вершин.
     */
    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    /**
     * Возвращает список индексов текстурных вершин многоугольника.
     *
     * @return Список индексов текстурных вершин.
     */
    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    /**
     * Возвращает список индексов нормалей многоугольника.
     *
     * @return Список индексов нормалей.
     */
    public List<Integer> getNormalIndices() {
        return normalIndices;
    }

    /**
     * Добавляет индекс вершины к многоугольнику.
     *
     * @param index Индекс вершины.
     */
    public void addVertexIndex(int index) {
        vertexIndices.add(index);
    }

    /**
     * Добавляет индекс текстурной вершины к многоугольнику.
     *
     * @param index Индекс текстурной вершины.
     */
    public void addTextureVertexIndex(int index) {
        textureVertexIndices.add(index);
    }

    /**
     * Добавляет индекс нормали к многоугольнику.
     *
     * @param index Индекс нормали.
     */
    public void addNormalIndex(int index) {
        normalIndices.add(index);
    }

    /**
     * Проверяет, является ли многоугольник треугольником.
     *
     * @return {@code true}, если многоугольник имеет три вершины, иначе {@code false}.
     */
    public boolean isTriangle() {
        return vertexIndices.size() == 3;
    }
}