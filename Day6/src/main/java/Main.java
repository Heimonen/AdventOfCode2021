import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("After 80 days there are " + task(getBufferedReader(), 80) + " lanternfish");
		System.out.println("After 256 days there are " + task(getBufferedReader(), 256) + " lanternfish");
	}

	private static long task(BufferedReader reader, int days) throws IOException {
		long[] input = Stream.of(reader.readLine().split(","))
				.mapToLong(Long::parseLong)
				.toArray();
		SortedMap<Integer, Long> lanternfish = new TreeMap<>();
		for (long fish: input) {
			if (lanternfish.containsKey((int) fish)) {
				lanternfish.put((int) fish, lanternfish.get((int) fish) + 1);
			} else {
				lanternfish.put((int) fish, 1L);
			}
		}
		IntStream.range(0, 9).forEach(i ->  {
			if (!lanternfish.containsKey(i)) {
				lanternfish.put(i, 0L);
			}
		});
		for (int i = 0; i < days; i++) {
			long newBornFish = 0L;
			long eggLayingFish = 0L;
			for (Map.Entry<Integer, Long> entry: lanternfish.entrySet()) {
				if (entry.getKey() == 0) {
					newBornFish = entry.getValue();
					eggLayingFish = entry.getValue();
					entry.setValue(0L);
				} else {
					int key = entry.getKey();
					lanternfish.put(key - 1, entry.getValue() + lanternfish.get(key - 1));
					lanternfish.put(key, 0L);
				}
			}
			lanternfish.put(6, lanternfish.get(6) + eggLayingFish);
			lanternfish.put(8, lanternfish.get(8) + newBornFish);
		}
		return lanternfish.values().stream().mapToLong(val -> val).sum();
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}
}
