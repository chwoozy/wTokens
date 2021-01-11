package me.yungweezy.tokens.tokens;

import me.yungweezy.tokens.main.fileaccessor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class guimain implements CommandExecutor {
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args){
		if (cmd.getName().equalsIgnoreCase("tokenshop")){
			if (sender instanceof Player){
				Player player = (Player) sender;
				gui.openMenu(player, gui.mainmenu);
				
				/*
				Inventory standard = gui.inv;
				Inventory pspecific = Bukkit.createInventory(null, 54, standard.getName());
				pspecific.setContents(standard.getContents());
				
				ItemStack bTokens = new ItemStack(Material.PAPER, 1);
				ItemMeta meta = bTokens.getItemMeta();
				meta.setDisplayName("Tokens:");
				ArrayList<String> lore = new ArrayList<String>(); lore.add(ChatColor.GREEN + "" + item.getTokens(player));
				meta.setLore(lore);
				bTokens.setItemMeta(meta);
				pspecific.setItem(0, bTokens);
				pspecific.setItem(8, bTokens);
				pspecific.setItem(45, bTokens);
				pspecific.setItem(53, bTokens);
				
				
				player.openInventory(pspecific);
				*/
			}
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("Tokens")){
			if (args.length == 0){
				Player p = (Player) sender;
				message.Balance(p);
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("give")){
					if (!(sender instanceof Player)){
						int amount = Integer.parseInt(args[2]);
						OfflinePlayer player = Bukkit.getPlayer(args[1]);
						
						if (player == null || player.getUniqueId() == null){
							fileaccessor.reloadFileF("tokens/offlineUnGiven");
							FileConfiguration f = fileaccessor.getFileF("tokens/offlineUnGiven");
							
							int set = amount;
							if (f.get(args[1]) != null){
								set = set + f.getInt(args[1]);
							}
							
							f.set(args[1], set);
							
							fileaccessor.saveFile("tokens/offlineUnGiven");
							
							sender.sendMessage(ChatColor.GOLD + "Player was offline, added to qeue");
							return true;
						}
						
						
						if (player.isOnline()){
							int old = item.getTokens((Player) player);
							((Player) player).sendMessage(ChatColor.GREEN + "You received " + amount + " tokens");
							log.addTransaction((Player) player, amount, "give_command", "+");
							item.setBalance((Player) player, amount + old);
							return true;
						} else {
							sender.sendMessage(ChatColor.RED + "That player is not online!");
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "This command can only be executed as console!");
					}
				} else {
					message.IncorrectUsage((Player) sender);
				}
			} else if (args.length == 2){
				if (args[0].equalsIgnoreCase("check")){
					if (sender.hasPermission("tokens.checkothers")){
						if (Bukkit.getPlayer(args[1]) == null){
							sender.sendMessage(ChatColor.RED + "Player isnt online!");
						} else {
							Player player = Bukkit.getPlayer(args[1]);
							int tokens = item.getTokens(player);
							sender.sendMessage(player.getName() + " has " + tokens + " tokens");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "youre not allowed to do that");
					}
				} else if (args[0].equalsIgnoreCase("reset")){
					if (sender.hasPermission("tokens.resetothers")){
						if (Bukkit.getPlayer(args[1]) == null){
							sender.sendMessage(ChatColor.RED + "Player isnt online!");
						} else {
							Player player = Bukkit.getPlayer(args[1]);
							item.setBalance(player, 0);
						}
					} else {
						sender.sendMessage(ChatColor.RED + "youre not allowed to do that");
					}
				} else {
					message.IncorrectUsage((Player) sender);
				}
			} else if (args.length == 1 && sender.hasPermission("tokens.export")){
				if (args[0].equalsIgnoreCase("export")){
					log.export();
					sender.sendMessage(ChatColor.GREEN + "exported log, name: " + log.getTime() + ".yml");
				} else {
					message.IncorrectUsage((Player) sender);
				}
			} else {
				message.IncorrectUsage((Player) sender);
			}
		}
		return false;
	}
}
