import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("Task 1: " + findOptimalTargetPosition(getBufferedReader(), Main::calculateFuelConsumptionSimple));
		System.out.println("Task 2: " + findOptimalTargetPosition(getBufferedReader(), Main::calculateExpensiveFuelConsumption));
	}

	private static int findOptimalTargetPosition(BufferedReader reader, BiFunction<Integer, Integer[], Integer> calculateFuelConsumption) throws IOException {
		Integer[] input = Stream.of(reader.readLine().split(","))
				.mapToInt(Integer::parseInt)
				.boxed()
				.toArray(Integer[]::new);
		Integer lowBound = Stream.of(input).min(Comparator.naturalOrder()).orElseThrow(NoSuchElementException::new);
		Integer highBound = Stream.of(input).max(Comparator.naturalOrder()).orElseThrow(NoSuchElementException::new);
		return IntStream.range(lowBound, highBound)
				.map(i -> calculateFuelConsumption.apply(i, input))
				.min()
				.orElseThrow(NoSuchElementException::new);
	}

	private static Integer calculateFuelConsumptionSimple(Integer targetPos, Integer[] startingGrid) {
		return Stream.of(startingGrid).mapToInt(startPos -> Math.abs(startPos - targetPos)).sum();
	}

	private static Integer calculateExpensiveFuelConsumption(Integer targetPos, Integer[] startingGrid) {
		return Stream.of(startingGrid).mapToInt(startPos -> IntStream.range(0, Math.abs(startPos - targetPos) + 1).sum()).sum();
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}
}
