import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

	private static final List<Character> openingCharacters = Arrays.asList('[', '(', '{', '<');

	public static void main(String[] args) throws IOException {
		int task1 = task1(getBufferedReader());
		assert task1 == 367059;
		System.out.println("Task 1: " + task1);
	}

	private static int task1(BufferedReader reader) throws IOException {
		String line;
		int score = 0;
		while ((line = reader.readLine()) != null) {
			score += parseLine(line);
		}
		return score;
	}

	private static int parseLine(String line) {
		Stack<Character> stack = new Stack<>();
		for (char c: line.toCharArray()) {
			if (openingCharacters.contains(c)) {
				stack.add(c);
			} else  if (isCorruptCharacter(stack.pop(), c)){
				System.out.println("Syntax error on " + c);
				return switch (c) {
					case ')' -> 3;
					case ']' -> 57;
					case '}' -> 1197;
					case '>' -> 25137;
					default -> 0;
				};
			}
		}
		return 0;
	}

	private static boolean isCorruptCharacter(Character left, Character right) {
		Character matchingRight = switch (left) {
			case '(' -> ')';
			case '[' -> ']';
			case '{' -> '}';
			case '<' -> '>';
			default -> '0';
		};
		return matchingRight != right;
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}
}
