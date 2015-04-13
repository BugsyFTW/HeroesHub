package me.bugsyftw.heroeshop.playerdata;

public class HeroCoin {

	private Integer amount;
	
	public HeroCoin(int amount){
		this.amount = amount;
	}
	
	public HeroCoin(){
		new HeroCoin(0);
	}
	
	public Integer getAmount(){
		return amount;
	}
	
	public void addHeroCoins(int a){
		this.amount = this.amount + a;
	}
	
	public void removeHeroCoins(int a){
		this.amount = this.amount - a;
	}
	
}
