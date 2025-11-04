package lab_5;

import java.util.Arrays;
import java.util.Objects;

import lab_5.exceptions.EmptyCollectionException;
import lab_5.exceptions.InvalidTrackDataException;

public class Album implements MusicCollection {

    private int[] trackDurations;
    private String artistName;
    private int releaseYear;

    public Album() {
        this.artistName = "Unknown Artist";
        this.releaseYear = 1970;
        this.trackDurations = new int[0];
    }

    public Album(String artistName, int releaseYear, int[] trackDurations) {
        this.artistName = artistName;
        this.releaseYear = releaseYear;
        setTracksData(trackDurations);
    }

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
        return trackDurations.clone();
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
                    throw new InvalidTrackDataException("Длительность трека не может быть отрицательной: " + duration);
                }
            }
        }
        this.trackDurations = tracksData;
    }

    @Override
    public int getTrackData(int index) {
        if (index < 0 || index >= trackDurations.length) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        return trackDurations[index];
    }

    @Override
    public void setTrackData(int index, int value) {
        if (index < 0 || index >= trackDurations.length) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        if (value < 0) {
            throw new InvalidTrackDataException("Длительность трека не может быть отрицательной: " + value);
        }
        trackDurations[index] = value;
    }

    @Override
    public double calculateMetric() throws EmptyCollectionException {
        if (trackDurations == null || trackDurations.length == 0) {
            throw new EmptyCollectionException("Альбом пуст, невозможно рассчитать общую длительность.");
        }
        int totalDuration = 0;
        for (int duration : trackDurations) {
            totalDuration += duration;
        }
        return totalDuration;
    }

    @Override
    public String toString() {
        return String.format("Альбом [Исполнитель: %s, Год: %d, Длительности треков (сек): %s]",
                artistName, releaseYear, Arrays.toString(trackDurations));
    }

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

