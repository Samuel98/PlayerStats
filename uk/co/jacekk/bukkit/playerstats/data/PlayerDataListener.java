package uk.co.jacekk.bukkit.playerstats.data;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.playerstats.PlayerStats;

public class PlayerDataListener extends BaseListener<PlayerStats> {
	
	public PlayerDataListener(PlayerStats plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		String playerName = event.getPlayer().getName();
		
		if (!plugin.playerDataManager.gotDataFor(playerName)){
			plugin.playerDataManager.registerPlayer(playerName);
		}else{
			plugin.playerDataManager.getDataFor(playerName).lastJoinTime =  System.currentTimeMillis() / 1000;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		PlayerData data = plugin.playerDataManager.getDataFor(event.getPlayer().getName());
		++data.totalChatMessages;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		PlayerData data = plugin.playerDataManager.getDataFor(event.getPlayer().getName());
		++data.totalCommands;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		plugin.playerDataManager.getDataFor(event.getPlayer().getName()).addBlockBreak(event.getBlock().getType());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBucketFill(PlayerBucketFillEvent event){
		plugin.playerDataManager.getDataFor(event.getPlayer().getName()).addBlockBreak(event.getBlockClicked().getRelative(event.getBlockFace()).getType());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		plugin.playerDataManager.getDataFor(event.getPlayer().getName()).addBlockPlace(event.getBlock().getType());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBucketEmpty(PlayerBucketEmptyEvent event){
		PlayerData data = plugin.playerDataManager.getDataFor(event.getPlayer().getName());
		
		Material bucketType = event.getPlayer().getItemInHand().getType();
		
		data.addBlockPlace((bucketType == Material.WATER_BUCKET) ? Material.WATER : Material.LAVA);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event){
		LivingEntity entity = event.getEntity();
		EntityDamageEvent damageEvent = entity.getLastDamageCause();
		
		if (damageEvent instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent entityDamageEvent = (EntityDamageByEntityEvent) damageEvent;
			
			Entity killer = entityDamageEvent.getDamager();
			
			if (killer instanceof LivingEntity){
				if (killer instanceof Player){
					String killerName = ((Player) killer).getName();
					
					if (entity instanceof Player){
						// player killed player
						plugin.playerDataManager.getDataFor(killerName).addPlayerKill(((Player) entity).getName());
					}else{
						// player killed mob
						plugin.playerDataManager.getDataFor(killerName).addMobKill(entity.getType());
					}
				}else{
					if (entity instanceof Player){
						// player killed mob
						plugin.playerDataManager.getDataFor(((Player) entity).getName()).addMobDeath(killer.getType());
					}
				}
			}
		}
	}
	
}
