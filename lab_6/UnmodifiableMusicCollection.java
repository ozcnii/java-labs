package lab_6;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;

import lab_6.exceptions.EmptyCollectionException;

/**
 * Задание 4: класс-декоратор для создания неизменяемой оболочки объекта
 * MusicCollection
 * Все методы изменения выбрасывают UnsupportedOperationException
 */
public class UnmodifiableMusicCollection implements MusicCollection {
    private MusicCollection delegate;

    public UnmodifiableMusicCollection(MusicCollection collection) {
        this.delegate = collection;
    }

    @Override
    public String getTitle() {
        return delegate.getTitle();
    }

    @Override
    public int getIdentifier() {
        return delegate.getIdentifier();
    }

    @Override
    public int[] getTracksData() {
        return delegate.getTracksData();
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIdentifier(int identifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTracksData(int[] tracksData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTrackData(int index) {
        return delegate.getTrackData(index);
    }

    @Override
    public void setTrackData(int index, int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double calculateMetric() throws EmptyCollectionException {
        return delegate.calculateMetric();
    }

    @Override
    public void output(OutputStream out) throws IOException {
        delegate.output(out);
    }

    @Override
    public void write(Writer out) throws IOException {
        delegate.write(out);
    }

    @Override
    public int compareTo(MusicCollection other) {
        return delegate.compareTo(other);
    }

    @Override
    public Iterator<Integer> iterator() {
        return delegate.iterator();
    }

    @Override
    public String toString() {
        return "Unmodifiable[" + delegate.toString() + "]";
    }
}
