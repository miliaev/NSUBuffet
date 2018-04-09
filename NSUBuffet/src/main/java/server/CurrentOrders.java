package server;

import order.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentOrders {
    private HashMap<Integer, HashMap<Integer, Order>> buffetOrders;
    private HashMap<Integer, Order> idOrders;

    public CurrentOrders() {
        this.buffetOrders = new HashMap<>();
        this.idOrders = new HashMap<>();
    }

    public ArrayList<Order> getCurrentOrders(Integer buffetID) {
        return new ArrayList<>(buffetOrders.get(buffetID).values());
    }

    public Order getOrderByID(Integer id) {
        if (idOrders.containsKey(id)) {
            return idOrders.get(id);
        }
        return null;
    }

    public void deleteOrderByID(Integer id) {
        buffetOrders.get(idOrders.get(id).getBuffetID() - 1).remove(id);
        idOrders.remove(id);
    }

    public void addNewOrder(Order order) {
        idOrders.put(order.getId(), order);
        if (buffetOrders.containsKey(order.getBuffetID() - 1)) {
            buffetOrders.get(order.getBuffetID() - 1).put(order.getId(), order);
        } else {
            HashMap<Integer, Order> orderHashMap = new HashMap<>();
            orderHashMap.put(order.getId(), order);
            buffetOrders.put(order.getBuffetID() - 1, orderHashMap);
        }
    }
}
