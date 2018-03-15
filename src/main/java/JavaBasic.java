/**
 * Created by zhouxw on 18/3/13.
 */

class Window {
    Window(String s) {
        System.out.println(s);
    }
}
public class JavaBasic {
    static Window W6;

    Window w2 = new Window("W2W2-------");
    int i;
    public static void f() {
        System.out.println("f()");
        System.out.println(w5);
    }
    public void f2() {
        System.out.println("i :" + i++);
    }
    JavaBasic() {
        w1 = new Window("W1");
        w2 = new Window("W2");
        System.out.println("constructor");
    }
    Window w1 = new Window("W1W1---------");
    static {
        System.out.println(W6);

    }
    {
        System.out.println("non static block");
    }
    static Window We3 = new Window("W3");
    static Window w5;
    static {
        W6 = new Window("W6");
    }

    public static void main(String[] args) {
        JavaBasic.f();
        System.out.println(BB.i );

    }
}
 interface AA {
    int i = 0;
 }
 class BB implements AA {

 }