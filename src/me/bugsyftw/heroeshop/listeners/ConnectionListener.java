package me.bugsyftw.heroeshop.listeners;

import me.bugsyftw.heroeshop.HeroesHub;
import me.bugsyftw.heroeshop.items.Item;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

public class ConnectionListener implements Listener {

	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		e.setJoinMessage("");
		e.getPlayer().setLevel(0);
		e.getPlayer().setHealth(20D);
		for (PotionEffect effect : e.getPlayer().getActivePotionEffects())
			e.getPlayer().removePotionEffect(effect.getType());
		Item.giveItems(p);
		p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		HeroesHub.giveScoreboard(p);
		p.teleport(HeroesHub.getLobbySpawn());
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		final Player p = e.getPlayer();
		HeroesHub.getInstance().datahandler.addToDatabase(p);
		HeroesHub.getInstance().datahandler.getPlayerProfile(p);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		e.setQuitMessage("");
		p.getInventory().clear();
		Bukkit.getScheduler().scheduleSyncDelayedTask(HeroesHub.getInstance(), new BukkitRunnable() {
			@Override
			public void run() {
				HeroesHub.getInstance().datahandler.saveProfile(p);
			}
		}, 5L);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent e) {
		final Player p = e.getPlayer();
		e.setLeaveMessage("");
		p.getInventory().clear();
		Bukkit.getScheduler().scheduleSyncDelayedTask(HeroesHub.getInstance(), new BukkitRunnable() {
			@Override
			public void run() {
				HeroesHub.getInstance().datahandler.saveProfile(p);
			}
		}, 5L);
	}
}
