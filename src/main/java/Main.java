import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("$ ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {break;}
            else if (command.startsWith("echo ")) {System.out.println(command.substring(5));}
            else if (command.startsWith("type ")) {
                switch (command.substring(5)) {
                    case "echo":
                        System.out.println("echo is a shell builtin");
                        break;
                    case "exit":
                        System.out.println("exit is a shell builtin");
                        break;
                    case "type":
                        System.out.println("type is a shell builtin");
                        break;
                    default:
                        System.out.println(command.substring(5) + ": not found");
                }
            }
            else {System.out.println(command + ": command not found");}
        } while (true);
    }
}
