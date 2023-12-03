

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
        new Thread(() -> views.admin.MainServerUI.main(new String[]{}))
                .start();
    }
}




