package lab_5;

/**
 * Задание 1: Нить для чтения данных из массива объекта MusicCollection
 * Наследуется от Thread
 */
public class ReaderThread extends Thread {
    private MusicCollection collection;

    public ReaderThread(MusicCollection collection) {
        this.collection = collection;
    }

    @Override
    public void run() {
        for (int i = 0; i < collection.getTracksDataSize(); i++) {
            int value = collection.getTrackData(i);
            System.out.println("Read: " + value + " from position " + i);
        }
    }
}
