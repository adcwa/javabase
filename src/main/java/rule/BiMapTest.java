package rule;

public class BiMapTest {

    public static void main(String[] args) {
        int cnt = 100;
        byte[] bytes = new byte[cnt/8+1];

        for (int i = 0; i < 50; i++) {
            BitMapUtils.setBit(bytes,i);
        }

        for (byte aByte : bytes) {
            String tString = Integer.toBinaryString((aByte & 0xFF) + 0x100).substring(1);
            System.out.println(aByte+":"+tString);
        }

        for (int i = 0; i < 100; i++) {
            System.out.println(BitMapUtils.getBit(bytes,i));;
        }

        //        bm.add(bytes,11);
        //        bm.add(bytes,100);
        //        bm.add(bytes,200);
    }
}
