package lab_5;

/**
 * Задание 2: Синхронизированная нить для чтения данных
 * Реализует Runnable и использует самописный семафор
 */
public class SynchronizedReaderRunnable implements Runnable {
    private MusicCollection collection;
    private Semaphore writeSemaphore;
    private Semaphore readSemaphore;

    public SynchronizedReaderRunnable(MusicCollection collection,
            Semaphore writeSemaphore,
            Semaphore readSemaphore) {
        this.collection = collection;
        this.writeSemaphore = writeSemaphore;
        this.readSemaphore = readSemaphore;
    }

    @Override
    public void run() {
        int[] data = collection.getTracksData();

        for (int i = 0; i < data.length; i++) {
            try {
                // Ждем разрешения на чтение
                readSemaphore.acquire();

                // Читаем данные
                int value = collection.getTrackData(i);
                System.out.println("Read: " + value + " from position " + i);

                // Разрешаем запись
                writeSemaphore.release();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
