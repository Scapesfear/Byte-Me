import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestsTest {
    Customer customer ;
    Item item1;
    Item item2;
    @Test
    void OrderOutofStockTest(){
        createCustomer();
        createItem();
        item1.setAvailable(false);
        customer.getCart().addItem("1", 1);
    }

    void createCustomer(){
        customer = new Customer("John", "Doe");
    }

    void createItem(){
         item1 = new Item("1", "item1", 10.0, "category1");
         item2 = new Item("2", "item2", 20.0, "category2");
    }

    void addItemToCart(){
        customer.getCart().addItem("1", 1);
        customer.getCart().addItem("2", 1);
    }

    @Test
    void CartOperationsTest()  {
        createCustomer();
        createItem();
        customer.getCart().viewCart();
        addItemToCart();
        customer.getCart().modifyQuantity("1",2);
        customer.getCart().modifyQuantity("2",-3);
    }

}