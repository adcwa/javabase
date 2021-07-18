package ikm;

import cn.hutool.core.lang.copier.Copier;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class Other {

    static int number2 =getValue();
    static int number1 =10;
    static int getValue(){
        return number1;
    }
    static int doSum(){
        return number1+number2;
    }
    static int doMinus(){
        return number1-number2;
    }

    public static String s ="";
    int i ;

    public static void print(Supplier s){
        Object o = s.get();
    }

    public static void main(String[] args) throws FileNotFoundException {


        System.out.println(Stream.of("green","yellow","blue").
                max((s1,s2)->s1.compareTo(s2)).filter(s->s.endsWith("n")).orElse("aaa"));
//        System.out.println(doSum());;
//        System.out.println(doMinus());;
//        int integer =Integer.valueOf(1);
//        int integer1 = integer;
//
//        integer+= 1;
//        System.out.println(integer);
//        System.out.println(integer1);

//        Collector.of()

//        System.out.println(new Other().i);

//        Formatter fmt = new Formatter(new StringBuilder("A"));
//        System.out.println(fmt.format("%-2s","B"));
//        System.out.println(fmt.format("%b",null));

//        Map<Integer,Integer> map = new HashMap<>();
//        Integer key = 5;
//        Integer value = key;
//        map.put(key++,value);
//        map.put(300,300);
//        int count = 0;
//        for (Integer i : map.keySet()) {
//            for (Integer j : map.values()) {
//                count++;
//                if(i==map.get(i)){
//                    System.out.println("break");
//                    break;
//                }else{
//                    System.out.println("continue");
//                    continue;
//                }
//            }
//        }
//        System.out.println(count);

//        Consumer<String> c = x-> System.out.println(x);
//        Consumer<String> d = x-> System.out.println(x);
//        c.andThen();

    }



}
