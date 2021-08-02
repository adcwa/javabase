package rule;

import java.util.ArrayList;
import java.util.List;

/**
 * bitmap 工具
 * 1、 规则解析
 * 2、 规则推导
 * 3、 结果输出
 * 4、 族下唯一？？？
 * @author 54117
 */
public class BitMapUtils {
    /**
     * 设置bitmap  ，将num对应位置的bit设置为1
     * @param bits
     * @param num
     */
    public  static  void setBit(byte[] bits, int num){
        bits[num>>3] |=  1 << num%8;
    }

    public  static  void setBitn(byte[] bits, int num){
        bits[num>>3] |=  1 << num%8;
    }

    /**
     * 判断是否存在，存在则返回 1
     * @param bits
     * @param num
     * @return
     */
    public  static  int exist(byte[] bits, int num){
        return (bits[num>>3] &  (1<<num%8)) >> num%8;
    }

    /**
     * 匹配两个bitmap， 返回cmp与base不匹配的数据，并推导出原数据下标位置
     * @param byteBase
     * @param byteCmp
     * @return  不匹配数据的原值
     */
    public static List<Integer> match(byte[] byteBase, byte[] byteCmp){
        //将两个bitmap 进行比较
        byte[] a = and(byteBase,byteCmp);
        byte[] x = xor(a,byteCmp);

        return deduceIdx(x);
    }

    /**
     * 推导原值
     * @param bytes bitmap
     * @return 顺序输出的原值
     */
    public static List<Integer> deduceIdx(byte[] bytes){
//        int[] idx = new int[bytes.length*8];
        List<Integer> idx = new ArrayList<>();

        for (int i = 0; i < bytes.length; i++) {
            if(bytes[i] != 0){
                int v = i*8;
                for (int offset = 0; offset < 8; offset++) {
                    int  b = 1<<offset;
                    if((bytes[i] &b) == b){
                        idx.add(v+offset);
                    }
                }
            }
        }

        return idx;
    }

    /**
     * and
     * @param byteBase
     * @param byteCmp
     * @return
     */
    public static byte[] and(byte[] byteBase, byte[] byteCmp){
        byte[] ret = new byte[byteCmp.length];
        for (int i = 0; i < byteBase.length; i++) {
            ret[i] = (byte) (byteBase[i] & byteCmp[i]);
        }
        return ret ;
    }

    /**
     * xor
     * @param byteBase
     * @param byteCmp
     * @return
     */
    public static byte[] xor(byte[] byteBase, byte[] byteCmp){
        byte[] ret = new byte[byteCmp.length];
        for (int i = 0; i < byteBase.length; i++) {
            ret[i] = (byte) (byteBase[i] ^ byteCmp[i]);
        }
        return ret ;
    }

}
