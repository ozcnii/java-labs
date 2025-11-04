package lab_6;

import java.util.Comparator;

import lab_6.exceptions.EmptyCollectionException;

/**
 * Задание 2: компаратор для сортировки по убыванию результата бизнес-метода
 */
public class DescendingMetricComparator implements Comparator<MusicCollection> {
    @Override
    public int compare(MusicCollection o1, MusicCollection o2) {
        try {
            double metric1 = o1.calculateMetric();
            double metric2 = o2.calculateMetric();
            return Double.compare(metric2, metric1);
        } catch (EmptyCollectionException e) {
            return 0;
        }
    }
}
