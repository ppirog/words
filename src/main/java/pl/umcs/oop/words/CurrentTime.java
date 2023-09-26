package pl.umcs.oop.words;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime {
    private final LocalTime currentTime;

    public CurrentTime() {
        this.currentTime = LocalTime.now();
    }

    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return currentTime.format(formatter);
    }
}
