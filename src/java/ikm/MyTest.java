package ikm;

import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Function;

public class MyTest {

    static int total = 10;
    public void call(){
        int total =5;
        System.out.println(this.total);
    }

    static public  void main(String argsk[]) {
        new MyTest().call();
    }

    static void test(){

    }
}

enum Element{
    HELIUM("He","GAS");
    private String s1;
    private String s2;
    private Element(String s1,String s2){
        this.s1=s1;
      this.  s2=s2;
    }
    public String s1(){
        return s1;
    }

    public String s2(){
        return s2;
    }
}
