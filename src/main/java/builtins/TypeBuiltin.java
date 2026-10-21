package builtins;

import java.nio.file.Path;
import java.util.List;

import shell.Shell;

public class TypeBuiltin implements Builtin {
    @Override
    public void run(List<String> args, Shell shell) {
        String target = String.join(" ", args);
        if (shell.isBuiltin(target)) {
            System.out.println(target + " is a shell builtin");
            return;
        }
        Path found = shell.findExecutable(target);
        if (found != null) {
            System.out.println(target + " is " + found);
        } else {
            System.out.println(target + ": not found");
        }
    }
}
