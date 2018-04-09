package server;

import builder.RequestBuilder;
import parser.Parser;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Parser> parsers = new ArrayList<>();
        parsers.add(new CurrentAssortmentWorker(new RequestBuilder()));
        parsers.add(new CurrentOrdersWorker(new RequestBuilder()));
        new Server(parsers).go();
    }
}
