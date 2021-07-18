package ikm;

public class OuterInnerClass {

    class InnerClass extends OuterInnerClass{
        private void innerMethod(){
            outerMethod();
        }
    }

    private void outerMethod(){

        new InnerClass().innerMethod();
    }
}
