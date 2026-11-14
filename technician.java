import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class technician {
    public static void serverSide() {

        InetSocketAddress addr = new InetSocketAddress(8080);
        int backlog = 0;
        String path = "/ticket";
        HttpHandler handler = new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    // internet (not finished)
                    InputStream is = exchange.getRequestBody();
                    byte[] bodyByte = is.readAllBytes();


                    // local
                    Charset charset = StandardCharsets.UTF_8;
                    String body = new String(bodyByte, charset);
                    byte[] replyBytes = "Received".getBytes(charset);
                    System.out.println(body);
                    exchange.sendResponseHeaders(200, replyBytes.length);
                    exchange.getResponseBody().write(replyBytes);
                    exchange.getResponseBody().close();
                }
            }
        };
        try {
            HttpServer server = HttpServer.create(addr, backlog);
            server.createContext(path, handler);
            server.start();

        }
        catch (IOException e) {
            System.out.println("Error: Server could not be established");
        }
    }
}
