package server;

import parser.Parser;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class StatisticDataWorker implements Parser {

    @Override
    public boolean parse(String request, ObjectInputStream reader, ObjectOutputStream writer, HashMap<Integer, ObjectOutputStream> buffetOutputStreams) {
        return false;
    }
}
