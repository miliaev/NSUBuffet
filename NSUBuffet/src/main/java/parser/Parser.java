package parser;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public interface Parser {
    boolean parse(String request, ObjectInputStream reader, ObjectOutputStream writer,
                  HashMap<Integer, ObjectOutputStream> buffetOutputStreams);
}
