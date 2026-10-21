import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String PATH = System.getenv("PATH");
    static String[] BUILT_IN_CMDS = new String[]{"echo", "exit", "type"};
    static List<Path> EXECUTABLES = getExecutablesFromPath();
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        label:
        do {
            System.out.print("$ ");
            String input = scanner.nextLine();
            String[] parts = input.trim().split("\\s+");
            String command = parts[0];
            String arg = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));

            switch (command) {
                case "exit":
                    break label;
                case "echo":
                    System.out.println(arg);
                    break;
                case "type":
                    String target = arg.trim();
                    if (Arrays.asList(BUILT_IN_CMDS).contains(target)) {
                        System.out.println(target + " is a shell builtin");
                        break;
                    }
                    Path found = findExecutable(target);
                    if (found != null) {
                        System.out.println(target + " is " + found);
                    } else {
                        System.out.println(target + ": not found");
                    }
                    break;
                default:
                    if (findExecutable(command) != null) {
                        Process process = new ProcessBuilder(parts)
                                .inheritIO()
                                .start();
                        process.waitFor();

                    } else {
                        System.out.println(command + ": command not found");
                    }

                    break;
            }
        } while (true);
    }

    static List<Path> getExecutablesFromPath() {
        List<Path> execs = new ArrayList<>();
        if (PATH == null || PATH.isEmpty()) {
            return execs;
        }

        for (String dir: PATH.split(File.pathSeparator)) {
            if (dir.isEmpty()) continue;

            Path dirPath = Paths.get(dir);
            if (!Files.isDirectory(dirPath)) continue;

            try (DirectoryStream<Path> entries = Files.newDirectoryStream(dirPath)) {
                for (Path entry: entries) {
                    if (Files.isRegularFile(entry) && Files.isExecutable(entry)) {
                        execs.add(entry);
                    }
                }
            } catch (IOException | SecurityException e) {

            }
        }
        return execs;
    }

    static Path findExecutable(String name) {
        for (Path exe : getExecutablesFromPath()) {
            if (exe.getFileName().toString().equals(name)) {
                return exe;
            }
        }
        return null;
    }
}
