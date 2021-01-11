package me.yungweezy.tokens.tokens;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import me.yungweezy.tokens.main.createitem;
import me.yungweezy.tokens.main.fileaccessor;

public class prices {

	public static HashMap<String, Integer> maxlvls = new HashMap<String, Integer>();
	public static HashMap<String, Integer> enchantprice = new HashMap<String, Integer>();
	public static HashMap<Integer, String> enchantbyslot = new HashMap<Integer, String>();
	public static HashMap<Integer, Integer> bpprice = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> bppricebyslot = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> bpsizebyslot = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> keyprice = new HashMap<Integer, Integer>();
	public static HashMap<Integer, String> keytype = new HashMap<Integer, String>();
	public static HashMap<Integer, Integer> keyamount = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> gempricebyslot = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> itempricebyslot = new HashMap<Integer, Integer>();
	public static HashMap<Integer, ItemStack> itembyslot = new HashMap<Integer, ItemStack>();
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void updatePricesAndMaxLevels(){
		fileaccessor.reloadFileF("gui");
		FileConfiguration gui = fileaccessor.getFileF("gui");
		Object[] enchanttypes = gui.getConfigurationSection("prices.enchants").getKeys(false).toArray();
		
		for (Object enchanttype : enchanttypes){
			int price = gui.getInt("prices.enchants." + enchanttype + ".price");
			int max = gui.getInt("prices.enchants." + enchanttype + ".max");
			maxlvls.put(enchanttype + "", max);
			enchantprice.put(enchanttype + "", price);
		}
		
		Object[] bptypes = gui.getConfigurationSection("prices.bp").getKeys(false).toArray();
		int bpslot = 0;
		for (Object bptype : bptypes){
			int price = gui.getInt("prices.bp." + bptype);
			bpprice.put(Integer.parseInt(bptype + ""), price);
			bppricebyslot.put(bpslot, price);
			bpsizebyslot.put(bpslot, Integer.parseInt(bptype + ""));
			bpslot = bpslot + 1;
		}
		
		for (int slot = 0; slot < 18; slot = slot + 1){
			if (gui.get("keys." + slot) != null){
				int price = gui.getInt("keys." + slot + ".price");
				keyprice.put(slot, price);
				String keyt = gui.getString("keys." + slot + ".key");
				keytype.put(slot, keyt);
				int keyamnt = gui.getInt("keys." + slot + ".amount");
				keyamount.put(slot, keyamnt);
			}
			
			if (gui.getString("enchants." + slot + ".type") != null){
				String enchtype = gui.getString("enchants." + slot + ".type");
				prices.enchantbyslot.put(slot, enchtype);
			}
		}
		
		for (int slot = 0; slot <= 17; slot = slot + 1){
			if (gui.get("items." + slot + ".price") != null){
				int price = gui.getInt("items." + slot + ".price");
				itempricebyslot.put(slot, price);
				
				ArrayList<String> lore = (ArrayList<String>) gui.get("items." + slot + ".lore");
				String name = gui.getString("items." + slot + ".name");
				int amount = gui.getInt("items." + slot + ".amount");
				int sub = gui.getInt("items." + slot + ".sub");
				Material mat = Material.getMaterial(gui.getInt("items." + slot + ".id"));
				ItemStack item = createitem.createItem(mat, sub, name, lore);
				item.setAmount(amount);
				itembyslot.put(slot, item);
			}
		}
		
		System.out.println("[TOKENS] succesfully registered all prices!");
	}
}
