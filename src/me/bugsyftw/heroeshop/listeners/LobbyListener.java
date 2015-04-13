package me.bugsyftw.heroeshop.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.bugsyftw.heroeshop.HeroesHub;
import me.bugsyftw.heroeshop.items.Item;
import me.bugsyftw.heroeshop.playerdata.Hero;
import me.bugsyftw.heroeshop.playerdata.PlayerProfile;
import me.bugsyftw.heroeshop.playerdata.SkyRank;
import me.bugsyftw.heroeshop.server.Server;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class LobbyListener implements Listener {

	private static Map<String, Integer> invItems = new HashMap<String, Integer>();
	private static List<String> inVisCooldown = new ArrayList<String>();

	static {
		invItems.put(Hero.THING.getDataName(), 3);
		invItems.put(Hero.HAWKEYE.getDataName(), 4);
		invItems.put(Hero.BLAKCWIDOW.getDataName(), 5);
		invItems.put(Hero.AQUAMAN.getDataName(), 9);
		invItems.put(Hero.INVISIBLE_WOMAM.getDataName(), 10);
		invItems.put(Hero.FLASH.getDataName(), 11);
		invItems.put(Hero.ANTMAN.getDataName(), 12);
		invItems.put(Hero.CAPTAIN_AMERICA.getDataName(), 13);
		invItems.put(Hero.HUMAN_TORCH.getDataName(), 14);
		invItems.put(Hero.WOLVERINE.getDataName(), 15);
		invItems.put(Hero.THOR.getDataName(), 16);
		invItems.put(Hero.IRON_MAN.getDataName(), 17);
		invItems.put(Hero.DEADPOOL.getDataName(), 20);
		invItems.put(Hero.SPIDER_MAN.getDataName(), 21);
		invItems.put(Hero.SUPER_MAN.getDataName(), 22);
		invItems.put(Hero.HULK.getDataName(), 23);
		invItems.put(Hero.BATMAN.getDataName(), 24);
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		PlayerProfile profile = PlayerProfile.fromName(p.getName());
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.getItemInHand().equals(Item.HEROES_SHOP())) {
				Inventory inv = Bukkit.createInventory(p, 45, "Heroes Shop");
				for (Entry<Hero, Boolean> h : profile.getHeroesMap().entrySet()) {
					h.getKey().setUnlocked(h.getValue());
					if (invItems.containsKey(h.getKey().getDataName())) {
						try {
							inv.setItem(invItems.get(h.getKey().getDataName()), createHeroSelectItem(h.getKey(), p));
						} catch (Exception ex) {
							HeroesHub.logMessage("COULD NOT LOAD HERO[" + h.getKey().getName() + "]");
							ex.printStackTrace();
						}
					}
				}
				p.openInventory(inv);
				return;
			} else if (p.getItemInHand().getType().equals(Material.ENDER_PEARL) || p.getItemInHand().getType().equals(Material.EYE_OF_ENDER)) {
				if (p.getItemInHand().equals(Item.PLAYER_VISIBLITY(true))) {
					if (inVisCooldown.contains(p.getName())) {
						p.sendMessage(HeroesHub.PREFIX + ChatColor.RED + "You must wait 3 seconds to use this item again!");
						e.setCancelled(true);
						e.setCancelled(true);
						p.updateInventory();
						return;
					}
					for (Player all : Bukkit.getServer().getOnlinePlayers()) {
						p.hidePlayer(all);
					}
					p.sendMessage(HeroesHub.PREFIX + "Players are now Invisible");
					p.setItemInHand(Item.PLAYER_VISIBLITY(false));
					inVisCooldown.add(p.getName());
					Bukkit.getScheduler().scheduleSyncDelayedTask(HeroesHub.getInstance(), new Runnable() {
						@Override
						public void run() {
							inVisCooldown.remove(p.getName());
						}
					}, (20 * 3));
				} else if (p.getItemInHand().equals(Item.PLAYER_VISIBLITY(false))) {
					if (inVisCooldown.contains(p.getName())) {
						p.sendMessage(HeroesHub.PREFIX + ChatColor.RED + "You must wait 3 seconds to use this item again!");
						e.setCancelled(true);
						p.updateInventory();
						return;
					}
					for (Player all : Bukkit.getServer().getOnlinePlayers()) {
						p.showPlayer(all);
					}
					p.sendMessage(HeroesHub.PREFIX + "Players are now Visible");
					p.setItemInHand(Item.PLAYER_VISIBLITY(true));
					inVisCooldown.add(p.getName());
					Bukkit.getScheduler().scheduleSyncDelayedTask(HeroesHub.getInstance(), new Runnable() {
						@Override
						public void run() {
							inVisCooldown.remove(p.getName());
						}
					}, (20 * 3));
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getTitle().equals("Heroes Shop")) {
			ItemStack is = e.getCurrentItem();
			if (is != null && is.getType() != Material.AIR) {
				for (Hero h : Hero.getHeroList()) {
					if (is.getType() == h.getItem().getType() && is.getDurability() == h.getItem().getDurability()) {
						buyHero(p, h);
						p.closeInventory();
						break;
					}
				}
				e.setResult(Result.DENY);
				e.setCancelled(true);
			}
		}
	}

	public void buyHero(Player p, Hero hero) {
		// List<String> can_buy = Arrays.asList("aquaman", "flash", "antman");
		PlayerProfile profile = PlayerProfile.fromName(p.getName());
		if (hero != Hero.DEADPOOL && hero != Hero.SPIDER_MAN && hero != Hero.SUPER_MAN && hero != Hero.HULK && hero != Hero.BATMAN) {
			if (hero.isLocked() == false) {
				if (profile.getHeroesMap().get(Hero.fromPostion(hero.getPosition() - 1)) == true) {
					if (profile.getCoins().getAmount() >= hero.getPrice()) {
						try {
							// if (can_buy.contains(hero.getDataName())) {
							HeroesHub.getInstance().datahandler.addHero(p, hero);
							profile.getCoins().removeHeroCoins(hero.getPrice());
							hero.setUnlocked(true);
							profile.getHeroesMap().remove(hero);
							profile.getHeroesMap().put(hero, true);
							p.sendMessage(HeroesHub.PREFIX + ChatColor.GREEN + "You just bought [" + ChatColor.GOLD + hero.getName() + ChatColor.GREEN + "] for " + hero.getPrice() + " SC");
							HeroesHub.updateMoneySlot(p);
							return;
							/*
							 * } else { p.sendMessage(HeroesShop.PREFIX + ChatColor.RED + "This Hero is not available!"); p.sendMessage(HeroesShop.PREFIX + ChatColor.GRAY + "Soon to Come!"); p.closeInventory(); return; }
							 */
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
					} else {
						p.sendMessage(HeroesHub.PREFIX + ChatColor.RED + "You do not have enought money to buy this Hero!");
						p.sendMessage(HeroesHub.PREFIX + ChatColor.GRAY + "Play more games to earn Hero Coins.");
						return;
					}
				} else {
					p.sendMessage(HeroesHub.PREFIX + ChatColor.RED + "This Hero is not available!");
					p.sendMessage(HeroesHub.PREFIX + ChatColor.GRAY + "You need to buy the previous hero first!");
					return;
				}
			}
		}
	}
	
	private void upgradeHero(PlayerProfile profile, Player p, Hero hero) {
		 if (hero.isLocked() == false){
			 if (hero.getUpgrade() != 2 && (hero.getUpgrade() > 3 == false)) {
				 if (profile.getCoins().getAmount() >= 1000) {
					 profile.getCoins().removeHeroCoins(1000);
					 hero.setUpgrade(hero.getUpgrade() + 1);
				 }
			 } else if (hero.getUpgrade() == 2) {
				 if (profile.getCoins().getAmount() >= 3000) {
					 profile.getCoins().removeHeroCoins(3000);
					 hero.setUpgrade(hero.getUpgrade() + 1);
				 }
			 }
		 } else if (hero.getUpgrade() == 3) {
			 p.sendMessage(ChatColor.RED + "Cannot upgrade anymore!");
			 return;
		 }
	}

	private static ItemStack createHeroSelectItem(Hero h, Player p) {
		PlayerProfile profile = PlayerProfile.fromName(p.getName());
		List<String> vip = Arrays.asList("deadpool", "spiderman", "superman", "hulk", "batman");
		boolean unlocked = h.isLocked();
		String lore1;
		ItemStack item = h.getItem();
		ItemMeta im = item.getItemMeta();
		if (vip.contains(h.getDataName())) {
			lore1 = unlocked ? null : ChatColor.GOLD + "Special Rank";
		} else {
			lore1 = unlocked ? null : ChatColor.GOLD + "Buy: " + h.getPrice() + ChatColor.GOLD + " SC";
		}
		im.setDisplayName(ChatColor.WHITE + h.getName());
		if (profile.getHeroesMap().get(h) == true) {
			im.setDisplayName(ChatColor.GREEN + h.getName());
		} else {
			im.setDisplayName(ChatColor.RED + h.getName());
		}
		List<String> lore = new ArrayList<String>();
		if (unlocked){
			lore.addAll(h.getLoreSaying());
			lore.add(ChatColor.GREEN + "Buy Upgrade (* " + (h.getUpgrade() + 1) + ")");
		}
		lore.add(lore1);
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}

	@EventHandler
	public void onItemdrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		PlayerProfile profile = PlayerProfile.fromName(e.getPlayer().getName());
		int rank_id = profile.getRank().getRankList().getID();
		SkyRank rank = profile.getRank().getRankList();
		e.setFormat(profile.getRank().getRankList().getChatDisplay() + e.getPlayer().getName() + ": " + ChatColor.WHITE + e.getMessage());
		if (rank_id == 0) {
			e.setFormat(rank.getChatDisplay() + " " + e.getPlayer().getName() + rank.getBracket() + ": " + ChatColor.GRAY + e.getMessage());
		} else if (rank_id == 6) {
			e.setFormat(rank.getChatDisplay() + " " + e.getPlayer().getName() + rank.getBracket() + ": " + ChatColor.GOLD + e.getMessage());
		} else {
			e.setFormat(rank.getChatDisplay() + " " + e.getPlayer().getName() + rank.getBracket() + ": " + ChatColor.WHITE + e.getMessage());
		}

	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (p.isOp() || PlayerProfile.fromName(p.getName()).getRank().getRankList().getID() > 4) {
			for (Entry<String, Server> servers : Server.getServerMap().entrySet()) {
				String name = servers.getKey();
				Server server = servers.getValue();
				if (e.getLine(0).equalsIgnoreCase(name)) {
					if (server.getType() == null) {
						e.setLine(0, ChatColor.BLACK + "[Offline]");
						server.setLoc(e.getBlock().getLocation());
					} else {
						switch (server.getType()) {
						case "1v1":
							e.setLine(0, ChatColor.BLACK + "[1 on 1]");
							break;
						case "FFA":
							e.setLine(0, ChatColor.BLACK + "[Free For All]");
							break;
						case "Tournament":
							e.setLine(0, ChatColor.BLACK + "[Tournament]");
							break;
						default:
							break;
						}
						e.setLine(1, ChatColor.BLACK.toString() + "(" + server.getOnline() + "/" + server.getMax() + ")");
						e.setLine(2, ChatColor.BLACK.toString() + server.getWin() + " SC");
						e.setLine(3, ChatColor.BLACK + name);
						server.setLoc(e.getBlock().getLocation());
					}
					server.setLoc(e.getBlock().getLocation());
					break;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSignClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			for (Entry<String, Server> servers : Server.getServersEntry()) {
				Server server = servers.getValue();
				if (e.getClickedBlock().getLocation().equals(server.getLoc())) {
					HeroesHub.sendToServer(e.getPlayer(), server.getBungeeName());
					break;
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		PlayerProfile profile = PlayerProfile.fromName(p.getName());
		if (p.isOp() || profile.getRank().getRankList().getID() == 5 || profile.getRank().getRankList().getID() == 6)
			e.setCancelled(false);
		else
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		PlayerProfile profile = PlayerProfile.fromName(p.getName());
		if (p.isOp() || profile.getRank().getRankList().getID() == 5 || profile.getRank().getRankList().getID() == 6)
			e.setCancelled(false);
		else
			e.setCancelled(true);
	}
}
