package lab_5;

import lab_5.exceptions.EmptyCollectionException;

public interface MusicCollection {
    String getTitle();

    int getIdentifier();

    int[] getTracksData();

    void setTitle(String title);

    void setIdentifier(int identifier);

    void setTracksData(int[] tracksData);

    int getTrackData(int index);

    void setTrackData(int index, int value);

    double calculateMetric() throws EmptyCollectionException;
}

