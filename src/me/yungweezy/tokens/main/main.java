package me.yungweezy.tokens.main;

import java.util.ArrayList;

//import me.clip.deluxechat.placeholders.DeluxePlaceholderHook;
//import me.clip.deluxechat.placeholders.PlaceholderHandler;
import me.yungweezy.tokens.bp.backpack;
import me.yungweezy.tokens.bp.bpevents;
import me.yungweezy.tokens.bp.loadall;
import me.yungweezy.tokens.fortuneblocks.explosive;
import me.yungweezy.tokens.tokens.bountySystem;
import me.yungweezy.tokens.tokens.clock;
import me.yungweezy.tokens.tokens.events;
import me.yungweezy.tokens.tokens.fireWorkSystem;
import me.yungweezy.tokens.tokens.gems;
import me.yungweezy.tokens.tokens.guimain;
import me.yungweezy.tokens.tokens.gui;
import me.yungweezy.tokens.tokens.item;
import me.yungweezy.tokens.tokens.log;
import me.yungweezy.tokens.tokens.prices;
import me.yungweezy.tokens.tokens.timeBombSystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;

public class main extends JavaPlugin {

	public static Plugin pl;

	public void onEnable(){
		pl = this;

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new explosive(), this);

		this.saveDefaultConfig();
		fileaccessor.saveXDefault("gui");

		explosive.updateLists();

		fileaccessor.saveXDefault("lang");

		gems.update();

		pm.registerEvents(new events(), this);
		pm.registerEvents(new gui(), this);
		pm.registerEvents(new bpevents(), this);
		pm.registerEvents(new bountySystem(), this);
		pm.registerEvents(new timeBombSystem(), this);
		pm.registerEvents(new fireWorkSystem(), this);
		pm.registerEvents(new item(), this);

		getCommand("tokenshop").setExecutor(new guimain());
		getCommand("token").setExecutor(new guimain());

		connectMysql.attemptConnect("prison1_tokens", "FmGeZ6QcGKBcyYyD");
		
		clock.startClock();
		clock.startClock2();
		prices.updatePricesAndMaxLevels();
		gui.updateBaseInvs();

		loadall.loadAll();
		//main.deluxeChat();

		bountySystem.cacheBountyAmounts(this);

		for (Player player : Bukkit.getOnlinePlayers()){
			item.cachedTokens.put(player.getUniqueId(), item.getTokens(player));
		}

		new messageManager().loadAll();

		if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			main.mvdwPlaceholders();
		}
	}

	public void onDisable(){
		loadall.saveAll();

		//PlaceholderHandler.unregisterPlaceholderHook(this);

		log.export();
		for (Player player : Bukkit.getOnlinePlayers()){
			if (item.cachedTokens.get(player.getName()) != null){
				item.setBalance(player, item.cachedTokens.get(player.getName()));
			}
		}
	}

	/*public static void deluxeChat(){
		if (Bukkit.getPluginManager().isPluginEnabled("DeluxeChat")) {
			Plugin pl = main.getPlugin(main.class);
			PlaceholderHandler.registerPlaceholderHook(pl, new DeluxePlaceholderHook() {
				@Override
				public String onPlaceholderRequest(Player p, String identifier) {

					switch (identifier) {
					case "tokens":
						return item.getTokens(p) + "";
					}
					return null;
				}
			});
		}
	}*/

	public static void mvdwPlaceholders(){
		PlaceholderAPI.registerPlaceholder(main.pl, "tokens_tokens", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();

				return item.getTokens(player) + "";
			}

		});

	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args){
		if (cmd.getName().equalsIgnoreCase("givegem")){
			if (args.length != 2){
				sender.sendMessage(ChatColor.RED + "/givegem <player> <nr>");
				return true;
			}

			if (Bukkit.getPlayer(args[0]) != null && Bukkit.getPlayer(args[0]).isOnline()){
				Player target = Bukkit.getPlayer(args[0]);
				int slot = 0;
				try {
					slot = Integer.parseInt(args[1]);
				} catch(Exception e) {
					sender.sendMessage("thats not a number...");
				}

				ArrayList<String> lore = new ArrayList<String>();
				ArrayList<String> defaultlore = me.yungweezy.tokens.tokens.gems.defaultlore;

				for (String s : defaultlore){
					String add = s + "";
					add = add.replace("{TIER}", me.yungweezy.tokens.tokens.gems.gettierbyslot.get(slot) + "x");
					add = add.replace("{TIME}", me.yungweezy.tokens.tokens.gems.gettimebyslot.get(slot) + "");
					lore.add(add);
				}

				ItemStack stack = createitem.createItem(Material.EMERALD, 0, me.yungweezy.tokens.tokens.gems.name.replace("{TIER}", me.yungweezy.tokens.tokens.gems.gettierbyslot.get(slot) + ""), lore);
				target.getInventory().addItem(stack);

			} else {
				sender.sendMessage(ChatColor.RED + "Player not found!");
			}
		} else if (cmd.getName().equalsIgnoreCase("backpack")){
			if (args.length != 3){
				sender.sendMessage(ChatColor.RED + "/backpack give <player> <size>");
				return true;
			} 

			if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()){
				Player target = Bukkit.getPlayer(args[1]);
				int size = 0;
				try {
					size = Integer.parseInt(args[2]);
					if (size % 9 != 0){
						sender.sendMessage(ChatColor.RED + "Size has to be a multiple of 9");
						return false;
					}
				} catch (Exception e){
					sender.sendMessage(ChatColor.RED + "Size has to be a number that can be devided by 9");
					return false;
				}

				backpack.giveRandomBackpack(target, size);
				sender.sendMessage(ChatColor.GREEN + "Gave " + target.getName() + " a " + backpack.getInvNameType(size) + " backpack ");
			} else {
				sender.sendMessage(ChatColor.RED + "Player not found!");
			}
		}
		return true;
	}
}
