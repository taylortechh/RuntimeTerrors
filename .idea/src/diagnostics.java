import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.swing.JOptionPane;

public class diagnostics {

    public static void main(String[] args) throws Exception {
        String ticketId = "T" + System.currentTimeMillis();
        sendInfo("http://localhost:8089/ticket", ticketId);
    }

    public static String clientInfo() {
        String tempdir = System.getProperty("java.io.tmpdir");
        ProcessBuilder pb1 = new ProcessBuilder("powershell.exe", "-Command", "Get-CimInstance Win32_VideoController | Select-Object Name, AdapterRAM; Get-CimInstance Win32_PhysicalMemory | Select-Object Capacity, Speed");
        ProcessBuilder pb2 = new ProcessBuilder("systeminfo");
        Path filePath = Path.of(tempdir, "info.txt");
        pb1.redirectOutput(filePath.toFile());
        pb2.redirectOutput(ProcessBuilder.Redirect.appendTo(filePath.toFile()));
        try {
            Process process1 = pb1.start();
            process1.waitFor(100L, TimeUnit.SECONDS);
            Process process2 = pb2.start();
            process2.waitFor(100L, TimeUnit.SECONDS);

            String content = Files.readString(filePath);
            return content;
        }

        catch (IOException | InterruptedException e) {
            System.out.println("Error: Diagnostic collection has been interrupted.");
            return "";
        }
    }

    public static void SaveInfo(Path outputFile) throws IOException {
        String diagnosticData = clientInfo();

        if (diagnosticData.isBlank()) {
            throw new IOException("No diagnostic data was returned.");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {

            writer.write("Line\tDiagnosticOutput");
            writer.newLine();

            String[] lines = diagnosticData.split("\\R");

            for (int i = 0; i < lines.length; i++) {
                // Quote the field and escape embedded double quotes.
                String escapedLine = "\"" + lines[i].replace("\"", "\"\"") + "\"";

                writer.write(Integer.toString(i + 1));
                writer.write('\t');
                writer.write(escapedLine);
                writer.newLine();

            }
        }
    }

    public static void sendInfo(String serverUrl, String ticketId) throws IOException, InterruptedException {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "This will send your computer's hardware and system information\n"
                        + "(including computer name and network details) to tech support.\n\nContinue?",
                "Send diagnostics",
                JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            System.out.println("Cancelled by user.");
            return;
        }

        String data = clientInfo();
        if (data.isBlank()) {
            throw new IOException("No diagnostic data was returned.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .header("X-Ticket-Id", ticketId)
                .POST(HttpRequest.BodyPublishers.ofString(data, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Upload failed: HTTP " + response.statusCode());
        }
        JOptionPane.showMessageDialog(null, "Diagnostics sent. Thank you!");
    }

    public static void openFile(Path file) throws IOException {
        if (file == null || !Files.exists(file)) {
            throw new IOException("File does not exist: " + file);
        }

        File target = file.toFile();

        // Preferred: open with the default associated application
        if (Desktop.isDesktopSupported()
                && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(target);
            return;
        }

        // Fallback for Windows if Desktop isn't supported (e.g., headless mode)
        new ProcessBuilder("notepad.exe", target.getAbsolutePath()).start();
    }
}



