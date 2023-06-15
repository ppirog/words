package pl.umcs.oop.words;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pl.umcs.oop.words.client.ServerThread;
import pl.umcs.oop.words.server.Server;
import pl.umcs.oop.words.server.WordBag;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    private final List<String> fullWords = new ArrayList<>();
    public TextField filterField;
    public ListView<String> wordList;
    public Label wordCountLabel;
    public Label currentCountLabel;
    private ServerThread serverThread;
    @FXML
    private Label welcomeText;
    private Server server;
    private WordBag wordBag;
    private boolean doJustOneTimeFilterList = false;
    private boolean doJustOneTimeAddToList = false;


    public void initialize() {

        wordBag = new WordBag();
        wordBag.populate();
        createServer();
        server.startSending();
        serverThread = new ServerThread("localhost", 5000, this);
        serverThread.setDaemon(true);
        serverThread.start();

    }

    public void addToList(String word, CurrentTime currentTime) {
        Platform.runLater(() -> {
            if (!doJustOneTimeAddToList) {
                wordList.getItems().clear();
                for (String word1 : fullWords) {
                    wordList.getItems().add(word1);
                }
                doJustOneTimeAddToList = true;
            }

            wordList.getItems().add(currentTime.getTime() + " " + word);
            wordCountLabel.setText(String.valueOf(wordList.getItems().size()));
            currentCountLabel.setText(String.valueOf(wordList.getItems().size()));
            doJustOneTimeFilterList = false;
            wordList.getItems().sort((o1, o2) -> o1.split(" ")[1].compareToIgnoreCase(o2.split(" ")[1]));

            fullWords.add(currentTime.getTime() + " " + word);


        });
    }

    public void createServer() {
        server = new Server(5000, wordBag);
        server.setDaemon(true);
        server.start();
    }


    public TextField getFilterField() {
        return filterField;
    }

    public void setFilterField(String word, CurrentTime currentTime) {
        String filter = filterField.getText();

        List<String> filteredList = new ArrayList<>();
        for (String word1 : fullWords) {
            String[] temp = word1.split(" ");
            if (temp[1].startsWith(filter)) {
                filteredList.add(word1);
            }
        }

        Platform.runLater(() -> {

            if (!doJustOneTimeFilterList) {
                wordList.getItems().clear();
                for (String word1 : filteredList) {
                    wordList.getItems().add(word1);
                }
                doJustOneTimeFilterList = true;
            }
            if (word.startsWith(filter)) {
                wordList.getItems().add(currentTime.getTime() + " " + word);

            }
            currentCountLabel.setText(String.valueOf(wordList.getItems().size()));
            wordCountLabel.setText(String.valueOf(fullWords.size()));
            doJustOneTimeAddToList = false;
            wordList.getItems().sort((o1, o2) -> o1.split(" ")[1].compareToIgnoreCase(o2.split(" ")[1]));

            fullWords.add(currentTime.getTime() + " " + word);
            //TODO: add to database

        });
    }
}