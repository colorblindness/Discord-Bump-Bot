package ohare.da.boss;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {
    public static final TimerUtil timer = new TimerUtil();
    public static boolean first = false;
    public static void main(String[] args) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Discord Token:");
        final String token = reader.readLine();
        System.out.println("Token Entered:" + token);
        System.out.print("Enter Channel ID:");
        final String channelID = reader.readLine();
        System.out.println("Channel ID Entered: " + channelID);
        System.out.print("Enter Message:");
        final String message = reader.readLine();
        System.out.println("Message Entered: " + message);
        System.out.print("Enter Delay in Milliseconds:");
        long delay = 0;
        try {
            delay = Long.parseLong(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Delay Entered: " + delay);
        System.out.println("Starting!");
        final String content = "{\"content\":\"" + message + "\"}";
        final byte[] json = content.getBytes(StandardCharsets.UTF_8);
        final URL url = new URL("https://discordapp.com/api/v7/channels/" + channelID + "/messages");
        timer.reset();
        while (true) {
            if (!first || timer.sleep(delay)) {
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.addRequestProperty("User-Agent", "Mozilla/4.76");
                connection.addRequestProperty("Authorization", token);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Content-Length", String.valueOf(json.length));
                try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
                    writer.write(json);
                    writer.flush();
                } finally {
                    connection.getResponseCode();
                    connection.disconnect();
                }
                if (!first) {
                    timer.reset();
                    first = true;
                }
                System.out.println("Sent the message \""+message+"\" to the channel " + channelID+" with the delay "+ delay+"!");
            }
        }
    }
}