package lab_7;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.util.Scanner;

public class MusicCollectionIO {

    public static void outputMusicCollection(MusicCollection o, OutputStream out) throws IOException {
        o.output(out);
    }

    public static MusicCollection inputMusicCollection(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);

        String title = dis.readUTF();
        int identifier = dis.readInt();
        int count = dis.readInt();
        int[] data = new int[count];
        for (int i = 0; i < count; i++) {
            data[i] = dis.readInt();
        }

        return new Album(title, identifier, data);
    }

    public static void writeMusicCollection(MusicCollection o, Writer out) throws IOException {
        o.write(out);
    }

    public static MusicCollection readMusicCollection(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        tokenizer.resetSyntax();
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('A', 'Z');
        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('_', '_');
        tokenizer.wordChars('-', '-');
        tokenizer.wordChars(128 + 32, 255); // для кириллицы
        tokenizer.whitespaceChars(0, ' ');
        tokenizer.parseNumbers();
        tokenizer.eolIsSignificant(true);

        tokenizer.nextToken();

        String title = tokenizer.sval != null ? tokenizer.sval.replace("_", " ") : "Unknown";

        tokenizer.nextToken();
        int identifier = (int) tokenizer.nval;

        tokenizer.nextToken();
        int count = (int) tokenizer.nval;

        int[] data = new int[count];
        for (int i = 0; i < count; i++) {
            tokenizer.nextToken();
            data[i] = (int) tokenizer.nval;
        }

        return new Album(title, identifier, data);
    }

    public static void serializeMusicCollection(MusicCollection o, OutputStream out) throws IOException {
        ObjectOutputStream oos;
        if (out instanceof ObjectOutputStream) {
            oos = (ObjectOutputStream) out;
        } else {
            oos = new ObjectOutputStream(out);
        }
        oos.writeObject(o);
        oos.flush();
    }

    public static MusicCollection deserializeMusicCollection(InputStream in)
            throws IOException, ClassNotFoundException {
        ObjectInputStream ois;
        boolean needCustomResolver = !(in instanceof ObjectInputStream);

        if (needCustomResolver) {
            ois = new ObjectInputStream(in) {
                @Override
                protected Class<?> resolveClass(java.io.ObjectStreamClass desc)
                        throws IOException, ClassNotFoundException {
                    String name = desc.getName();
                    // Конвертируем классы из lab_4 в lab_7
                    if (name.startsWith("lab_4.")) {
                        String newName = name.replace("lab_4.", "lab_7.");
                        try {
                            return Class.forName(newName);
                        } catch (ClassNotFoundException e) {
                            // Если класс не найден в lab_7, загружаем оригинальный из lab_4
                            return super.resolveClass(desc);
                        }
                    }
                    return super.resolveClass(desc);
                }
            };
        } else {
            ois = (ObjectInputStream) in;
        }

        try {
            Object obj = ois.readObject();

            // Если объект из lab_4, конвертируем его в lab_7
            if (obj.getClass().getName().startsWith("lab_4.")) {
                return convertFromLab4(obj);
            }

            return (MusicCollection) obj;
        } catch (ClassCastException e) {
            throw new IOException(
                    "Не удалось загрузить объект. Возможно, файл создан в другой версии программы (lab_4).", e);
        }
    }

    private static MusicCollection convertFromLab4(Object obj) throws IOException {
        try {
            java.lang.reflect.Method getTitle = obj.getClass().getMethod("getTitle");
            java.lang.reflect.Method getIdentifier = obj.getClass().getMethod("getIdentifier");
            java.lang.reflect.Method getTracksData = obj.getClass().getMethod("getTracksData");

            String title = (String) getTitle.invoke(obj);
            int identifier = (Integer) getIdentifier.invoke(obj);
            int[] tracksData = (int[]) getTracksData.invoke(obj);

            String className = obj.getClass().getSimpleName();
            if ("Album".equals(className)) {
                return new Album(title, identifier, tracksData);
            } else if ("Playlist".equals(className)) {
                return new Playlist(title, identifier, tracksData);
            }
        } catch (Exception e) {
            throw new IOException("Ошибка при конвертации объекта из lab_4: " + e.getMessage(), e);
        }
        throw new IOException("Неизвестный тип объекта из lab_4: " + obj.getClass().getName());
    }

    public static void writeFormatMusicCollection(MusicCollection o, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(out);

        String safeTitle = (o.getTitle() != null ? o.getTitle() : "Unknown").replace(" ", "_");
        pw.printf("%s %d %d", safeTitle, o.getIdentifier(), o.getTracksData().length);

        for (int data : o.getTracksData()) {
            pw.printf(" %d", data);
        }

        pw.println();
        pw.flush();
    }

    public static MusicCollection readFormatMusicCollection(Scanner in) {
        if (!in.hasNext()) {
            return null;
        }

        String title = in.next().replace("_", " ");
        int identifier = in.nextInt();
        int count = in.nextInt();

        int[] data = new int[count];
        for (int i = 0; i < count; i++) {
            data[i] = in.nextInt();
        }

        return new Album(title, identifier, data);
    }
}
