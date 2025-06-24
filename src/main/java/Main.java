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
        if (search(validCmds, input[1])) {
            System.out.println(input[1] + "is a shell builtin");
        } else {
            System.out.println(input[1] + ": not found");
        }
    }

    static boolean search(String[] input, String command) {
        for (String s: input) {
            if (s.equals(command)) return true;
        }
        return false;
    }
}
