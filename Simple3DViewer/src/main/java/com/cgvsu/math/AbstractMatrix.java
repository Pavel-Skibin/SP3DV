package com.cgvsu.math;

public abstract class AbstractMatrix {
    protected float[][] elements;

    // Constructors
    public AbstractMatrix(float... array) {
        int size = this.getSize();
        if (array.length != size * size) {
            throw new IllegalArgumentException("Массив должен содержать ровно " + (size * size) + " элементов.");
        }
        this.elements = new float[size][size];
        int k = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++, k++) {
                this.elements[i][j] = array[k];
            }
        }
    }

    public AbstractMatrix(int one) {
        int size = this.getSize();
        this.elements = new float[size][size];
        for (int i = 0; i < size; i++) {
            this.elements[i][i] = 1;
        }
    }

    public AbstractMatrix() {
        int size = this.getSize();
        this.elements = new float[size][size];
    }

    public AbstractMatrix(float[][] array) {
        int size = this.getSize();
        if (array.length != size || array[0].length != size) {
            throw new IllegalArgumentException("Массив должен содержать ровно " + (size * size) + " элементов.");
        }
        this.elements = array;
    }


    public AbstractMatrix(AbstractMatrix elements) {
        float[][] el = elements.elements;
        if (el.length != getSize() && el[0].length != getSize()) {
            throw new IllegalArgumentException("Массив должен содержать ровно " + (getSize() * getSize()) + " элементов.");
        }
        this.elements = new float[getSize()][getSize()];
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                this.elements[i][j] = elements.getElement(i, j);
            }
        }
    }

    protected abstract AbstractMatrix createInstance(float[] elements);

    protected abstract AbstractMatrix createInstance(float[][] elements);


    protected abstract AbstractMatrix createInstance();

    protected abstract AbstractVector instantiateVector();

    protected abstract int getSize();

    public AbstractMatrix sub(AbstractMatrix other) {
        return subMatrix(other);
    }

    private AbstractMatrix subMatrix(AbstractMatrix other){
        if (other == null || other.getSize() != this.getSize()) {
            throw new IllegalArgumentException("Матрицы должны иметь одинаковый размер.");
        }
        float[] a = new float[this.getSize() * this.getSize()];
        int k = 0;
        for (int i = 0; i < this.getSize(); i++) {
            for (int j = 0; j < this.getSize(); j++, k++) {
                a[k] = this.elements[i][j] - other.getElement(i, j);
            }
        }
        return createInstance(a);
    }

    public void subV(AbstractMatrix other) {
        AbstractMatrix abstractMatrix = subMatrix(other);
        this.elements = abstractMatrix.elements;
    }

    public void transposition() {
        AbstractMatrix res = trans();
        this.elements = res.elements;
    }

    private AbstractMatrix trans(){
        AbstractMatrix result = createInstance(elements);
        for (int i = 0; i < getSize(); i++) {
            for (int j = i + 1; j < getSize(); j++) {
                float temp = elements[i][j];
                result.elements[i][j] = elements[j][i];
                result.elements[j][i] = temp;
            }
        }
        return result;
    }

    public AbstractMatrix transpositionNew() {
        return trans();
    }

    private AbstractMatrix addMatrix(AbstractMatrix other){
        if (other == null || other.getSize() != this.getSize()) {
            throw new IllegalArgumentException("Матрицы должны иметь одинаковый размер.");
        }
        float[] a = new float[this.getSize() * this.getSize()];
        int k = 0;
        for (int i = 0; i < this.getSize(); i++) {
            for (int j = 0; j < this.getSize(); j++, k++) {
                a[k] = this.elements[i][j] + other.getElement(i, j);
            }
        }
        return createInstance(a);
    }

    public AbstractMatrix add(AbstractMatrix other) {
        return addMatrix(other);
    }

    public void addV(AbstractMatrix other) {
        AbstractMatrix abstractMatrix = addMatrix(other);
        this.elements = abstractMatrix.elements;
    }

    /**
     * Я затрахался путаться, я умножаю матрицу на вектор, т.е. A*B = C
     * Где А - матрица, B- вектор {@code other} , С - вектор
     *
     * @param other вектор
     * @return {@code result} новый вектор
     */
    public AbstractVector multiply(AbstractVector other) {
        if (other.components.length != this.elements[0].length) {
            throw new IllegalArgumentException("Кол-во строк должно быть равно кол-ву столбцов");
        }
        AbstractVector result = instantiateVector();
        for (int i = 0; i < getSize(); i++) {
            float res = 0;
            for (int j = 0; j < getSize(); j++) {
                res += (elements[i][j] * other.getNum(j));
            }
            result.setNum(i, res);
        }
        return result;
    }

    /**
     * Я затрахался путаться, я умножаю матрицу на вектор, т.е. A*B = C
     * Где А - матрица this, B - oter, С - вектор
     *
     * @param other матрица, умножаем справа
     */

    private AbstractMatrix mult(AbstractMatrix other){
        if (other == null || other.getSize() != this.getSize()) {
            throw new IllegalArgumentException("Матрицы должны иметь одинаковый размер.");
        }
        AbstractMatrix result = createInstance();
        for (int i = 0; i < getSize(); i++) {
            for (int k = 0; k < getSize(); k++) {
                float res = 0;
                for (int j = 0; j < getSize(); j++) {
                    res += this.elements[i][j] * other.elements[j][k];
                }
                result.elements[i][k] = res;
            }
        }
        return result;
    }

    public void multiply(AbstractMatrix other) {
        AbstractMatrix result = mult(other);
        this.elements = result.elements;
    }
    public AbstractMatrix multiplyNew(AbstractMatrix other) {
        return mult(other);
    }

    private float determinant(float[][] elements, int size) {
        float det = 0;
        if (size == 1) {
            return elements[0][0];
        }
        if (size == 2) {
            return elements[0][0] * elements[1][1] - elements[0][1] * elements[1][0];
        }
        for (int col = 0; col < size; col++) {
            float sign = (col % 2 == 0) ? 1 : -1;
            float[][] minor = getMinorMatrix(elements, 0, col, size - 1);
            det += sign * elements[0][col] * determinant(minor, size - 1);
        }
        return det;
    }

    /**
     * Получение минорной матрицы, исключая указанную строку и столбец
     *
     * @param rowToExclude Строка для исключения
     * @param colToExclude Столбец для исключения
     * @return Минорная матрица
     */

    private float[][] getMinorMatrix(float[][] elements, int rowToExclude, int colToExclude, int newSize) {
        float[][] minorElements = new float[newSize][newSize];
        int n = elements.length;
        for (int i = 0; i < n; i++) {
            if (i == rowToExclude) continue;
            for (int j = 0; j < n; j++) {
                if (j == colToExclude) continue;
                int minorI = (i < rowToExclude) ? i : i - 1;
                int minorJ = (j < colToExclude) ? j : j - 1;
                minorElements[minorI][minorJ] = elements[i][j];
            }
        }
        return minorElements;
    }

    public float determinant() {
        return determinant(this.elements, getSize());
    }


    public void inverseV(){
        AbstractMatrix a = inverse();
        this.elements = a.elements;
    }

    /**
     * Вычисление обратной матрицы
     *
     * @return Обратная матрица
     * @throws ArithmeticException если матрица вырождена и не имеет обратной
     */
    public AbstractMatrix inverse() {
        float det = determinant();
        if (det == 0) {
            throw new ArithmeticException("Матрица вырождена и не имеет обратной.");
        }
        float[][] tilda = computeTildaMatrix();
        float[] inverseElements = new float[getSize() * getSize()];
        int k = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++, k++) {
                inverseElements[k] = tilda[i][j] / det;
            }
        }
        return createInstance(inverseElements);
    }

    /**
     * Вычисление матрицы алгебраических дополнений для матрицы
     *
     * @return Матрица алгебраических дополнений
     */
    private float[][] computeDopMatrix() {
        int size = getSize();
        float[][] dopMatrix = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float sign = (i + j) % 2 == 0 ? 1 : -1;
                float minor = determinant(getMinorMatrix(elements, i, j, size - 1),size-1);
                dopMatrix[i][j] = sign * minor;
            }
        }
        return dopMatrix;
    }

    /**
     * Вычисление матрицы тильда для матрицы
     * @return Матрица присоединённых
     */

    private float[][] computeTildaMatrix() {
        float[][] cofactors = computeDopMatrix();
        float[][] tilda = new float[getSize()][getSize()];
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                tilda[j][i] = cofactors[i][j];
            }
        }
        return tilda;
    }


    public abstract float getElement(int row, int col);

    public abstract void setElement(int row, int col, float value);
}