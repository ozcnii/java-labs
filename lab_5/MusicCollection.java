package lab_5;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import lab_5.exceptions.EmptyCollectionException;

public interface MusicCollection {
    String getTitle();

    int getIdentifier();

    int[] getTracksData();

    void setTitle(String title);

    void setIdentifier(int identifier);

    void setTracksData(int[] tracksData);

    int getTracksDataSize();

    int getTrackData(int index);

    void setTrackData(int index, int value);

    double calculateMetric() throws EmptyCollectionException;

    void output(OutputStream out) throws IOException;

    void write(Writer out) throws IOException;
}
