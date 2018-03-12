package DesignPattern;

/**
 * Created by zhouxw on 18/1/20.
 */
public class ConcreteProductFactory extends ProductFactory {
    @Override
    public void createProduct() {
        System.out.println("over");
    }

    public static void main(String[] args) {
        ConcreteProductFactory concreteProductFactory = new ConcreteProductFactory();
        concreteProductFactory.createProduct();
    }
}
