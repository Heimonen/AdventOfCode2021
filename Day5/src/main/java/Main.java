import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("Task 1 count: " + findVents(getBufferedReader(), false));
		System.out.println("Task 2 count: " + findVents(getBufferedReader(), true));
	}

	private static int findVents(BufferedReader reader, boolean includeDiagonal) throws IOException {
		String line;
		List<Map<String, Map<String, Integer>>> ventLines = new ArrayList<>();
		int xMax = 0;
		int yMax = 0;
		while ((line = reader.readLine()) != null) {
			String[] coordinates = line.split(" -> ");
			String[] left = coordinates[0].split(",");
			String[] right = coordinates[1].split(",");
			int leftX = Integer.parseInt(left[0]);
			int leftY = Integer.parseInt(left[1]);
			int rightX = Integer.parseInt(right[0]);
			int rightY = Integer.parseInt(right[1]);
			Map<String, Integer> leftPairMap = Map.of("x", leftX, "y", leftY);
			Map<String, Integer> rightPairMap = Map.of("x", rightX, "y", rightY);
			ventLines.add(Map.of("from", leftPairMap, "to", rightPairMap));
			if (Math.max(leftX, rightX) > xMax) {
				xMax = Math.max(leftX, rightX);
			}
			if (Math.max(leftY, rightY) > yMax) {
				yMax = Math.max(leftY, rightY);
			}
		}
		int[][] matrix = new int[yMax + 1][xMax + 1];
		Arrays.stream(matrix).forEach(entry -> Arrays.fill(entry, 0));
		ventLines.forEach(ventLine -> {
			drawLine(ventLine.get("from"), ventLine.get("to"), matrix, includeDiagonal);
		});
		return Arrays.stream(matrix)
				.flatMapToInt(Arrays::stream)
				.boxed()
				.collect(Collectors.toList())
				.stream()
				.reduce(0, (currentCount, ventCount) -> ventCount > 1 ? ++currentCount : currentCount);
	}

	private static void drawLine(Map<String, Integer> fromCoordinate, Map<String, Integer> toCoordinate, int[][] matrix, boolean includeDiagonal) {
		double theta = Math.atan2(fromCoordinate.get("y") - toCoordinate.get("y"), fromCoordinate.get("x") - toCoordinate.get("x"));
		if (theta <= 0) {
			Map<String, Integer> tempFromCoordinate = fromCoordinate;
			fromCoordinate = toCoordinate;
			toCoordinate = tempFromCoordinate;
		}
		int currentX = fromCoordinate.get("x");
		int currentY = fromCoordinate.get("y");
		if (fromCoordinate.get("x").equals(toCoordinate.get("x"))) {
			while (currentY >= toCoordinate.get("y")) {
				matrix[currentY][fromCoordinate.get("x")]++;
				currentY--;
			}
		} else if (fromCoordinate.get("y").equals(toCoordinate.get("y"))) {
			while (currentX <= toCoordinate.get("x")) {
				matrix[fromCoordinate.get("y")][currentX]++;
				currentX++;
			}
		} else if (includeDiagonal) {
			if (currentX < toCoordinate.get("x") && currentY > toCoordinate.get("y")) {
				while (currentX <= toCoordinate.get("x") && currentY >= toCoordinate.get("y")) {
					matrix[currentY][currentX]++;
					currentX++;
					currentY--;
				}
			} else if (currentX > toCoordinate.get("x") && currentY > toCoordinate.get("y")) {
				while (currentX >= toCoordinate.get("x") && currentY >= toCoordinate.get("y")) {
					matrix[currentY][currentX]++;
					currentX--;
					currentY--;
				}
			}
		}
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}
}
