import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) throws IOException {
		long task1 = task1(getBufferedReader());
		long task2 = task2(getBufferedReader());
		assert task1 == 321L;
		assert task2 == 1028926L;
		System.out.println("Task 1: " + task1);
		System.out.println("Task 2: " + task2);
	}

	private static long task1(BufferedReader reader) throws IOException {
		long counter = 0;
		String line;
		Set<Integer> conditions = Set.of(2, 3, 4, 7);
		while ((line = reader.readLine()) != null) {
			counter += Stream.of(line.split(" \\| ")[1].split("\\s+"))
					.filter(combination -> conditions.contains(combination.length()))
					.count();
		}
		return counter;
	}

	private static long task2(BufferedReader reader) throws IOException {
		long counter = 0;
		String line;
		// Determine input mappings and read the output
		while ((line = reader.readLine()) != null) {
			Map<Integer, String> mappings = new HashMap<>();
			String[] parsedLine = line.split(" \\| ");
			List<String> input = List.of(parsedLine[0].split("\\s+"));
			List<String> output = List.of(parsedLine[1].split("\\s+"));
			List<List<Character>> fiveLengthList = new ArrayList<>();
			List<List<Character>> sixLengthList = new ArrayList<>();
			input.forEach((String combination) -> {
				switch (combination.length()) {
					case 2 -> mappings.put(1, combination);
					case 3 -> mappings.put(7, combination);
					case 4 -> mappings.put(4, combination);
					case 7 -> mappings.put(8, combination);
					case 5 -> fiveLengthList.add(convertString(combination));
					case 6 -> sixLengthList.add(convertString(combination));
				}
			});
			// length 6
			List<Character> fourList = convertString(mappings.get(4));
			List<Character> nineList = findMatch(sixLengthList, fourList);
			sixLengthList.removeIf(item -> item.containsAll(nineList));
			List<Character> sevenList = convertString(mappings.get(7));
			List<Character> zeroList = findMatch(sixLengthList, sevenList);
			sixLengthList.removeIf(item -> item.containsAll(zeroList));
			putMapping(0, zeroList, mappings);
			putMapping(9, nineList, mappings);
			putMapping(6, sixLengthList.get(0), mappings);

			// Length 5
			List<Character> oneList = convertString(mappings.get(1));
			List<Character> threeList = findMatch(fiveLengthList, oneList);
			fiveLengthList.removeIf(item -> item.containsAll(threeList));
			List<Character> fiveList = fiveLengthList.stream().filter(nineList::containsAll).findAny().orElseThrow();
			fiveLengthList.removeIf(item -> item.containsAll(fiveList));
			putMapping(3, threeList, mappings);
			putMapping(5, fiveList, mappings);
			putMapping(2, fiveLengthList.get(0), mappings);
			// Calculate result
			StringBuilder result = new StringBuilder();
			for (String screen: output) {
				result.append(lookup(screen, mappings));
			}
			counter += Integer.parseInt(result.toString());
		}
		return counter;
	}

	private static List<Character> findMatch(List<List<Character>> targetList, List<Character> toMatch) {
		return targetList.stream().filter(it -> it.containsAll(toMatch)).findAny().orElseThrow();
	}

	private static List<Character> convertString(String toConvert) {
		return toConvert.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
	}

	private static void putMapping(int key, List<Character> value, Map<Integer, String> mappings) {
		mappings.put(key, value.stream()
				.map(String::valueOf)
				.collect(Collectors.joining()));
	}

	private static String lookup(String output, Map<Integer, String> mappings) {
		return mappings.entrySet().stream().filter(it -> {
			List<Character> a = it.getValue().chars().mapToObj(e -> (char) e).collect(Collectors.toList());
			List<Character> b = output.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
			return a.size() == b.size() && a.containsAll(b);
		}).findAny().orElseThrow().getKey().toString();
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}
}
