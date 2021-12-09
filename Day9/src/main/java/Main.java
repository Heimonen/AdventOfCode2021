import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * My idea was to prepare the data into a linked Node data structure. While it was easy to implement the logic
 * in the tasks, I'm not sure if it was worth the effort.
 */
public class Main {

	enum Direction {
		LEFT,
		RIGHT,
		UP,
		DOWN
	}

	public static void main(String[] args) throws IOException {
		Node firstNode = prepareData(getBufferedReader());
		int task1 = findLowPoints(firstNode, false);
		int task2 = findLowPoints(firstNode, true);
		assert task1 == 528;
		assert task2 == 920448;
		System.out.println("Task 1: " + task1);
		System.out.println("Task 2: " + task2);
	}

	private static Node prepareData(BufferedReader reader) throws IOException {
		Node[][] matrix = new Node[100][100];
		int[][] depthMatrix = new int[100][100];
		String line;
		int i = 0;
		while ((line = reader.readLine()) != null) {
			char[] chars = line.toCharArray();
			for (int j = 0; j < chars.length; j++) {
				depthMatrix[i][j] = Character.getNumericValue(chars[j]);
			}
			i++;
		}
		Node firstNode = new Node(depthMatrix[0][0]);
		matrix[0][0] = firstNode;
		addConnectedNode(Direction.RIGHT, firstNode, depthMatrix, matrix, 0, 0);
		addConnectedNode(Direction.DOWN, firstNode, depthMatrix, matrix, 0, 0);
		for (int y = 0; y < depthMatrix.length; y++) {
			for (int x = 0; x < depthMatrix[0].length; x++) {
				if (x != 0 || y != 0) {
					Node currentNode = matrix[y][x];
					addConnectedNode(Direction.LEFT, currentNode, depthMatrix, matrix, x, y);
					addConnectedNode(Direction.UP, currentNode, depthMatrix, matrix, x, y);
					addConnectedNode(Direction.RIGHT, currentNode, depthMatrix, matrix, x, y);
					addConnectedNode(Direction.DOWN, currentNode, depthMatrix, matrix, x, y);
				}
			}
		}
		return firstNode;
	}

	private static void addConnectedNode(Direction direction, Node currentNode, int[][] depthMatrix, Node[][] matrix, int x, int y) {
		if (direction == Direction.UP && y > 0) {
			Node node = matrix[y - 1][x];
			currentNode.setNorthNode(node);
		} else if (direction == Direction.DOWN && y < depthMatrix.length - 1) {
			matrix[y + 1][x] = new Node(depthMatrix[y + 1][x]);
			currentNode.setSouthNode(matrix[y + 1][x]);
		} else if (direction == Direction.LEFT && x > 0) {
			Node node = matrix[y][x - 1];
			currentNode.setWestNode(node);
		} else if (direction == Direction.RIGHT) {
			if (x < depthMatrix[0].length - 1) {
				Node node;
				if (matrix[y][x + 1] == null) {
					node = new Node(depthMatrix[y][x + 1]);
					matrix[y][x + 1] = node;
				} else {
					node = matrix[y][x + 1];
				}
				currentNode.setNextNode(node);
				currentNode.setEastNode(node);
			} else if (y != depthMatrix.length - 1) {
				currentNode.setNextNode(matrix[y + 1][0]);
			}
		}
	}

	private static int findLowPoints(Node node, boolean doCalculateBasins) {
		int totalRiskLevel = 0;
		PriorityQueue<Integer> basinHeights = new PriorityQueue<>(3, Collections.reverseOrder());
		while (node.getNextNode() != null) {
			Node finalNode = node;
			if (node.getConnectedNodes().stream().allMatch(connectedNode -> finalNode.getHeight() < connectedNode.getHeight())) {
				int riskLevel = node.getHeight() + 1;
				totalRiskLevel += riskLevel;
				if (riskLevel > 0 && doCalculateBasins) {
					basinHeights.add(findBasinSize(node));
				}
			}
			node = node.getNextNode();
		}
		int totalBasinHeight = -1;
		if (doCalculateBasins && basinHeights.size() >= 3) {
			totalBasinHeight = basinHeights.poll() * basinHeights.poll() * basinHeights.poll();
		}
		return doCalculateBasins ? totalBasinHeight : totalRiskLevel;
	}

	private static int findBasinSize(Node node) {
		int basinCount = 0;
		Queue<Node> queue = new LinkedList<>();
		queue.add(node);
		while (!queue.isEmpty()) {
			Node currentNode = queue.poll();
			if (currentNode != null && currentNode.getHeight() != 9 && !currentNode.isMarked()) {
				basinCount++;
				queue.add(currentNode.getNorthNode());
				queue.add(currentNode.getSouthNode());
				queue.add(currentNode.getWestNode());
				queue.add(currentNode.getEastNode());
			}
			if (currentNode != null) {
				currentNode.markNode();
			}
		}
		return basinCount;
	}

	private static BufferedReader getBufferedReader() throws IOException {
		Path path = Paths.get("src/main/resources/input.txt");
		return Files.newBufferedReader(path);
	}

}
