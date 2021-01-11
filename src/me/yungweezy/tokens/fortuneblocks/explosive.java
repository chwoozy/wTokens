package me.yungweezy.tokens.fortuneblocks;

import java.util.ArrayList;
import java.util.Random;

import me.clip.ezblocks.EZBlocks;
import me.yungweezy.tokens.bp.backpack;
import me.yungweezy.tokens.main.fileaccessor;
import me.yungweezy.tokens.tokens.gui;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class explosive implements Listener {
	
	public static ArrayList<String> blacklistworld = new ArrayList<String>();
	public static ArrayList<Material> blacklistblocks = new ArrayList<Material>();
	public static ArrayList<Material> workingtools = new ArrayList<Material>();
	
	@SuppressWarnings("unchecked")
	public static void updateLists(){
		fileaccessor.reloadFileF("config");
		FileConfiguration config = fileaccessor.getFileF("config");
		
		gui.exchange = config.getBoolean("exchange");
		
		blacklistworld = (ArrayList<String>) config.get("enchants.blacklist.worlds");
		
		ArrayList<String> mats = (ArrayList<String>) config.get("enchants.blacklist.blocks");
		
		for (String s : mats){
			if (Material.getMaterial(s) != null){
				blacklistblocks.add(Material.getMaterial(s));
			} else {
				System.out.println(s + " is not a valid material! (blocks blacklist)");
			}
		}
		
		mats = (ArrayList<String>) config.get("enchants.workwith");
		
		for (String s : mats){
			if (Material.getMaterial(s) != null){
				workingtools.add(Material.getMaterial(s));
			} else {
				System.out.println(s + " is not a valid material! (enchants workwith)");
			}
		}
	}
	
	public int nerfFortune(int level){
		double lvl = 1;
		
		if (level == 0){
			lvl = 1;
		} else {
			lvl = (level * 0.2);
			lvl = 1 + lvl;
		}
		
		return (int) Math.round(lvl);
	}
	
	/*
		if (fortune == 0){
			fortune = 1;  
		} else if (fortune == 1){
			fortune = 4;
		} else {
			fortune = (int) (4 + fortune * 0.3);
		}
	 */
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event){
		if (blacklistworld.contains(event.getBlock().getLocation().getWorld().getName())){
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		Material m = item.getType();

		if (!workingtools.contains(m)){
			return;
		}
		
		int explosivelvl = level.getLevel(player, "Explosive");
		int spherelvl = level.getLevel(player, "Sphere");
		int circlelvl = level.getLevel(player, "Hex");
		int cubedlvl = level.getLevel(player, "Blast");
		int downwards = level.getLevel(player, "Drill");
		
		if (explosivelvl != 0 || spherelvl != 0 || circlelvl != 0 || cubedlvl != 0){
			Location blockloc = event.getBlock().getLocation();
			blockloc.getWorld().createExplosion(blockloc, 0);
		}
		
		EZBlocks.getEZBlocks().getBreakHandler().handleBlockBreakEvent(event);
		
		int fort = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
		int fortune = nerfFortune(fort);
		
		if (item.getItemMeta().getLore() != null && !item.getItemMeta().getLore().isEmpty() || (explosivelvl == 0 || spherelvl == 0 || circlelvl == 0 || cubedlvl == 0)){
			Location loc = event.getBlock().getLocation();

			if (wg.getWorldGuard().canBuild(player, event.getBlock())){
				Material mtype = event.getBlock().getType();
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

				ItemStack originaldrops = new ItemStack(mtype, fortune, event.getBlock().getData());
				backpack.giveItems(player, originaldrops);
				event.getBlock().setType(Material.AIR);
				
				event.setCancelled(true);
			} else {
				event.setCancelled(true);
				return;
			}

			Boolean tocontinue = true;
			int round = 0;

			if (explosivelvl >= 1){
				loc.getWorld().createExplosion(loc, 0);
				loc.setY(loc.getY() + 1);
				loc.getWorld().createExplosion(loc, 0);
				loc.setX(loc.getX() + 1);
				loc.getWorld().createExplosion(loc, 0);
			}

			if (explosivelvl < 1000 && spherelvl == 0 && circlelvl == 0 && cubedlvl == 0){
				while (tocontinue == true) {
					int xyz = toadd.getXZ();
					if (xyz == 1){
						loc.setX(loc.getX() + toadd.toAdd());
					} else if (xyz == 2){
						loc.setZ(loc.getZ() + toadd.toAdd());
					} 

					if (round != explosivelvl){

						Location original = event.getBlock().getLocation();
						double dist = loc.distance(original);

						if (dist >= 5){
							loc = original;
						}

						if (wg.getWorldGuard().canBuild(player, loc)){	
							Material mtype = loc.getBlock().getType();

							if (blacklistblocks.contains(mtype)){
								return;
							}

							if (loc.getBlock().isLiquid()){
								return;
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

							ItemStack itemtogive = new ItemStack(mtype, fortune, loc.getBlock().getData());
							backpack.giveItems(player, itemtogive);
							player.updateInventory();

							loc.getBlock().setType(Material.AIR);
							
							if (downwards > 0){
								double oldy = loc.getY();
								Random random = new Random();
								for (int d = 0; d <= downwards; d = d + 1){
									if (random.nextInt(3) != 2){
										loc.setY(oldy);
										break;
									}
									
									loc.setY(loc.getY() - 1);
									
									Material mtype1 = loc.getBlock().getType();

									if (blacklistblocks.contains(mtype1)){
										return;
									}

									if (loc.getBlock().isLiquid()){
										return;
									}


									if (mtype1.toString().contains("_ORE")){
										if (mtype1 == Material.IRON_ORE){
											mtype1 = Material.IRON_INGOT;
										} else if (mtype1 == Material.GOLD_ORE){
											mtype1 = Material.GOLD_INGOT;
										} else {
											if (Material.getMaterial(mtype1.toString().replaceAll("_ORE", "")) != null){
												mtype1 = Material.getMaterial(mtype1.toString().replaceAll("_ORE", ""));
											}
										}
									}

									ItemStack itemtogive1 = new ItemStack(mtype1, fortune, loc.getBlock().getData());
									backpack.giveItems(player, itemtogive1);
									player.updateInventory();

									loc.getBlock().setType(Material.AIR);
								}
								
								loc.setY(oldy);
							}
						}
						
						round = round + 2;
						if (round >= explosivelvl){ 
							tocontinue = false;
						}
					} else {
						return;
					}
				}
			} else if (explosivelvl >= 1000 && spherelvl == 0 && circlelvl == 0 && cubedlvl == 0){
				// FIERKENT
				Location topcorner = loc.clone();
				topcorner.setX(topcorner.getX() + 3);
				topcorner.setZ(topcorner.getZ() + 3);
				topcorner.setY(topcorner.getY() - 1);

				Location working = topcorner.clone();
				for (int z = topcorner.getBlockZ(); z >= topcorner.getBlockZ() - 7; z = z -1){
					for (int y = topcorner.getBlockY(); y >= topcorner.getBlockY() - 5; y = y - 1){
						for (int x = topcorner.getBlockX(); x >= topcorner.getBlockX() - 8; x = x - 1){
							working.setX(x);
							if (wg.getWorldGuard().canBuild(player, working)){	
								Material mtype = working.getBlock().getType();

								Boolean set = true;

								if (blacklistblocks.contains(mtype)){
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
									ItemStack itemtogive = new ItemStack(mtype, fortune, loc.getBlock().getData());
									backpack.giveItems(player, itemtogive);
									player.updateInventory();

									working.getBlock().setType(Material.AIR);
								}
							}
						}
						working.setY(y);
					}
					working.setZ(z);
				}
			} else if (spherelvl > 0 && circlelvl == 0){
				Location topcorner = loc.clone();
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
								
								if (blacklistblocks.contains(mtype)){
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
								}
							}
						}
						working.setY(y);
					}
					working.setZ(z);
				}
			} else if (circlelvl > 0 && cubedlvl == 0){
				Location topcorner = loc.clone();
				
				int radius = 1;
				switch (circlelvl){
				case (1):
					radius = 3;
				break;
				case (2):
					radius = 5;
				break;
				case (3):
					radius = 7;
				break;
				case (4):
					radius = 9;
				break;
				case (5):
					radius = 11;
				break;
				case (6):
					radius = 13;
				break;
				}		
				
				int off = radius / 2;
				
				topcorner.setX(topcorner.getX() + off);
				topcorner.setZ(topcorner.getZ() + off);
				
				Location working = topcorner.clone();
				for (int z = topcorner.getBlockZ(); z >= topcorner.getBlockZ() - radius; z = z -1){
					for (int x = topcorner.getBlockX(); x >= topcorner.getBlockX() - radius; x = x - 1){
						working.setX(x);
						if (wg.getWorldGuard().canBuild(player, working)){	
							Material mtype = working.getBlock().getType();

							Boolean set = true;

							if (working.distance(loc) > off){
								set = false;
							}

							if (blacklistblocks.contains(mtype)){
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
							} 
						}
					}
					working.setZ(z);
				}
			} else if (cubedlvl > 0){
				Location topcorner = loc.clone();
				
				int radius = 1;
				switch (cubedlvl){
				case (1):
					radius = 3;
				break;
				case (2):
					radius = 5;
				break;
				case (3):
					radius = 7;
				break;
				case (4):
					radius = 9;
				break;
				case (5):
					radius = 11;
				break;
				case (6):
					radius = 13;
				break;
				}		
				
				int off = radius / 2;
				
				topcorner.setX(topcorner.getX() + off);
				topcorner.setZ(topcorner.getZ() + off);
				topcorner.setY(topcorner.getY() + off);

				Location working = topcorner.clone();
				for (int z = topcorner.getBlockZ(); z >= topcorner.getBlockZ() - radius; z = z -1){
					for (int y = topcorner.getBlockY(); y > topcorner.getBlockY() - radius; y = y - 1){
						for (int x = topcorner.getBlockX(); x > topcorner.getBlockX() - radius; x = x - 1){
							working.setX(x);
							if (wg.getWorldGuard().canBuild(player, working)){	
								Material mtype = working.getBlock().getType();

								Boolean set = true;
								
								if (blacklistblocks.contains(mtype)){
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
								}
							}
						}
						working.setY(y);
					}
					working.setZ(z);
				}
			} 
		} else {
			Material mtype = event.getBlock().getType();
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

			ItemStack originaldrops = new ItemStack(mtype, fortune, event.getBlock().getData());
			backpack.giveItems(player, originaldrops);
			event.getBlock().setType(Material.AIR);

			event.setCancelled(true);
		}
	} 
}
