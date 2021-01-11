package me.yungweezy.tokens.tokens;

import me.yungweezy.tokens.main.fileaccessor;
import me.yungweezy.tokens.main.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class events implements Listener {

	@EventHandler
	public void onLogin(PlayerJoinEvent event){
		fileaccessor.reloadFileF("tokens/offlineUnGiven");
		FileConfiguration f = fileaccessor.getFileF("tokens/offlineUnGiven");
		
		if (f.get(event.getPlayer().getName()) == null){
			return;
		}
		
		int give = f.getInt(event.getPlayer().getName());
		
		f.set(event.getPlayer().getName(), null);
		
		fileaccessor.saveFile("tokens/offlineUnGiven");
		
		main.getPlugin(main.class).getServer().dispatchCommand(Bukkit.getConsoleSender(), "tokens give " + event.getPlayer().getName() + " " + give);
	}
	
	@EventHandler
	public void onRedeem(PlayerInteractEvent event){
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
			Block block = event.getClickedBlock();
			Player player = event.getPlayer();

			if (block != null){
				if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST){
					Sign sign = (Sign) block.getState();
					String line0 = sign.getLine(0);
					line0 = ChatColor.stripColor(line0);

					String line1 = sign.getLine(1);
					line1 = ChatColor.stripColor(line1);
					line1 = line1.toLowerCase();
					line1 = line1.replaceAll("[^\\d.]", "");
					
					if (line0.equalsIgnoreCase("[tokens]")){
						if (sign.getLine(1).isEmpty() || line1.equalsIgnoreCase("")){
							item.giveToken(player, 1);
							log.addTransaction(player, 1, "redeem_sign", "-");
							return;
						}
						int amount = Integer.parseInt(line1);
						if (amount > 1){
							item.giveToken(player, amount);
							log.addTransaction(player, amount, "redeem_sign", "-");
						}
					}
				} else {
					item.redeemTokens(player);
				}
			} else {
				item.redeemTokens(player);
			}
		} 
	}
	
	@EventHandler
	public void onGemRedeem(PlayerInteractEvent event){
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK){
			return;
		}
		
		Player player = event.getPlayer();
		if (player.getItemInHand() == null || player.getItemInHand().getType() == null || player.getItemInHand().getType() == Material.AIR){
			return;
		}
		
		ItemStack hand = player.getItemInHand();
		if (hand.getType() != Material.EMERALD){
			return;
		}
		
		if (!gems.isGem(hand)){
			return;
		}
		
		double tier = gems.getTier(hand);
		int time = gems.getTime(hand);
		hand.setAmount(hand.getAmount() - 1);
		
		player.setItemInHand(hand);
		
		// sellall multiply {PLAYER} {TIER} {TIME}
		String command = "sellall:sellall multiply " + player.getName() + " " + tier + " " + time;
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
			
	}
}
