package me.yungweezy.tokens.tokens;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.yungweezy.tokens.fortuneblocks.explosive;
import me.yungweezy.tokens.fortuneblocks.level;

public class bountySystem implements Listener {

	private HashMap<UUID, Integer> blocksBroken = new HashMap<UUID, Integer>();
	public static HashMap<String, Double> bountyAmounts = new HashMap<String, Double>();
	
	public static void cacheBountyAmounts(Plugin pl){
		FileConfiguration config = pl.getConfig();
		
		for (String s : config.getConfigurationSection("bounty").getKeys(false)){
			bountyAmounts.put(s, config.getDouble("bounty." + s));
		}
	}
	
	public double getBounty(Player player){
		double amount = 1;
		for (String s : bountyAmounts.keySet()){
			if (player.hasPermission("essentials.warps." + s)){
				amount = bountyAmounts.get(s);
			}
		}
		
		return amount;
	}
	
	public int hasBroken(Player player){
		if (!blocksBroken.containsKey(player.getUniqueId())){
			return 0;
		}
		
		return blocksBroken.get(player.getUniqueId());
	}
	
	public int getRequired(int level){
		int amount = 10000;
		amount = amount - (level * 1000);
		
		return 10000;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event){
		if (explosive.blacklistworld.contains(event.getBlock().getLocation().getWorld().getName())){
			return;
		}

		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		Material handm = hand.getType();

		if (!explosive.workingtools.contains(handm)){
			return;
		}
		
		if (explosive.blacklistblocks.contains(event.getBlock().getType())){
			return;
		}
		
		int bountyLevel = level.getLevel(player, "bounty");
		
		if (bountyLevel <= 0){
			return;
		}
		
		int broken = hasBroken(player);
		broken = broken + 1;
		blocksBroken.put(player.getUniqueId(), broken);
		
		int required = getRequired(bountyLevel);
		if (broken >= required){
			double bountyamount = getBounty(player);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lReceived a bounty of &c&l" + bountyamount + "&e&l for mining &c&l" + required + "&e&l blocks"));
			
			blocksBroken.put(player.getUniqueId(), 0);
		}
	}
	
	
}
