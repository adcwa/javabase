package rule;

/**
 * @author 54117
 */
public class BitMapUtils {

    public  static  void setBit(byte[] bits, int num){
        bits[num>>3] |=  1 << num%8;
    }
    public  static  int getBit(byte[] bits, int num){
        return bits[num>>3] &  (1<<num%8);
    }
}
