package server;

import abstractServer.AbstractServer;
import builder.RequestBuilder;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import parser.Parser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class Server implements AbstractServer {
    private ArrayList<Parser> parsers;
    private ServerSocket serverSocket;
    private HashMap<Integer, ObjectOutputStream> buffetOutputStreams;

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    public Server(ArrayList<Parser> parsers) {
        this.parsers = parsers;
        configureSessionFactory();
    }

    private void configureSessionFactory() throws HibernateException
    {
        Configuration configuration = new Configuration();
        configuration.configure();

        Properties properties = configuration.getProperties();

        serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }


    public class ClientHandler implements Runnable {
        ObjectInputStream reader;
        ObjectOutputStream writer;

        public ClientHandler (ObjectOutputStream writer, ObjectInputStream reader) {
            this.reader = reader;
            this.writer = writer;
        }

        public void run() {
            Object object;
            try {
                while ((object = reader.readObject()) != null) {
                    String request = (String) object;
                    System.out.println(request);
                    for (Parser parser : parsers) {
                        if (parser.parse(request, reader, writer, buffetOutputStreams)) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Client has been disconnected");
            }
        }
    }

    public void go() {
        buffetOutputStreams = new HashMap<>();
        try {
            serverSocket = new ServerSocket(5000);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("new client");
                ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
                int type = reader.readInt();
                if (type == 0) {
                    Thread t = new Thread(new ClientHandler(writer, reader));
                    t.start();
                } else {
                    int buffetId = reader.readInt();
                    buffetOutputStreams.put(buffetId - 1, writer);
                    Thread t = new Thread(new ClientHandler(writer, reader));
                    t.start();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
