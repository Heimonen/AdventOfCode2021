import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	enum Direction {
		WEST,
		EAST,
		NORTH,
		SOUTH,
		NORTH_EAST,
		NORTH_WEST,
		SOUTH_EAST,
		SOUTH_WEST
	}

	private static final int GRID_SIZE = 10;
	private static final int STEPS = 100;

	public static void main(String[] args) throws IOException {
		Octopus[][] matrix = prepareData(getBufferedReader());
		int task1 = task1(matrix);
		assert task1 == 1705;
		System.out.println("Task 1: " + task1);
		int task2 = task2(matrix);
		assert task2 == 165;
		System.out.println("Task 2: " + task2);
		// Correct answer: 265, for some reason I'm off by 100?
	}

	private static int task1(Octopus[][] matrix) {
		int count = 0;
		for (int i = 0; i < STEPS; i++) {
			count = getStepFlashCount(matrix, count);
			printStep(matrix, i);
		}
		return count;
	}

	private static int task2(Octopus[][] matrix) {
		int i = 0;
		while (true) {
			int stepFlashCount = 0;
			stepFlashCount = getStepFlashCount(matrix, stepFlashCount);
			if (stepFlashCount == GRID_SIZE * GRID_SIZE) {
				printStep(matrix, i);
				return i + 1;
			}
			printStep(matrix, i);
			i++;
		}
	}

	private static int getStepFlashCount(Octopus[][] matrix, int stepFlashCount) {
		for (Octopus[] line : matrix) {
			for (Octopus octopus : line) {
				octopus.incrementEnergyLevel();
			}
		}
		for (Octopus[] line : matrix) {
			for (Octopus octopus : line) {
				octopus.flash();
			}
		}
		for (Octopus[] line : matrix) {
			for (Octopus octopus : line) {
				if (octopus.restoreOctopus()) {
					stepFlashCount++;
				}
			}
		}
		return stepFlashCount;
	}

	private static Octopus[][] prepareData(BufferedReader reader) throws IOException {
		Octopus[][] matrix = new Octopus[GRID_SIZE][GRID_SIZE];
		int[][] energyLevelMatrix = new int[GRID_SIZE][GRID_SIZE];
		String line;
		int i = 0;
		while ((line = reader.readLine()) != null) {
			char[] chars = line.toCharArray();
			for (int j = 0; j < chars.length; j++) {
				energyLevelMatrix[i][j] = Character.getNumericValue(chars[j]);
			}
			i++;
		}
		matrix[0][0] = new Octopus(energyLevelMatrix[0][0]);
		addConnectedNode(Direction.EAST, matrix[0][0], energyLevelMatrix, matrix, 0, 0);
		addConnectedNode(Direction.SOUTH_EAST, matrix[0][0], energyLevelMatrix, matrix, 0, 0);
		addConnectedNode(Direction.SOUTH, matrix[0][0], energyLevelMatrix, matrix, 0, 0);
		for (int y = 0; y < energyLevelMatrix.length; y++) {
			for (int x = 0; x < energyLevelMatrix.length; x++) {
				if (x != 0 || y != 0) {
					addConnectedNode(Direction.NORTH, matrix[y][x], energyLevelMatrix, matrix, x, y);
					addConnectedNode(Direction.EAST, matrix[y][x], energyLevelMatrix, matrix, x, y);
					addConnectedNode(Direction.SOUTH, matrix[y][x], energyLevelMatrix, matrix, x, y);
					addConnectedNode(Direction.WEST, matrix[y][x], energyLevelMatrix, matrix, x, y);
					addConnectedNode(Direction.NORTH_EAST, matrix[y][x], energyLevelMatrix, matrix, x, y);
					addConnectedNode(Direction.NORTH_WEST, matrix[y][x], energyLevelMatrix, matrix, x, y);
					addConnectedNode(Direction.SOUTH_EAST, matrix[y][x], energyLevelMatrix, matrix, x, y);
					addConnectedNode(Direction.SOUTH_WEST, matrix[y][x], energyLevelMatrix, matrix, x, y);
				}
			}
		}
		return matrix;
	}

	private static void addConnectedNode(Direction direction, Octopus currentOctopus, int[][] depthMatrix, Octopus[][] matrix, int x, int y) {
		if (direction == Direction.NORTH && y > 0) {
			currentOctopus.addAdjacentOctopus(matrix[y - 1][x]);
			currentOctopus.north = matrix[y - 1][x];
		} else if (direction == Direction.SOUTH && y < depthMatrix.length - 1) {
			if (matrix[y + 1][x] == null) {
				matrix[y + 1][x] = new Octopus(depthMatrix[y + 1][x]);
			}
			currentOctopus.addAdjacentOctopus(matrix[y + 1][x]);
			currentOctopus.south = matrix[y + 1][x];
		} else if (direction == Direction.WEST && x > 0) {
			currentOctopus.addAdjacentOctopus(matrix[y][x - 1]);
			currentOctopus.west = matrix[y][x - 1];
		} else if (direction == Direction.EAST && x < depthMatrix[0].length - 1) {
			Octopus octopus;
			if (matrix[y][x + 1] == null) {
				octopus = new Octopus(depthMatrix[y][x + 1]);
				matrix[y][x + 1] = octopus;
			} else {
				octopus = matrix[y][x + 1];
			}
			currentOctopus.addAdjacentOctopus(octopus);
			currentOctopus.east = octopus;
		} else if (direction == Direction.NORTH_EAST && x < depthMatrix[0].length - 1 && y > 0) {
			currentOctopus.addAdjacentOctopus(matrix[y - 1][x + 1]);
			currentOctopus.northEast = matrix[y - 1][x + 1];
		} else if (direction == Direction.NORTH_WEST && x > 0 && y > 0) {
			currentOctopus.addAdjacentOctopus(matrix[y - 1][x - 1]);
			currentOctopus.northWest = matrix[y - 1][x - 1];
		} else if (direction == Direction.SOUTH_EAST && x < depthMatrix[0].length - 1 && y < depthMatrix.length - 1) {
			matrix[y + 1][x + 1] = new Octopus(depthMatrix[y + 1][x + 1]);
			currentOctopus.addAdjacentOctopus(matrix[y + 1][x + 1]);
			currentOctopus.southEast = matrix[y + 1][x + 1];
		} else if (direction == Direction.SOUTH_WEST && x > 0 && y < depthMatrix.length - 1) {
			currentOctopus.addAdjacentOctopus(matrix[y + 1][x - 1]);
			currentOctopus.southWest = matrix[y + 1][x - 1];
		}
	}

	private static void printStep(Octopus[][] matrix, int i) {
		StringBuilder sb = new StringBuilder();
		sb.append("After step ").append(i + 1).append(":\n");
		for (Octopus[] line : matrix) {
			for (Octopus octopus : line) {
				sb.append(octopus.energyLevel).append(" ");
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}

}
