package me.bugsyftw.heroeshop.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import me.bugsyftw.heroeshop.HeroesHub;
import me.bugsyftw.heroeshop.server.ServerInfo.StatusResponse;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class Server {

	public static HashMap<String, Server> servers = new HashMap<String, Server>();
	
	private String ip;
	private int port;
	private Location loc;
	private String sign_display;
	private String bungee_name;
	private String type;
	private String status;
	private int online;
	private int max;
	private int win;

	public Server() {
	}

	public Server(String bungee_name, String sign_display, String ip, int port) {
		this.sign_display = sign_display;
		this.bungee_name = bungee_name;
		this.ip = ip;
		this.port = port;
		servers.put(bungee_name, this);
		String address = ip + ", " + port;
		/*HeroesHub.getInstance().getConfig().set("Server." + bungee_name + ".address", address);
		HeroesHub.getInstance().getConfig().set("Server." + bungee_name + ".display", sign_display);
		HeroesHub.getInstance().saveConfig();*/
	}

	public static Server addServer(String bungee, String name, String ip, int port) {
		Server server = new Server(bungee, name, ip, port);
		servers.put(bungee, server);
		return server;
	}

	public static HashMap<String, Server> getServerMap() {
		return servers;
	}

	public static Set<Entry<String, Server>> getServersEntry() {
		return servers.entrySet();
	}

	public static Server getServer(String name) {
		if (servers.containsKey(name)) {
			return servers.get(name);
		}
		return null;
	}

	public void updateSign() {
		Block b = getLoc().getBlock();
		if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
			Sign sign = (Sign) b.getState();
			ServerInfo info = new ServerInfo();
			info.setAddress(new InetSocketAddress(getIp(), getPort()));
			try {
				StatusResponse response  = info.fetchData();
				setOnline(response.getPlayers().getOnline());
				setMax(response.getPlayers().getMax());
				String[] motd = response.getDescription().split(", ");
				setStatus(motd[0]);
				setType(motd[1]);
			} catch (IOException e) {
				sign.setLine(0, ChatColor.RED + "[Offline]");
				sign.setLine(1, ChatColor.BLACK + "");
				sign.setLine(2, ChatColor.BLACK + "");
				sign.setLine(3, ChatColor.BLACK + "");
				return;
			}
			switch (getType()) {
			case "1v1":
				sign.setLine(0, ChatColor.BLACK + "[1 on 1]");
				break;
			case "FFA":
				sign.setLine(0, ChatColor.BLACK + "[Free For All]");
				break;
			case "Tournament":
				sign.setLine(0, ChatColor.BLACK + "[Tournament]");
				break;
			default:
				break;
			}
			sign.setLine(1, ChatColor.BLACK.toString() + "(" + getOnline() + "/" + getMax() + ")");
			sign.setLine(2, ChatColor.BLACK.toString() + "[" + getStatus() + "]");
			sign.setLine(3, ChatColor.BLACK + getSignName());
			sign.update();
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getSignName() {
		return sign_display;
	}
	
	public String getBungeeName() {
		return bungee_name;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
		String code = loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ();
		HeroesHub.getInstance().getConfig().set("Server." + bungee_name + ".SLocation", code);
		HeroesHub.getInstance().saveConfig();
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public static void loadServers() {
		for (String key : HeroesHub.getInstance().getConfig().getConfigurationSection("Server").getKeys(false)) {
			String name = key;
			if(getServer(name) == null){
				String add = HeroesHub.getInstance().getConfig().getString("Server." + name + ".address");
				String display = HeroesHub.getInstance().getConfig().getString("Server." + name + ".display");
				String[] address = add.split(", ");
				Server server = Server.addServer(key, display, address[0], Integer.parseInt(address[1]));
				String loc = HeroesHub.getInstance().getConfig().getString("Server." + key + ".SLocation");
				System.out.println(loc);
				String[] decodify = loc.split(", ");
				World w = HeroesHub.getInstance().getServer().getWorld(decodify[0]);
				double x = Double.valueOf(decodify[1]);
				double y = Double.valueOf(decodify[2]);
				double z = Double.valueOf(decodify[3]);
				Location loca = new Location(w, x, y, z);
				server.setLocation(loca);
			}
		}
	}
	
	public static String DEFAULT() {
		return "88.198.219.140";
	}
}
