package lab_6;

import java.util.Comparator;

/**
 * Задание 2: компаратор для сортировки по возрастанию целого поля (identifier)
 */
public class AscendingIdentifierComparator implements Comparator<MusicCollection> {
    @Override
    public int compare(MusicCollection o1, MusicCollection o2) {
        return Integer.compare(o1.getIdentifier(), o2.getIdentifier());
    }
}
