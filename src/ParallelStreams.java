import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ParallelStreams {
    public static void main(String[] args) throws IOException {
        String contents = Files.readString(Paths.get("./src/ParallelStreams.java"));
        List<String> wordList = List.of(contents.split("\\PL+"));

        // very bad code
        int[] shortWords = new int[10];
        wordList.parallelStream().forEach(s -> {
            if(s.length() < 10) shortWords[s.length()]++;
        });
        System.out.println("shortWords = " + Arrays.toString(shortWords));

        // bad code
        Arrays.fill(shortWords, 0);
        wordList.parallelStream().forEach(s -> {
            if(s.length() < 10) shortWords[s.length()]++;
        });
        System.out.println("shortWords = " + Arrays.toString(shortWords));

        // remedy
        Map<Integer, Long> shortWordCounts = wordList.parallelStream().filter(s -> s.length() < 10).collect(Collectors.groupingBy(String::length, Collectors.counting()));
        System.out.println("shortWordCounts = " + shortWordCounts);

        // downstream order not deterministic
        ConcurrentMap<Integer, List<String>> result = wordList.parallelStream().collect(Collectors.groupingByConcurrent(String::length));
        System.out.println("result = " + result);

        ConcurrentMap<Integer, Long> wordCounts = wordList.parallelStream().collect(Collectors.groupingByConcurrent(String::length, Collectors.counting()));
        System.out.println("wordCounts = " + wordCounts);
    }
}
