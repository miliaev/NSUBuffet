package server;

import builder.Builder;
import order.Order;
import parser.Parser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class CurrentAssortmentWorker implements Parser {

    private CurrentAssortment currentAssortment;
    private Builder requestBuilder;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private HashMap<String, Runnable> commands = new HashMap<>();
    private HashMap<Integer, ObjectOutputStream> buffetOutputStreams;

    public CurrentAssortmentWorker(Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
        this.currentAssortment = new CurrentAssortment("src/main/java/currentAssortment.txt");
        this.commands.put(requestBuilder.getCurrentItems(), this::getCurrentItems);
        this.commands.put(requestBuilder.updateCurrentAssortment(), this::updateCurrentAssortment);
    }

    private void getCurrentItems() {
        try {
            //ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = reader.readObject();
            Integer buffetId = (Integer) object;
            System.out.println(buffetId);
            //ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(currentAssortment.getCurrentItems(buffetId - 1).toString());
            writer.writeObject(currentAssortment.getCurrentItems(buffetId - 1));
            writer.reset();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateCurrentAssortment() {
        try {
            //ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = reader.readObject();
            Order order = (Order) object;
            //ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            currentAssortment.updateCurrentAssortment(order);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean parse(String request, ObjectInputStream reader, ObjectOutputStream writer, HashMap<Integer, ObjectOutputStream> buffets) {
        this.reader = reader;
        this.writer = writer;
        this.buffetOutputStreams = buffets;
        if (commands.containsKey(request)) {
            commands.get(request).run();
            return true;
        }
        return false;
    }
}
