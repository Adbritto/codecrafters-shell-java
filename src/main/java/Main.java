import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input.trim().equals("exit 0")) {
                break;
            } else if (input.trim().contains("echo")) {
                System.out.println(input.substring(4));
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }
}
