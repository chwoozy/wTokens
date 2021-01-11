package me.yungweezy.tokens.bp;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.yungweezy.tokens.main.messageManager;

public class bpevents implements Listener {

	@EventHandler
	public void onInvClick(InventoryClickEvent event){	
		Player player = (Player) event.getWhoClicked();
		ItemStack inhand = player.getItemInHand();
		ItemStack current = event.getCurrentItem();
		
		if (event.getAction() == InventoryAction.HOTBAR_SWAP){
			current = player.getInventory().getItem(event.getHotbarButton());
		}
		
		if (!backpack.isBackPack(inhand)){
			return;
		}

		if (!backpack.isBackPack(current)){
			return;
		}
		
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		sizes.add(9);
		sizes.add(18);
		sizes.add(27);
		sizes.add(36);
		sizes.add(45);
		sizes.add(54);
		sizes.add(63);
		sizes.add(72);
		
		boolean validtitle = false;
		
		for (int size : sizes){
			String name = backpack.getInvNameType(size);
			name = ChatColor.translateAlternateColorCodes('&', name);
			if (event.getInventory().getName().equals(name)){
				validtitle = true;
			}
		}
		
		if (validtitle == false){
			return;
		}
		
		event.setCancelled(true);
		messageManager.sendMessage(player, "bp_in_bp");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onRightClick(PlayerInteractEvent event){
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR){
			return;
		}
		
		Player player = event.getPlayer();
		
		if (player.getItemInHand() == null || player.getItemInHand().getType() == null || player.getItemInHand().getType() == Material.AIR){
			return;
		}
		
		ItemStack hand = player.getItemInHand();
		
		if (backpack.isBackPack(hand) == false){
			return;
		}
		
		ArrayList<String> lore = (ArrayList<String>) hand.getItemMeta().getLore();
		
		event.setCancelled(true);
		
		String id = lore.get(1);
		id = ChatColor.stripColor(id);
		if (backpack.bps.get(id) == null){
			player.sendMessage(ChatColor.RED + "Backpack not loaded, inform an admin!");
			return;
		}
		
		Inventory bp = backpack.bps.get(id);
		player.openInventory(bp);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		loadall.loadAllFor(player);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();
		loadall.saveAllFor(player);
	}
}
