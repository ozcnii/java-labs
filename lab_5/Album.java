package lab_5;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;

import lab_5.exceptions.EmptyCollectionException;
import lab_5.exceptions.InvalidTrackDataException;

public class Album implements MusicCollection, Serializable {
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
    public void output(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeUTF(artistName != null ? artistName : "");
        dos.writeInt(releaseYear);
        dos.writeInt(trackDurations != null ? trackDurations.length : 0);
        if (trackDurations != null) {
            for (int duration : trackDurations) {
                dos.writeInt(duration);
            }
        }

        dos.flush();
    }

    @Override
    public void write(Writer out) throws IOException {
        String safeTitle = (artistName != null ? artistName : "Unknown").replace(" ", "_");
        out.write(safeTitle + " ");
        out.write(releaseYear + " ");
        int length = trackDurations != null ? trackDurations.length : 0;
        out.write(length + " ");
        if (trackDurations != null) {
            for (int i = 0; i < trackDurations.length; i++) {
                out.write(trackDurations[i] + (i < trackDurations.length - 1 ? " " : ""));
            }
        }
        out.write("\n");
        out.flush();
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

    @Override
    public int getTracksDataSize() {
        return trackDurations.length;
    }
}
