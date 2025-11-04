package lab_5;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Лабораторная работа №5: Многопоточные приложения ===");
            System.out.println("1. Задание 1: Потоки Thread (асинхронная запись/чтение)");
            System.out.println("2. Задание 2: Runnable с семафором (синхронизация write-read)");
            System.out.println("3. Задание 3: Тест синхронизированной обертки");
            System.out.println("0. Выход");
            System.out.print("Выберите задание: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    runTask1();
                    break;
                case 2:
                    runTask2();
                    break;
                case 3:
                    runTask3();
                    break;
                case 0:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Задание 1: Демонстрация работы двух потоков (Thread)
     * Один поток записывает, другой читает (асинхронно)
     */
    private static void runTask1() {
        System.out.println("\n--- Задание 1: Потоки Thread ---");
        System.out.println("Создание объекта с массивом из 100 элементов...\n");

        // Создаем объект с массивом из 100 элементов
        int[] initialData = new int[100];
        MusicCollection collection = new Album("Test Album", 2004, initialData);

        // Создаем и запускаем потоки
        WriterThread writer = new WriterThread(collection);
        ReaderThread reader = new ReaderThread(collection);

        writer.start();
        reader.start();

        // Ждем завершения обоих потоков
        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка ожидания завершения потоков: " + e.getMessage());
        }
    }

    /**
     * Задание 2: Демонстрация работы синхронизированных потоков (Runnable)
     * с использованием самописного семафора
     */
    private static void runTask2() {
        System.out.println("\n--- Задание 2: Runnable с семафором ---");
        System.out.println("Создание объекта с массивом из 20 элементов...\n");

        int[] initialData = new int[20];
        MusicCollection collection = new Album("Sync Album", 2004, initialData);

        // Создаем семафоры: изначально разрешена запись, чтение заблокировано
        Semaphore writeSemaphore = new Semaphore(1);
        Semaphore readSemaphore = new Semaphore(0);

        // Создаем Runnable
        SynchronizedWriterRunnable writer = new SynchronizedWriterRunnable(collection, writeSemaphore, readSemaphore);
        SynchronizedReaderRunnable reader = new SynchronizedReaderRunnable(collection, writeSemaphore, readSemaphore);

        // Создаем потоки
        Thread writerThread = new Thread(writer);
        Thread readerThread = new Thread(reader);

        writerThread.start();
        readerThread.start();

        // Ждем завершения
        try {
            writerThread.join();
            readerThread.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка ожидания завершения потоков: " + e.getMessage());
        }
    }

    /**
     * Задание 3: Демонстрация работы синхронизированной обертки
     */
    private static void runTask3() {
        System.out.println("\n--- Задание 3: Синхронизированная обертка ---");
        System.out.println("Создание объекта и его синхронизированной обертки...\n");

        // Создаем обычный объект
        int[] initialData = new int[50];
        MusicCollection originalCollection = new Album("Original Album", 2004, initialData);

        // Создаем синхронизированную обертку
        MusicCollection syncCollection = MusicCollectionIO.synchronizedMusicCollection(originalCollection);

        System.out.println("Оригинальный объект: " + originalCollection);
        System.out.println("Синхронизированный объект: " + syncCollection + "\n");

        // Тестируем работу в многопоточной среде
        System.out.println("Тестирование работы в многопоточной среде:");
        System.out.println("Запуск 3 потоков, одновременно изменяющих данные...\n");

        Thread thread1 = new Thread(new TestWriterRunnable(syncCollection, 0, 16, 100));
        Thread thread2 = new Thread(new TestWriterRunnable(syncCollection, 17, 33, 200));
        Thread thread3 = new Thread(new TestWriterRunnable(syncCollection, 34, 49, 300));

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка ожидания завершения потоков: " + e.getMessage());
        }

        System.out.println("\nВсе потоки завершены.");
        System.out.println("Проверка целостности данных:");

        // Проверяем, что все данные записаны корректно
        int[] finalData = syncCollection.getTracksData();
        boolean dataIsConsistent = true;

        for (int i = 0; i <= 16; i++) {
            if (finalData[i] != 100) {
                dataIsConsistent = false;
                System.out.println("Ошибка: позиция " + i + " = " + finalData[i] + " (ожидалось 100)");
            }
        }
        for (int i = 17; i <= 33; i++) {
            if (finalData[i] != 200) {
                dataIsConsistent = false;
                System.out.println("Ошибка: позиция " + i + " = " + finalData[i] + " (ожидалось 200)");
            }
        }
        for (int i = 34; i <= 49; i++) {
            if (finalData[i] != 300) {
                dataIsConsistent = false;
                System.out.println("Ошибка: позиция " + i + " = " + finalData[i] + " (ожидалось 300)");
            }
        }

        if (dataIsConsistent) {
            System.out.println("✓ Все данные корректны! Синхронизация работает правильно.");
        } else {
            System.out.println("✗ Обнаружены ошибки в данных!");
        }

        System.out.println("\nЗадание 3 завершено.");
    }

    /**
     * Вспомогательный класс для тестирования синхронизированной обертки
     */
    private static class TestWriterRunnable implements Runnable {
        private MusicCollection collection;
        private int startIndex;
        private int endIndex;
        private int value;

        public TestWriterRunnable(MusicCollection collection, int startIndex, int endIndex, int value) {
            this.collection = collection;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.value = value;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " начал запись значения " + value +
                    " в диапазон [" + startIndex + "-" + endIndex + "]");

            for (int i = startIndex; i <= endIndex; i++) {
                collection.setTrackData(i, value);
            }

            System.out.println(Thread.currentThread().getName() + " завершил запись");
        }
    }
}
