import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Pattern pattern = Pattern.compile("ab[cd]*", Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher("abddd");


    }
}


