package lab_3;

import lab_3.exceptions.EmptyCollectionException;

public interface MusicCollection {
    String getTitle();

    int getIdentifier(); // Будет возвращать год выпуска для альбома или ID жанра для плейлиста

    int[] getTracksData();

    void setTitle(String title);

    void setIdentifier(int identifier);

    void setTracksData(int[] tracksData);

    double calculateMetric() throws EmptyCollectionException;
}