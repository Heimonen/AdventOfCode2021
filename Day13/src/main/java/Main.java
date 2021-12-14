import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		List<String[]> instructions = new ArrayList<>();
		Integer[][] matrix = prepareData(getBufferedReader(), instructions);
		int count = task(matrix, instructions);
		System.out.println("Final task size: " + count);
	}

	private static int task(Integer[][] matrix, List<String[]> instructions) {
		int count = 0;
		for (String[] instruction : instructions) {
			Integer[][] left;
			Integer[][] right;
			int foldPos = Integer.parseInt(instruction[1]);
			if (instruction[0].equals("x")) {
				int size = Math.max(foldPos, matrix[0].length - foldPos - 1);
				left = new Integer[matrix.length][size];
				right = new Integer[matrix.length][size];
				for (int y = 0; y < matrix.length; y++) {
					if (foldPos >= 0) System.arraycopy(matrix[y], 0, left[y], 0, foldPos);
					for (int x = foldPos + 1; x < matrix[0].length; x++) {
						right[y][x - foldPos - 1] = matrix[y][x];
					}
					Collections.reverse(Arrays.asList(right[y]));
				}
			} else {
				int size = Math.max(foldPos, matrix.length - foldPos - 1);
				Integer[][] leftInitial = Arrays.copyOfRange(matrix, 0, foldPos);
				Integer[][] rightInitial = Arrays.copyOfRange(matrix, foldPos + 1, matrix.length);
				left = Arrays.copyOf(leftInitial, size);
				right = Arrays.copyOf(rightInitial, size);

				// Fill all null spots with 0 (wasn't necessary for x thanks to nice input data)
				for (int y = 0; y < size - leftInitial.length; y++) {
					left[y] = new Integer[left[0].length];
					for (int x = 0; x < left[0].length; x++) {
						left[y][x] = 0;
					}
				}
				for (int y = size - (size - rightInitial.length); y < right.length; y++) {
					right[y] = new Integer[right[0].length];
					for (int x = 0; x < right[0].length; x++) {
						right[y][x] = 0;
					}
				}
				Collections.reverse(Arrays.asList(right));
			}
			matrix = foldPaper(left, right);
		}
		StringBuilder sb = new StringBuilder();
		for (Integer[] integers : matrix) {
			for (int x = 0; x < matrix[0].length; x++) {
				sb.append(integers[x] == 1 ? "X " : "  ");
				count += integers[x];
			}
			sb.append("\n");
		}
		System.out.println(sb);
		return count;
	}

	private static Integer[][] foldPaper(Integer[][] left, Integer[][] right) {
		Integer[][] matrix;
		matrix = new Integer[left.length][left[0].length];
		for (int y = 0; y < left.length; y++) {
			for (int x = 0; x < left[0].length; x++) {
				boolean isDot = left[y][x] == 1 || right[y][x] == 1;
				matrix[y][x] = isDot ? 1 : 0;
			}
		}
		return matrix;
	}

	private static Integer[][] prepareData(BufferedReader reader, List<String[]> instructions) throws IOException {
		String line;
		List<Integer[]> hotCaves = new ArrayList<>();
		int maxX = 0;
		int maxY = 0;
		while ((line = reader.readLine()) != null) {
			String[] hotCave = line.split(",");
			if (hotCave.length == 2) {
				int x = Integer.parseInt(hotCave[0]);
				int y = Integer.parseInt(hotCave[1]);
				maxX = Math.max(x, maxX);
				maxY = Math.max(y, maxY);
				hotCaves.add(new Integer[]{x, y});
			} else if (line.length() > 0){
				String[] instruction = line.split("=");
				instructions.add(new String[]{instruction[0].substring(instruction[0].length() - 1), instruction[1]});
			}
		}
		Integer[][] matrix = new Integer[maxY + 1][maxX + 1];
		Arrays.stream(matrix).forEach(a -> Arrays.fill(a, 0));
		for (Integer[] entry : hotCaves) {
			matrix[entry[1]][entry[0]] = 1;
		}
		return matrix;
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}
}
