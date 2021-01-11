package me.yungweezy.tokens.tokens;


import java.util.HashMap;

import me.yungweezy.tokens.fortuneblocks.level;
import me.yungweezy.tokens.main.ParticleEffect;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class clock {

	public static HashMap<String, Integer> flytime = new HashMap<String, Integer>();
	
	public static void startClock2(){
		Plugin plugin = me.yungweezy.tokens.main.main.getPlugin(me.yungweezy.tokens.main.main.class);
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()){
					if (player.isFlying() && player.getAllowFlight() == true && player.getGameMode() != GameMode.getByValue(3) && !player.isSneaking()){
						Location belowplayer = player.getLocation();
						belowplayer.setY(belowplayer.getY() - 0.5);
						
						ParticleEffect effect = new ParticleEffect(ParticleEffect.ParticleType.FIREWORKS_SPARK, 0.02, 20, 0.001);
						effect.sendToLocation(belowplayer);
					}
				}
			}
		}, 2L, 2L);
	}
	
	public static void startClock(){
		Plugin plugin = me.yungweezy.tokens.main.main.getPlugin(me.yungweezy.tokens.main.main.class);
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()){
					int speed = level.getLevel(player, "Speed");
					int nightvision = level.getLevel(player, "NightVision");
					int haste = level.getLevel(player, "Haste");
					int regen = level.getLevel(player, "Regen");
					int fly = level.getLevel(player, "Jetpack");
					int autosell = level.getLevel(player, "AutoSell");
					
					if (autosell > 0){
						autoSell.passFromClock(player, autosell);
					}
					
					if (fly >= 1){
						if (!player.getLocation().getWorld().getName().equalsIgnoreCase("PvP")){
							flytime.put(player.getName(), fly * 10);
							player.setAllowFlight(true);
						} else {
							flytime.remove(player.getName());
							player.setAllowFlight(false);
							player.setFlying(false);
						}
					} else if (flytime.containsKey(player.getName())){
						int time = flytime.get(player.getName());
						if (time - 1 <= 0){
							if (player.getGameMode() != GameMode.CREATIVE){
								player.setFlying(false);
								player.setAllowFlight(false);
							}
						} else {
							if (!player.getLocation().getWorld().getName().equalsIgnoreCase("PvP")){
								flytime.put(player.getName(), time - 1);
							} else {
								flytime.remove(player.getName());
								player.setFlying(false);
								player.setAllowFlight(false);
							}
						}
					}
					
					int speedtier = speed - 1;
					if (speedtier > 5) {
						speedtier = 5;
					}
					
					PotionEffect speedp = new PotionEffect(PotionEffectType.SPEED, speed * 100, speedtier);
					if (speed != 0){
						player.removePotionEffect(PotionEffectType.SPEED);
						player.addPotionEffect(speedp);
					}
					
					PotionEffect nightvisionp = new PotionEffect(PotionEffectType.NIGHT_VISION, nightvision * 100, 1);
					if (nightvision != 0){
						player.removePotionEffect(PotionEffectType.NIGHT_VISION);
						player.addPotionEffect(nightvisionp);
					}
					int hastetier = haste - 1;
					if (hastetier > 5) {
						hastetier = 5;
					}
					PotionEffect hastep = new PotionEffect(PotionEffectType.FAST_DIGGING, haste * 100, hastetier);
					if (haste != 0){
						player.removePotionEffect(PotionEffectType.FAST_DIGGING);
						player.addPotionEffect(hastep);
					}	
					
					int regentier = regen - 1;
					if (regentier > 5) {
						regentier = 5;
					}
					PotionEffect regenp = new PotionEffect(PotionEffectType.REGENERATION, regen * 100, regentier);
					if (regen != 0){
						player.removePotionEffect(PotionEffectType.REGENERATION);
						player.addPotionEffect(regenp);
					}
					
				}
			}
		}, 20L, 20L);
	}
}
