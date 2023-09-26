package pl.umcs.oop.words.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordBag {


    private final Random rand = new Random();
    private List<String> words = new ArrayList<>();

    public void populate() {
        String path = "../words/src/main/resources/slowa.txt";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            words = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get() {
        return words.get(rand.nextInt(words.size()));
    }

    public List<String> getWords() {
        return words;
    }
}
