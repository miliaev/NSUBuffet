package builder;

import builder.Builder;
import order.Order;

public class RequestBuilder implements Builder {
    @Override
    public String getCurrentItems() {
        return "getCurrentItems";
    }

    @Override
    public String getOrderByID() {
        return "getOrderByID";
    }

    @Override
    public String deleteOrderByID() {
        return "deleteOrderByID";
    }

    @Override
    public String addNewOrder() {
        return "addNewOrder";
    }

    @Override
    public String needUpdateView() {return "needUpdateView"; }
}
