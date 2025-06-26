import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String cmd;
		String arg = null;

		while(true) {
			System.out.print("$ ");
			String input = scanner.nextLine();

			if (input.contains(" ")) {
				String[] commands = input.split(" ", 2);
				cmd = commands[0];
				arg = commands[1];
			} else cmd = input;


			switch (cmd) {
				case "exit" -> System.exit(Integer.parseInt(arg));
				case "type" -> type(arg);
				case "echo" -> echo(arg);
				case "pwd" -> pwd();
				case "cd" -> cd(arg);
				default -> otherCmd(cmd, arg);
			}
		}
	}

	static void echo(String input) {
		System.out.println(input);
	}

	static void cd(String path) {
		try {
			if (path.contentEquals("~")) {
				System.setProperty("user.dir", System.getenv("HOME"));
				return;
			}
			Path targetDir = Path.of(path);
			Path cwd = Path.of(System.getProperty("user.dir"));
			Path p = cwd.resolve(targetDir);
			if (Files.isDirectory(p)) {
				System.setProperty("user.dir", p.toAbsolutePath().normalize().toString());
			} else {
				System.out.println("cd: " + path + ": No such file or directory");
			}
		} catch (NullPointerException npe) {
			System.out.println("cd: " + path + ": No such file or directory");
		}
	}

	static void pwd() {
		System.out.println(System.getProperty("user.dir"));
	}

	static void otherCmd(String cmd, String args) {
		if (!runFile(cmd, args)) {
			printCNF(cmd);
		}
	}

	static boolean runFile(String cmd, String args) {
		String[] cmdPaths = System.getenv("PATH").split(File.pathSeparator);

		List<String> cmdList = listArgs(cmd, args);

		for (String path: cmdPaths) {
			File file = new File(path, cmd);
			if (file.canExecute() && file.exists()) {
				ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
				try {
					Process process = processBuilder.start();
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line;

					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}

					reader.close();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	static List<String> listArgs(String cmd, String args) {
		if (args != null) {
			List<String> cmdList = new ArrayList<>();
			cmdList.add(cmd);

			cmdList.addAll(Arrays.asList(args.trim().split("\\s+")));
			return cmdList;
		}
		return List.of();
	}

	static void printCNF(String input) {
		System.err.println(input + ": command not found");
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

	static void type(String input) {
		String[] builtins = {"exit", "type", "echo", "pwd", "cd"};
		String path = getPath(input);


		for (String s: builtins) {
			if (input.equals(s)) {
				System.out.println(input + " is a shell builtin");
				return;
			}
		}

		if (path != null) {
			System.out.println(input + " is " + path);
			return;
		}
		System.out.println(input + ": not found");
	}
}
