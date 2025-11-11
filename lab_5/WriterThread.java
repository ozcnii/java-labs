package lab_5;

import java.util.Random;

/**
 * Задание 1: Нить для записи данных в массив объекта MusicCollection
 * Наследуется от Thread
 */
public class WriterThread extends Thread {
    private MusicCollection collection;

    public WriterThread(MusicCollection collection) {
        this.collection = collection;
    }

    @Override
    public void run() {
        var random = new Random();
        for (int i = 0; i < collection.getTracksDataSize(); i++) {
            // Генерируем случайное значение от 1 до 1000
            int value = random.nextInt(1000) + 1;
            collection.setTrackData(i, value);
            System.out.println("Write: " + value + " to position " + i);
        }
    }
}
