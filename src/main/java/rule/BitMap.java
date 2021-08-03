package rule;

public class BitMap {
    int capacity;
    byte[] bits ;
    public BitMap(int capacity) {
        this.capacity = capacity;
        bits = new byte[capacity/8+1];
    }

    public byte[] getBits() {
        return bits;
    }

    public void setBits(byte[] bits) {
        this.bits = bits;
    }

}
