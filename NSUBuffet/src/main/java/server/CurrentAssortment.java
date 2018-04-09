package server;

import order.Order;
import order.ProductInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CurrentAssortment {
    private HashMap<Integer, HashMap<String, ProductInfo>> assortment;
    private ArrayList<HashMap<String, ProductInfo>> buffets;
    private static String CURRENT_ASSORTMENT_FILE;
    private static final int NUMBER_OF_BUFFETS = 2;

    public CurrentAssortment(String currentAssortment) {
        this.buffets = new ArrayList<>(NUMBER_OF_BUFFETS);
        for (int i = 0; i < NUMBER_OF_BUFFETS; ++i) {
            buffets.add(new HashMap<>());
        }
        this.assortment = new HashMap<>();
        CURRENT_ASSORTMENT_FILE = currentAssortment;
        loadAssortment(CURRENT_ASSORTMENT_FILE);
    }

    public HashMap<Integer, HashMap<String, ProductInfo>> getCurrentAssortment() {
        return assortment;
    }

    private void loadAssortment(String assortmentFile) {
        try {
            Scanner scanner = new Scanner(new File(assortmentFile));

            while (scanner.hasNext()) {
                String currentString = scanner.nextLine();
                String[] splitString = currentString.split("\\|");
                String key = splitString[0];
                Integer amount = Integer.parseInt(splitString[1]);
                Double price = Double.parseDouble(splitString[2]);
                Integer buffetNumber = Integer.parseInt(splitString[3]);
                buffets.get(buffetNumber - 1).put(key, new ProductInfo(price, amount));
            }

            for (int i = 0; i < NUMBER_OF_BUFFETS; ++i) {
                assortment.put(i, buffets.get(i));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, ProductInfo> getCurrentItems(Integer buffetID) {
        return assortment.get(buffetID);
    }

    public void updateCurrentAssortment(Order order) {
        HashMap<String, Integer> newOrder = order.getOrderItems();
        int buffetID = order.getBuffetID() - 1;
        for (String orderItem: newOrder.keySet()) {
            ProductInfo productInfo = assortment.get(order.getBuffetID() - 1).get(orderItem);
            productInfo.setAmount(productInfo.getAmount() - newOrder.get(orderItem));
            assortment.get(buffetID).put(orderItem, productInfo);
        }
    }

    public void printAssortment(String assortmentFile) {
        try {
            FileWriter fileWriter = new FileWriter(assortmentFile, false);
            for (int buffet : assortment.keySet()) {
                for (HashMap.Entry<String, ProductInfo> item : assortment.get(buffet).entrySet()) {
                    fileWriter.write(item.getKey());
                    fileWriter.append('|');
                    fileWriter.append(item.getValue().getAmount().toString());
                    fileWriter.append('|');
                    fileWriter.append(item.getValue().getPrice().toString());
                    fileWriter.append('|');
                    Integer buffetNumber = buffet + 1;
                    fileWriter.append(buffetNumber.toString());
                    fileWriter.append("\n");
                    fileWriter.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
