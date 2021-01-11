package me.yungweezy.tokens.fortuneblocks;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class level {

	public static boolean addLevel(Player player, String customenchant){
		if (player.getItemInHand() == null || player.getItemInHand().getType() == null || player.getItemInHand().getType() == Material.AIR){
			return false;
		}
		
		ItemStack item = player.getItemInHand();
		Material m = item.getType();
		
		if (explosive.workingtools.contains(m)){
			
			ArrayList<String> vanillaenchants = new ArrayList<String>();
			vanillaenchants.add("efficiency");
			vanillaenchants.add("fortune");
			vanillaenchants.add("unbreaking");
			
			if (vanillaenchants.contains(customenchant.toLowerCase())){
				if (customenchant.equalsIgnoreCase("efficiency")){
					int lvl = player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED);
					player.getItemInHand().removeEnchantment(Enchantment.DIG_SPEED);
					player.getItemInHand().addUnsafeEnchantment(Enchantment.DIG_SPEED, lvl + 1);
					return true;
				} else if (customenchant.equalsIgnoreCase("fortune")){
					int lvl = player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
					player.getItemInHand().removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
					player.getItemInHand().addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, lvl + 1);
					return true;
				} else if (customenchant.equalsIgnoreCase("unbreaking")){
					int lvl = player.getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY);
					player.getItemInHand().removeEnchantment(Enchantment.DURABILITY);
					player.getItemInHand().addUnsafeEnchantment(Enchantment.DURABILITY, lvl + 1);
					return true;
				}
				return false;
			}
			
			
			ArrayList<String> lore;
			int old = 0;
			String remove = customenchant;
			if (item.getItemMeta().getLore() != null){
				lore = (ArrayList<String>) item.getItemMeta().getLore();
				
				for (String s : lore){
					s = ChatColor.stripColor(s);
					String[] s2 = s.split(":");
					int round = 0;
					int needed = -1;
					for (String s3 : s2){
						if (s3.equalsIgnoreCase(customenchant)){
							needed = round + 1;
						}
						
						if (round == needed){
							s3 = s3.replaceAll(" ", "");
							old = Integer.parseInt(s3);
							remove = remove + ": " + s3;
						}
						round = round + 1;
					}
				}
				
			} else {
				lore = new ArrayList<String>();
			}
			
			lore.remove(ChatColor.GOLD + remove);
			lore.remove(ChatColor.GRAY + remove);
			
			lore.add(ChatColor.GOLD + customenchant + ": " + (old + 1));
			
			for (int i = old; i >= 0; i = i - 1){
				lore.remove(ChatColor.GRAY + customenchant + ": " + i);
				lore.remove(ChatColor.GOLD + customenchant + ": " + i);
			}
			
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
			player.setItemInHand(item);
			return true;
		} else {
			return false;
		}
	}
	
	public static int getLevel(Player player, String customenchant){
		if (player.getItemInHand() == null || player.getItemInHand().getType() == null || player.getItemInHand().getType() == Material.AIR){
			return 0;
		}
		
		ItemStack item = player.getItemInHand();
		
		if (item.getItemMeta().getLore() != null && !item.getItemMeta().getLore().isEmpty()){
			ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();

			int level = 0;
			for (String s : lore){
				s = ChatColor.stripColor(s);
				String[] s2 = s.split(":");
				int round = 0;
				int needed = -1;
				for (String s3 : s2){
					if (s3.equalsIgnoreCase(customenchant)){
						needed = round + 1;
					}

					if (round == needed){
						s3 = s3.replaceAll(" ", "");
						level = Integer.parseInt(s3);
					}
					round = round + 1;
				}
			}
			return level;
		}
		
		return 0;
	}
}
