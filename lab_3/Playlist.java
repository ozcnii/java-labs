package lab_3;

import java.util.Arrays;
import java.util.Objects;

import lab_3.exceptions.EmptyCollectionException;
import lab_3.exceptions.InvalidTrackDataException;

public class Playlist implements MusicCollection {

    private int[] trackRatings; // Рейтинги треков (1-5)
    private String playlistTitle; // Название плейлиста
    private int genreId; // ID жанра

    // Конструктор по умолчанию
    public Playlist() {
        this.playlistTitle = "New Playlist";
        this.genreId = 0;
        this.trackRatings = new int[0];
    }

    // Конструктор с параметрами
    public Playlist(String playlistTitle, int genreId, int[] trackRatings) {
        this.playlistTitle = playlistTitle;
        this.genreId = genreId;
        setTracksData(trackRatings); // Используем сеттер для проверки
    }

    // Реализация методов интерфейса
    @Override
    public String getTitle() {
        return playlistTitle;
    }

    @Override
    public int getIdentifier() {
        return genreId;
    }

    @Override
    public int[] getTracksData() {
        return trackRatings.clone();
    }

    @Override
    public void setTitle(String title) {
        this.playlistTitle = title;
    }

    @Override
    public void setIdentifier(int identifier) {
        this.genreId = identifier;
    }

    @Override
    public void setTracksData(int[] tracksData) {
        if (tracksData != null) {
            for (int rating : tracksData) {
                if (rating < 1 || rating > 5) {
                    // Выбрасываем необъявляемое исключение
                    throw new InvalidTrackDataException("Рейтинг трека должен быть в диапазоне от 1 до 5: " + rating);
                }
            }
        }
        this.trackRatings = tracksData;
    }

    // Реализация бизнес-метода
    @Override
    public double calculateMetric() throws EmptyCollectionException {
        if (trackRatings == null || trackRatings.length == 0) {
            // Выбрасываем объявляемое исключение
            throw new EmptyCollectionException("Плейлист пуст, невозможно рассчитать средний рейтинг.");
        }
        double sum = 0;
        for (int rating : trackRatings) {
            sum += rating;
        }
        return sum / trackRatings.length; // Возвращаем средний рейтинг
    }

    // Переопределение методов класса Object
    @Override
    public String toString() {
        return String.format("Плейлист [Название: %s, ID жанра: %d, Рейтинги: %s]",
                playlistTitle, genreId, Arrays.toString(trackRatings));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Playlist playlist = (Playlist) o;
        return genreId == playlist.genreId &&
                Objects.equals(playlistTitle, playlist.playlistTitle) &&
                Arrays.equals(trackRatings, playlist.trackRatings);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(playlistTitle, genreId);
        result = 31 * result + Arrays.hashCode(trackRatings);
        return result;
    }
}