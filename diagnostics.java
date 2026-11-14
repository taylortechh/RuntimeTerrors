import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

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
}







