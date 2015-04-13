package me.bugsyftw.heroeshop;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.bugsyftw.heroeshop.database.DataHandler;
import me.bugsyftw.heroeshop.listeners.ConnectionListener;
import me.bugsyftw.heroeshop.listeners.LobbyListener;
import me.bugsyftw.heroeshop.playerdata.PlayerProfile;
import me.bugsyftw.heroeshop.playerdata.SkyRank;
import me.bugsyftw.heroeshop.server.Server;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class HeroesHub extends JavaPlugin {

	private static Logger log = Logger.getLogger("Minecraft");
	private static HeroesHub instance;
	public DataHandler datahandler = new DataHandler(this);
	public SpawnCollection spawns;
	public static String PREFIX = "§7[§cHB§7]§r ";
	public static String serverName;

	public static void logMessage(String msg) {
		log.info(msg);
	}

	public void onEnable() {
		instance = this;
		datahandler.init();
		if (getConfig().contains("Spawn.Lobby")) {
			spawns = new SpawnCollection(SpawnCollection.getLocations());
			logMessage("[" + Level.INFO + "] Loaded Spawns Map Correctly!");
		} else {
			spawns = new SpawnCollection();
			logMessage("[" + Level.INFO + "] Creating new Spawns Map!");
		}
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ConnectionListener(), this);
		pm.registerEvents(new LobbyListener(), this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Server.loadServers();
		
		try {
			for(Entry<String, Server> server : Server.getServersEntry()){
				server.getValue().updateSign();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable() {
			public void run() {
				try {
					if (Bukkit.getOnlinePlayers().length > 0) {
						for(Entry<String, Server> server : Server.getServersEntry()){
							server.getValue().updateSign();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0L, (20 * 2));
	}

	public void onDisable() {
		if (Bukkit.getServer().getOnlinePlayers().length > 0) {
			for (Player all : Bukkit.getServer().getOnlinePlayers()) {
				datahandler.saveProfile(all);
			}
		}
		datahandler.db.closeConnection();
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("hub")) {
			if (sender.isOp()) {
				if (args.length == 0) {

					return false;
				}
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (PlayerProfile.fromName(p.getName()).getRank().getRankList().getID() < 4 && PlayerProfile.fromName(p.getName()).getRank().getRankList().getID() > 6) return false;
				}
				if (args[0].equalsIgnoreCase("spawn")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						if (spawns.addSpawn("Lobby", p.getLocation())) {
							p.sendMessage(ChatColor.YELLOW + "You have added lobby spawn!");
							return true;
						}
					} else {
						sender.sendMessage("You must be player to use this command!");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("give")) {
					if (isInteger(args[2])) {
						Player player = Bukkit.getServer().getPlayer(args[1]);
						if (player != null) {
							PlayerProfile profile = PlayerProfile.fromName(player.getName());
							profile.getCoins().addHeroCoins(Integer.parseInt(args[2]));
							sender.sendMessage(ChatColor.GREEN + "You just gave " + args[2] + " HC to " + player.getName());
							updateMoneySlot(player);
							return true;
						} else {
							datahandler.addOfflineMoney(sender, Bukkit.getServer().getOfflinePlayer(args[1]), Integer.parseInt(args[2]));
							sender.sendMessage(ChatColor.GREEN + "You just gave " + args[2] + " HC to " + args[1]);

						}
					} else {
						sender.sendMessage(ChatColor.RED + "Ammount must be a number!");
						return false;
					}
				} else if (args[0].equalsIgnoreCase("set")) {
					if (args[1].equalsIgnoreCase("rank")) {
						Player player = Bukkit.getServer().getPlayer(args[2]);
						if (player != null) {
							PlayerProfile profile = PlayerProfile.fromName(player.getName());
							profile.getRank().setRank(SkyRank.fromID(Integer.parseInt(args[3])));
							datahandler.saveProfile(player);
							sender.sendMessage(ChatColor.GREEN + "You just set " + SkyRank.fromID(Integer.parseInt(args[3])).getName() + " Rank to " + player.getName());
							return true;
						} else {
							datahandler.setRank(Bukkit.getServer().getOfflinePlayer(args[2]).getUniqueId(), SkyRank.fromID(Integer.parseInt(args[3])));
							sender.sendMessage(ChatColor.GREEN + "You just gave " + args[2] + " HC to " + args[1]);

						}
					}
				} else if (args[0].equalsIgnoreCase("server")) {
					// /hub server add <bungee_name> <sign_display> <port>
					if (args.length == 5) {
						if (args[1].equalsIgnoreCase("add")) {
							if(isInteger(args[4])){
								Server.addServer(args[2], args[3], Server.DEFAULT(), Integer.parseInt(args[4]));
								sender.sendMessage(ChatColor.YELLOW + "You just added a server with the name: " + args[3] + " with the BungeeCord Name: " + args[2] + " and with the port: " + args[4] + " for the default server IP");
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Wrong Syntax: /hub server add <bungee_name> <sign_display> <port>");
					}
				}
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static void giveScoreboard(Player p) {
		PlayerProfile profile = PlayerProfile.fromName(p.getName());
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective ob = board.registerNewObjective("Stats", "dummy");
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		ob.setDisplayName(ChatColor.YELLOW + PREFIX + "Stats");
		Score coins = ob.getScore(Bukkit.getOfflinePlayer("Coins:"));
		Score wins = ob.getScore(Bukkit.getOfflinePlayer("Wins:"));
		Score kills = ob.getScore(Bukkit.getOfflinePlayer("Kills: "));
		coins.setScore(profile.getCoins().getAmount());
		wins.setScore(profile.getWinKill().getWins());
		kills.setScore(profile.getWinKill().getKills());
		p.setScoreboard(board);

	}

	@SuppressWarnings("deprecation")
	public static void updateMoneySlot(Player p) {
		Scoreboard board = p.getScoreboard();
		board.getObjective(DisplaySlot.SIDEBAR).getScore(Bukkit.getOfflinePlayer("Coins:")).setScore(PlayerProfile.fromName(p.getName()).getCoins().getAmount());
	}

	public static void sendToServer(Player p, String targetServer) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(targetServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendPluginMessage(HeroesHub.getInstance(), "BungeeCord", b.toByteArray());
	}

	public static Location getLobbySpawn() {
		return HeroesHub.getInstance().spawns.getSpawn("Lobby");
	}

	public static HeroesHub getInstance() {
		return instance;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
