package uk.co.jacekk.bukkit.playerstats;

import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.jacekk.bukkit.baseplugin.scheduler.BaseTask;
import uk.co.jacekk.bukkit.playerstats.data.PlayerData;

public class DatabaseUpdateTask extends BaseTask<PlayerStats> {
	
	public DatabaseUpdateTask(PlayerStats plugin){
		super(plugin);
	}
	
	public void run(){
		HashMap<String, PlayerData> reset = new HashMap<String, PlayerData>();
		
		HashMap<String, PlayerData> updatePlayers = new HashMap<String, PlayerData>();
		
		HashMap<String, PlayerData> updateBlocksPlaced = new HashMap<String, PlayerData>();
		HashMap<String, PlayerData> updateBlocksBroken = new HashMap<String, PlayerData>();
		
		HashMap<String, PlayerData> updateMobKills = new HashMap<String, PlayerData>();
		HashMap<String, PlayerData> updateMobDeaths = new HashMap<String, PlayerData>();
		
		HashMap<String, PlayerData> updatePlayerKills = new HashMap<String, PlayerData>();
		
		for (Entry<String, PlayerData> entry : plugin.playerDataManager.getAll().entrySet()){
			String playerName = entry.getKey();
			PlayerData data = entry.getValue();
			
			if (data.totalChatMessages > 0 || data.totalCommands > 0 || data.lastJoinTime == data.lastUpdate){
				updatePlayers.put(playerName, data);
			}
			
			if (data.blocksPlaced.size() > 0){
				updateBlocksPlaced.put(playerName, data);
			}
			
			if (data.blocksBroken.size() > 0){
				updateBlocksBroken.put(playerName, data);
			}
			
			if (data.mobsKilled.size() > 0){
				updateMobKills.put(playerName, data);
			}
			
			if (data.mobDeaths.size() > 0){
				updateMobDeaths.put(playerName, data);
			}
			
			if (data.playersKilled.size() > 0){
				updatePlayerKills.put(playerName, data);
			}
		}
		
		if (updatePlayers.size() > 0){
			plugin.log.info("Player data updated");
			plugin.mysql.performQuery(QueryBuilder.updatePlayers(updatePlayers));
		}
		
		if (updateBlocksPlaced.size() > 0){
			plugin.mysql.performQuery(QueryBuilder.updateBlocksPlaced(updateBlocksPlaced));
		}
		
		if (updateBlocksBroken.size() > 0){
			plugin.mysql.performQuery(QueryBuilder.updateBlocksBroken(updateBlocksBroken));
		}
		
		if (updateMobKills.size() > 0){
			plugin.mysql.performQuery(QueryBuilder.updateMobKills(updateMobKills));
		}
		
		if (updateMobDeaths.size() > 0){
			plugin.mysql.performQuery(QueryBuilder.updateMobDeaths(updateMobDeaths));
		}
		
		if (updatePlayerKills.size() > 0){
			plugin.mysql.performQuery(QueryBuilder.updatePlayerKills(updatePlayerKills));
		}
		
		reset.putAll(updatePlayers);
		
		reset.putAll(updateBlocksPlaced);
		reset.putAll(updateBlocksBroken);
		
		reset.putAll(updateMobKills);
		reset.putAll(updateMobDeaths);
		
		reset.putAll(updatePlayerKills);
		
		for (Entry<String, PlayerData> entry : reset.entrySet()){
			String playerName = entry.getKey();
			PlayerData data = entry.getValue();
			
			if (plugin.server.getPlayer(playerName) == null){
				plugin.playerDataManager.unregisterPlayer(playerName);
			}else{
				data.reset();
			}
		}
	}
	
}