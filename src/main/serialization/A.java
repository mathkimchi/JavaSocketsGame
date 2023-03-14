package src.main.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class A implements Serializable {
    private static final long serialVersionUID = 1L;

    private int field;

    public static void main(final String[] args) throws Exception {
        final ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream("serialized_object.bin"));
        final ObjectInputStream reader = new ObjectInputStream(new FileInputStream("serialized_object.bin"));

        final A obj = new A();

        obj.field = 2;
        writer.writeObject(obj);
        writer.reset();
        obj.field = 3;
        writer.writeObject(obj);

        final A deserialized1 = (A) reader.readObject();
        System.out.println("first deserialized's field: " + deserialized1.field);
        final A deserialized2 = (A) reader.readObject();
        System.out.println("second deserialized's field: " + deserialized2.field);

        writer.close();
        reader.close();
    }
}
