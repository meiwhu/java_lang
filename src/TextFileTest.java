import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class TextFileTest {
    public static void main(String[] args) throws IOException {
        Employee[] staff = new Employee[3];

        staff[0] = new Employee("Alice", 10000, 1999, 10, 10);
        staff[1] = new Employee("Bob", 20000, 1995, 1, 1);
        staff[2] = new Employee("Nice", 30000, 1998, 8, 8);

        try (PrintWriter out = new PrintWriter("employee.dat", StandardCharsets.UTF_8)) {
            writeData(staff, out);
        }

        try (Scanner in = new Scanner(new FileInputStream("employee.dat"), StandardCharsets.UTF_8)) {
            Employee[] newStaff = readData(in);
            System.out.println("newStaff = " + Arrays.toString(newStaff));
        }


    }

    private static void writeData(Employee[] employees, PrintWriter printWriter) {
        printWriter.println(employees.length);
        for (var e : employees) {
            writeEmployee(printWriter, e);
        }
    }

    private static void writeEmployee(PrintWriter printWriter, Employee e) {
        printWriter.println(e.getName() + "|" + e.getSalary() + "|" + e.getHireDay());
    }

    public static Employee[] readData(Scanner in) {
        int n = in.nextInt();
        in.nextLine();

        Employee[] employees = new Employee[n];
        for (int i = 0; i < employees.length; i++) {
            employees[i] = readEmployee(in);
        }
        return employees;
    }

    public static Employee readEmployee(Scanner in) {
        String line = in.nextLine();
        String[] tokens = line.split("\\|");
        String name = tokens[0];
        double salary = Double.parseDouble(tokens[1]);
        LocalDate hireDate = LocalDate.parse(tokens[2]);
        int year = hireDate.getYear();
        int month = hireDate.getMonthValue();
        int day = hireDate.getDayOfMonth();
        return new Employee(name, salary, year, month, day);
    }

}

class Employee implements Serializable {
    private String name;
    private double salary;
    private LocalDate hireDay;

    public Employee(String name, double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireDay = LocalDate.of(year, month, day);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getHireDay() {
        return hireDay;
    }

    public void setHireDay(LocalDate hireDay) {
        this.hireDay = hireDay;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                ", hireDay=" + hireDay +
                '}';
    }
}