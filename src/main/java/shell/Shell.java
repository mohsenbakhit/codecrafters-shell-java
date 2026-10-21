package shell;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import builtins.Builtin;
import builtins.TypeBuiltin;

public class Shell {
    private final String path = System.getenv("PATH");
    private final Map<String, Builtin> builtins = new LinkedHashMap<>();
    private boolean running = true;

    public Shell() {
        builtins.put("echo", (args, shell) -> System.out.println(String.join(" ", args)));
        builtins.put("exit", (args, shell) -> shell.stop());
        builtins.put("type", new TypeBuiltin());
    }

    public void run() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            String[] parts = input.trim().split("\\s+");
            execute(Arrays.asList(parts));
        }
    }

    public void stop() {
        running = false;
    }

    public boolean isBuiltin(String name) {
        return builtins.containsKey(name);
    }

    private void execute(List<String> tokens) throws Exception {
        String command = tokens.get(0);
        List<String> args = tokens.subList(1, tokens.size());

        Builtin builtin = builtins.get(command);
        if (builtin != null) {
            builtin.run(args, this);
        } else if (findExecutable(command) != null) {
            Process process = new ProcessBuilder(tokens)
                    .inheritIO()
                    .start();
            process.waitFor();
        } else {
            System.out.println(command + ": command not found");
        }
    }

    List<Path> getExecutablesFromPath() {
        List<Path> execs = new ArrayList<>();
        if (path == null || path.isEmpty()) {
            return execs;
        }

        for (String dir: path.split(File.pathSeparator)) {
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

    public Path findExecutable(String name) {
        for (Path exe : getExecutablesFromPath()) {
            if (exe.getFileName().toString().equals(name)) {
                return exe;
            }
        }
        return null;
    }
}
