package server;

import order.Order;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class StatisticData {
    private HashMap<String, Integer> currentStatistic;
    private static String STATISTIC_DATA_FILE;

    public StatisticData(String statisticData) {
        this.currentStatistic = new HashMap<>();
        STATISTIC_DATA_FILE = statisticData;
        loadStatistic(STATISTIC_DATA_FILE);
    }

    public HashMap<String, Integer> getCurrentStatistic() {
        return currentStatistic;
    }

    private void loadStatistic(String statisticData) {
        try {
            Scanner scanner = new Scanner(new File(STATISTIC_DATA_FILE));
            while (scanner.hasNext()) {
                String currentString = scanner.nextLine();
                String[] splitString = currentString.split("\\|");
                String key = splitString[0];
                Integer value = Integer.parseInt(splitString[1]);
                currentStatistic.put(key, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateStatistic(Order order) {
        HashMap<String, Integer> newOrder = order.getOrderItems();
        for (String orderItem: newOrder.keySet()) {
            if (currentStatistic.containsKey(orderItem)) {
                int valueItem = currentStatistic.get(orderItem);
                currentStatistic.put(orderItem, ++valueItem);
            } else {
                currentStatistic.put(orderItem, 1);
            }
        }
    }

    private void printStatistic() {
        try {
            FileWriter fileWriter = new FileWriter(STATISTIC_DATA_FILE, false);
            for (HashMap.Entry<String, Integer> item : currentStatistic.entrySet()) {
                fileWriter.write(item.getKey());
                fileWriter.append('|');
                fileWriter.append(item.getValue().toString());
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
