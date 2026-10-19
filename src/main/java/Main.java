import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("$ ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {break;}
            if (command.startsWith("echo ")) {System.out.println(command.substring(5));}
            System.out.println(command + ": command not found");
        } while (true);
    }
}
