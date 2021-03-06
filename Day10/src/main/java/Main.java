import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

	private static final List<Character> openingCharacters = Arrays.asList('[', '(', '{', '<');
	private static final List<String> incompleteLines = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		int task1 = task1(getBufferedReader());
		long task2 = task2();
		assert task1 == 367059;
		assert task2 == 1952146692L;
		System.out.println("Task 1: " + task1);
		System.out.println("Task 2: " + task2);
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
		for (char c : line.toCharArray()) {
			if (openingCharacters.contains(c)) {
				stack.add(c);
			} else if (isCorruptCharacter(stack.pop(), c)) {
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
		incompleteLines.add(line);
		return 0;
	}

	private static boolean isCorruptCharacter(Character left, Character right) {
		return getMatchingClosingChar(left) != right;
	}

	private static Character getMatchingClosingChar(Character c) {
		return switch (c) {
			case '(' -> ')';
			case '[' -> ']';
			case '{' -> '}';
			case '<' -> '>';
			default -> '0';
		};
	}

	private static long task2() {
		LinkedList<Long> scores = new LinkedList<>();
		for (String line : incompleteLines) {
			Stack<Character> stack = new Stack<>();
			StringBuilder completionCharacters = new StringBuilder();
			for (char c : line.toCharArray()) {
				if (openingCharacters.contains(c)) {
					stack.add(c);
				} else if (getMatchingClosingChar(stack.peek()) == c) {
					stack.pop();
				}
			}
			long score = 0L;
			while (!stack.isEmpty()) {
				char completionCharacter = getMatchingClosingChar(stack.pop());
				completionCharacters.append(completionCharacter);
				score = score * 5 + switch (completionCharacter) {
					case ')' -> 1L;
					case ']' -> 2L;
					case '}' -> 3L;
					case '>' -> 4L;
					default -> -1L;
				};
			}
			scores.add(score);
			System.out.println("Complete by adding " + completionCharacters + ". Total points: " + score);
		}
		Collections.sort(scores);
		return scores.get((scores.size() - 1) / 2);
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}
}
