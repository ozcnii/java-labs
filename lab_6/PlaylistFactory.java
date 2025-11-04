package lab_6;

/**
 * Задание 6: фабрика для создания экземпляров Playlist
 */
public class PlaylistFactory implements MusicCollectionFactory {
    @Override
    public MusicCollection createInstance() {
        return new Playlist();
    }
}

