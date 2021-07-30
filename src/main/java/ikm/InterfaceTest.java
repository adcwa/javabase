package ikm;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

public interface InterfaceTest {
    static void test1() {
        System.out.println("interface");
    }

    BigDecimal balance = new BigDecimal(0);
}

class Account implements InterfaceTest{
    int i ;
    public void test(){
//        balance = new BigDecimal(1);
    }

    static void test1(){
        System.out.println("acct");
    }

    public static void main(String[] args) {
        test1();
    }
}
