import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CompletableFutureDemo {
    private static final Pattern IMG_PATTERN = Pattern.compile("[<]\\s*[iI][mM][gG]\\s*[^>]*[sS][rR][cC]\\s*[=]\\s*['\"]([^'\"]*)['\"][^>]*[>]");
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private URL urlToProcess;

    public static void main(String[] args) {
        try {
            new CompletableFutureDemo().run(new URL("https://baijiahao.baidu.com/s?id=1699809027384580134&wfr=spider&for=pc"));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<String> readPage(URL url) {
        return CompletableFuture.supplyAsync(() -> {
            String contents = null;
            try {
                contents = new String(url.openStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            System.out.println("Read page from " + url);
            return contents;
        }, executorService);
    }

    public List<URL> getImageURLs(String webpage) {
        try {
            var ret = new ArrayList<URL>();
            var matcher = IMG_PATTERN.matcher(webpage);
            while (matcher.find()) {
                var url = new URL(urlToProcess, matcher.group(1));
                ret.add(url);
            }
            System.out.println("Found URLs: " + ret);
            return ret;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public CompletableFuture<List<BufferedImage>> getImages(List<URL> urls) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var ret = new ArrayList<BufferedImage>();
                for (URL url : urls) {
                    ret.add(ImageIO.read(url));
                    System.out.println("Loaded " + url);
                }
                return ret;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }, executorService);
    }

    public void saveImages(List<BufferedImage> images) {
        System.out.println("Saving " + images.size() + " images.");
        try {
            images = images.stream().filter(Objects::nonNull).collect(Collectors.toList());
            System.out.println(images.size());
            for (int i = 0; i < images.size(); i++) {
                ImageIO.write(images.get(i), "PNG", new File(i + "_img.png"));
                System.out.println(i);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        executorService.shutdown();
    }

    public void run(URL url) throws IOException, InterruptedException {
        urlToProcess = url;
        CompletableFuture.completedFuture(url)
                .thenComposeAsync(this::readPage, executorService)
                .thenApply(this::getImageURLs)
                .thenCompose(this::getImages)
                .thenAccept(this::saveImages)
                .whenComplete((ret, exp) -> {
                    if(exp != null) {
                        throw new UncheckedIOException((IOException) exp);
                    }
                });
    }
}
