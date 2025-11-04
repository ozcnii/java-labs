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

public class Playlist implements MusicCollection, Serializable {
    private int[] trackRatings;
    private String playlistTitle;
    private int genreId;

    public Playlist() {
        this.playlistTitle = "New Playlist";
        this.genreId = 0;
        this.trackRatings = new int[0];
    }

    public Playlist(String playlistTitle, int genreId, int[] trackRatings) {
        this.playlistTitle = playlistTitle;
        this.genreId = genreId;
        setTracksData(trackRatings);
    }

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
                    throw new InvalidTrackDataException("Рейтинг должен быть в диапазоне от 1 до 5: " + rating);
                }
            }
        }
        this.trackRatings = tracksData;
    }

    @Override
    public int getTrackData(int index) {
        if (index < 0 || index >= trackRatings.length) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        return trackRatings[index];
    }

    @Override
    public void setTrackData(int index, int value) {
        if (index < 0 || index >= trackRatings.length) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        if (value < 1 || value > 5) {
            throw new InvalidTrackDataException("Рейтинг должен быть в диапазоне от 1 до 5: " + value);
        }
        trackRatings[index] = value;
    }

    @Override
    public double calculateMetric() throws EmptyCollectionException {
        if (trackRatings == null || trackRatings.length == 0) {
            throw new EmptyCollectionException("Плейлист пуст, невозможно рассчитать средний рейтинг.");
        }
        double sum = 0;
        for (int rating : trackRatings) {
            sum += rating;
        }
        return sum / trackRatings.length;
    }

    @Override
    public void output(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeUTF(playlistTitle != null ? playlistTitle : "");
        dos.writeInt(genreId);
        dos.writeInt(trackRatings != null ? trackRatings.length : 0);
        if (trackRatings != null) {
            for (int rating : trackRatings) {
                dos.writeInt(rating);
            }
        }

        dos.flush();
    }

    @Override
    public void write(Writer out) throws IOException {
        String safeTitle = (playlistTitle != null ? playlistTitle : "Untitled").replace(" ", "_");
        out.write(safeTitle + " ");
        out.write(genreId + " ");
        int length = trackRatings != null ? trackRatings.length : 0;
        out.write(length + " ");
        if (trackRatings != null) {
            for (int i = 0; i < trackRatings.length; i++) {
                out.write(trackRatings[i] + (i < trackRatings.length - 1 ? " " : ""));
            }
        }
        out.write("\n");
        out.flush();
    }

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
