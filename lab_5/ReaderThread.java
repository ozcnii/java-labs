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
        int[] data = collection.getTracksData();
        
        for (int i = 0; i < data.length; i++) {
            int value = collection.getTrackData(i);
            System.out.println("Read: " + value + " from position " + i);
        }
    }
}

