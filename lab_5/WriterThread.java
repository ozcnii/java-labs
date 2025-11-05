package lab_5;

import java.util.Random;

/**
 * Задание 1: Нить для записи данных в массив объекта MusicCollection
 * Наследуется от Thread
 */
public class WriterThread extends Thread {
    private MusicCollection collection;
    private Random random;

    public WriterThread(MusicCollection collection) {
        this.collection = collection;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < collection.getTracksDataSize(); i++) {
            // Генерируем случайное значение от 1 до 1000
            int value = random.nextInt(1000) + 1;
            collection.setTrackData(i, value);
            System.out.println("Write: " + value + " to position " + i);
        }
    }
}
