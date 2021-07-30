package ikm;

public class SuperThisTest extends  ParentClass{

    SuperThisTest(){
        System.out.println("this constrant");
    }

    public static void main(String[] args) {
        new SuperThisTest();
    }
}

class ParentClass{

    ParentClass(){
        System.out.println("parent constructor");
    }

    ParentClass(Long a){
        System.out.println("parent constructor a ");
    }
}
