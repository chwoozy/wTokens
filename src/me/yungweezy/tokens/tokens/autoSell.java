package me.yungweezy.tokens.tokens;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.yungweezy.tokens.main.main;

public class autoSell {

	private static HashMap<UUID, Long> lastTime = new HashMap<UUID, Long>();
	
	private static long getLastTime(Player player){
		if (!lastTime.containsKey(player.getUniqueId())){
			lastTime.put(player.getUniqueId(), System.currentTimeMillis());
		}
		
		return lastTime.get(player.getUniqueId());
	}
	
	private static void triggerAutoSell(Player player){
		String rank = null;
		
		for (String s : bountySystem.bountyAmounts.keySet()){
			if (player.hasPermission("essentials.warps." + s)){
				rank = s;
			}
		}
		
		if (rank == null){
			// no rank found
			return;
		}
		
		lastTime.put(player.getUniqueId(), System.currentTimeMillis());
		
		Plugin pl = main.getPlugin(main.class);
		pl.getServer().dispatchCommand(pl.getServer().getConsoleSender(), "autosell " + player.getName() + " " + rank);
	}
	
	private static long getMinDif(int level){
		int mindif = 60; //60 seconds, multiplied by 1k later
		
		mindif = mindif - (level * 2);
		
		return mindif * 1000;
	}
	
	public static void passFromClock(Player player, int level){
		long lasttime = getLastTime(player);
		long now = System.currentTimeMillis();
		long mindif = getMinDif(level);
		
		if (now - lasttime < mindif){
			return;
		}
		
		triggerAutoSell(player);
	}
}
