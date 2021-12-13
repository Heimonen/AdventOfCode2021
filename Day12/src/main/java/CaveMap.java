import java.util.HashMap;
import java.util.Map;

public class CaveMap {
	private final Map<String, Cave> caves;

	public CaveMap() {
		caves = new HashMap<>();
	}

	public void addCave(String start, String end) {
		Cave startCave = caves.get(start);
		Cave endCave = caves.get(end);
		if (startCave == null) {
			startCave = new Cave(start);
			caves.put(start, startCave);
		}
		if (endCave == null) {
			endCave = new Cave(end);
			caves.put(end, endCave);
		}
		startCave.addConnection(endCave);
		endCave.addConnection(startCave);
	}

	public Cave getCave(String name) {
		return caves.get(name);
	}
}
