package lab_6;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import lab_6.exceptions.EmptyCollectionException;

/**
 * Задание 1: extends Comparable<MusicCollection>
 * Задание 3: extends Iterable<Integer>
 */
public interface MusicCollection extends Comparable<MusicCollection>, Iterable<Integer> {
    String getTitle();

    int getIdentifier();

    int[] getTracksData();

    void setTitle(String title);

    void setIdentifier(int identifier);

    void setTracksData(int[] tracksData);

    int getTrackData(int index);

    void setTrackData(int index, int value);

    double calculateMetric() throws EmptyCollectionException;

    void output(OutputStream out) throws IOException;

    void write(Writer out) throws IOException;
}
