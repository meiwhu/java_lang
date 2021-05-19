import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CHMDemo {
    public static ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        Path pathRoot = Path.of(".");
        for (Path p : descendants(pathRoot)) {
            if (p.getFileName().toString().endsWith(".java"))
                executor.execute(() -> process(p));
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
        map.entrySet().stream().sorted(Comparator.comparing((Function<Entry<String, Long>, Long>) Entry::getValue).reversed()).forEach((e) -> {
            if (e.getValue() >= 10) {
                System.out.println(e.getKey() + " occurs " + e.getValue() + " times.");
            }
        });
    }

    public static Set<Path> descendants(Path rootPath) throws IOException {
        try (Stream<Path> entries = Files.walk(rootPath)) {
            return entries.collect(Collectors.toSet());
        }
    }

    public static void process(Path file) {
        try (var in = new Scanner(file)) {
            while (in.hasNext()) {
                String word = in.next();
                map.merge(word, 1L, Long::sum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
