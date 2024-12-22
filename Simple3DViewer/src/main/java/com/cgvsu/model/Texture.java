package com.cgvsu.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * Класс для управления текстурами.
 */
public class Texture {
    private Image image;
    private PixelReader pixelReader;
    private int width;
    private int height;

    /**
     * Конструктор Texture.
     *
     * @param filePath Путь к файлу текстуры.
     */
    public Texture(String filePath) {
        image = new Image("file:" + filePath);
        pixelReader = image.getPixelReader();
        width = (int) image.getWidth();
        height = (int) image.getHeight();
    }

    /**
     * Получает цвет по текстурным координатам (u, v).
     *
     * @param u Координата U (от 0 до 1).
     * @param v Координата V (от 0 до 1).
     * @return Цвет пикселя.
     */
    public Color getColor(float u, float v) {
        u = clamp(u, 0.0f, 1.0f);
        v = clamp(v, 0.0f, 1.0f);

        int x = Math.min((int) (u * (width - 1)), width - 1);
        int y = Math.min((int) ((1 - v) * (height - 1)), height - 1); // Инвертируем V

        return pixelReader.getColor(x, y);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}