package rule;

import java.util.List;

public class BiMapTest {

    public static void main(String[] args) {
        int cnt = 20;
        byte[] v1 = new byte[cnt/8+1];

        for (int i = 0; i < 4; i++) {
            BitMapUtils.setBit(v1,i);
        }

        BitMapUtils.setBit(v1,8);
        BitMapUtils.setBit(v1,16);
        BitMapUtils.setBit(v1,17);
        BitMapUtils.setBit(v1,18);
        BitMapUtils.setBit(v1,20);

        byte[] v2 = new byte[cnt/8+1];

        for (int i = 0; i < 20; i++) {
            BitMapUtils.setBit(v2,i);
        }
        BitMapUtils.setBit(v2,5);
        BitMapUtils.setBit(v2,10);
        BitMapUtils.setBit(v2,16);
        BitMapUtils.setBit(v2,17);
        BitMapUtils.setBit(v2,20);

        for (int i = 0; i < v1.length; i++) {
            System.out.println("v1:"+Integer.toBinaryString((v1[i] & 0xFF) + 0x100).substring(1));
            System.out.println("v2:"+Integer.toBinaryString((v2[i] & 0xFF) + 0x100).substring(1));
            System.out.println("------------------------------------------------------------------");
        }


//        byte[] a = BitMapUtils.and(v1,v2);
//        byte[] x = BitMapUtils.xor(a,v2);
//        for (byte b : x) {
//            System.out.println(b);
//        }
//
////
//       List<Integer> list = BitMapUtils.match(v1,v2        );
        List<Integer> list = BitMapUtils.deduceIdx(v2);
        for (int i : list) {
            System.out.println(i);
        }


//
//        for (int i = 0; i < 104; i++) {
//            System.out.println(BitMapUtils.getBit(v2,i));;
//        }


    }
}
