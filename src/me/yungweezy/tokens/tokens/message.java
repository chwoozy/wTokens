package me.yungweezy.tokens.tokens;

import org.bukkit.entity.Player;

import me.yungweezy.tokens.main.messageManager;

public class message {

	public static void Balance(Player player){
		messageManager.sendMessage(player, "balance");
	}
	
	public static void IncorrectUsage(Player player){
		messageManager.sendMessage(player, "usage");
	}
}
