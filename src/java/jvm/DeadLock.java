package jvm;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLock {


    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

      Thread t1  =  new Thread(()->{
          System.out.println("t1");
          lock.lock();
          System.out.println("locked get");
          System.out.println("t1 do something");
      });
      t1.start();
      Thread t2  =  new Thread(()->{
          System.out.println("t2");
          lock.lock();
          System.out.println("locked get");
          System.out.println("t2 do something");

      });
      t2.start();
        System.out.println("finished");

    }

}
