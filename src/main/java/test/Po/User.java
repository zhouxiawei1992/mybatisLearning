package test.Po;

/**
 * Created by zhouxw on 17/12/6.
 */
public class User {
    public String name =null;
    static int count = 0;
    public void setName(String name) {
        this.name = name;
    }

    public static void f(int i) {
       count++;
    }

    public static void main(String[] args) {
        f(1);
        f(2);
        System.out.println(count);
    }
}
