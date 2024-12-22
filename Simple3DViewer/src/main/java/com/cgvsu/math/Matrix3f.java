package com.cgvsu.math;


public class Matrix3f extends AbstractMatrix {
    private static final int SIZE = 3;

    // Конструкторы + getters and setters
    public Matrix3f(float... elements) {
        super(elements);
    }

    public Matrix3f(int one) {
        super(one);
    }

    public Matrix3f() {
        super();
    }

    public Matrix3f(Matrix3f elements) {
        super(elements);
    }

    public Matrix3f(float[][] elements){
        super(elements);
    }

    @Override
    protected Matrix3f createInstance(float[] elements) {
        return new Matrix3f(elements);
    }

    @Override
    protected AbstractMatrix createInstance(float[][] elements) {
        return new Matrix3f(elements);
    }

    @Override
    protected Matrix3f createInstance() {
        return new Matrix3f();
    }

    @Override
    protected Vector3f instantiateVector() {
        return new Vector3f();
    }

    @Override
    protected int getSize() {
        return SIZE;
    }

    @Override
    public Matrix3f add(AbstractMatrix other) {
        isMatrix3x3(other);
        return (Matrix3f) super.add(other);
    }

    @Override
    public void addV(AbstractMatrix other) {
        isMatrix3x3(other);
        super.addV(other);
    }

    @Override
    public Matrix3f sub(AbstractMatrix other) {
        isMatrix3x3(other);
        return (Matrix3f) super.sub(other);
    }

    @Override
    public void subV(AbstractMatrix other) {
        isMatrix3x3(other);
        super.subV(other);
    }

    // умножение матрицы на столбец - получаем столбец
    @Override
    public Vector3f multiply(AbstractVector other) {
        isVector3f(other);
        return (Vector3f) super.multiply(other);
    }

    @Override
    public void multiply(AbstractMatrix other) {
        isMatrix3x3(other);
        super.multiply(other);
    }

    @Override
    public Matrix3f multiplyNew(AbstractMatrix other) {
        isMatrix3x3(other);
        return (Matrix3f) super.multiplyNew(other);
    }



    @Override
    public Matrix3f transpositionNew() {
        return (Matrix3f) super.transpositionNew();
    }


    /**
     * Вычисление обратной матрицы 3x3
     *
     * @return Обратная матрица 3x3
     * @throws ArithmeticException если матрица вырождена и не имеет обратной
     */
    @Override
    public Matrix3f inverse() {
        return (Matrix3f) (super.inverse());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) { // Итерация по строкам
            for (int j = 0; j < SIZE; j++) { // Итерация по столбцам
                sb.append(String.format("%.2f\t", elements[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void isMatrix3x3(AbstractMatrix other) {
        if (!(other instanceof Matrix3f)) {
            throw new IllegalArgumentException("Invalid matrix type for dot product");
        }
    }

    private void isVector3f(AbstractVector other) {
        if (!(other instanceof Vector3f)) {
            throw new IllegalArgumentException("Invalid vector type for dot product");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix3f)) return false;
        Matrix3f other = (Matrix3f) obj;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Math.abs(this.elements[i][j] - other.elements[i][j]) > 1e-6) {
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

    @Override
    public float getElement(int rows, int col) {
        return elements[rows][col];
    }

    @Override
    public void setElement(int rows, int col, float result) {
        elements[rows][col] = result;
    }
}
