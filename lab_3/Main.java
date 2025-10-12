package lab_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import lab_3.exceptions.EmptyCollectionException;
import lab_3.exceptions.InvalidTrackDataException;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите количество музыкальных коллекций для создания: ");
        int size = scanner.nextInt();
        scanner.nextLine();

        MusicCollection[] collections = new MusicCollection[size];

        // 1. Заполнение массива объектами
        for (int i = 0; i < size; i++) {
            System.out.printf("\n--- Создание коллекции №%d ---\n", i + 1);
            System.out.print("Выберите тип: 1 - Альбом, 2 - Плейлист: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                if (choice == 1) {
                    System.out.print("Введите имя исполнителя: ");
                    String artist = scanner.nextLine();
                    System.out.print("Введите год выпуска: ");
                    int year = scanner.nextInt();
                    System.out.print("Введите длительности треков через пробел (напр., 180 245 210): ");
                    scanner.nextLine();
                    String[] durationsStr = scanner.nextLine().split(" ");
                    int[] durations = Arrays.stream(durationsStr).mapToInt(Integer::parseInt).toArray();
                    collections[i] = new Album(artist, year, durations);
                } else {
                    System.out.print("Введите название плейлиста: ");
                    String title = scanner.nextLine();
                    System.out.print("Введите ID жанра: ");
                    int genreId = scanner.nextInt();
                    System.out.print("Введите рейтинги треков через пробел (1-5): ");
                    scanner.nextLine();
                    String[] ratingsStr = scanner.nextLine().split(" ");
                    int[] ratings = Arrays.stream(ratingsStr).mapToInt(Integer::parseInt).toArray();
                    collections[i] = new Playlist(title, genreId, ratings);
                }
            } catch (InvalidTrackDataException e) {
                System.out.println("Ошибка создания объекта: " + e.getMessage());
                System.out.println("Этот элемент останется пустым (null).");
            } catch (Exception e) {
                System.out.println("Произошла непредвиденная ошибка ввода. Этот элемент останется пустым (null).");
            }
        }

        collections = Arrays.stream(collections).filter(Objects::nonNull).toArray(MusicCollection[]::new);

        // 2. Вывод полной информации
        System.out.println("\n--- Полная информация обо всех коллекциях ---");
        for (MusicCollection collection : collections) {
            System.out.println(collection);
        }

        // 3. Поиск объектов с одинаковым результатом бизнес-метода
        System.out.println("\n--- Группы коллекций с одинаковым результатом бизнес-метода ---");
        Map<Double, List<MusicCollection>> groups = new HashMap<>();
        for (MusicCollection collection : collections) {
            try {
                double result = collection.calculateMetric();
                List<MusicCollection> group = groups.get(result);
                if (group == null) {
                    group = new ArrayList<>();
                    groups.put(result, group);
                }
                group.add(collection);
            } catch (EmptyCollectionException e) {
                System.out.println("Предупреждение: " + e.getMessage() + " для объекта: " + collection.getTitle());
            }
        }

        boolean foundGroups = false;
        for (Map.Entry<Double, List<MusicCollection>> entry : groups.entrySet()) {
            if (entry.getValue().size() > 1) {
                foundGroups = true;
                System.out.printf("Группа с результатом %.2f:\n", entry.getKey());
                for (MusicCollection item : entry.getValue()) {
                    System.out.println("\t" + item);
                }
            }
        }
        if (!foundGroups) {
            System.out.println("Групп с одинаковыми результатами не найдено.");
        }

        // 4. Разбиение исходного массива на два по типам
        System.out.println("\n--- Разделение коллекций по типам ---");
        List<Album> albums = new ArrayList<>();
        List<Playlist> playlists = new ArrayList<>();

        for (MusicCollection collection : collections) {
            if (collection instanceof Album) {
                albums.add((Album) collection);
            } else if (collection instanceof Playlist) {
                playlists.add((Playlist) collection);
            }
        }

        System.out.println("Все альбомы:");
        if (albums.isEmpty()) {
            System.out.println("Альбомы отсутствуют.");
        } else {
            albums.forEach(System.out::println);
        }

        System.out.println("\nВсе плейлисты:");
        if (playlists.isEmpty()) {
            System.out.println("Плейлисты отсутствуют.");
        } else {
            playlists.forEach(System.out::println);
        }

        scanner.close();
    }
}