import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path absolute = Paths.get("D:", "home", "alice");
        Path relative = Paths.get("myprog", "second");
        System.out.println("absolute = " + absolute.isAbsolute());
        System.out.println("relative = " + relative.toAbsolutePath());


        long size = Files.size(Paths.get("./employee.dat"));
        System.out.println("size = " + size);


    }
}


