import java.util.Arrays;

public class Vector {
    private final double[] coordinates;
    private final int size;

    public Vector(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер вектора должен быть положительным.");
        }
        this.size = size;
        this.coordinates = new double[size];
    }

    public double get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Индекс " + index + " находится вне допустимых границ для вектора размера " + size);
        }
        return coordinates[index];
    }

    public void set(int index, double value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Индекс " + index + " находится вне допустимых границ для вектора размера " + size);
        }
        coordinates[index] = value;
    }

    public int getSize() {
        return size;
    }

    public double getMin() {
        double min = coordinates[0];
        for (int i = 1; i < size; i++) {
            if (coordinates[i] < min) {
                min = coordinates[i];
            }
        }
        return min;
    }

    public double getMax() {
        double max = coordinates[0];
        for (int i = 1; i < size; i++) {
            if (coordinates[i] > max) {
                max = coordinates[i];
            }
        }
        return max;
    }

    public void sort() {
        Arrays.sort(coordinates);
    }

    // евклидова норма (длину) вектора
    public double getEuclideanNorm() {
        double sumOfSquares = 0;
        for (double coord : coordinates) {
            sumOfSquares += coord * coord;
        }
        return Math.sqrt(sumOfSquares);
    }

    public void multiply(double scalar) {
        for (int i = 0; i < this.size; i++) {
            coordinates[i] *= scalar;
        }
    }

    public static Vector add(Vector v1, Vector v2) {
        if (v1.size != v2.size) {
            throw new IllegalArgumentException("Векторы должны иметь одинаковую длину для сложения.");
        }
        Vector result = new Vector(v1.size);
        for (int i = 0; i < v1.size; i++) {
            result.coordinates[i] = v1.coordinates[i] + v2.coordinates[i];
        }
        return result;
    }

    // скалярное произведение двух векторов
    public static double dotProduct(Vector v1, Vector v2) {
        if (v1.size != v2.size) {
            throw new IllegalArgumentException("Векторы должны иметь одинаковую длину для скалярного произведения.");
        }
        double product = 0;
        for (int i = 0; i < v1.size; i++) {
            product += v1.coordinates[i] * v2.coordinates[i];
        }
        return product;
    }

    @Override
    public String toString() {
        return "Vector" + Arrays.toString(coordinates);
    }
}