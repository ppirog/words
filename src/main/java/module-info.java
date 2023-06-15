module pl.umcs.oop.words {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens pl.umcs.oop.words to javafx.fxml;
    exports pl.umcs.oop.words;
}