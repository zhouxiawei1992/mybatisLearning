package test.Po;

import mybatis.po.*;

/**
 * Created by zhouxw on 17/12/6.
 */
public class Product {
    private float price = 0f;
    private int age = 0;
    private String Name = null;
    private User user = null;

    public Product(float price, int age, String name ,User user) {
        this.price = price;
        this.user = user;
        Name = name;
        this.age = age;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Product() {
    }


    public String getName() {
        return Name;
    }

    public float getPrice() {
        return price;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Product(float price, int age, String name) {
        this.price = price;
        this.age = age;
        Name = name;
    }

}
