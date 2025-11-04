package lab_6;

/**
 * Задание 6: фабрика для создания экземпляров Album
 */
public class AlbumFactory implements MusicCollectionFactory {
    @Override
    public MusicCollection createInstance() {
        return new Album();
    }
}

