package me.yungweezy.tokens.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.yungweezy.tokens.main.createitem;
import me.yungweezy.tokens.main.fileaccessor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class backpack {

	public static HashMap<String, Inventory> bps = new HashMap<String, Inventory>();
	//public static HashMap<String, Long> lastspam = new HashMap<String, Long>();
	
	
	public static boolean giveRandomBackpack(Player player, int size){
		String id = name.createRandomID();
		boolean success = backpack.giveBackpack(player, size, id);
		return success;
	}
	
	public static boolean giveBackpack(Player player, int size, String id){
		int firstempty = -100;
		for (int slot = 0; slot <= player.getInventory().getSize(); slot = slot + 1){
			if (player.getInventory().getItem(slot) == null || player.getInventory().getItem(slot).getType() == null || player.getInventory().getItem(slot).getType() == Material.AIR){
				firstempty = slot;
			}
		}
		
		if (firstempty == -100){
			return false;
		}
		
		backpack.loadBP(id, size, player);
		
		Inventory inv = bps.get(id);
		backpack.saveBP(id, inv);
		backpack.loadBP(id, size, player);
		inv = bps.get(id);
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(backpack.getInvNameType(size));
		lore.add("&3" + id);
		lore.add("&2" + size + " slots");
		
		ItemStack stack = createitem.createItem(Material.CHEST, 0, backpack.getInvNameType(size), lore);
		player.getInventory().addItem(stack);
		
		return true;
	}
	
	public static void unloadBP(String id){
		if (bps.get(id) == null){
			return;
		}
		
		Inventory tosave = bps.get(id);
		bps.remove(id);
		
		fileaccessor.reloadFileF("backpacks/" + id);
		FileConfiguration bpfile = fileaccessor.getFileF("backpacks/" + id);
		bpfile.set("contents", tosave.getContents());
	}
	
	@SuppressWarnings("unchecked")
	public static void loadBP(String id, int size, Player player){
		fileaccessor.reloadFileF("backpacks/" + id);
		FileConfiguration bpfile = fileaccessor.getFileF("backpacks/" + id);
		if (bpfile.get("contents") != null){
			Inventory inv = Bukkit.createInventory(null, size, backpack.getInvNameType(size));
			for (int i = 0; i < inv.getSize(); i = i + 1){
				if (bpfile.get("contents." + i) != null){
					ItemStack stack = new ItemStack(Material.getMaterial(bpfile.getString("contents." + i + ".material")), bpfile.getInt("contents." + i + ".amount"), (byte) bpfile.getInt("contents." + i + ".subid"));
					ItemMeta meta = stack.getItemMeta();
					if (bpfile.get("contents." + i + ".name") != null){
						meta.setDisplayName(bpfile.getString("contents." + i + ".name"));
					}
					if (bpfile.get("contents." + i + ".lore") != null){
						meta.setLore((List<String>) bpfile.get("contents." + i + ".lore"));
					}
					
					stack.setItemMeta(meta);
					
					if (bpfile.get("contents." + i + ".enchantments") != null){
						Map<String, Object> lvls = bpfile.getConfigurationSection("contents." + i + ".enchantments").getValues(false);
						for (String enchname : lvls.keySet()){
							Enchantment ench = Enchantment.getByName(enchname);
							int lvl = (int) lvls.get(enchname);
							stack.addUnsafeEnchantment(ench, lvl);
						}
					}
					
					inv.setItem(i, stack);
				}
			}
			bps.put(id, inv);
		} else {
			Inventory inv = Bukkit.createInventory(null, size, backpack.getInvNameType(size));
			bps.put(id, inv);
		}
	}
	
	public static void saveBP(String id, Inventory inv){
		fileaccessor.reloadFileF("backpacks/" + id);
		FileConfiguration bpfile = fileaccessor.getFileF("backpacks/" + id);
		bps.remove(id);
		for (int i = 0; i < inv.getSize(); i = i + 1){
			if (inv.getItem(i) != null && inv.getItem(i).getType() != null && inv.getItem(i).getType() != Material.AIR){
				ItemStack stack = inv.getItem(i);
				bpfile.set("contents." + i + ".material", stack.getType().toString());
				bpfile.set("contents." + i + ".amount", stack.getAmount());
				bpfile.set("contents." + i + ".subid", stack.getDurability());
				if (stack.getItemMeta().getDisplayName() != null){
					bpfile.set("contents." + i + ".name", stack.getItemMeta().getDisplayName());
				} else {
					bpfile.set("contents." + i + ".name", null);
				}
				
				if (stack.getItemMeta().getLore() != null && !stack.getItemMeta().getLore().isEmpty()){
					bpfile.set("contents." + i + ".lore", stack.getItemMeta().getLore());
				} else {
					bpfile.set("contents." + i + ".lore", null);
				}
				
				if (stack.getEnchantments() != null && !stack.getEnchantments().isEmpty()){
					HashMap<String, Integer> lvls = new HashMap<String, Integer>();
					for (Enchantment ench : stack.getEnchantments().keySet()){
						int level = stack.getEnchantmentLevel(ench);
						lvls.put(ench.getName(), level);
					}
					bpfile.set("contents." + i + ".enchantments", lvls);
				} else {
					bpfile.set("contents." + i + ".enchantments", null);
				}
			} else {
				bpfile.set("contents." + i, null);
			}
		}
		fileaccessor.saveFile("backpacks/" + id);
	}
	
	public static String getInvNameType(int size){
		
		switch (size){
		case (9):
			return ChatColor.translateAlternateColorCodes('&', "&fTiny Backpack");
		case (18):
			return ChatColor.translateAlternateColorCodes('&', "&aMedium Backpack");
		case (27):
			return ChatColor.translateAlternateColorCodes('&', "&bLarge Backpack");
		case (36):
			return ChatColor.translateAlternateColorCodes('&', "&dHuge Backpack");
		case (45):
			return ChatColor.translateAlternateColorCodes('&', "&eParadise Backpack");
		case (54):
			return ChatColor.translateAlternateColorCodes('&', "&3Space Backpack");
		case (63):
			return ChatColor.translateAlternateColorCodes('&', "&5Epic Backpack");
		case (72):
			return ChatColor.translateAlternateColorCodes('&', "&6Heavenly Backpack");
		}
		
		
		return "&cUnknown Backpack";
	}
	
	public static boolean isBackPack(ItemStack stack){
		if (stack == null || stack.getType() == null || stack.getType() == Material.AIR){
			return false;
		}
		
		if (stack.getType() != Material.CHEST){
			return false;
		}
		
		if (stack.getItemMeta() == null){
			return false;
		}
		
		if (stack.getItemMeta().getLore() == null || stack.getItemMeta().getDisplayName() == null){
			return false;
		}
		
		ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();
		if (lore.size() != 3){
			return false;
		}
		
		String b9s = backpack.getInvNameType(9);
		String b18s = backpack.getInvNameType(18);
		String b27s = backpack.getInvNameType(27);
		String b36s = backpack.getInvNameType(36);
		String b45s = backpack.getInvNameType(45);
		String b54s = backpack.getInvNameType(54);
		String b63s = backpack.getInvNameType(63);
		String b72s = backpack.getInvNameType(72);

		String btype = lore.get(0);
		if (!btype.equals(b9s) && !btype.equals(b18s) && !btype.equals(b27s) && !btype.equals(b36s) && !btype.equals(b45s) && !btype.equals(b54s) && !btype.equals(b63s) && !btype.equals(b72s)){
			return false;
		}
		
		return true;
	}
	
	public static ArrayList<String> getAllBackPacks(Player player){
		ArrayList<String> bpz = new ArrayList<String>();
		for (ItemStack stack : player.getInventory().getContents()){
			if (backpack.isBackPack(stack)){
				ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();
				String id = lore.get(1);
				id = ChatColor.stripColor(id);
				bpz.add(id);
			}
		}
		
		return bpz;
	}
	
	public static boolean giveItems(Player player, ItemStack give){
		Random rand = new Random();
		int randint = rand.nextInt(1000);
		if (randint == 102){
			// give timeshard
			int timeshardnr = 1 + (rand.nextInt(8));
			String cmd = "boss giveshard " + player.getName() + " " + timeshardnr;
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
		}
		
		Inventory pinv = player.getInventory();
		
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		ArrayList<String> invids = me.yungweezy.tokens.bp.backpack.getAllBackPacks(player);
		for (String id : invids){
			Inventory bp = me.yungweezy.tokens.bp.backpack.bps.get(id);
			invs.add(bp);
		}
		
		invs.add(pinv);
		
		int stilltogive = give.getAmount();
		
		if (give.getType() == Material.LAPIS_ORE){
			give.setType(Material.INK_SACK);
			give.setDurability((short) 4);
		} else if (give.getType() == Material.REDSTONE_ORE || give.getType() == Material.REDSTONE || give.getType() == Material.GLOWING_REDSTONE_ORE){
			give.setType(Material.REDSTONE);
		}
		
		for (Inventory inv : invs){
			int i = inv.firstEmpty();
			if (i == -1){
				for (int slot = 0; slot < inv.getSize(); slot = slot + 1){
					ItemStack stack = inv.getItem(slot);
					boolean same = true;
					if (stack.getType() != give.getType() || stack.getDurability() != give.getDurability()){
						same = false;
					}
					
					if (give.getItemMeta() != null && give.getItemMeta().getDisplayName() != null){
						if (stack.getItemMeta().getDisplayName() == null){
							same = false;
						} else {
							if (!stack.getItemMeta().getDisplayName().equals(give.getItemMeta().getDisplayName())){
								same = false;
							}
						}
					}
					
					if (stack.getAmount() >= stack.getMaxStackSize()){
						same = false; // just to shorten the code
					}
					
					if (same == true){
						if (inv.getItem(slot).getAmount() + stilltogive <= stack.getMaxStackSize()){
							ItemStack set = give.clone(); 
							set.setAmount(stack.getAmount() + stilltogive);
							inv.setItem(slot, set);
							return true;
						}
						
						ItemStack set = give.clone();
						set.setAmount(set.getMaxStackSize());
						stilltogive = stilltogive - (set.getAmount() - stack.getAmount());
						inv.setItem(slot, set);
					}
				}
			} else {
				inv.addItem(give);
				return true;
			}
		}
		
		/*if (stilltogive > 0){
			if (backpack.lastspam.get(player.getName()) == null || (backpack.lastspam.get(player.getName()) - System.currentTimeMillis()) < -3){
				player.sendMessage(ChatColor.RED + "Your inventory is full!");	
			}
			backpack.lastspam.put(player.getName(), System.currentTimeMillis());
		}*/
		
		player.updateInventory();
		
		return true;
	}
}
