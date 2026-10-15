import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
        Scanner scanner = new Scanner(System.in);
        System.out.print("$ ");
        String command = scanner.next();
        System.out.println("{%s}: command not found", command);
    }
}
