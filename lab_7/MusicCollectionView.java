package lab_7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;

import lab_7.exceptions.EmptyCollectionException;
import lab_7.exceptions.InvalidTrackDataException;

public class MusicCollectionView {
    private JFrame frame;
    private MusicCollectionController controller;
    private JPanel contentPanel;
    private JPanel scrollContentPanel;
    private JScrollPane scrollPane;

    public MusicCollectionView(MusicCollectionController musicCollectionController) {
        controller = musicCollectionController;
        frame = new JFrame();
        initializeGUI();
    }

    private void initializeGUI() {
        frame.setTitle("Музыкальная коллекция - Лабораторная работа №7");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        createMenuBar();
        createContentPanel();

        frame.setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");

        JMenuItem loadBinaryItem = new JMenuItem("Загрузить из бинарного файла (.dat)");
        loadBinaryItem.addActionListener(e -> loadFromBinaryFile());
        fileMenu.add(loadBinaryItem);

        JMenuItem loadTextItem = new JMenuItem("Загрузить из текстового файла (.txt)");
        loadTextItem.addActionListener(e -> loadFromTextFile());
        fileMenu.add(loadTextItem);

        JMenuItem loadSerializedItem = new JMenuItem("Загрузить из сериализованного файла (.ser)");
        loadSerializedItem.addActionListener(e -> loadFromSerializedFile());
        fileMenu.add(loadSerializedItem);

        JMenuItem loadFormattedItem = new JMenuItem("Загрузить из форматированного файла (.txt)");
        loadFormattedItem.addActionListener(e -> loadFromFormattedFile());
        fileMenu.add(loadFormattedItem);

        fileMenu.addSeparator();

        JMenuItem autoFillItem = new JMenuItem("Автоматическое заполнение базы");
        autoFillItem.addActionListener(e -> autoFillDatabase());
        fileMenu.add(autoFillItem);

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Редактирование");

        JMenuItem addItem = new JMenuItem("Добавить объект");
        addItem.addActionListener(e -> addCollection());
        editMenu.add(addItem);

        JMenuItem removeItem = new JMenuItem("Удалить объект по номеру");
        removeItem.addActionListener(e -> removeCollection());
        editMenu.add(removeItem);

        menuBar.add(editMenu);

        JMenu lookAndFeelMenu = new JMenu("Внешний вид");
        ButtonGroup lafGroup = new ButtonGroup();

        LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        String currentLAF = UIManager.getLookAndFeel().getName();

        for (LookAndFeelInfo lafInfo : lafs) {
            JRadioButtonMenuItem lafItem = new JRadioButtonMenuItem(lafInfo.getName());
            lafItem.setSelected(lafInfo.getName().equals(currentLAF));
            lafItem.addActionListener(e -> changeLookAndFeel(lafInfo.getClassName()));
            lafGroup.add(lafItem);
            lookAndFeelMenu.add(lafItem);
        }

        menuBar.add(lookAndFeelMenu);

        frame.setJMenuBar(menuBar);
    }

    private void createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());

        scrollContentPanel = new JPanel();
        scrollContentPanel.setLayout(new GridLayout(0, 1, 5, 5));
        scrollContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(scrollContentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder("База музыкальных коллекций"));

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(contentPanel);
        refreshDisplay();
    }

    public void refreshDisplay() {
        scrollContentPanel.removeAll();

        for (int i = 0; i < controller.size(); i++) {
            MusicCollection collection = controller.get(i);
            JPanel itemPanel = createCollectionPanel(collection, i);
            scrollContentPanel.add(itemPanel);
        }

        scrollContentPanel.revalidate();
        scrollContentPanel.repaint();
    }

    private JPanel createCollectionPanel(MusicCollection collection, int index) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(750, 100));

        String type = collection instanceof Album ? "Альбом" : "Плейлист";
        String title = collection.getTitle();
        int identifier = collection.getIdentifier();
        int[] tracksData = collection.getTracksData();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel headerLabel = new JLabel(String.format("№%d - %s", index + 1, type));
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
        infoPanel.add(headerLabel);

        JLabel titleLabel = new JLabel("Название: " + title);
        infoPanel.add(titleLabel);

        JLabel idLabel = new JLabel("Идентификатор: " + identifier);
        infoPanel.add(idLabel);

        JLabel tracksLabel = new JLabel("Количество треков: " + tracksData.length);
        infoPanel.add(tracksLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showCollectionDetails(collection, index);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });

        return panel;
    }

    private boolean validateFile(File file) {
        if (file.length() == 0) {
            JOptionPane.showMessageDialog(frame,
                    "Файл пуст. Невозможно загрузить данные.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void showCollectionDetails(MusicCollection collection, int index) {
        String type = collection instanceof Album ? "Альбом" : "Плейлист";
        String title = collection.getTitle();
        int identifier = collection.getIdentifier();
        int[] tracksData = collection.getTracksData();

        String description = String.format(
                "Тип: %s\n" +
                        "Название: %s\n" +
                        "Идентификатор: %d\n" +
                        "Данные треков: %s\n" +
                        "Количество треков: %d",
                type, title, identifier, Arrays.toString(tracksData), tracksData.length);

        String metricResult;
        try {
            double metric = collection.calculateMetric();
            if (collection instanceof Album) {
                metricResult = String.format("Общая длительность: %.2f секунд", metric);
            } else {
                metricResult = String.format("Средний рейтинг: %.2f", metric);
            }
        } catch (EmptyCollectionException e) {
            metricResult = "Ошибка: " + e.getMessage();
        }

        String message = String.format(
                "Номер объекта в базе: %d\n\n" +
                        "Описание объекта:\n%s\n\n" +
                        "Результат бизнес-метода:\n%s",
                index + 1, description, metricResult);

        JOptionPane.showMessageDialog(
                frame,
                message,
                "Детали объекта",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadFromBinaryFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите бинарный файл");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Бинарные файлы (*.dat)", "dat"));
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!validateFile(file)) {
                return;
            }

            try {
                controller.loadFromBinaryFile(file);
                refreshDisplay();
                JOptionPane.showMessageDialog(frame,
                        "Загружено элементов: " + controller.size(),
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "Ошибка при загрузке файла. Файл поврежден или имеет неверный формат.\n" + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        "Неожиданная ошибка при загрузке файла: " + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadFromTextFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите текстовый файл");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt"));
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!validateFile(file)) {
                return;
            }

            try {
                controller.loadFromTextFile(file);
                refreshDisplay();
                JOptionPane.showMessageDialog(frame,
                        "Загружено элементов: " + controller.size(),
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame,
                        "Ошибка формата файла. Первая строка должна содержать число элементов.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "Ошибка при загрузке файла. Файл поврежден или имеет неверный формат.\n" + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        "Неожиданная ошибка при загрузке файла: " + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadFromSerializedFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите сериализованный файл");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Сериализованные файлы (*.ser)", "ser"));
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!validateFile(file)) {
                return;
            }

            try {
                controller.loadFromSerializedFile(file);
                refreshDisplay();
                JOptionPane.showMessageDialog(frame,
                        "Загружено элементов: " + controller.size(),
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(frame,
                        "Ошибка: класс объекта не найден. Возможно, файл был создан другой версией программы.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "Ошибка при загрузке файла. Файл поврежден или имеет неверный формат.\n" + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        "Неожиданная ошибка при загрузке файла: " + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadFromFormattedFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите форматированный файл");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Форматированные файлы (*.txt)", "txt"));
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!validateFile(file)) {
                return;
            }

            try {
                controller.loadFromFormattedFile(file);
                refreshDisplay();
                JOptionPane.showMessageDialog(frame,
                        "Загружено элементов: " + controller.size(),
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (java.util.NoSuchElementException e) {
                JOptionPane.showMessageDialog(frame,
                        "Ошибка формата файла. Файл не содержит ожидаемых данных или имеет неверный формат.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "Ошибка при загрузке файла. Файл поврежден или имеет неверный формат.\n" + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        "Неожиданная ошибка при загрузке файла: " + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void autoFillDatabase() {
        controller.autoFillDatabase();
        refreshDisplay();
        JOptionPane.showMessageDialog(frame,
                "Автоматически создано элементов: " + controller.size(),
                "Успех",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void addCollection() {
        String[] options = { "Альбом", "Плейлист" };
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Выберите тип объекта для добавления:",
                "Добавить объект",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == -1)
            return;

        try {
            if (choice == 0) {
                // Добавление альбома
                String artist = JOptionPane.showInputDialog(frame, "Введите имя исполнителя:");
                if (artist == null || artist.trim().isEmpty())
                    return;

                String yearStr = JOptionPane.showInputDialog(frame, "Введите год выпуска:");
                if (yearStr == null)
                    return;
                int year = Integer.parseInt(yearStr);

                String durationsStr = JOptionPane.showInputDialog(frame,
                        "Введите длительности треков через пробел (в секундах):");
                if (durationsStr == null)
                    return;
                String[] durationsArray = durationsStr.trim().split("\\s+");
                int[] durations = Arrays.stream(durationsArray)
                        .mapToInt(Integer::parseInt)
                        .toArray();

                controller.addAlbum(artist, year, durations);
            } else {
                // Добавление плейлиста
                String title = JOptionPane.showInputDialog(frame, "Введите название плейлиста:");
                if (title == null || title.trim().isEmpty())
                    return;

                String genreIdStr = JOptionPane.showInputDialog(frame, "Введите ID жанра:");
                if (genreIdStr == null)
                    return;
                int genreId = Integer.parseInt(genreIdStr);

                String ratingsStr = JOptionPane.showInputDialog(frame,
                        "Введите рейтинги треков через пробел (1-5):");
                if (ratingsStr == null)
                    return;
                String[] ratingsArray = ratingsStr.trim().split("\\s+");
                int[] ratings = Arrays.stream(ratingsArray)
                        .mapToInt(Integer::parseInt)
                        .toArray();

                controller.addPlaylist(title, genreId, ratings);
            }
            refreshDisplay();

            if (controller.hasLastLoadedFile()) {
                try {
                    controller.saveToLastFile();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame,
                            "Объект добавлен, но не удалось сохранить в файл: " + e.getMessage(),
                            "Предупреждение",
                            JOptionPane.WARNING_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(frame,
                    "Объект успешно добавлен. Всего элементов: " + controller.size(),
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Ошибка: неверный формат числа",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InvalidTrackDataException e) {
            JOptionPane.showMessageDialog(frame,
                    "Ошибка: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeCollection() {
        if (controller.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "База пуста. Нечего удалять.",
                    "Информация",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String numberStr = JOptionPane.showInputDialog(frame,
                String.format("Введите номер объекта для удаления (1-%d):", controller.size()));
        if (numberStr == null)
            return;

        try {
            int number = Integer.parseInt(numberStr);
            if (number < 1 || number > controller.size()) {
                JOptionPane.showMessageDialog(frame,
                        "Неверный номер. Введите число от 1 до " + controller.size(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            controller.removeCollection(number - 1);
            refreshDisplay();

            if (controller.hasLastLoadedFile()) {
                try {
                    controller.saveToLastFile();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame,
                            "Объект удален, но не удалось сохранить в файл: " + e.getMessage(),
                            "Предупреждение",
                            JOptionPane.WARNING_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(frame,
                    "Объект №" + number + " успешно удален. Осталось элементов: " + controller.size(),
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Ошибка: неверный формат числа",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeLookAndFeel(String lafClassName) {
        try {
            UIManager.setLookAndFeel(lafClassName);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Ошибка при изменении внешнего вида: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
