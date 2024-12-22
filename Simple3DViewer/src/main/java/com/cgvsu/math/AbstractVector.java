package com.cgvsu.math;

// todo навести красоту, сейчас мне впадлу...
public abstract class AbstractVector {
    protected float[] components;
    protected float length;

    /**
     * Конструктор для создания вектора с заданными компонентами.
     */

    public AbstractVector(float... components) {
        if (components.length != getSize()) {
            throw new IndexOutOfBoundsException("Неверная длина");
        }
        this.components = components;
        calcLength();
    }

    /**
     * Конструктор для создания нулевого вектора.
     */
    public AbstractVector() {
        components = new float[getSize()];
        for (int i = 0; i < getSize(); i++) {
            this.components[i] = 0;
        }
        calcLength();
    }


    /**
     * Вычисляет длину вектора и сохраняет её.
     */
    protected void calcLength() {
        float res = 0;
        for (int i = 0; i < getSize(); i++) {
            res += (components[i] * components[i]);
        }
        length = (float) Math.sqrt(res);
    }

    protected abstract int getSize();

    protected abstract AbstractVector instantiateVector(float[] elements);

    // Вычисление длины вектора
    public float length() {
        return length;
    }

    // Нормализация вектора

    /**
     * Складывает текущий вектор с другим вектором.
     *
     * @param other Другой вектор для сложения.
     * @return Новый вектор, являющийся суммой двух векторов.
     * @throws IllegalArgumentException если тип другого вектора не Vector2f.
     */
    public AbstractVector add(AbstractVector other) {
        return addVector(other);
    }

    private AbstractVector addVector(AbstractVector other){
        equalsLength(other);
        float[] res = new float[getSize()];
        for (int i = 0; i < getSize(); i++) {
            res[i] = this.components[i] + other.components[i];
        }
        return instantiateVector(res);
    }

    public void addV(AbstractVector other) {
        this.components = addVector(other).components;
    }

    /**
     * Вычитает из текущего вектора другой вектор.
     *
     * @param other Другой вектор для вычитания.
     * @return Новый вектор, являющийся разностью двух векторов.
     * @throws IllegalArgumentException если тип другого вектора не Vector2f.
     */
    private AbstractVector subVector(AbstractVector other){
        equalsLength(other);
        float[] res = new float[getSize()];
        for (int i = 0; i < getSize(); i++) {
            res[i] = this.components[i] - other.components[i];
        }
        return instantiateVector(res);
    }


    public AbstractVector sub(AbstractVector other) {
        return subVector(other);
    }

    /**
     * вычитание с изменением состояния текущего вектора
     *
     * @param other
     */
    public void subV(AbstractVector other) {
        this.components = subVector(other).components;
    }
// todo понять, что это нахуй такое и надо ли оно :? ... Хз что это, но пусть будет.. мало ли
    public void sub(AbstractVector first, AbstractVector second) {
        equalsLength(first);
        equalsLength(second);
        for (int i = 0; i < getSize(); i++) {
            this.components[i] = first.components[i] - second.components[i];
        }
    }


    /**
     * Умножает текущий вектор на скаляр.
     *
     * @param scalar Скаляр для умножения.
     */
    public void multiply(float scalar) {
        for (int i = 0; i < components.length; i++) {
            this.components[i] *= scalar;
        }
        calcLength();
    }

    public AbstractVector multiplyV(float scalar) {
        AbstractVector res = instantiateVector(this.components);
        res.multiply(scalar);
        return res;
    }


    /**
     * Делит текущий вектор на скаляр.
     *
     * @param scalar Скаляр для деления.
     * @throws ArithmeticException если скаляр равен нулю.
     */
    public void divide(float scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        for (int i = 0; i < components.length; i++) {
            this.components[i] /= scalar;
        }
        calcLength();
    }

    public AbstractVector divideV(float scalar) {
        AbstractVector res = instantiateVector(this.components);
        res.divide(scalar);
        return res;
    }



    /**
     * Вычисляет скалярное произведение текущего вектора с другим вектором.
     *
     * @param other Другой вектор для скалярного произведения.
     * @return Значение скалярного произведения.
     * @throws IllegalArgumentException если тип другого вектора не Vector2f.
     */
    public float dot(AbstractVector other) {
        equalsLength(other);
        float res = 0;
        for (int i = 0; i < getSize(); i++) {
            res += (this.components[i] * other.components[i]);
        }
        return res;
    }

    /**
     * Нормализует вектор.
     * Если длина вектора равна нулю, метод ничего не делает.
     */
    public void normalize() {
        calcNormalize();
    } // todo сделать такое же, но чтобы оно возвращало вектор

    public AbstractVector normalizeV() {
        AbstractVector res = instantiateVector(this.components);
        res.normalize();
        return res;
    }

    /**
     * Возвращает компонент вектора по индексу.
     *
     * @param a Индекс компонента (0 для x, 1 для y, 2 для z).
     * @return Значение компонента.
     * @throws IndexOutOfBoundsException если индекс не равен 0, 1 или 2.
     */

    public float getNum(int a) {
        if (a < 0 || a >= components.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + a);
        }
        return components[a];
    }

    /**
     * Заменяет значения для x,y,z по индексу.
     *
     * @param a   Индекс компонента (0 для x, 1 для y, 2 для z).
     * @param num Новое число для переменной
     * @throws IndexOutOfBoundsException если индекс не равен 0, 1 или 2.
     */

    public void setNum(int a, float num) {
        if (a < 0 || a >= components.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + a);
        }
        components[a] = num;
        calcLength();
    }


    private void equalsLength(AbstractVector other) {
        if (this.components.length != other.components.length) {
            throw new IndexOutOfBoundsException("Разная длина");
        }
    }

    private void calcNormalize() {
        calcLength();
        if (length == 0) {
            return;
        }
        divide(length);
    }

}