package order;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Order {
    public List<String> ingredients = null;

    public Order() {
    }

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static Order random(List<String> availableIngr, int quantity) {
        Order orders = new Order();
        orders.ingredients = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < quantity; i++) {
            orders.ingredients.add(availableIngr.get(random.nextInt(availableIngr.size())));
        }
        return orders;
    }

    public static Order withHash(String hash) {
        Order order = new Order();
        order.ingredients = new ArrayList<>();
        order.ingredients.add(hash);
        return order;
    }
}
