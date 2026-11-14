import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;

public class diagnostics {
    public static String clientInfo() {
        String tempdir = System.getProperty("java.io.tmpdir");
        ProcessBuilder pb1 = new ProcessBuilder("powershell.exe", "-Command", "Get-CimInstance Win32_VideoController | Select-Object Name, AdapterRAM; Get-CimInstance Win32_PhysicalMemory | Select-Object Capacity, Speed");
        ProcessBuilder pb2 = new ProcessBuilder("systeminfo");
        pb1.redirectOutput(new File(tempdir + "info.txt"));
        pb2.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(tempdir + "info.txt")));
        try {
            Process process1 = pb1.start();
            process1.waitFor(100L, TimeUnit.SECONDS);
            Process process2 = pb2.start();
            process2.waitFor(100L, TimeUnit.SECONDS);

            Path filePath = Path.of(tempdir, "info.txt");
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
}



