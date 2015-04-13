package me.bugsyftw.heroeshop.playerdata;

public class WinKill {
	
	private int wins;
	private int kills;
	
	public WinKill(int wins, int kills) {
		this.wins = wins;
		this.kills = kills;
	}
	
	public int getWins() {
		return wins;
	}
	
	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public int getKills() {
		return kills;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
}
