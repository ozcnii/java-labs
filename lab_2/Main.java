
public class Main {
    public static void main(String[] args) {
        System.out.println("--- Демонстрация класса Vector ---");

        // 1. Создание векторов
        System.out.println("\n1. Создание векторов:");
        Vector v1 = new Vector(3);
        System.out.println("Пустой Vector v1: " + v1);

        v1.set(0, 1.0);
        v1.set(1, 2.0);
        v1.set(2, 3.0);
        System.out.println("Vector v1 после установки значений: " + v1);

        Vector v2 = new Vector(3);
        v2.set(0, 4.0);
        v2.set(1, 5.0);
        v2.set(2, 6.0);
        System.out.println("Vector v2: " + v2);

        Vector v3 = new Vector(5);
        v3.set(0, -1.0);
        v3.set(1, 5.0);
        v3.set(2, 0.0);
        v3.set(3, 8.0);
        v3.set(4, 2.0);
        System.out.println("Vector v3: " + v3);

        try {
            new Vector(0);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании вектора с размеров 0: " + e.getMessage());
        }
        try {
            new Vector(-100);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании вектора с размером -100: " + e.getMessage());
        }

        // 2. Доступ к элементам вектора
        System.out.println("\n2. Доступ к элементам:");
        System.out.println("Элемент v1[0]: " + v1.get(0));
        v1.set(0, 10.0);
        System.out.println("v1 после изменения v1[0] на 10.0: " + v1);

        try {
            v1.get(5);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Ошибка при доступе к элементу: " + e.getMessage());
        }

        // 3. Получение "длины" вектора
        System.out.println("\n3. Длина вектора:");
        System.out.println("Длина v1: " + v1.getSize());
        System.out.println("Длина v2: " + v2.getSize());
        System.out.println("Длина v3: " + v3.getSize());

        // 4. Поиск минимального и максимального значений
        System.out.println("\n4. Мин/Макс значения:");
        System.out.println("Минимальное значение в v3: " + v3.getMin());
        System.out.println("Максимальное значение в v3: " + v3.getMax());

        System.out.println("\n5. Сортировка вектора:");
        System.out.println("v3 до сортировки: " + v3);
        v3.sort();
        System.out.println("v3 после сортировки: " + v3);

        // 6. Нахождение евклидовой нормы
        System.out.println("\n6. Евклидова норма:");
        // Для v1: sqrt(10^2 + 2^2 + 3^2) = sqrt(100 + 4 + 9) = sqrt(113) ≈ 10.63
        System.out.println("Евклидова норма v1: " + v1.getEuclideanNorm());
        // Для v2: sqrt(4^2 + 5^2 + 6^2) = sqrt(16 + 25 + 36) = sqrt(77) ≈ 8.77
        System.out.println("Евклидова норма v2: " + v2.getEuclideanNorm());

        // 7. Умножение вектора на число
        System.out.println("\n7. Умножение вектора на число:");
        double scalar = 2.5;
        Vector v1Scaled = v1.multiply(scalar);
        System.out.println("v1 (" + v1 + ") * " + scalar + " = " + v1Scaled);
        // Ожидаемый результат для v1 (10.0, 2.0, 3.0) * 2.5 = (25.0, 5.0, 7.5)

        // 8. Сложение двух векторов (статический метод)
        System.out.println("\n8. Сложение двух векторов:");
        Vector v_sum = Vector.add(v1, v2);
        System.out.println(v1 + " + " + v2 + " = " + v_sum);
        // Ожидаемый результат:
        // v1 (10.0, 2.0, 3.0) + v2 (4.0, 5.0, 6.0) = (14.0, 7.0, 9.0)

        try {
            Vector.add(v1, v3);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при сложении векторов: " + e.getMessage());
        }

        // 9. Нахождение скалярного произведения двух векторов (статический метод)
        System.out.println("\n9. Скалярное произведение двух векторов:");
        double dot_product = Vector.dotProduct(v1, v2);
        System.out.println(v1 + " . " + v2 + " = " + dot_product);
        // Ожидаемый результат для v1 (10.0, 2.0, 3.0) . v2 (4.0, 5.0, 6.0)
        // = (10*4) + (2*5) + (3*6) = 40 + 10 + 18 = 68.0

        try {
            Vector.dotProduct(v1, v3);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при скалярном произведении векторов: " + e.getMessage());
        }
    }
}