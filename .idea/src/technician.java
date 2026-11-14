import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class technician {

    public static void main(String[] args) {
        serverSide();
    }

    public static void serverSide() {

        InetSocketAddress addr = new InetSocketAddress(8089);   // was 8080 (AirPlay uses it)
        int backlog = 0;
        String path = "/ticket";
        HttpHandler handler = new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                        InputStream is = exchange.getRequestBody();
                        byte[] bodyByte = is.readAllBytes();

                        Charset charset = StandardCharsets.UTF_8;
                        String body = new String(bodyByte, charset);

                        // Ticket ID comes from the client; make one if it's missing
                        String ticketId = exchange.getRequestHeaders().getFirst("X-Ticket-Id");
                        if (ticketId == null) {
                            ticketId = "T" + System.currentTimeMillis();
                        }
                        if (!ticketId.matches("[A-Za-z0-9-]{1,30}")) {
                            exchange.sendResponseHeaders(400, -1);   // reject unsafe IDs
                            return;
                        }

                        System.out.println("Received ticket " + ticketId + " (" + bodyByte.length + " bytes)");

                        // Save a read-only copy
                        Path ticketFile = Path.of("ticket-" + ticketId + ".txt");
                        Files.write(ticketFile, bodyByte);
                        ticketFile.toFile().setReadOnly();

                        // Show the read-only ticket window
                        final String id = ticketId;
                        SwingUtilities.invokeLater(() -> TicketViewer.show(id, body));

                        // Reply to the client
                        byte[] replyBytes = "Received".getBytes(charset);
                        exchange.sendResponseHeaders(200, replyBytes.length);
                        exchange.getResponseBody().write(replyBytes);
                    } else {
                        exchange.sendResponseHeaders(405, -1);   // only POST allowed
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, -1);
                } finally {
                    exchange.close();
                }
            }
        };
        try {
            HttpServer server = HttpServer.create(addr, backlog);
            server.createContext(path, handler);
            server.start();
            System.out.println("Listening on port 8089...");
        }
        catch (IOException e) {
            System.out.println("Error: Server could not be established");
            e.printStackTrace();
        }
    }
}