package lab_7;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import lab_7.exceptions.InvalidTrackDataException;

public class MusicCollectionController {
    private List<MusicCollection> collections;

    public MusicCollectionController() {
        collections = new ArrayList<>();
    }

    public List<MusicCollection> getCollections() {
        return collections;
    }

    public void loadFromBinaryFile(File file) throws IOException {
        collections.clear();
        try (FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis)) {
            int count = dis.readInt();

            for (int i = 0; i < count; i++) {
                MusicCollection mc = MusicCollectionIO.inputMusicCollection(fis);
                collections.add(mc);
            }
        }
    }

    public void loadFromTextFile(File file) throws IOException, NumberFormatException {
        collections.clear();
        try (FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr)) {

            String countLine = br.readLine();
            if (countLine == null || countLine.trim().isEmpty()) {
                throw new IOException("Файл пуст или не содержит количество элементов");
            }

            int count = Integer.parseInt(countLine.trim());

            for (int i = 0; i < count; i++) {
                MusicCollection mc = MusicCollectionIO.readMusicCollection(br);
                collections.add(mc);
            }
        }
    }

    public void loadFromSerializedFile(File file) throws IOException, ClassNotFoundException {
        collections.clear();
        try (FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)) {

            int count = ois.readInt();

            for (int i = 0; i < count; i++) {
                MusicCollection mc = MusicCollectionIO.deserializeMusicCollection(ois);
                collections.add(mc);
            }
        }
    }

    public void loadFromFormattedFile(File file) throws IOException, java.util.NoSuchElementException {
        collections.clear();
        try (FileReader fr = new FileReader(file);
                Scanner scanner = new Scanner(fr)) {

            scanner.useLocale(Locale.US);

            if (!scanner.hasNextInt()) {
                throw new IOException("Файл не содержит количество элементов или имеет неверный формат");
            }

            int count = scanner.nextInt();

            for (int i = 0; i < count; i++) {
                MusicCollection mc = MusicCollectionIO.readFormatMusicCollection(scanner);
                if (mc != null) {
                    collections.add(mc);
                }
            }
        }
    }

    public void autoFillDatabase() {
        Random random = new Random();

        collections.clear();
        String[] artists = { "The Beatles", "Pink Floyd", "Led Zeppelin", "Queen", "The Rolling Stones" };
        int[] years = { 1965, 1973, 1971, 1975, 1969 };

        for (int i = 0; i < 5; i++) {
            int[] durations = new int[random.nextInt(5) + 3];
            for (int j = 0; j < durations.length; j++) {
                durations[j] = random.nextInt(300) + 120; // 2-7 минут
            }
            collections.add(new Album(artists[i], years[i], durations));
        }

        String[] playlistNames = { "Rock Classics", "Jazz Vibes", "Electronic Beats", "Acoustic Sessions" };
        int[] genreIds = { 1, 2, 3, 4 };

        for (int i = 0; i < 4; i++) {
            int[] ratings = new int[random.nextInt(5) + 2];
            for (int j = 0; j < ratings.length; j++) {
                ratings[j] = random.nextInt(5) + 1; // 1-5
            }
            collections.add(new Playlist(playlistNames[i], genreIds[i], ratings));
        }
    }

    public void addAlbum(String artist, int year, int[] durations) throws InvalidTrackDataException {
        collections.add(new Album(artist, year, durations));
    }

    public void addPlaylist(String title, int genreId, int[] ratings) throws InvalidTrackDataException {
        collections.add(new Playlist(title, genreId, ratings));
    }

    public void removeCollection(int index) {
        if (index >= 0 && index < collections.size()) {
            collections.remove(index);
        }
    }

    public boolean isEmpty() {
        return collections.isEmpty();
    }

    public int size() {
        return collections.size();
    }

    public MusicCollection get(int index) {
        return collections.get(index);
    }
}
