package me.yungweezy.tokens.tokens;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.yungweezy.tokens.bp.backpack;
import me.yungweezy.tokens.fortuneblocks.explosive;
import me.yungweezy.tokens.fortuneblocks.level;
import me.yungweezy.tokens.fortuneblocks.wg;

public class timeBombSystem implements Listener {

	private HashMap<UUID, Long> lastUsed = new HashMap<UUID, Long>();
	
	public long getLastUsed(UUID uuid){
		if (lastUsed.get(uuid) == null){
			lastUsed.put(uuid, System.currentTimeMillis());
			return 0;
		}
		
		return lastUsed.get(uuid);
	}
	
	public long getCooldownLength(int timebomblevel){
		long cooldown = 120000; // 2mins default?
		cooldown = cooldown - (1000 * timebomblevel);
		return cooldown;
	}
	
	@EventHandler
	public void onTimeBombInteract(PlayerInteractEvent event){
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK){
			return;
		}
		
		if (explosive.blacklistworld.contains(event.getPlayer().getLocation().getWorld().getName())){
			return;
		}

		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		Material handm = hand.getType();

		if (!explosive.workingtools.contains(handm)){
			return;
		}
		
		//if (explosive.blacklistblocks.contains(event.getBlock().getType())){
		//	return;
		//}
		
		int timebomblevel = level.getLevel(player, "timebomb");
		
		if (timebomblevel <= 0){
			return;
		}
		
		long lUsed = getLastUsed(player.getUniqueId());
		long cooldown = getCooldownLength(timebomblevel);
		long current = System.currentTimeMillis();
		
		if (current - lUsed < cooldown){
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lYour TimeBomb is still on cooldown for " + (cooldown - (current - lUsed)) / 1000) + " seconds");
			return;
		}
		
		triggerTimeBomb(player);
		
		lastUsed.put(player.getUniqueId(), System.currentTimeMillis());
	}
	
	@SuppressWarnings("deprecation")
	public void triggerTimeBomb(Player player){
		Location loc = player.getLocation();
		Location topcorner = loc.clone();
		int spherelvl = 5;
		int fortune = level.getLevel(player, "fortune");
		
		if (fortune == 0){
			fortune = 1;
		}
		
		Random random = new Random();
		
		topcorner.setX(topcorner.getX() + spherelvl);
		topcorner.setZ(topcorner.getZ() + spherelvl);
		topcorner.setY(topcorner.getY() + spherelvl);

		Location working = topcorner.clone();
		for (int z = topcorner.getBlockZ(); z >= topcorner.getBlockZ() - 2 * spherelvl; z = z -1){
			for (int y = topcorner.getBlockY(); y >= topcorner.getBlockY() - 2 * spherelvl; y = y - 1){
				for (int x = topcorner.getBlockX(); x >= topcorner.getBlockX() - 2 * spherelvl; x = x - 1){
					working.setX(x);
					if (wg.getWorldGuard().canBuild(player, working)){	
						Material mtype = working.getBlock().getType();

						Boolean set = true;
						
						if (working.distance(loc) > spherelvl){
							set = false;
						}
						
						if (explosive.blacklistblocks.contains(mtype)){
							set = false;
						}
						
						if (working.getBlock().isLiquid()){
							set = false;
						}

						if (mtype == Material.AIR || mtype == null){
							set = false;
						}

						if (mtype.toString().contains("_ORE")){
							if (mtype == Material.IRON_ORE){
								mtype = Material.IRON_INGOT;
							} else if (mtype == Material.GOLD_ORE){
								mtype = Material.GOLD_INGOT;
							} else {
								if (Material.getMaterial(mtype.toString().replaceAll("_ORE", "")) != null){
									mtype = Material.getMaterial(mtype.toString().replaceAll("_ORE", ""));
								}
							}
						}

						if (set){
							ItemStack itemtogive = new ItemStack(mtype, fortune, working.getBlock().getData());
							backpack.giveItems(player, itemtogive);
							player.updateInventory();

							working.getBlock().setType(Material.AIR);
							
							if (random.nextInt(4) == 2){
								working.getWorld().createExplosion(working, 0);
							}
						}
					}
				}
				working.setY(y);
			}
			working.setZ(z);
		}
	}
}
