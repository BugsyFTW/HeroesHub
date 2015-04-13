package me.bugsyftw.heroeshop.playerdata;

public class PlayerRank {

	private SkyRank rank;

	public PlayerRank(SkyRank rank) {
		this.rank = rank;
	}

	public void setRank(SkyRank rank) {
		if (getRankList() == rank) return;
		this.rank = rank;
	}

	public SkyRank getRankList() {
		return rank;
	}

}
