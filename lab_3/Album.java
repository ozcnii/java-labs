package lab_3;

import java.util.Arrays;
import java.util.Objects;

import lab_3.exceptions.EmptyCollectionException;
import lab_3.exceptions.InvalidTrackDataException;

public class Album implements MusicCollection {

    private int[] trackDurations; // Длительности треков в секундах
    private String artistName; // Имя исполнителя
    private int releaseYear; // Год выпуска

    // Конструктор по умолчанию
    public Album() {
        this.artistName = "Unknown Artist";
        this.releaseYear = 1970;
        this.trackDurations = new int[0];
    }

    // Конструктор с параметрами
    public Album(String artistName, int releaseYear, int[] trackDurations) {
        this.artistName = artistName;
        this.releaseYear = releaseYear;
        setTracksData(trackDurations); // Используем сеттер для проверки
    }

    // Реализация методов интерфейса
    @Override
    public String getTitle() {
        return artistName;
    }

    @Override
    public int getIdentifier() {
        return releaseYear;
    }

    @Override
    public int[] getTracksData() {
        return trackDurations.clone(); // Возвращаем копию для инкапсуляции
    }

    @Override
    public void setTitle(String title) {
        this.artistName = title;
    }

    @Override
    public void setIdentifier(int identifier) {
        this.releaseYear = identifier;
    }

    @Override
    public void setTracksData(int[] tracksData) {
        if (tracksData != null) {
            for (int duration : tracksData) {
                if (duration < 0) {
                    // Выбрасываем необъявляемое исключение при некорректных данных
                    throw new InvalidTrackDataException("Длительность трека не может быть отрицательной: " + duration);
                }
            }
        }
        this.trackDurations = tracksData;
    }

    // Реализация бизнес-метода
    @Override
    public double calculateMetric() throws EmptyCollectionException {
        if (trackDurations == null || trackDurations.length == 0) {
            // Выбрасываем объявляемое исключение, если коллекция пуста
            throw new EmptyCollectionException("Альбом пуст, невозможно рассчитать общую длительность.");
        }
        int totalDuration = 0;
        for (int duration : trackDurations) {
            totalDuration += duration;
        }
        return totalDuration; // Возвращаем общую длительность в секундах
    }

    // Переопределение методов класса Object
    @Override
    public String toString() {
        return String.format("Альбом [Исполнитель: %s, Год: %d, Длительности треков (сек): %s]",
                artistName, releaseYear, Arrays.toString(trackDurations));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Album album = (Album) o;
        return releaseYear == album.releaseYear &&
                Objects.equals(artistName, album.artistName) &&
                Arrays.equals(trackDurations, album.trackDurations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(artistName, releaseYear);
        result = 31 * result + Arrays.hashCode(trackDurations);
        return result;
    }
}