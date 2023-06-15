package pl.umcs.oop.words;

import pl.umcs.oop.words.server.Server;
import pl.umcs.oop.words.server.WordBag;

public class Main {
    public static void main(String[] args) {
        WordBag wordBag = new WordBag();
        wordBag.populate();
        Server server = new Server(5000, wordBag);
        server.start();
        server.startSending();
    }
}
