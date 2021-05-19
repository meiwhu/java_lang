import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/C", "dir");
        Process process = processBuilder.start();
        process.onExit().thenAccept((a) -> {
            System.out.println("Done=======================================");
        });
        try (var in = new Scanner(process.getInputStream())) {
            while (in.hasNext()) {
                System.out.println(in.next());
            }
        }
    }
}


