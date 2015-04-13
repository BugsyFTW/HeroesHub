package me.bugsyftw.heroeshop.playerdata;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Hero {
	
	THING("Thing", Arrays.asList(ChatColor.WHITE + "It's clobberin time! - Ben Grimm"),"HThingB", true, 0, 0, 1),
	HAWKEYE("Hawkeye", Arrays.asList(ChatColor.WHITE + "It's okay, everybody.", ChatColor.WHITE + "It's okay, I'm an Avenger  - Clint Barton"), "HHawkeyeB", true, 0, 0, 2),
	BLAKCWIDOW("Black Widow", Arrays.asList(ChatColor.WHITE + "Здравствуйте, я Наташа Романова - Natasha Romanoff"), "HBlackWidowB", true, 0, 0, 3),
	AQUAMAN("Aquaman", Arrays.asList(ChatColor.WHITE + "EeeeeeiK eEik *Bloop* eEeeK ~Splash~ - Random Dolphins"), "HAquaManB", false, 250, 0, 4),
	INVISIBLE_WOMAM("Invisible Woman", Arrays.asList(ChatColor.WHITE + "I'm a smart girl.", ChatColor.WHITE + "I'm smarter than you. - Susan Storm"),"HInvisibleWomanB", false, 500, 0, 5),
	FLASH("Flash", Arrays.asList(ChatColor.WHITE + "I'll have us wrapped up in a flash - Barry Allen"), "HFlashB", false, 750, 0, 6),
	ANTMAN("Antman", Arrays.asList(ChatColor.WHITE + "Go to the ants thou sluggard! - Henry Pym"), "HAntmanB", false, 1000, 0, 7),
	CAPTAIN_AMERICA("Captain America", Arrays.asList(ChatColor.WHITE + "I've been asleep for 70 years.", ChatColor.WHITE + "I think I've had enough rest - Steve Rogers"), "HCaptainAmericaB", false, 1500, 0, 8),
	HUMAN_TORCH("Human Torch", Arrays.asList(ChatColor.WHITE + "\"Flame on!\" - Jonathan (Johnny) Storm"), "HHumanTorchB", false,2000, 0, 9),
	WOLVERINE("Wolverine", Arrays.asList(ChatColor.WHITE + "I can do this all day", ChatColor.WHITE + "you twisted mutant bitch! - James (Logan) Howlett"), "HWolverineB", false, 2500, 0, 10),
	THOR("Thor", Arrays.asList(ChatColor.WHITE + "Whosoever holds this hammer,", ChatColor.WHITE + "if he be worthy, shall possess the power of THOR! - Odin"), "HThorB", false, 5000, 0, 11),
	IRON_MAN("Iron Man", Arrays.asList(ChatColor.WHITE + "Genius, billionaire, playboy, philanthropist - Tony Stark"), "HIronManB", false, 7500, 0, 12),
	DEADPOOL("Deadpool", Arrays.asList(ChatColor.WHITE + "My common sense is tingling - Wade Wilson"), "HDeapoolB", false, 0, 0, 13),
	SPIDER_MAN("Spider-Man", Arrays.asList(ChatColor.WHITE + "With great power comes great responsibility - Uncle Ben"), "HSpiderManB", false, 0, 0, 14),
	SUPER_MAN("Superman", Arrays.asList(ChatColor.WHITE + "It's a bird... It's a plane... It's Superman! - Random Strangers"), "HSupermanB", false, 0, 0, 15),
	HULK("Hulk", Arrays.asList(ChatColor.WHITE + "HULK SMASH! - Bruce Banner"), "HHulkB", false, 0, 0, 16),
	BATMAN("Batman", Arrays.asList(ChatColor.WHITE + "I'm Batman - Bruce Wayne"), "HBatmanB", false, 0, 0, 17);

	private String name;
	private List<String> saying;
	private boolean b;
	private int price;
	private int position;
	private int upgrade;

	private Hero(String name, List<String> saying, String username, boolean b, int price, int upgrade, int position) {
		this.name = name;
		this.saying = saying;
		this.b = b;
		this.price = price;
		this.position = position;
		this.setUpgrade(upgrade);
	}

	public static List<Hero> getHeroList() {
		return Arrays.asList(values());

	}

	public static Hero fromString(String s) {
		for (Hero h : Hero.values()) {
			if (s.equalsIgnoreCase(s.toString())) {
				return h;
			}
		}
		return null;
	}

	public ItemStack getItem() {
		ItemStack item;
		switch (this) {
		case THING:
			item = new ItemStack(Material.CLAY_BRICK);
			return item;
		case HAWKEYE:
			item = new ItemStack(Material.BOW);
			return item;
		case BLAKCWIDOW:
			item = new ItemStack(Material.SPIDER_EYE);
			return item;
		case AQUAMAN:
			item = new ItemStack(Material.WATER_BUCKET);
			return item;
		case INVISIBLE_WOMAM:
			item = new ItemStack(Material.POTION, 1, (short) 14);
			return item;
		case FLASH:
			item = new ItemStack(Material.FEATHER);
			return item;
		case ANTMAN:
			item = new ItemStack(Material.MONSTER_EGG, 1, (short) 60);
			return item;
		case CAPTAIN_AMERICA:
			item = new ItemStack(Material.NETHER_STAR);
			return item;
		case HUMAN_TORCH:
			item = new ItemStack(Material.FIRE);
			return item;
		case WOLVERINE:
			item = new ItemStack(Material.BONE);
			return item;
		case THOR:
			item = new ItemStack(Material.IRON_AXE);
			return item;
		case IRON_MAN:
			item = new ItemStack(Material.IRON_INGOT);
			return item;
		case DEADPOOL:
			item = new ItemStack(Material.SULPHUR);
			return item;
		case SPIDER_MAN:
			item = new ItemStack(Material.WEB);
			return item;
		case SUPER_MAN:
			item = new ItemStack(Material.EMERALD);
			return item;
		case BATMAN:
			item = new ItemStack(Material.MONSTER_EGG, 1, (short) 65);
			return item;
		case HULK:
			item = new ItemStack(Material.LEATHER_LEGGINGS);
			return item;
		default:
			break;
		}
		return null;
	}

	public String getDataName() {
		String s = this.name;
		s = s.replace(" ", "");
		s = s.replace("-", "");
		return s.toLowerCase();
	}

	public List<String> getLoreSaying() {
		return saying;
	}

	public int getPrice() {
		return price;
	}

	public boolean isLocked() {
		return b;
	}

	public void setUnlocked(boolean b) {
		this.b = b;
	}

	public String getName() {
		return name;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(int upgrade) {
		this.upgrade = upgrade;
	}

	public String username() {
		switch (this) {
		case THING:
			return "HThingB";
		case HAWKEYE:
			return "HHawkeyeB";
		case BLAKCWIDOW:
			return "HBlackWidowB";
		case AQUAMAN:
			return "HAquaManB";
		case INVISIBLE_WOMAM:
			return "HInvisibleWomanB";
		case FLASH:
			return "HFlashB";
		case ANTMAN:
			return "HAntmanB";
		case CAPTAIN_AMERICA:
			return "HCaptainAmericaB";
		case HUMAN_TORCH:
			return "HHumanTorchB";
		case WOLVERINE:
			return "HWolverineB";
		case THOR:
			return "HThorB";
		case IRON_MAN:
			return "HIronManB";
		case DEADPOOL:
		case SPIDER_MAN:
			return "HSpidermanB";
		case SUPER_MAN:
			return "HSupermanB";
		case BATMAN:
			return "HBatmanB";
		case HULK:
			return "HHulkB";
		default:
			break;
		}
		return "ERROR";
	}
	
	public static Hero fromPostion(int position){
		for (Hero h : Hero.values()){
			if (h.getPosition() == position){
				return h;
			}
		}
		return null;
	}
}
