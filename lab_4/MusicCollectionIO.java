package lab_4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.flush();

        byte[] data = baos.toByteArray();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.length);
        dos.write(data);
        dos.flush();
    }

    public static MusicCollection deserializeMusicCollection(InputStream in)
            throws IOException, ClassNotFoundException {
        DataInputStream dis = new DataInputStream(in);
        int length = dis.readInt();
        byte[] data = new byte[length];
        dis.readFully(data);

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (MusicCollection) ois.readObject();
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
