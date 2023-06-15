package pl.umcs.oop.words;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime {
    private LocalTime currentTime;

    public CurrentTime() {
        this.currentTime = LocalTime.now();
    }
    public LocalTime getCurrentTime() {
        return currentTime;
    }
    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        String time = formattedTime;
        return time;
    }
}
