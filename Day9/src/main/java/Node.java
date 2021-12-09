import java.util.ArrayList;
import java.util.List;

public class Node {
	private final int height;
	private Node nextNode;
	private Node northNode;
	private Node southNode;
	private Node westNode;
	private Node eastNode;
	private boolean isMarked;

	public Node(int height) {
		this.height = height;
	}

	public List<Node> getConnectedNodes() {
		List<Node> nodes = new ArrayList<>();
		addIfNotNUll(northNode, nodes);
		addIfNotNUll(eastNode, nodes);
		addIfNotNUll(southNode, nodes);
		addIfNotNUll(westNode, nodes);
		return nodes;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node node) {
		nextNode = node;
	}

	public int getHeight() {
		return height;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void markNode() {
		isMarked = true;
	}

	public Node getNorthNode() {
		return northNode;
	}

	public void setNorthNode(Node northNode) {
		this.northNode = northNode;
	}

	public Node getSouthNode() {
		return southNode;
	}

	public void setSouthNode(Node southNode) {
		this.southNode = southNode;
	}

	public Node getWestNode() {
		return westNode;
	}

	public void setWestNode(Node westNode) {
		this.westNode = westNode;
	}

	public Node getEastNode() {
		return eastNode;
	}

	public void setEastNode(Node eastNode) {
		this.eastNode = eastNode;
	}

	private void addIfNotNUll(Node toAdd, List<Node> list) {
		if (toAdd != null) {
			list.add(toAdd);
		}
	}
}
