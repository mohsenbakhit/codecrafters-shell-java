package builtins;

import java.util.List;

import shell.Shell;

@FunctionalInterface
public interface Builtin {
    void run(List<String> args, Shell shell) throws Exception;
}
