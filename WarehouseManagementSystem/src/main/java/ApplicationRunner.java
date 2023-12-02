
//    Server, MainServerUI, MainClientUI
import java.io.IOException;

public class ApplicationRunner {
    public static void main(String[] args) {

        // Start Server
        new Thread(() -> {
            try {
                httpServer.Server.main(new String[]{});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Start admin
        new Thread(() -> views.admin.MainServerUI.main(new String[]{})).start();

        // Start client 1
        new Thread(() -> views.client.MainClientUI.main(new String[]{})).start();

        // Start client 2
        new Thread(() -> views.client.MainClientUI.main(new String[]{})).start();
    }
}




