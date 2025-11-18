package lab_6;

import java.util.Scanner;

import lab_6.exceptions.EmptyCollectionException;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Лабораторная работа №6: Паттерны проектирования ===");
            System.out.println("1. Задание 1: Сортировка через Comparable");
            System.out.println("2. Задание 2: Сортировка через Comparator");
            System.out.println("3. Задание 3: Тест Iterator (while и for-each)");
            System.out.println("4. Задание 4: Тест Unmodifiable декоратора");
            System.out.println("5. Задания 5-7: Тест Фабричного метода");
            System.out.println("0. Выход");
            System.out.print("Выберите задание: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    testTask1();
                    break;
                case 2:
                    testTask2();
                    break;
                case 3:
                    testTask3();
                    break;
                case 4:
                    testTask4();
                    break;
                case 5:
                    testTask567();
                    break;
                case 0:
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void testTask1() {
        System.out.println("\n--- Задание 1: Сортировка через Comparable (по возрастанию метрики) ---");

        MusicCollection[] collections = new MusicCollection[5];
        collections[0] = MusicCollectionIO.createInstance();
        collections[0].setTitle("Album A");
        collections[0].setIdentifier(2000);
        collections[0].setTracksData(new int[] { 100, 200 });

        collections[1] = MusicCollectionIO.createInstance();
        collections[1].setTitle("Album B");
        collections[1].setIdentifier(2005);
        collections[1].setTracksData(new int[] { 50 });

        collections[2] = MusicCollectionIO.createInstance();
        collections[2].setTitle("Album C");
        collections[2].setIdentifier(2010);
        collections[2].setTracksData(new int[] { 150, 150, 150 });

        collections[3] = MusicCollectionIO.createInstance();
        collections[3].setTitle("Album D");
        collections[3].setIdentifier(1995);
        collections[3].setTracksData(new int[] { 80, 90 });

        collections[4] = MusicCollectionIO.createInstance();
        collections[4].setTitle("Album E");
        collections[4].setIdentifier(2015);
        collections[4].setTracksData(new int[] { 200, 200, 200 });

        System.out.println("До сортировки:");
        printCollections(collections);

        MusicCollectionIO.sortMusicCollection(collections);

        System.out.println("\nПосле сортировки (по возрастанию метрики):");
        printCollections(collections);
    }

    private static void testTask2() {
        System.out.println("\n--- Задание 2: Сортировка через Comparator ---");

        MusicCollection[] collections = new MusicCollection[5];
        collections[0] = MusicCollectionIO.createInstance();
        collections[0].setTitle("Album A");
        collections[0].setIdentifier(2000);
        collections[0].setTracksData(new int[] { 100, 200 });

        collections[1] = MusicCollectionIO.createInstance();
        collections[1].setTitle("Album B");
        collections[1].setIdentifier(2005);
        collections[1].setTracksData(new int[] { 50 });

        collections[2] = MusicCollectionIO.createInstance();
        collections[2].setTitle("Album C");
        collections[2].setIdentifier(2010);
        collections[2].setTracksData(new int[] { 150, 150, 150 });

        collections[3] = MusicCollectionIO.createInstance();
        collections[3].setTitle("Album D");
        collections[3].setIdentifier(1995);
        collections[3].setTracksData(new int[] { 80, 90 });

        collections[4] = MusicCollectionIO.createInstance();
        collections[4].setTitle("Album E");
        collections[4].setIdentifier(2015);
        collections[4].setTracksData(new int[] { 200, 200, 200 });

        System.out.println("До сортировки:");
        printCollections(collections);

        MusicCollectionIO.sortMusicCollection(collections, new DescendingMetricComparator());
        System.out.println("\nПосле сортировки по убыванию метрики:");
        printCollections(collections);

        MusicCollectionIO.sortMusicCollection(collections, new AscendingIdentifierComparator());
        System.out.println("\nПосле сортировки по возрастанию identifier:");
        printCollections(collections);
    }

    private static void testTask3() {
        System.out.println("\n--- Задание 3: Тест Iterator ---");

        MusicCollection collection = MusicCollectionIO.createInstance();
        collection.setTitle("Test Album");
        collection.setIdentifier(2020);
        collection.setTracksData(new int[] { 100, 200, 300, 400, 500 });

        System.out.println("Коллекция: " + collection);

        System.out.println("\nИтерация с помощью while:");
        java.util.Iterator<Integer> iterator = collection.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();

        System.out.println("\nИтерация с помощью for-each:");
        for (int value : collection) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private static void testTask4() {
        System.out.println("\n--- Задание 4: Тест Unmodifiable декоратора ---");

        MusicCollection original = MusicCollectionIO.createInstance();
        original.setTitle("Original Album");
        original.setIdentifier(2020);
        original.setTracksData(new int[] { 100, 200, 300 });

        System.out.println("Оригинал: " + original);

        MusicCollection unmodifiable = MusicCollectionIO.unmodifiableMusicCollection(original);
        System.out.println("Unmodifiable: " + unmodifiable);

        System.out.println("\nПопытка изменить title:");
        try {
            unmodifiable.setTitle("New Title");
            System.out.println("Изменение прошло успешно");
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException - правильно!");
        }

        System.out.println("\nПопытка изменить identifier:");
        try {
            unmodifiable.setIdentifier(2025);
            System.out.println("Изменение прошло успешно");
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException - правильно!");
        }

        System.out.println("\nПопытка изменить tracksData:");
        try {
            unmodifiable.setTracksData(new int[] { 1, 2, 3 });
            System.out.println("Изменение прошло успешно");
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException - правильно!");
        }

        System.out.println("\nПопытка изменить отдельный элемент:");
        try {
            unmodifiable.setTrackData(0, 999);
            System.out.println("Изменение прошло успешно");
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException - правильно!");
        }

        System.out.println("\nЧтение данных работает:");
        System.out.println("Title: " + unmodifiable.getTitle());
        System.out.println("Identifier: " + unmodifiable.getIdentifier());
        System.out.println("Track 0: " + unmodifiable.getTrackData(0));
    }

    private static void testTask567() {
        System.out.println("\n--- Задания 5-7: Тест Фабричного метода ---");

        System.out.println("\nТекущая фабрика: AlbumFactory (по умолчанию)");
        MusicCollection obj1 = MusicCollectionIO.createInstance();
        System.out.println("Создан объект: " + obj1.getClass().getSimpleName());

        System.out.println("\nСмена фабрики на PlaylistFactory:");
        MusicCollectionIO.setMusicCollectionFactory(new PlaylistFactory());
        MusicCollection obj2 = MusicCollectionIO.createInstance();
        System.out.println("Создан объект: " + obj2.getClass().getSimpleName());

        System.out.println("\nСмена фабрики обратно на AlbumFactory:");
        MusicCollectionIO.setMusicCollectionFactory(new AlbumFactory());
        MusicCollection obj3 = MusicCollectionIO.createInstance();
        System.out.println("Создан объект: " + obj3.getClass().getSimpleName());

        System.out.println("\nВсе объекты созданы через MusicCollectionIO.createInstance()");
    }

    private static void printCollections(MusicCollection[] collections) {
        for (MusicCollection c : collections) {
            try {
                System.out.printf("%s - метрика: %.2f, identifier: %d%n",
                        c.getTitle(), c.calculateMetric(), c.getIdentifier());
            } catch (EmptyCollectionException e) {
                System.out.println(c.getTitle() + " - ошибка: " + e.getMessage());
            }
        }
    }
}
