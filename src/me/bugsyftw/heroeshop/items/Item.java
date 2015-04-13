package me.bugsyftw.heroeshop.items;

import java.util.ArrayList;

import me.bugsyftw.heroeshop.playerdata.AchievementList;
import me.bugsyftw.heroeshop.playerdata.PlayerProfile;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {
	
	public static String RIGHT = ChatColor.GRAY + " (Right-Click)";
	
	public static ItemStack HEROES_SHOP(){
		ItemStack item = new ItemStack(Material.GOLD_INGOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Heroes Shop" + RIGHT);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "View the Shop!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		EnchantGlow.addGlow(item);
		return item;
	}
	
	public static ItemStack PLAYER_VISIBLITY(boolean b){
		ItemStack item;
		if(b) {
			item = new ItemStack(Material.EYE_OF_ENDER);
		} else {
			item = new ItemStack(Material.ENDER_PEARL);
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Player Visibility" + RIGHT);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "Modify the Player Visiblity!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		if(b)EnchantGlow.addGlow(item);
		return item;
	}
	
	public static ItemStack ACHIEVEMENTS(PlayerProfile hp){
		boolean b = hp.getAchievementsMap().get(AchievementList.FIRST_KILL);
		String s = b ? ChatColor.GREEN + "Achievement 1" : ChatColor.BLACK + "Achievement 1";
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta)item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Achievements" + RIGHT);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "View your Achievements!");
		meta.setLore(lore);
		meta.setAuthor("§bSky§dPiggies");
		meta.addPage(ChatColor.BOLD + "    " + ChatColor.UNDERLINE.toString() + "Achievements" + ChatColor.RESET + "   \n\n" + ChatColor.RESET + 
		s);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack LOBBY_SELECTION(){
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Lobby Selection" + RIGHT);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "Select your Game Lobby!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		EnchantGlow.addGlow(item);
		return item;
	}
	
	public static ItemStack UNIVERSE_SELECTION(){
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Universe Selection" + RIGHT);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "Select your Universe (Server)!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		EnchantGlow.addGlow(item);
		return item;
	}
	
	public static void giveItems(Player p) {
		PlayerProfile profile = PlayerProfile.fromName(p.getName());
		p.getInventory().setItem(0, Item.HEROES_SHOP());
		p.getInventory().setItem(1, Item.PLAYER_VISIBLITY(true));
		p.getInventory().setItem(2, Item.ACHIEVEMENTS(profile));
		p.getInventory().setItem(7, Item.LOBBY_SELECTION());
		p.getInventory().setItem(8, Item.UNIVERSE_SELECTION());
	}
}
