package ikm;

import java.util.SortedSet;
import java.util.TreeSet;

public class TreeSetTest {

    public static void main(String[] args) {
        SortedSet<Eelement>  s= new TreeSet<>();
        s.add(new Eelement(15));
        s.add(new Eelement(10));
        s.add(new Eelement(25));
        s.add(new Eelement(10));
        System.out.println(s.first()+" "+s.size());
    }


    public void test(){
        class A{

        }

    }


}

class Eelement implements Comparable{
    int id ;

    Eelement(int id){
        this.id=id;
    }

    @Override
    public int compareTo(Object o) {
        Eelement c = (Eelement)o;
        return this.id-c.id;
    }

    public String toString(){
        return ""+this.id;
    }


}
