import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class ProxyTest {
    public static void main(String[] args) {
        var elements = new Object[1000];
        for (int i = 0; i < elements.length; i++) {
            Integer value = i + 1;
            var handler = new TraceHandler(value);
            var proxy = Proxy.newProxyInstance(
                    ClassLoader.getSystemClassLoader(),
                    new Class[]{Comparable.class},
                    handler
            );
            elements[i] = proxy;
        }
        Integer key = new Random().nextInt(elements.length) + 1;

        int ret = Arrays.binarySearch(elements, key);

        if (ret >= 0) {
            System.out.println(elements[ret]);
        }

        try (var in = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (in.hasNext()) {
                String input = in.next();
                System.out.println(input);
                if (Objects.equals(input, "exit")) {
                    System.exit(0);
                }
            }
        }
    }
}

class TraceHandler implements InvocationHandler {
    private final Object target;

    public TraceHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print(target);
        System.out.print("." + method.getName() + "(");
        if (args != null) {
            for (var arg : args) {
                System.out.print(arg);
            }
        }
        System.out.println(")");

        return method.invoke(target, args);
    }
}
