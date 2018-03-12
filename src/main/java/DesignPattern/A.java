package DesignPattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhouxw on 18/1/21.
 */

public class A {
    private Logger logger = LoggerFactory.getLogger(getClass());
   private A() {
       System.out.println("A-----");
   }
    {
        System.out.println(B.a);
    }
    private static class B {
        private static A a = new A();
    }

    public static void main(String[] args) {
        A a = new A();
    }
}
