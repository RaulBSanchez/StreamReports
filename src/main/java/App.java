import java.io.IOException;

public class App {
    // Fetches Big Fishing Creek's latest USGS reading, stores it on the stream and prints it.
    public static void main(String[] args) {
        Stream creek = new Stream(
            "Big Fishing Creek",
            "Lamar",
            "PA",
            "01548030");

        try {
            creek.addReading(new UsgsClient().fetchLatest(creek.getUsgsGaugeId()));
        } catch (IOException e) {
            System.out.println("Couldn't get the live reading: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        creek.displayStream();
    }
}