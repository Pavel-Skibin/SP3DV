package com.cgvsu.math;

import java.util.Objects;

/**
 * Класс для работы с двумерными векторами с плавающей точкой.
 * Это заготовка для собственной библиотеки линейной алгебры.
 * Опять, потом буду делать много чего интересного, а пока, спать)
 */
public class Vector2f extends AbstractVector {
    private static final int SIZE = 2;

    public Vector2f(float... components) {
        super(components);
    }
    // Геттеры и сеттеры для компонентов X и Y
    public float getX() {
        return components[0];
    }

    public void setX(float x) {
        components[0] = x;
        calcLength();
    }

    public float getY() {
        return components[1];
    }

    public void setY(float y) {
        components[1] = y;
        calcLength();
    }


    @Override
    protected int getSize() {
        return SIZE;
    }

    @Override
    protected Vector2f instantiateVector(float[] elements) {
        return new Vector2f(elements);
    }


    @Override
    public Vector2f add(AbstractVector other) {
        isVector2f(other);
        return (Vector2f) super.add(other);
    }

    @Override
    public Vector2f sub(AbstractVector other) {
        isVector2f(other);
        return (Vector2f) super.sub(other);
    }

    @Override
    public void subV(AbstractVector other) {
        isVector2f(other);
        super.subV((Vector2f) other);
    }
    @Override
    public void addV(AbstractVector other) {
        isVector2f(other);
        super.addV((Vector2f) other);
    }

    @Override
    public void sub(AbstractVector first, AbstractVector second) {
        isVector2f(first);
        isVector2f(second);
        super.sub(first,second);
    }


    @Override
    public float dot(AbstractVector other) {
        isVector2f(other);
        return super.dot(other);
    }

    /**
     * Проверяет, является ли другой вектор экземпляром Vector2f.
     *
     * @param other Другой вектор для проверки.
     * @return true если другой вектор является Vector2f, иначе false.
     */
    private void isVector2f(AbstractVector other) {
        if(!(other instanceof Vector2f)){
            throw new IllegalArgumentException("Неверный тип вектора для вычитания");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector2f)) return false;
        Vector2f other = (Vector2f) obj;
        return Float.compare(components[0], other.components[0]) == 0 &&
                Float.compare(components[1], other.components[1]) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(components[0], components[1]);
    }

    @Override
    public String toString() {
        return "Vector2f{" +
                "x=" + components[0] +
                ", y=" + components[1] +
                '}';
    }
}