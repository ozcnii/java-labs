package lab_5;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import lab_5.exceptions.EmptyCollectionException;

/**
 * Задание 3: Синхронизированная обертка для интерфейса MusicCollection
 * Все методы синхронизированы для безопасности в многопоточной среде
 */
public class SynchronizedMusicCollection implements MusicCollection {
    private MusicCollection delegate;

    public SynchronizedMusicCollection(MusicCollection collection) {
        this.delegate = collection;
    }

    @Override
    public synchronized int getTracksDataSize() {
        return delegate.getTracksDataSize();
    }

    @Override
    public synchronized String getTitle() {
        return delegate.getTitle();
    }

    @Override
    public synchronized int getIdentifier() {
        return delegate.getIdentifier();
    }

    @Override
    public synchronized int[] getTracksData() {
        return delegate.getTracksData();
    }

    @Override
    public synchronized void setTitle(String title) {
        delegate.setTitle(title);
    }

    @Override
    public synchronized void setIdentifier(int identifier) {
        delegate.setIdentifier(identifier);
    }

    @Override
    public synchronized void setTracksData(int[] tracksData) {
        delegate.setTracksData(tracksData);
    }

    @Override
    public synchronized int getTrackData(int index) {
        return delegate.getTrackData(index);
    }

    @Override
    public synchronized void setTrackData(int index, int value) {
        delegate.setTrackData(index, value);
    }

    @Override
    public synchronized double calculateMetric() throws EmptyCollectionException {
        return delegate.calculateMetric();
    }

    @Override
    public synchronized void output(OutputStream out) throws IOException {
        delegate.output(out);
    }

    @Override
    public synchronized void write(Writer out) throws IOException {
        delegate.write(out);
    }

    @Override
    public synchronized String toString() {
        return "Synchronized[" + delegate.toString() + "]";
    }
}
