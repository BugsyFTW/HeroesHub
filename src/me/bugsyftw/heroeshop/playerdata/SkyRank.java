package me.bugsyftw.heroeshop.playerdata;

public enum SkyRank {

	DEFAULT("Piggy", 0, 1, "§8[§7Piggy§8]§7", "§8"),
	PRO_PIGGY("Pro Piggy", 1, 2, "§7[§aProPiggy§7]§a", "§7"),
	SUPER_PIGGY("Super Piggy", 2, 3, "§7[§6SuperPiggy§7]§6", "§7"),
	SKY_PIGGY("Sky Piggy", 3, 5, "§f[§bSkyPiggy§f]§b", "§f"),
	//MOD ADMIN RANKS
	HELPER("S.P.H.D", 4, 1, "§9[§3S§9.§3P§9.§3H§9.§3D§9]§3", "§9"),
	PIGGY_ANGEL("Piggy Angel", 5, 1, "§6[§cAngelPiggy§6]§c", "§6"),
	PIGGY_GOD("Piggy God", 6, 1, "§6[§4PiggyGod§6]§4",  "§6"),
	//PERKS RANKS
	TRUE_HERO("True Hero", 7, 5, "§f[§cTrueHero§f]§c", "§f"),
	YOUTUBER("YouTuber", 8, 5, "§7[§fYou§cTuber§7]§f",  "§7");
	

	private String name;
	private int id;
	private int boost;
	private String chat;
	private String bracket;
	
	private SkyRank(String name, int id, int boost, String chat, String bracket) {
		this.name = name;
		this.id = id;
		this.boost = boost;
		this.chat = chat;
		this.bracket = bracket;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public int getBoost() {
		return boost;
	}
	
	public String getBracket() {
		return bracket;
	}

	public static SkyRank fromString(String rank) {
		for (SkyRank sk : values()) {
			if (sk.getName().equalsIgnoreCase(rank)) {
				return sk;
			}
		}
		return null;
	}
	
	public static SkyRank fromID(int id) {
		for (SkyRank sk : values()) {
			if (sk.getID() == id) {
				return sk;
			}
		}
		return null;
	}

	public String getChatDisplay() {
		return chat;
	}
}
