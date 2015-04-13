package me.bugsyftw.heroeshop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;

public class SpawnCollection {

	private Map<String, Location> locations;

	public SpawnCollection(Map<String, Location> locations) {
		this.locations = locations;
	}

	public SpawnCollection() {
		this.locations = new HashMap<String, Location>();
	}

	public boolean addSpawn(String name, Location loc) {
		locations.put(name, loc);
		toConfig(name, loc);
		return true;
	}

	public void addSpawns(Map<String, Location> locs) {
		for (Entry<String, Location> l : locs.entrySet()) {
			addSpawn(l.getKey(), l.getValue());
		}
	}

	public boolean removeSpawn(String name) {

		if (locations.containsKey(name)) {
			locations.remove(name);
			return true;
		}

		return false;
	}

	public Location getSpawn(String name) {
		if (locations.containsKey(name)) {
			return locations.get(name);
		}

		return null;
	}

	public Location getRandomSpawn() {
		Random r = new Random();
		if (getSpawnSize() > 0) return getSpawnList().get(r.nextInt(getSpawnSize()));

		return null;
	}

	private void toConfig(String name, Location loc) {
		String code = loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getPitch() + ", " + loc.getYaw();
		HeroesHub.getInstance().getConfig().set("Spawn." + name, code);
		HeroesHub.getInstance().saveConfig();
	}

	public static void main(String[] args) {
		String code = "Lobby, -408.4838977815272, 65.0, 1816.6521049751739, 1.3499755, -181.3574";
		String[] decodify = code.split(", ");
		for (int i = 0; i < decodify.length; i++) {
			System.out.println(i + "= " + decodify[i] + "\n");
		}
		System.out.println(decodify.length);
	}

	public static Map<String, Location> getLocations() {
		Map<String, Location> locs = new HashMap<String, Location>();

		for (String key : HeroesHub.getInstance().getConfig().getConfigurationSection("Spawn").getKeys(false)) {
			String loc = HeroesHub.getInstance().getConfig().getString("Spawn." + key);
			String[] decodify = loc.split(", ");
			World w = HeroesHub.getInstance().getServer().getWorld(decodify[0]);
			double x = Double.valueOf(decodify[1]);
			double y = Double.valueOf(decodify[2]);
			double z = Double.valueOf(decodify[3]);
			float pitch = Float.valueOf(decodify[5]);
			float yaw = Float.valueOf(decodify[4]);
			Location loca = new Location(w, x, y, z, pitch, yaw);
			locs.put(key, loca);
		}
		return locs;
	}

	public int getSpawnSize() {
		return locations.size();
	}

	public List<Location> getSpawnList() {
		return new ArrayList<Location>(locations.values());
	}

	public List<String> getSpawnNameList() {
		return new ArrayList<String>(locations.keySet());
	}

	public Collection<Location> getLocationsCollection() {
		return locations.values();
	}

	public Map<String, Location> getLocationsMap() {
		return locations;
	}
}
