package user_gui_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SharedCart {
    public static final ObservableList<HashMap<String, Object>> cartItems = FXCollections.observableArrayList();

    public static void addItem(String name, double price, int quantity) {
        for (HashMap<String, Object> item : cartItems) {
            if (item.get("name").equals(name)) {
                int newQty = (int) item.get("quantity") + quantity;
                item.put("quantity", newQty);
                item.put("total", price * newQty);
                cartItems.set(cartItems.indexOf(item), item); // force update
                return;
            }
        }

        HashMap<String, Object> newItem = new HashMap<>();
        newItem.put("name", name);
        newItem.put("price", price);
        newItem.put("quantity", quantity);
        newItem.put("total", price * quantity);
        cartItems.add(newItem);
    }

    public static int getTotalItemCount() {
        return cartItems.stream().mapToInt(item -> (int) item.get("quantity")).sum();
    }
}
