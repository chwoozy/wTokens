package me.yungweezy.tokens.tokens;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

import me.yungweezy.tokens.bp.backpack;
import me.yungweezy.tokens.fortuneblocks.explosive;
import me.yungweezy.tokens.fortuneblocks.level;
import me.yungweezy.tokens.fortuneblocks.wg;
import me.yungweezy.tokens.main.ParticleEffect;

public class fireWorkSystem implements Listener {

	@EventHandler
	public void onFireWorkBlockBreak(BlockBreakEvent event){
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

		int fwlevel = level.getLevel(player, "firework");

		if (fwlevel <= 0){
			return;
		}

		int fortune = level.getLevel(player, "fortune");

		if (fortune == 0){
			fortune = 1;
		}

		Location loc = event.getBlock().getLocation();

		jaqueCantExplain(player, loc, fwlevel, fortune);
	}

	public void jaqueCantExplain(Player player, Location start, int fwlevel, int fortune){
		int left = fwlevel;
		Location working = start.clone();
		Random random = new Random();

		for (int i = 0; i <= left; i = i + 1){
			int rn = random.nextInt(4);

			//System.out.println(i + ": " + rn);
			
			if (rn == 1){
				if (random.nextBoolean() == true){
					working.setX(working.getX() + 1);
				} else {
					working.setX(working.getX() - 1);
				}
			} else if (rn == 2){
				if (random.nextBoolean() == true){
					working.setZ(working.getZ() + 1);
				} else {
					working.setZ(working.getZ() - 1);
				}
			} else if (rn == 0){
				working.setY(working.getY() - 1);
			} else if (rn == 3){ 
				working.setY(start.getY());
			}

			breakBlock(working.clone(), player, fortune, i * 4, false);
		}
		
		breakBlock(working.clone(), player, fortune, (left + 1) * 4, true);
	}

	@SuppressWarnings("deprecation")
	public void breakBlock(final Location working, final Player player, final int fortune, int delay, final boolean last){
		Plugin plugin = me.yungweezy.tokens.main.main.getPlugin(me.yungweezy.tokens.main.main.class);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (wg.getWorldGuard().canBuild(player, working)){	
					Material mtype = working.getBlock().getType();

					Boolean set = true;

					if (explosive.blacklistblocks.contains(mtype) && last == false){
						set = false;
					}

					if (working.getBlock().isLiquid() && last == false){
						set = false;
					}

					if (mtype == Material.AIR || mtype == null){
						if (!last){
							set = false;
						}
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
						if (!last){
							ItemStack itemtogive = new ItemStack(mtype, fortune, working.getBlock().getData());
							backpack.giveItems(player, itemtogive);
							player.updateInventory();

							working.getBlock().setType(Material.AIR);
						}
						//working.getWorld().playEffect(loc, Effect.STEP_SOUND, working.getBlock().getTypeId());
						//working.getWorld().playEffect(working, Effect.TILE_BREAK, working.getBlock().getType(), 0);
						//working.getWorld().playEffect(loc, Effect.ITEM_BREAK, 0);
						
						Location loc = working.clone();
						loc.setX(loc.getX() + 0.5);
						loc.setZ(loc.getZ() + 0.5);
						
						if (last == false){
							ParticleEffect effect = new ParticleEffect(ParticleEffect.ParticleType.FIREWORKS_SPARK, 0.02, 5, 0.001);
							effect.sendToLocation(loc);
						} else {
							//System.out.println("Triggering FW");
							for (int i = 0; i < 5; i = i + 1){
								Firework fire = (Firework) working.getWorld().spawnEntity(loc, EntityType.FIREWORK);
								FireworkMeta firemeta = fire.getFireworkMeta();
								firemeta.addEffects((FireworkEffect.builder().withColor(Color.RED).with(Type.BURST).build()));
								firemeta.setPower(0);
								fire.setFireworkMeta(firemeta);
							}
						}
					}
				} 
			}
		}, delay);
	}
}
