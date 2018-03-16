/**
 * Created by zhouxw on 18/3/15.
 */
public class JavaBasicSub extends JavaBasic {
    public static void hello() {
        System.out.println("hello");
    }
    private class Inner {

    }
    public static void main(String[] args) {

        System.out.println("hello");
        JavaBasicSub javaBasicSub = new JavaBasicSub();
        JavaBasicSub.Inner inner = javaBasicSub.new Inner();

    }
}
