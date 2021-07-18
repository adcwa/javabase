package ikm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayDemo {

    public static void main(String[] args) throws IOException {
        byte c1[] = {10,20,30,40,50};
        byte c2[] = {60,70,80,90};
        ByteArrayOutputStream b1 = new ByteArrayOutputStream();
        ByteArrayOutputStream b2 = new ByteArrayOutputStream(10);

        b1.write(1);
        b1.writeTo(b2);
        System.out.println(b2.size());
        System.out.println(b2.size());
    }
}
