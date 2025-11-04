package lab_5;

import java.util.Random;

/**
 * Задание 2: Синхронизированная нить для записи данных
 * Реализует Runnable и использует самописный семафор
 */
public class SynchronizedWriterRunnable implements Runnable {
    private MusicCollection collection;
    private Semaphore writeSemaphore;
    private Semaphore readSemaphore;
    private Random random;

    public SynchronizedWriterRunnable(MusicCollection collection,
            Semaphore writeSemaphore,
            Semaphore readSemaphore) {
        this.collection = collection;
        this.writeSemaphore = writeSemaphore;
        this.readSemaphore = readSemaphore;
        this.random = new Random();
    }

    @Override
    public void run() {
        int[] data = collection.getTracksData();

        for (int i = 0; i < data.length; i++) {
            try {
                // Ждем разрешения на запись
                writeSemaphore.acquire();

                // Записываем данные
                int value = random.nextInt(1000) + 1;
                collection.setTrackData(i, value);
                System.out.println("Write: " + value + " to position " + i);

                // Разрешаем чтение
                readSemaphore.release();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
