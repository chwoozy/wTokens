package me.yungweezy.tokens.main;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.yungweezy.tokens.tokens.item;
import net.md_5.bungee.api.ChatColor;

public class messageManager {

	public static HashMap<String, String> messages = new HashMap<String, String>();
	
	public void loadAll(){
		fileaccessor.reloadFileF("lang");
		FileConfiguration config = fileaccessor.getFileF("lang");
		
		for (String identifier : config.getConfigurationSection("").getKeys(false)){
			String message = config.getString(identifier);
			message = ChatColor.translateAlternateColorCodes('&', message);
			messages.put(identifier, message);
		}
	}
	
	public static String getMessage(Player player, String identifier){
		if (messages.get(identifier) == null){
			return "UNCONFIGURED MESSAGE: " + identifier;
		}
		
		String message = messages.get(identifier);
		
		message = message.replace("{PLAYER}", player.getName());
		message = message.replace("{TOKENS}", item.getTokens(player) + "");
		
		return message;
	}
	
	public static void sendMessage(Player player, String identifier){
		String message = getMessage(player, identifier);
		
		player.sendMessage(message);
	}
}
