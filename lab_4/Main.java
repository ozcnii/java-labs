package lab_4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import lab_4.exceptions.InvalidTrackDataException;

public class Main {

    private static final String FILE_SERIALIZED = "collections_serialized.ser";
    private static final String FILE_TEXT = "collections_text.txt";
    private static final String FILE_BINARY = "collections_binary.dat";
    private static final String FILE_FORMATTED = "collections_formatted.txt";

    private static Scanner scanner = new Scanner(System.in);
    private static List<MusicCollection> collections = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа №4: Работа с потоками данных ===\n");

        loadExistingData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Выберите действие: ");
            System.out.println();

            switch (choice) {
                case 1:
                    fillCollections();
                    break;
                case 2:
                    testBinaryIO();
                    break;
                case 3:
                    testTextIO();
                    break;
                case 4:
                    testSerialization();
                    break;
                case 5:
                    testFormattedIO();
                    break;
                case 6:
                    displayCollections();
                    break;
                case 0:
                    running = false;
                    System.out.println("Программа завершена.");
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("╔═════════════════════════════════════════════════════╗");
        System.out.println("║                     МЕНЮ                            ║");
        System.out.println("╠═════════════════════════════════════════════════════╣");
        System.out.println("║ 1. Заполнить базу элементов с консоли               ║");
        System.out.println("║ 2. Тест байтового ввода/вывода                      ║");
        System.out.println("║ 3. Тест текстового ввода/вывода                     ║");
        System.out.println("║ 4. Тест сериализации/десериализации                 ║");
        System.out.println("║ 5. Тест форматного текстового ввода/вывода          ║");
        System.out.println("║ 6. Показать текущую базу элементов                  ║");
        System.out.println("║ 0. Выход                                            ║");
        System.out.println("╚═════════════════════════════════════════════════════╝");
    }

    private static void loadExistingData() {
        File serFile = new File(FILE_SERIALIZED);
        File textFile = new File(FILE_TEXT);
        File binaryFile = new File(FILE_BINARY);
        File formattedFile = new File(FILE_FORMATTED);

        List<String> availableFiles = new ArrayList<>();
        if (serFile.exists()) {
            availableFiles.add("1 - Сериализованный файл (" + FILE_SERIALIZED + ")");
        }
        if (textFile.exists()) {
            availableFiles.add("2 - Текстовый файл (" + FILE_TEXT + ")");
        }
        if (binaryFile.exists()) {
            availableFiles.add("3 - Бинарный файл (" + FILE_BINARY + ")");
        }
        if (formattedFile.exists()) {
            availableFiles.add("4 - Форматированный файл (" + FILE_FORMATTED + ")");
        }

        if (availableFiles.isEmpty()) {
            System.out.println("Существующие файлы с данными не найдены.");
            System.out.println("База элементов пуста. Используйте меню для заполнения.\n");
            return;
        }

        System.out.println("Обнаружены существующие файлы с данными:");
        for (String file : availableFiles) {
            System.out.println("  " + file);
        }

        System.out.print("\nЗагрузить данные из файла? (1-4 для выбора файла, 0 - пропустить): ");
        int choice = getIntInput("");
        System.out.println();

        try {
            switch (choice) {
                case 1:
                    if (serFile.exists()) {
                        loadFromSerializedFile(FILE_SERIALIZED);
                    } else {
                        System.out.println("Файл не найден.");
                    }
                    break;
                case 2:
                    if (textFile.exists()) {
                        loadFromTextFile(FILE_TEXT);
                    } else {
                        System.out.println("Файл не найден.");
                    }
                    break;
                case 3:
                    if (binaryFile.exists()) {
                        loadFromBinaryFile(FILE_BINARY);
                    } else {
                        System.out.println("Файл не найден.");
                    }
                    break;
                case 4:
                    if (formattedFile.exists()) {
                        loadFromFormattedFile(FILE_FORMATTED);
                    } else {
                        System.out.println("Файл не найден.");
                    }
                    break;
                case 0:
                    System.out.println("Загрузка данных пропущена.");
                    break;
                default:
                    System.out.println("Неверный выбор. Загрузка данных пропущена.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }

    private static void loadFromSerializedFile(String filename) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            int count = ois.readInt();

            for (int i = 0; i < count; i++) {
                MusicCollection mc = (MusicCollection) ois.readObject();
                collections.add(mc);
            }
        }
        System.out.println("✓ Загружено элементов из " + filename + ": " + collections.size());
    }

    private static void loadFromTextFile(String filename) throws IOException {
        try (FileReader fr = new FileReader(filename);
                BufferedReader br = new BufferedReader(fr)) {

            String countLine = br.readLine();
            int count = Integer.parseInt(countLine.trim());

            for (int i = 0; i < count; i++) {
                MusicCollection mc = MusicCollectionIO.readMusicCollection(br);
                collections.add(mc);
            }
        }
        System.out.println("✓ Загружено элементов из " + filename + ": " + collections.size());
    }

    private static void loadFromBinaryFile(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            DataInputStream dis = new DataInputStream(fis);
            int count = dis.readInt();

            for (int i = 0; i < count; i++) {
                MusicCollection mc = MusicCollectionIO.inputMusicCollection(fis);
                collections.add(mc);
            }
        }
        System.out.println("✓ Загружено элементов из " + filename + ": " + collections.size());
    }

    private static void loadFromFormattedFile(String filename) throws IOException {
        try (FileReader fr = new FileReader(filename);
                Scanner fileScanner = new Scanner(fr)) {

            fileScanner.useLocale(Locale.US);
            int count = fileScanner.nextInt();

            for (int i = 0; i < count; i++) {
                MusicCollection mc = MusicCollectionIO.readFormatMusicCollection(fileScanner);
                if (mc != null) {
                    collections.add(mc);
                }
            }
        }
        System.out.println("✓ Загружено элементов из " + filename + ": " + collections.size());
    }

    private static void fillCollections() {
        System.out.println("=== Заполнение базы элементов ===");
        int count = getIntInput("Введите количество элементов для создания: ");

        for (int i = 0; i < count; i++) {
            System.out.printf("\n--- Создание элемента №%d ---\n", i + 1);
            System.out.print("Выберите тип: 1 - Альбом, 2 - Плейлист: ");
            int choice = getIntInput("");

            try {
                if (choice == 1) {
                    collections.add(createAlbum());
                } else if (choice == 2) {
                    collections.add(createPlaylist());
                } else {
                    System.out.println("Неверный выбор типа.");
                    i--;
                }
            } catch (InvalidTrackDataException e) {
                System.out.println("Ошибка: " + e.getMessage());
                i--;
            }
        }

        System.out.printf("\nУспешно создано элементов: %d\n", count);
    }

    private static Album createAlbum() {
        scanner.nextLine();
        System.out.print("Введите имя исполнителя: ");
        String artist = scanner.nextLine();

        int year = getIntInput("Введите год выпуска: ");

        System.out.print("Введите длительности треков через пробел (в секундах): ");
        scanner.nextLine();
        String[] durationsStr = scanner.nextLine().trim().split("\\s+");
        int[] durations = Arrays.stream(durationsStr)
                .mapToInt(Integer::parseInt)
                .toArray();

        return new Album(artist, year, durations);
    }

    private static Playlist createPlaylist() {
        scanner.nextLine();
        System.out.print("Введите название плейлиста: ");
        String title = scanner.nextLine();

        int genreId = getIntInput("Введите ID жанра: ");

        System.out.print("Введите рейтинги треков через пробел (1-5): ");
        scanner.nextLine();
        String[] ratingsStr = scanner.nextLine().trim().split("\\s+");
        int[] ratings = Arrays.stream(ratingsStr)
                .mapToInt(Integer::parseInt)
                .toArray();

        return new Playlist(title, genreId, ratings);
    }

    private static void testBinaryIO() {
        System.out.println("=== Тест байтового ввода/вывода ===");

        if (collections.isEmpty()) {
            System.out.println("База элементов пуста. Сначала заполните её.");
            return;
        }

        String filename = FILE_BINARY;

        try {
            System.out.println("Запись в файл " + filename + "...");
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                DataOutputStream dos = new DataOutputStream(fos);
                dos.writeInt(collections.size());

                for (MusicCollection collection : collections) {
                    MusicCollectionIO.outputMusicCollection(collection, fos);
                }
            }
            System.out.println("✓ Запись завершена.");

            System.out.println("Чтение из файла " + filename + "...");
            List<MusicCollection> readCollections = new ArrayList<>();
            try (FileInputStream fis = new FileInputStream(filename)) {
                DataInputStream dis = new DataInputStream(fis);
                int count = dis.readInt();

                for (int i = 0; i < count; i++) {
                    MusicCollection mc = MusicCollectionIO.inputMusicCollection(fis);
                    readCollections.add(mc);
                }
            }
            System.out.println("✓ Чтение завершено. Прочитано элементов: " + readCollections.size());

            System.out.println("\nПрочитанные элементы:");
            for (int i = 0; i < readCollections.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, readCollections.get(i));
            }

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testTextIO() {
        System.out.println("=== Тест текстового ввода/вывода ===");

        if (collections.isEmpty()) {
            System.out.println("База элементов пуста. Сначала заполните её.");
            return;
        }

        String filename = FILE_TEXT;

        try {
            System.out.println("Запись в файл " + filename + "...");
            try (FileWriter fw = new FileWriter(filename);
                    BufferedWriter bw = new BufferedWriter(fw)) {

                bw.write(collections.size() + "\n");

                for (MusicCollection collection : collections) {
                    MusicCollectionIO.writeMusicCollection(collection, bw);
                }
            }
            System.out.println("✓ Запись завершена.");

            System.out.println("Чтение из файла " + filename + "...");
            List<MusicCollection> readCollections = new ArrayList<>();
            try (FileReader fr = new FileReader(filename);
                    BufferedReader br = new BufferedReader(fr)) {

                String countLine = br.readLine();
                int count = Integer.parseInt(countLine.trim());

                for (int i = 0; i < count; i++) {
                    MusicCollection mc = MusicCollectionIO.readMusicCollection(br);
                    readCollections.add(mc);
                }
            }
            System.out.println("✓ Чтение завершено. Прочитано элементов: " + readCollections.size());

            System.out.println("\nПрочитанные элементы:");
            for (int i = 0; i < readCollections.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, readCollections.get(i));
            }

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testSerialization() {
        System.out.println("=== Тест сериализации/десериализации ===");

        if (collections.isEmpty()) {
            System.out.println("База элементов пуста. Сначала заполните её.");
            return;
        }

        String filename = FILE_SERIALIZED;

        try {
            System.out.println("Сериализация в файл " + filename + "...");
            try (FileOutputStream fos = new FileOutputStream(filename);
                    ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeInt(collections.size());

                for (MusicCollection collection : collections) {
                    oos.writeObject(collection);
                }
            }
            System.out.println("✓ Сериализация завершена.");

            System.out.println("Десериализация из файла " + filename + "...");
            List<MusicCollection> readCollections = new ArrayList<>();
            try (FileInputStream fis = new FileInputStream(filename);
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                int count = ois.readInt();

                for (int i = 0; i < count; i++) {
                    MusicCollection mc = (MusicCollection) ois.readObject();
                    readCollections.add(mc);
                }
            }
            System.out.println("✓ Десериализация завершена. Прочитано элементов: " + readCollections.size());

            System.out.println("\nДесериализованные элементы:");
            for (int i = 0; i < readCollections.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, readCollections.get(i));
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testFormattedIO() {
        System.out.println("=== Тест форматного текстового ввода/вывода ===");

        if (collections.isEmpty()) {
            System.out.println("База элементов пуста. Сначала заполните её.");
            return;
        }

        String filename = FILE_FORMATTED;

        try {
            System.out.println("Форматный вывод в файл " + filename + "...");
            try (FileWriter fw = new FileWriter(filename);
                    BufferedWriter bw = new BufferedWriter(fw)) {

                PrintWriter pw = new PrintWriter(bw);
                pw.println(collections.size());

                for (MusicCollection collection : collections) {
                    MusicCollectionIO.writeFormatMusicCollection(collection, pw);
                }
            }
            System.out.println("✓ Форматный вывод завершен.");

            System.out.println("Форматный ввод из файла " + filename + "...");
            List<MusicCollection> readCollections = new ArrayList<>();
            try (FileReader fr = new FileReader(filename);
                    Scanner fileScanner = new Scanner(fr)) {

                fileScanner.useLocale(Locale.US);

                int count = fileScanner.nextInt();

                for (int i = 0; i < count; i++) {
                    MusicCollection mc = MusicCollectionIO.readFormatMusicCollection(fileScanner);
                    if (mc != null) {
                        readCollections.add(mc);
                    }
                }
            }
            System.out.println("✓ Форматный ввод завершен. Прочитано элементов: " + readCollections.size());

            System.out.println("\nПрочитанные элементы:");
            for (int i = 0; i < readCollections.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, readCollections.get(i));
            }

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayCollections() {
        System.out.println("=== Текущая база элементов ===");

        if (collections.isEmpty()) {
            System.out.println("База элементов пуста.");
            return;
        }

        for (int i = 0; i < collections.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, collections.get(i));
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                if (!prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число.");
                scanner.nextLine();
            }
        }
    }
}
