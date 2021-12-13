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
		CaveMap caveMap = prepareData(getBufferedReader());
		int task1 = findPaths(caveMap, false);
		int task2 = findPaths(caveMap, true);
		System.out.println("Task 1: " + task1);
		System.out.println("Task 2: " + task2);
		assert task1 == 4792;
		assert task2 == 133360;
	}

	private static int findPaths(CaveMap caveMap, boolean isTask2) {
		Stack<List<Cave>> queue = new Stack<>();
		List<List<Cave>> paths = new ArrayList<>();
		Cave rootCave = caveMap.getCave("start");
		queue.add(new ArrayList<>(List.of(rootCave)));
		while (!queue.isEmpty()) {
			List<Cave> cavePath = queue.pop();
			for (Cave adjacentCave : cavePath.get(cavePath.size() - 1).connectedCaves) {
				if (adjacentCave.toString().equals("end")) {
					paths.add(Stream.of(cavePath, new ArrayList<>(List.of(adjacentCave)))
							.flatMap(Collection::stream)
							.collect(Collectors.toList()));
				} else if (adjacentCave.isBig || !cavePath.contains(adjacentCave) || canVisitSecondSmallCave(cavePath, adjacentCave, isTask2)) {
					queue.add(Stream.of(cavePath, new ArrayList<>(List.of(adjacentCave)))
							.flatMap(Collection::stream)
							.collect(Collectors.toList()));
				}
			}
		}
		return paths.size();
	}

	private static boolean canVisitSecondSmallCave(List<Cave> caves, Cave adjacentCave, boolean isTask2) {
		Set<Cave> set = new HashSet<>();
		boolean containsDuplicate = false;
		for (Cave cave : caves) {
			if (!Character.isUpperCase(cave.toString().charAt(0))) {
				if (!set.add(cave)) {
					containsDuplicate = true;
				}
			}
		}
		return !containsDuplicate && Collections.frequency(caves, adjacentCave) < 2 && !adjacentCave.toString().equals("start") && isTask2;
	}

	private static CaveMap prepareData(BufferedReader reader) throws IOException {
		String line;
		CaveMap caveMap = new CaveMap();
		while ((line = reader.readLine()) != null) {
			String[] connections = line.split("-");
			caveMap.addCave(connections[0], connections[1]);
		}
		return caveMap;
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}

}
