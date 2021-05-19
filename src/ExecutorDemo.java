import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExecutorDemo {
    public static long occurrences(String word, Path path) {
        try (Scanner in = new Scanner(path)) {
            int count = 0;
            while (in.hasNext()) {
                if (in.next().equals(word)) count++;
            }
            return count;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Set<Path> descendents(Path rootDir) throws IOException {
        try (Stream<Path> entries = Files.walk(rootDir)) {
            return entries.filter(Files::isRegularFile).collect(Collectors.toSet());
        }
    }

    public static Callable<Path> searchForTask(String word, Path path) {
        return () -> {
            try (var in = new Scanner(path)) {
                while (in.hasNext()) {
                    if (in.next().equals(word)) return path;
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Search in " + path + " canceled.");
                        return null;
                    }
                }
                throw new NoSuchElementException();
            }
        };
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        try (var in = new Scanner(System.in)) {
            System.out.print("Enter base directory:");
            String directory = in.nextLine();
            System.out.print("Enter keyword:");
            String keyword = in.nextLine();

            var files = descendents(Path.of(directory));
            var tasks = new ArrayList<Callable<Long>>();
            for (Path file : files) {
                Callable<Long> task = () -> occurrences(keyword, file);
                tasks.add(task);
            }
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            Instant startTime = Instant.now();
            var results = executorService.invokeAll(tasks);
            long total = 0;
            for(var it:results) {
                total += it.get();
            }
            Instant endTime = Instant.now();
            System.out.println("Occurrences of " + keyword + ":" + total);
            System.out.println("Time elapsed: " + Duration.between(startTime, endTime).toMillis() + "ms");

            var searchTasks = new ArrayList<Callable<Path>>();
            for(Path file:files) {
                searchTasks.add(searchForTask(keyword, file));
            }
            Path found = executorService.invokeAny(searchTasks);
            System.out.println(keyword + " occurs in: " + found);

            if(executorService instanceof ThreadPoolExecutor) {
                System.out.println("Largest pool size: " + ((ThreadPoolExecutor) executorService).getLargestPoolSize());
            }
            executorService.shutdown();
        }
    }
}
