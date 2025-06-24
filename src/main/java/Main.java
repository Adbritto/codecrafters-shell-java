import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            String[] commands = input.split(" ");

            switch (commands[0]) {
                case "exit" -> System.exit(Integer.parseInt(commands[1]));
                case "type" -> type(commands);
                case "echo" -> echo(input);
                default -> printCNF(commands);
            }
        }
    }

    static void echo(String input) {
        System.out.println(input.substring(5));
    }

    static void printCNF(String[] input) {
        if (getPath(input[0]) != null) {
            try {
                Process process = Runtime.getRuntime().exec(input);
                process.getInputStream().transferTo(System.out);
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.err.println(input[0] + ": command not found");
    }

    static String getPath(String input) {
        String pathInput = System.getenv("PATH");
        String[] pathCommands = pathInput.split(File.pathSeparator);

        for (String p: pathCommands) {
            Path fullPath = Path.of(p, input);
            if (Files.isRegularFile(fullPath)) {
                return fullPath.toString();
            }
        }
        return null;
    }

    static void type(String[] input) {
        String[] validCmds = {"exit", "type", "echo"};
        String path = getPath(input[1]);

        if (path != null) {
            System.out.println(input[1] + " is " + path);
            return;
        }

        for (String s: validCmds) {
            if (input[1].equals(s)) {
                System.out.println(input[1] + " is a shell builtin");
                return;
            }
        }

        System.out.println(input[1] + ": not found");
    }
}
