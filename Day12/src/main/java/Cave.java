import java.util.HashSet;
import java.util.Set;

public class Cave {
	String name;
	boolean isBig;
	Set<Cave> connectedCaves;

	Cave(String name) {
		this.name = name;
		isBig = Character.isUpperCase(name.charAt(0));
		connectedCaves = new HashSet<>();
	}

	void addConnection(Cave cave) {
		connectedCaves.add(cave);
	}

	@Override
	public String toString() {
		return name;
	}
}
