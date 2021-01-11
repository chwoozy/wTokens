package me.yungweezy.tokens.tokens;

import java.util.ArrayList;
import java.util.HashMap;

import me.yungweezy.tokens.main.fileaccessor;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class gems {

	public static HashMap<Integer, Double> gettierbyslot = new HashMap<Integer, Double>();
	public static HashMap<Integer, Integer> gettimebyslot = new HashMap<Integer, Integer>();
	private static ArrayList<Integer> possibletiers = new ArrayList<Integer>();
	public static ArrayList<String> defaultlore = new ArrayList<String>();
	public static String name;
	
	public static void update(){
		name = ChatColor.DARK_AQUA + "Sell Buff " + ChatColor.RED + "({TIER}x)";
		defaultlore.add(ChatColor.AQUA + "Right click to redeem a");
		defaultlore.add(ChatColor.GRAY + "{TIER}" + ChatColor.AQUA + " multiplier for " + ChatColor.GRAY + "{TIME}" + ChatColor.AQUA + " seconds");
		fileaccessor.reloadFileF("gui");
		FileConfiguration f = fileaccessor.getFileF("gui");
		for (int slot = 0; slot < 8; slot = slot + 1){
			if (f.get("gem_bufs." + slot) != null){
				possibletiers.add(f.getInt("gem_bufs." + slot + ".tier"));
				gettierbyslot.put(slot, f.getDouble("gem_bufs." + slot + ".tier"));
				gettimebyslot.put(slot, f.getInt("gem_bufs." + slot + ".time"));
				prices.gempricebyslot.put(slot, f.getInt("gem_bufs." + slot + ".price"));
			}
		}
	}
	
	public static boolean isGem(ItemStack stack){
		if (stack.getItemMeta() == null){
			return false;
		}
		
		ItemMeta meta = stack.getItemMeta();
		
		if (meta.getDisplayName() == null){
			return false;
		}
		
		if (meta.getLore() == null || meta.getLore().isEmpty()){
			return false;
		}
		
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		if (lore.size() != 2){
			return false;
		}
		
		String focus = ChatColor.GRAY + "{TIER}" + ChatColor.AQUA + " multiplier for " + ChatColor.GRAY + "{TIME}" + ChatColor.AQUA + " seconds";
		String controll = lore.get(1);
		
		for (int slot = 0; slot < 8; slot = slot + 1){
			String f = focus + "";
			if (gettierbyslot != null){
				f = f.replace("{TIER}", gettierbyslot.get(slot) + "x");
				f = f.replace("{TIME}", gettimebyslot.get(slot) + "");
			}
			
			if (f.equals(controll)){
				return true;
			} 
		}
		return false;
	}
	
	public static Double getTier(ItemStack stack){
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		String focus = ChatColor.GRAY + "{TIER}" + ChatColor.AQUA + " multiplier for " + ChatColor.GRAY + "{TIME}" + ChatColor.AQUA + " seconds";
		String controll = lore.get(1);
		
		for (int slot = 0; slot < 8; slot = slot + 1){
			String f = focus + "";
			if (gettierbyslot != null){
				f = f.replace("{TIER}", gettierbyslot.get(slot) + "x");
				f = f.replace("{TIME}", gettimebyslot.get(slot) + "");
			}
			
			if (f.equals(controll)){
				return gettierbyslot.get(slot);
			} 
		}
		
		return -1.0;
	}
	
	public static Integer getTime(ItemStack stack){
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		String focus = ChatColor.GRAY + "{TIER}" + ChatColor.AQUA + " multiplier for " + ChatColor.GRAY + "{TIME}" + ChatColor.AQUA + " seconds";
		String controll = lore.get(1);
		
		for (int slot = 0; slot < 8; slot = slot + 1){
			String f = focus + "";
			if (gettierbyslot != null){
				f = f.replace("{TIER}", gettierbyslot.get(slot) + "x");
				f = f.replace("{TIME}", gettimebyslot.get(slot) + "");
			}
			
			if (f.equals(controll)){
				return gettimebyslot.get(slot);
			} 
		}
		
		return -1;
	}
	
	public static Integer getCorrespondingSlot(ItemStack stack){
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		String focus = ChatColor.GRAY + "{TIER}" + ChatColor.AQUA + " multiplier for " + ChatColor.GRAY + "{TIME}" + ChatColor.AQUA + " seconds";
		String controll = lore.get(1);
		
		for (int slot = 0; slot < 8; slot = slot + 1){
			String f = focus + "";
			if (gettierbyslot != null){
				f = f.replace("{TIER}", gettierbyslot.get(slot) + "x");
				f = f.replace("{TIME}", gettimebyslot.get(slot) + "");
			}
			
			if (f.equals(controll)){
				return slot;
			} 
		}
		
		return -1;
	}
	
	
	
}
