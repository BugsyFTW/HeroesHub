package me.bugsyftw.heroeshop.playerdata;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import me.bugsyftw.heroeshop.HeroesHub;

import org.bukkit.entity.Player;

public class PlayerProfile {

	public static Map<String, PlayerProfile> players = new HashMap<String, PlayerProfile>();
	
	private HeroCoin coins;
	private KillDeathRatio kd;
	private WinLossRatio wl;
	private Map<Hero, Boolean> heroes = new HashMap<Hero, Boolean>();
	private Map<Hero, Integer> hero_upgrades = new HashMap<Hero, Integer>();
	private Map<AchievementList, Boolean> achieved = new HashMap<AchievementList, Boolean>();
	private PlayerRank rank;
	private WinKill winkill;

	public PlayerProfile(Player p, HeroCoin coins, KillDeathRatio kd, WinLossRatio wl, Map<Hero, Boolean> heroes,Map<Hero, Integer> hero_upgrades, PlayerRank rank, Map<AchievementList, Boolean> achievements, WinKill winkill) {
		this.coins = coins;
		this.kd = kd;
		this.wl = wl;
		this.heroes = heroes;
		this.rank = rank;
		this.achieved = achievements;
		this.winkill = winkill;
		this.hero_upgrades = hero_upgrades;
		players.put(p.getName(), this);
		HeroesHub.logMessage("[" + Level.INFO + "] Loaded Profile for " + p.getName());
	}

	public PlayerRank getRank() {
		return rank;
	}

	public KillDeathRatio getKD() {
		return kd;
	}

	public WinLossRatio getWL() {
		return wl;
	}

	public HeroCoin getCoins() {
		return coins;
	}

	public Map<AchievementList, Boolean> getAchievementsMap() {
		return achieved;
	}

	public Map<Hero, Boolean> getHeroesMap() {
		return heroes;
	}
	
	public Map<Hero, Integer> getUpgradesMap() {
		return hero_upgrades;
	}
	
	public WinKill getWinKill() {
		return winkill;
	}

	public static PlayerProfile fromName(String name) {
		for (Entry<String, PlayerProfile> profile : players.entrySet()) {
			if(profile.getKey().equals(name)) {
				return profile.getValue();
			}
		}
		return null;
	}
}
