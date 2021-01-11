package me.yungweezy.tokens.bp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class loadall {

	public static void loadAll(){
		for (Player player : Bukkit.getOnlinePlayers()){
			for (ItemStack stack : player.getInventory().getContents()){
				if (backpack.isBackPack(stack)){
					ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();
					String id = lore.get(1);
					id = ChatColor.stripColor(id);
					
					if (backpack.bps.get(id) != null){
						player.sendMessage(ChatColor.RED + "Found a backpack that was allready loaded, most likely duped, removed!");
						player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "If you believe this is an error, please contact an admin and give him a screenshot with this information:");
						player.sendMessage(ChatColor.AQUA + id + " || " + Integer.parseInt(ChatColor.stripColor(lore.get(2).replace(" slots", ""))) + " || " + System.currentTimeMillis());
						for (int slot = 0; slot <= player.getInventory().getSize(); slot = slot + 1){
							if (player.getInventory().getItem(slot) != null && player.getInventory().getItem(slot).getType() != null){
								if (player.getInventory().getItem(slot).isSimilar(stack)){
									player.getInventory().setItem(slot, null);
								}
							}
						}
					} else {
						int size = Integer.parseInt(ChatColor.stripColor(lore.get(2).replace(" slots", "")));
						backpack.loadBP(id, size, player);
					}
				}
			}
		}
	}
	
	public static void saveAll(){
		for (Player player : Bukkit.getOnlinePlayers()){
			for (ItemStack stack : player.getInventory().getContents()){
				if (backpack.isBackPack(stack)){
					ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();
					String id = lore.get(1);
					id = ChatColor.stripColor(id);
					
					if (backpack.bps.get(id) != null){ 
						backpack.saveBP(id, backpack.bps.get(id));
					}
				}
			}
		}
	}
	
	public static void loadAllFor(Player player){
		for (ItemStack stack : player.getInventory().getContents()){
			if (backpack.isBackPack(stack)){
				ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();
				String id = lore.get(1);
				id = ChatColor.stripColor(id);
				
				if (backpack.bps.get(id) != null){
					player.sendMessage(ChatColor.RED + "Found a backpack that was allready loaded, most likely duped, removed!");
					player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "If you believe this is an error, please contact an admin and give him a screenshot with this information:");
					player.sendMessage(ChatColor.AQUA + id + " || " + Integer.parseInt(ChatColor.stripColor(lore.get(2).replace(" slots", ""))) + " || " + System.currentTimeMillis());
					for (int slot = 0; slot <= player.getInventory().getSize(); slot = slot + 1){
						if (player.getInventory().getItem(slot) != null && player.getInventory().getItem(slot).getType() != null){
							if (player.getInventory().getItem(slot).isSimilar(stack)){
								player.getInventory().setItem(slot, null);
							}
						}
					}
				} else {
					int size = Integer.parseInt(ChatColor.stripColor(lore.get(2).replace(" slots", "")));
					backpack.loadBP(id, size, player);
				}
			}
		}
	}
	
	public static void saveAllFor(Player player){
		for (ItemStack stack : player.getInventory().getContents()){
			if (backpack.isBackPack(stack)){
				ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();
				String id = lore.get(1);
				id = ChatColor.stripColor(id);
				
				if (backpack.bps.get(id) != null){
					backpack.saveBP(id, backpack.bps.get(id));
				}
			}
		}
	}
}
