import java.io.File;
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
                default -> printCNF(commands[0]);
            }
        }
    }

    static void echo(String input) {
        System.out.println(input.substring(5));
    }

    static void printCNF(String input) {
        System.out.println(input + ": command not found");
    }

    static void type(String[] input) {
        String[] validCmds = {"exit", "type", "echo"};
        String pathInput = System.getenv("PATH");
        String[] pathCommands = pathInput.split(";");

        for (String s: validCmds) {
            if (input[1].equals(s)) {
                System.out.println(input[1] + " is a shell builtin");
                return;
            }
        }

        for (String s: pathCommands) {
            File file = new File(s, input[1]);
            if(file.exists()) {
                System.out.println(input[1] + " is " + file.getAbsolutePath());
                return;
            }
        }

        System.out.println(input[1] + ": not found");
    }
}
