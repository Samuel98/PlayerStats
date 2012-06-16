package uk.co.jacekk.bukkit.playerstats.data;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class PlayerData {
	
	// the number of chat messages a player has sent
	public int totalChatMessages;
	public int totalCommands;
	
	// the number of blocks a player has changed
	public HashMap<Material, Integer> blocksBroken;
	public HashMap<Material, Integer> blocksPlaced;
	
	// how many times a player has killed something
	public HashMap<EntityType, Integer> mobsKilled;
	public HashMap<String, Integer> playersKilled;
	
	// how many times a player has been killed
	public HashMap<EntityType, Integer> mobDeaths;
	public HashMap<String, Integer> playerDeaths;
	
	public PlayerData(){
		this.totalChatMessages = 0;
		this.totalCommands = 0;
		
		this.blocksBroken = new HashMap<Material, Integer>();
		this.blocksPlaced = new HashMap<Material, Integer>();
		
		this.mobsKilled = new HashMap<EntityType, Integer>();
		this.playersKilled = new HashMap<String, Integer>();
		
		this.mobDeaths = new HashMap<EntityType, Integer>();
		this.playerDeaths = new HashMap<String, Integer>();
	}
	
	public void addBlockBreak(Material type){
		this.blocksBroken.put(type, (this.blocksBroken.containsKey(type)) ? this.blocksBroken.get(type) + 1 : 1);
	}
	
	public void addBlockPlace(Material type){
		this.blocksPlaced.put(type, (this.blocksPlaced.containsKey(type)) ? this.blocksPlaced.get(type) + 1 : 1);
	}
	
	public void addMobKill(EntityType type){
		this.mobsKilled.put(type, (this.mobsKilled.containsKey(type)) ? this.mobsKilled.get(type) + 1 : 1);
	}
	
	public void addMobDeath(EntityType type){
		this.mobDeaths.put(type, (this.mobDeaths.containsKey(type)) ? this.mobDeaths.get(type) + 1 : 1);
	}
	
	public void addPlayerKill(String name){
		this.playersKilled.put(name, (this.playersKilled.containsKey(name)) ? this.playersKilled.get(name) + 1 : 1);
	}
	
	public void addPlayerDeath(String name){
		this.playerDeaths.put(name, (this.playerDeaths.containsKey(name)) ? this.playerDeaths.get(name) + 1 : 1);
	}
	
}