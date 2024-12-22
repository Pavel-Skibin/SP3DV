package com.cgvsu.math;

//todo тот же прикол
public class Matrix4f extends AbstractMatrix {
    private static final int SIZE = 4;
    // Конструкторы + getters and setters

    public Matrix4f(float[][] elements){
        super(elements);
    }

    public Matrix4f(float... elements) {
        super(elements);
    }

    public Matrix4f(Matrix4f elements) {
        super(elements);
    }

    public Matrix4f(int one) {
        super(1);
    }

    public Matrix4f() {
        super();
    }

    @Override
    public Matrix4f add(AbstractMatrix other) {
        isMatrix4x4(other);
        return (Matrix4f) super.add(other);
    }

    @Override
    public void addV(AbstractMatrix other) {
        isMatrix4x4(other);
        super.addV(other);
    }

    @Override
    protected Matrix4f createInstance(float[] elements) {
        return new Matrix4f(elements);
    }

    @Override
    protected AbstractMatrix createInstance(float[][] elements) {
        return new Matrix4f(elements);
    }

    @Override
    protected Matrix4f createInstance() {
        return new Matrix4f();
    }


    @Override
    protected Vector4f instantiateVector() {
        return new Vector4f();
    }

    @Override
    protected int getSize() {
        return SIZE;
    }

    @Override
    public Matrix4f sub(AbstractMatrix other) {
        isMatrix4x4(other);
        return (Matrix4f) super.sub(other);
    }

    @Override
    public void subV(AbstractMatrix other) {
        isMatrix4x4(other);
        super.subV(other);
    }

    @Override
    public Vector4f multiply(AbstractVector other) {
        isVector4f(other);
        return (Vector4f) super.multiply(other);
    }

    @Override
    public void multiply(AbstractMatrix other) {
        isMatrix4x4(other);
        super.multiply(other);
    }

    @Override
    public Matrix4f multiplyNew(AbstractMatrix other) {
        isMatrix4x4(other);
        return (Matrix4f) super.multiplyNew(other);
    }


    @Override
    public Matrix4f transpositionNew() {
        return (Matrix4f) super.transpositionNew();
    }

    @Override
    public Matrix4f inverse() {
        return (Matrix4f) super.inverse();
    }

    @Override
    public float getElement(int rows, int col) {
        return elements[rows][col];
    }

    @Override
    public void setElement(int rows, int col, float result) {
        elements[rows][col] = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (float[] row : elements) {
            for (float elem : row) {
                sb.append(String.format("%.2f\t", elem));
            }
            sb.append("\n");
        }
        boolean a = false;
        return sb.toString();
    }

    private void isMatrix4x4(AbstractMatrix other) {
        if (!(other instanceof Matrix4f)) {
            throw new IllegalArgumentException("Invalid matrix type for dot product");
        }
    }

    private void isVector4f(AbstractVector other) {
        if (!(other instanceof Vector4f)) {
            throw new IllegalArgumentException("Неверный тип вектора для");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix4f)) return false;
        Matrix4f other = (Matrix4f) obj;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Float.compare(this.elements[i][j], other.elements[i][j]) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result = 31 * result + Float.hashCode(elements[i][j]);
            }
        }
        return result;
    }

    // Геттер для элементов матрицы
    public float[][] getElements() {
        return elements;
    }
}
