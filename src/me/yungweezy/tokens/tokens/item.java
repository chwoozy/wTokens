package me.yungweezy.tokens.tokens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.yungweezy.tokens.main.connectMysql;
import me.yungweezy.tokens.main.fileaccessor;
import me.yungweezy.tokens.main.messageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class item implements Listener {

	public static HashMap<UUID, Integer> cachedTokens = new HashMap<UUID, Integer>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		int tokens = item.getTokens(event.getPlayer());
		cachedTokens.put(event.getPlayer().getUniqueId(), tokens);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		item.setBalance(event.getPlayer(), cachedTokens.get(event.getPlayer().getUniqueId()));
		cachedTokens.remove(event.getPlayer().getUniqueId());
	}
	
	public static void setBalance(Player player, int amount){
		cachedTokens.put(player.getUniqueId(), amount);
		Connection con = connectMysql.getConnection();

		String saveQuery = "insert into PlayerData (uuid, TOKENS) VALUES (?, ?)";
		
		try {
			PreparedStatement checkstate = con.prepareStatement("SELECT * FROM PlayerData where uuid=" + "'" + player.getUniqueId().toString().replace("-", "|") + "'");
			ResultSet result = checkstate.executeQuery();
			if (result.next()){
				saveQuery = saveQuery.replace("insert", "replace");
			}
		} catch (Exception e){
			System.out.println("[TokenSQL] Something went wrong while checking if having to update or insert");
			e.printStackTrace();
			return;
		}
		
		try {
			PreparedStatement saveState = connectMysql.getConnection().prepareStatement(saveQuery);
			saveState.setString(1, player.getUniqueId().toString().replace("-", "|"));
			saveState.setInt(2, amount);
			saveState.executeUpdate();
			System.out.println("[TokenSQL] Saved playerdata succesfully! " + player.getName());
		} catch (Exception e){
			System.out.println("Something went wrong while saving data to MYSQL! " + player.getName());
			e.printStackTrace();
			return;
		}
	}
	
	@Deprecated
	public static void setBalanceObsolete(Player player, int amount){
		cachedTokens.put(player.getUniqueId(), amount);
		
		fileaccessor.reloadFileF("tokens/" + player.getUniqueId().toString());
		FileConfiguration f = fileaccessor.getFileF("tokens/" + player.getUniqueId().toString());
		f.set("tokens", amount);
		fileaccessor.saveFile("tokens/" + player.getUniqueId().toString());
	}
	
	
	public static void giveToken(Player player, int amount){
		int bal = item.getTokens(player);
		if (bal < amount){
			messageManager.sendMessage(player, "neo_red");
			return;
		}
		ItemStack bToken = new ItemStack(Material.MAGMA_CREAM, 1);
		ItemMeta meta = bToken.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Token");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Right click to redeem!");
		meta.setLore(lore);
		bToken.setItemMeta(meta);
		
		int a = 0;
		boolean bool = true;
		
		while (bool == true) {
			a = a + 1;
			if (a >= amount){
				bool = false;
			}
			Boolean newbool = true;
			for (ItemStack stack2 : player.getInventory().getContents()){
				if (stack2 != null){
					if (bToken.getType() == stack2.getType()){
						if (stack2.getAmount() < 64){
							if (stack2.getItemMeta().getLore() != null){
								if (stack2.getItemMeta().getLore() == bToken.getItemMeta().getLore()){
									if (1 + stack2.getAmount() <= 64){
										if (newbool == true){
											bToken.setAmount(stack2.getAmount() + 1);
											player.getInventory().addItem(bToken);
											newbool = false;
											player.updateInventory();
										}
									}
								}
							}
						}
					}
				} else {
					if (newbool == true){
						player.getInventory().addItem(bToken);
						newbool = false;
						player.updateInventory();
					}
				}
			}
		}
		
		String message = messageManager.getMessage(player, "tokens_substract");
		message = message.replace("{AMOUNT}", a + "");
		player.sendMessage(message);
		
		int oldamount = item.getTokens(player);
		int newamount = oldamount - a;
		item.setBalance(player, newamount);
	}
	
	
	public static int getTokens(Player player){
		if (item.cachedTokens.get(player.getUniqueId()) != null){
			return item.cachedTokens.get(player.getUniqueId());
		}
		
		boolean exists = false;
		String quer = "SELECT * FROM PlayerData where uuid=" + "'" + player.getUniqueId().toString().replace("-", "|") + "'"; 
		
		Connection con = connectMysql.getConnection();
		Statement state = null;
		
		try {
			state = con.createStatement();
		} catch (SQLException e1) {
			System.out.println("[TokenSQL] Couldnt create statement! (E 472)");
			e1.printStackTrace();
			exists = false;
		}
		
		ResultSet rs = null;
		
		try {
			rs = state.executeQuery(quer);
			if (rs.next()){
				exists = true;
			} else {
				System.out.println("[TokenSQL] Player doesnt exist in table! Using obsolete method! " + player.getName());
				exists = false;
			}
		} catch (Exception e){
			System.out.println("[TokenSQL] Something went wrong while trying to discover the token balance in the database! " + player.getName());
			System.out.println(e);
			return 0;
		}
		
		if (!exists){
			int amount = getTokensObsolete(player);
			item.setBalance(player, amount);
			return amount;
		}
		
		try {
			int amount = rs.getInt("TOKENS");
			cachedTokens.put(player.getUniqueId(), amount);
			System.out.println("[TokensSQL] Completely loaded from MySQL " + player.getUniqueId());
			return amount;
		} catch (SQLException e) {
			System.out.println("[TokenSQL] Player tokens were somehow not possible to retrieve! " + player.getName());
			e.printStackTrace();
			cachedTokens.put(player.getUniqueId(), 0);
			return 0;
		}
		
	}
	
	@Deprecated
	public static int getTokensObsolete(Player player){
		if (item.cachedTokens.get(player.getUniqueId()) != null){
			return item.cachedTokens.get(player.getName());
		}
		
		fileaccessor.reloadFileF("tokens/" + player.getUniqueId().toString());
		FileConfiguration f = fileaccessor.getFileF("tokens/" + player.getUniqueId().toString());
		
		int bal = 0;
		if (f.get("tokens") == null){
			f.set("tokens", 0);
			fileaccessor.saveFile("tokens/" + player.getUniqueId().toString());
			bal = 0;
		} else {
			bal = f.getInt("tokens");
		}
		
		return bal;
	}
	
	public static void redeemTokens(Player player){
		ItemStack bToken = new ItemStack(Material.MAGMA_CREAM, 1);
		ItemMeta meta = bToken.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Token");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Right click to redeem!");
		meta.setLore(lore);
		bToken.setItemMeta(meta);
		
		if (player.getItemInHand() == null) {
			return;
		}
		
		ItemStack hand = player.getItemInHand();
		
		if (hand.getItemMeta() == null){
			return;
		}
		if (hand.getItemMeta().getLore() == null){
			return;
		}

		if (hand.getItemMeta().getLore().equals(bToken.getItemMeta().getLore())){
			if (hand.getItemMeta().getDisplayName().equals(bToken.getItemMeta().getDisplayName())){
				int amount = hand.getAmount();
				int oldamount = item.getTokens(player);
				int newamount = amount + oldamount;
				
				item.cachedTokens.put(player.getUniqueId(), newamount);
				item.setBalance(player, newamount);
				
				player.setItemInHand(new ItemStack(Material.AIR, 0));
				player.updateInventory();
				
				String message = messageManager.getMessage(player, "tokens_add");
				message = message.replace("{AMOUNT}", amount + "");
				player.sendMessage(message);
				
				log.addTransaction(player, amount, "redeem_item", "+");
			}
		}
	}
}
