import java.util.ArrayList;
import java.util.List;

public class Octopus {
	int energyLevel;
	boolean hasFlashed;
	final List<Octopus> adjacentOctopuses;

	Octopus north;
	Octopus east;
	Octopus south;
	Octopus west;
	Octopus northEast;
	Octopus northWest;
	Octopus southEast;
	Octopus southWest;

	Octopus(int energyLevel) {
		this.energyLevel = energyLevel;
		adjacentOctopuses = new ArrayList<>();
	}

	void addAdjacentOctopus(Octopus octopus) {
		adjacentOctopuses.add(octopus);
	}

	void incrementEnergyLevel() {
		energyLevel++;
	}

	void flash() {
		if (energyLevel > 9 && !hasFlashed) {
			hasFlashed = true;
			adjacentOctopuses.forEach(Octopus::incrementEnergyLevelOnFlash);
		}
	}

	boolean restoreOctopus() {
		if (hasFlashed) {
			energyLevel = 0;
			hasFlashed = false;
			return true;
		}
		return false;
	}

	private void incrementEnergyLevelOnFlash() {
		incrementEnergyLevel();
		flash();
	}
}
