package me.bugsyftw.heroeshop.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import me.bugsyftw.heroeshop.HeroesHub;
import me.bugsyftw.heroeshop.playerdata.AchievementList;
import me.bugsyftw.heroeshop.playerdata.Hero;
import me.bugsyftw.heroeshop.playerdata.HeroCoin;
import me.bugsyftw.heroeshop.playerdata.KillDeathRatio;
import me.bugsyftw.heroeshop.playerdata.PlayerProfile;
import me.bugsyftw.heroeshop.playerdata.PlayerRank;
import me.bugsyftw.heroeshop.playerdata.SkyRank;
import me.bugsyftw.heroeshop.playerdata.WinKill;
import me.bugsyftw.heroeshop.playerdata.WinLossRatio;
import me.bugsyftw.heroeshop.uuid.UUIDManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DataHandler {

	private HeroesHub plugin;
	public MySQL db;

	public DataHandler(HeroesHub plugin) {
		this.plugin = plugin;
	}
	
	public DataHandler() {
	}

	public void init() {
		db = new MySQL(plugin, "88.198.219.140", "3306", "skypiggies", "PiggyDev", "skypiggies");
		db.openConnection();
	}

	public synchronized boolean containsUUID(UUID uuid) {
		try {
			PreparedStatement sql = db.getConnection().prepareStatement("SELECT * FROM playerdata, herodata, heroupgrades, achievements WHERE playerdata.UUID=? AND herodata.UUID=? AND achievements.UUID=? AND heroupgrades.UUID=?;");
			sql.setString(1, uuid.toString());
			sql.setString(2, uuid.toString());
			sql.setString(3, uuid.toString());
			sql.setString(4, uuid.toString());

			ResultSet result = sql.executeQuery();
			boolean contains_player = result.next();
			

			sql.close();
			result.close();

			return contains_player;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getHeroCoins(PlayerProfile profile) {
		return profile.getCoins().getAmount();
	}

	public double getKD(PlayerProfile profile) {
		return profile.getKD().getRatio();
	}

	public double getWL(PlayerProfile profile) {
		return profile.getWL().getRation();
	}

	public PlayerRank getRank(Player hp) {
		String uuid = UUIDManager.getUUIDFromPlayer(hp.getName()).toString();
		if (!db.checkConnection()) db.openConnection();
		try {
			Statement query = db.getConnection().createStatement();
			ResultSet rs = query.executeQuery("SELECT * FROM 'playerdata' WHERE 'UUID'='" + uuid + "';");
			if (!rs.next()) return null;
			return new PlayerRank(SkyRank.fromString(rs.getString("rank")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PlayerProfile getPlayerProfile(Player p) {
		String uuid = UUIDManager.getUUIDFromPlayer(p.getName()).toString();
		PlayerProfile profile = null;
		Map<AchievementList, Boolean> achievements = new HashMap<AchievementList, Boolean>();
		Map<Hero, Boolean> heroes = new HashMap<Hero, Boolean>();
		Map<Hero, Integer> hero_upgrades = new HashMap<Hero, Integer>();
		if (!db.checkConnection()) db.openConnection();
		try {
			PreparedStatement query = db.getConnection().prepareStatement("SELECT * FROM playerdata, herodata, heroupgrades, achievements WHERE playerdata.UUID=? AND herodata.UUID=? AND achievements.UUID=? AND heroupgrades.UUID=?;");
			query.setString(1, uuid);
			query.setString(2, uuid);
			query.setString(3, uuid);
			query.setString(4, uuid);
			
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				for (Hero h : Hero.values()) {
					if(heroes.containsKey(h) == false)
						heroes.put(h, rs.getBoolean(h.getDataName()));
				}
				for (AchievementList al : AchievementList.values()) {
					if (achievements.containsKey(al) == false){
						achievements.put(al, rs.getBoolean(al.dataName()));
					}
				}
				for(Hero h : Hero.values()){
					if(hero_upgrades.containsKey(h) == false){
						hero_upgrades.put(h, rs.getInt(h.getDataName()));
					}
				}
				if (profile == null) {
					profile = new PlayerProfile(p, new HeroCoin(rs.getInt("herocoins")), new KillDeathRatio(Double.valueOf(rs.getDouble("kdratio"))), new WinLossRatio(Double.valueOf(rs.getDouble("wlratio"))), heroes, hero_upgrades,new PlayerRank(SkyRank.fromString(rs.getString("rank"))), achievements, new WinKill(rs.getInt("wins"), rs.getInt("kills")));
				}
			}
			for(Entry<Hero, Boolean> h : profile.getHeroesMap().entrySet()){
				h.getKey().setUnlocked(h.getValue());
			}
			rs.close();
			query.close();
			return profile;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveProfile(Player p) {
		String uuid = UUIDManager.getUUIDFromPlayer(p.getName()).toString();
		PlayerProfile profile = PlayerProfile.fromName(p.getName());
		if (!db.checkConnection()) db.openConnection();
		try {
			PreparedStatement query = db.getConnection().prepareStatement("UPDATE playerdata SET herocoins=?, kdratio=?, wlratio=?, rank=? WHERE UUID=?;");
			query.setInt(1, profile.getCoins().getAmount());
			query.setDouble(2, profile.getKD().getRatio());
			query.setDouble(3, profile.getWL().getRation());
			query.setString(4, profile.getRank().getRankList().getName());
			query.setString(5, uuid);
			query.executeUpdate();
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addHero(Player p, Hero hero) throws Exception{
		String uuid = UUIDManager.getUUIDFromPlayer(p.getName()).toString();
		if (!db.checkConnection()) db.openConnection();
		try {
			PreparedStatement query = db.getConnection().prepareStatement("UPDATE herodata SET " + hero.getDataName() + "=?" + " WHERE UUID=?;");
			query.setBoolean(1, true);
			query.setString(2, uuid);
			query.executeUpdate();
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		saveProfile(p);
	}
	
	public boolean addOfflineMoney(CommandSender sender, OfflinePlayer op, int ammount) {
		UUID uuid = Bukkit.getServer().getOfflinePlayer(op.getUniqueId()).getUniqueId();
		if (!db.checkConnection()) db.openConnection();
		if(containsUUID(uuid)){
			try {
				PreparedStatement pt = db.getConnection().prepareStatement("SELECT * FROM playerdata WHERE UUID=?");
				pt.setString(1, uuid.toString());
				ResultSet rs = pt.executeQuery();
				int money = rs.getInt("herocoins");
				pt.close();
				PreparedStatement query = db.getConnection().prepareStatement("UPDATE playerdata SET herocoins=?" + " WHERE UUID=?;");
				query.setInt(1, money + ammount);
				query.setString(2, uuid.toString());
				query.executeUpdate();
				query.close();
				
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The Player you specified does not exist in our database!");
		}
		return false;
	}

	public void addToDatabase(Player p) {
		String uuid = UUIDManager.getUUIDFromPlayer(p.getName()).toString();
		if (!db.checkConnection()) db.openConnection();
		try {
			if (containsUUID(UUIDManager.getUUIDFromPlayer(p.getName()))) return;
			PreparedStatement pt = db.getConnection().prepareStatement("INSERT INTO playerdata (UUID,herocoins,kdratio,wlratio,rank,wins,kills) VALUES (?,0,0,0,?,0,0);");
			pt.setString(1, uuid);
			pt.setString(2, SkyRank.DEFAULT.getName());

			pt.execute();

			pt.close();

			PreparedStatement pt2 = db.getConnection().prepareStatement("INSERT INTO herodata VALUES (?,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0);");
			pt2.setString(1, uuid);

			pt2.execute();

			pt2.close();

			PreparedStatement pt3 = db.getConnection().prepareStatement("INSERT INTO achievements VALUES (?,0,0,0,0,0,0,0,0,0,0);");
			pt3.setString(1, uuid);

			pt3.execute();

			pt3.close();
			
			PreparedStatement pt4 = db.getConnection().prepareStatement("INSERT INTO heroupgrades VALUES (?,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);");
			pt4.setString(1, uuid);

			pt4.execute();

			pt4.close();

		} catch (SQLException e) {
			e.printStackTrace();
			p.kickPlayer(ChatColor.RED + "Something went wrong when you connected to the server, reconnect!");
		}
	}

	public boolean setRank(UUID uuid, SkyRank rank) {
		if (!db.checkConnection()) db.openConnection();
		if (containsUUID(uuid)) {
			try {

				PreparedStatement pt = db.getConnection().prepareStatement("UPDATE playerdata SET rank=? WHERE UUID='?';");
				pt.setString(1, rank.getName());
				pt.setString(2, uuid.toString());

				pt.executeUpdate();
				pt.close();

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
	
	public synchronized boolean containsServer(String name) {
		try {
			PreparedStatement sql = db.getConnection().prepareStatement("SELECT * FROM `serverdata` WHERE name=?;");
			sql.setString(1, name);

			ResultSet result = sql.executeQuery();
			boolean contains_server = result.next();

			sql.close();
			result.close();

			return contains_server;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Player getPlayerByUUID(UUID uuid) {
		for (Player p : HeroesHub.getInstance().getServer().getOnlinePlayers())
			if (p.getUniqueId().equals(uuid)) return p;

		throw new IllegalArgumentException();
	}
}
