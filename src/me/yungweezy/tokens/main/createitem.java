package me.yungweezy.tokens.main;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class createitem {

	
	public static ItemStack createItem(Material mat, int sub, String name, ArrayList<String> lore){
		ItemStack stack = new ItemStack(mat, 1, (byte) sub);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		
		ArrayList<String> formattedlore = new ArrayList<String>();
		if (!lore.isEmpty()){
			for (String s : lore){
				String b = ChatColor.translateAlternateColorCodes('&', s);
				formattedlore.add(b);
			}
		}
		
		meta.setLore(formattedlore);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack setName(ItemStack stack, String name){
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack setLore(ItemStack stack, ArrayList<String> lore){
		ItemMeta meta = stack.getItemMeta();
		
		ArrayList<String> formattedlore = new ArrayList<String>();
		if (!lore.isEmpty()){
			for (String s : lore){
				String b = ChatColor.translateAlternateColorCodes('&', s);
				formattedlore.add(b);
			}
		}
		
		meta.setLore(formattedlore);
		
		stack.setItemMeta(meta);
		
		return stack;
	}
}
