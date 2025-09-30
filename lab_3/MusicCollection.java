package lab_3;

import lab_3.exceptions.EmptyCollectionException;

public interface MusicCollection {
    String getTitle();

    int getIdentifier();

    int[] getTracksData();

    void setTitle(String title);

    void setIdentifier(int identifier);

    void setTracksData(int[] tracksData);

    double calculateMetric() throws EmptyCollectionException;
}