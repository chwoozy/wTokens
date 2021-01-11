package me.yungweezy.tokens.tokens;

import java.util.ArrayList;

import me.yungweezy.tokens.bp.backpack;
import me.yungweezy.tokens.fortuneblocks.level;
import me.yungweezy.tokens.main.createitem;
import me.yungweezy.tokens.main.fileaccessor;
import me.yungweezy.tokens.main.main;
import me.yungweezy.tokens.main.messageManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class gui implements Listener {

	public static Inventory mainmenu;
	public static Inventory enchants;
	public static Inventory redeem;
	public static Inventory keys;
	public static Inventory backpacks;
	public static Inventory gems;
	public static Inventory items;
	
	public static ArrayList<String> standardInfoLore = new ArrayList<String>();
	public static String infoName = "";
	public static String backName = "";
	public static boolean exchange = true;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void updateBaseInvs(){
		fileaccessor.reloadFileF("gui");
		FileConfiguration config = fileaccessor.getFileF("gui");
		
		
		mainmenu = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', config.getString("guinames.main")));
		enchants = Bukkit.createInventory(null, 18, ChatColor.translateAlternateColorCodes('&', config.getString("guinames.enchants")));
		redeem = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', config.getString("guinames.redeem")));
		keys = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + "Tokens - Keys");
		backpacks = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', config.getString("guinames.backpacks")));
		gems = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', config.getString("guinames.gems")));
		items = Bukkit.createInventory(null, 18, ChatColor.DARK_AQUA + "Tokens - Item Shop");
		
		// MAINMENU //
		ArrayList<String> empty = new ArrayList<String>();
		ArrayList<String> enchlore = (ArrayList<String>) config.get("mainmenu.enchants.lore");
		ArrayList<String> redeemlore = (ArrayList<String>) config.get("mainmenu.redeem.lore");
		ArrayList<String> backpackslore = (ArrayList<String>) config.get("mainmenu.backpacks.lore");
		ArrayList<String> buffslore = (ArrayList<String>) config.get("mainmenu.gems.lore");
		
		ArrayList<String> keyslore = (ArrayList<String>) empty.clone();
		keyslore.add("&ePurchase mysterious keys");
		keyslore.add("&eto unbound the magical force");
		keyslore.add("&ewithin the crates!");
		ArrayList<String> itemslore = (ArrayList<String>) empty.clone();
		itemslore.add("&ePurchase a variety of things,");
		itemslore.add("&eranging from blocks to weapons to spawners!");
		
		
		if (exchange == false){
			redeemlore.clear();
			redeemlore.add("&4&llocked");
		}
		
		
		//ItemStack items = createitem.createItem(Material.GOLD_INGOT, 0, "&3Buy &6Items", itemslore);
		ItemStack enchants = createitem.createItem(Material.getMaterial(config.getString("mainmenu.enchants.mat")), config.getInt("mainmenu.enchants.sub"), config.getString("mainmenu.enchants.name"), enchlore);
		ItemStack redeem = createitem.createItem(Material.getMaterial(config.getString("mainmenu.redeem.mat")), config.getInt("mainmenu.redeem.sub"), config.getString("mainmenu.redeem.name"), redeemlore);
		//ItemStack keys = createitem.createItem(Material.TRIPWIRE_HOOK, 0, ChatColor.YELLOW + "&3Buy &6Crate Keys", keyslore);
		ItemStack backpakcs = createitem.createItem(Material.getMaterial(config.getString("mainmenu.backpacks.mat")), config.getInt("mainmenu.backpacks.sub"), config.getString("mainmenu.backpacks.name"), backpackslore);
		ItemStack gem = createitem.createItem(Material.getMaterial(config.getString("mainmenu.gems.mat")), config.getInt("mainmenu.gems.sub"), config.getString("mainmenu.gems.name"), buffslore);
		ItemStack back = createitem.createItem(Material.getMaterial(175), 0, ChatColor.DARK_AQUA + "Back", new ArrayList<String>());
		
		if (exchange == false){
			redeem.setType(Material.getMaterial(166));
		}
		
		//mainmenu.setItem(1, items);
		mainmenu.setItem(2, enchants);
		mainmenu.setItem(3, redeem);
		//mainmenu.setItem(5, keys);
		mainmenu.setItem(6, backpakcs);
		mainmenu.setItem(5, gem);
		
		me.yungweezy.tokens.tokens.gui.keys.setItem(8, back);
		me.yungweezy.tokens.tokens.gui.enchants.setItem(8, back);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(8, back);
		me.yungweezy.tokens.tokens.gui.backpacks.setItem(8, back);
		me.yungweezy.tokens.tokens.gui.gems.setItem(8, back);
		me.yungweezy.tokens.tokens.gui.items.setItem(8, back);
		// MAINMENU //
		
		fileaccessor.reloadFileF("gui");
		FileConfiguration gui = fileaccessor.getFileF("gui");
		
		// ENCHANTS //
		ArrayList<String> baseenchlore = new ArrayList<String>();

		for (int slot = 0; slot < 18; slot = slot + 1){
			if (gui.get("enchants." + slot) != null){
				ArrayList<String> newlore = (ArrayList<String>) baseenchlore.clone();;
				String nick = gui.getString("enchants." + slot + ".nick");
				Object c = 3;
				newlore.add("&3Effect: &a&l+1");
				
				if (gui.getString("enchants." + slot + ".lore") != null){
					newlore.addAll((ArrayList<String>) gui.get("enchants." + slot + ".lore"));
					c = gui.get("enchants." + slot + ".color");
				} else {
					newlore.add("&4&lMISSING DESCRIPTION");
				}
				
				/*switch (nick.toLowerCase()){
				case "fortune":
					newlore.add("&8Increases drops per block broken.");
					c = 6;
					break;
				case "efficiency":
					newlore.add("&8Allows pickaxe to be swung faster.");
					c = 2;
					break;
				case "flight":
					newlore.add("&8Grants wielder the ability of flight.");
					c = "a";
					break;
				case "jetpack":
					newlore.add("&8Grants wielder the ability of flight by technology.");
					c = "a";
					break;
				case "speed":
					newlore.add("&8Allows user to run faster while holding pickaxe.");
					c = "a";
					break;
				case "nightvision":
					newlore.add("&8Gives the wielder night vision.");
					c = "d";
					break;
				case "hex":
					newlore.add("&8Grants pickaxe the strength to mine a circle radius.");
					c = "e";
					break;
				case "sphere":
					newlore.add("&8Allows pickaxe to deal a powerful mining blast.");
					c = "b";
					break;
				case "explosive":
					newlore.add("&8Enchants pickaxe to blow surronding blocks while mining.");
					c = "c";
					break;
				case "bounty":
					newlore.add("&8Gain money after mining a certain amount of blocks.");
					c = 5;
					break;
				case "timebomb":
					newlore.add("&8A massive mining boost that you can use based on a cooldown.");
					c = "c";
					break;
				case "haste":
					newlore.add("&8Be able to mine a bit faster.");
					c = "6";
					break;
				case "unbreaking":
					newlore.add("&8Lets your tool last for a little longer.");
					c = "6";
					break;
				case "drill":
					newlore.add("&8An upgrade to your explosive enchantment to make it go down as well!");
					c = "4";
					break;
				case "firework":
					newlore.add("&8Why dont you buy it and see it for yourself?");
					c = "4";
					break;
				case "autosell":
					newlore.add("&8Automatically sell your items based on a cooldown");
					newlore.add("&8(level 1 = 60 seconds, level 2 = 58 seconds, etc)");
					c = "4";
					break;
				default:
					newlore.add("&4&lStill missing a description!");
					c = "4&l";
					break;
				}*/

				ItemStack enchitem = createitem.createItem(Material.DIAMOND_PICKAXE, 0, "&" + c + nick, newlore);
				me.yungweezy.tokens.tokens.gui.enchants.setItem(slot, enchitem);
			}
		}

		System.out.println(prices.enchantprice);
		
		// ENCHANTS //
		
		
		// REDEEM //
		ArrayList<String> tokenlore = (ArrayList<String>) config.get("redeem.lore");
		ItemStack redeemstack = createitem.createItem(Material.MAGMA_CREAM, 0, config.getString("redeem.name"), tokenlore);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(0, redeemstack);
		redeemstack.setAmount(5);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(1, redeemstack);
		redeemstack.setAmount(10);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(2, redeemstack);
		redeemstack.setAmount(15);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(3, redeemstack);
		redeemstack.setAmount(20);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(4, redeemstack);
		redeemstack.setAmount(25);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(5, redeemstack);
		redeemstack.setAmount(32);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(6, redeemstack);
		redeemstack.setAmount(64);
		me.yungweezy.tokens.tokens.gui.redeem.setItem(7, redeemstack);
		// REDEEM //
		
		
		// KEYS //	
		for (int slot = 0; slot < 10; slot = slot + 1){
			if (gui.get("keys." + slot) != null){
				ItemStack stack = createitem.createItem(Material.getMaterial(gui.getInt("keys." + slot + ".item.id")), gui.getInt("keys." + slot + ".item.sub"), gui.getString("keys." + slot + ".item.name"), (ArrayList<String>) gui.get("keys." + slot + ".item.lore"));
				me.yungweezy.tokens.tokens.gui.keys.setItem(slot, stack);
			}
		}
		// KEYS //
		
		
		// BACKPACKS //
		for (int slot = 0; slot < 10; slot = slot + 1){
			if (gui.get("backpacks." + slot) != null){
				ArrayList<String> lore = new ArrayList<String>();
				int slots = gui.getInt("backpacks." + slot);
				int price = gui.getInt("prices.bp." + slots);
				lore.add(backpack.getInvNameType(slots));
				lore.add("&7Buy a &l" + slots + "&f&7 slots backpack");
				prices.bppricebyslot.put(slot, price);
				ItemStack stack = createitem.createItem(Material.CHEST, 0, "&3&l" + slots + " &f&3slots", lore);
				me.yungweezy.tokens.tokens.gui.backpacks.setItem(slot, stack);
			}
		}
		// BACKPACKS //
		
		
		// GEMS //
		fileaccessor.reloadFileF("gui");
		FileConfiguration f = fileaccessor.getFileF("gui");
		for (int slot = 0; slot < 8; slot = slot + 1){
			if (f.get("gem_bufs." + slot) != null){
				ArrayList<String> lore = new ArrayList<String>();
				String add = ChatColor.AQUA + "Click to buy a";
				String add2 = ChatColor.GRAY + "{TIER}x" + ChatColor.AQUA + " multiplier for " + ChatColor.GRAY + "{TIME} seconds";
				add2 = add2.replace("{TIER}", me.yungweezy.tokens.tokens.gems.gettierbyslot.get(slot) + "");
				add2 = add2.replace("{TIME}", me.yungweezy.tokens.tokens.gems.gettimebyslot.get(slot) + "");
				lore.add(add);
				lore.add(add2);
				
				ItemStack stack = createitem.createItem(Material.EMERALD, 0, me.yungweezy.tokens.tokens.gems.name.replace("{TIER}", me.yungweezy.tokens.tokens.gems.gettierbyslot.get(slot) + ""), lore);
				gems.setItem(slot, stack);
			}
		}
		// GEMS //
		
		// ITEMS //
		fileaccessor.reloadFileF("gui");
		f = fileaccessor.getFileF("gui");
		
		for (int slot = 0; slot <= 17; slot = slot + 1){
			if (slot != 8 && f.get("items." + slot + ".id") != null){
				String name = f.getString("items." + slot + ".menuname");
				ArrayList<String> lore = (ArrayList<String>) f.get("items." + slot + ".menulore");
				Material mat = Material.getMaterial(f.getInt("items." + slot + ".id"));
				int sub = f.getInt("items." + slot + ".sub");
				ItemStack stack = createitem.createItem(mat, sub, name, lore);
				me.yungweezy.tokens.tokens.gui.items.setItem(slot, stack);
			}
		}
		// ITEMS //
		
		// standard info item //
		me.yungweezy.tokens.tokens.gui.standardInfoLore = (ArrayList<String>) f.get("info.lore");
		me.yungweezy.tokens.tokens.gui.infoName = f.getString("info.mainname");
		me.yungweezy.tokens.tokens.gui.backName = f.getString("info.backname");
		
	}
	
	public static void openMenu(final Player player, final Inventory inv){
		Plugin plugin = me.yungweezy.tokens.main.main.getPlugin(me.yungweezy.tokens.main.main.class);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				Inventory toopen = Bukkit.createInventory(null, inv.getSize(), inv.getName());
				toopen.setContents(inv.getContents());

				if (toopen.getItem(8) != null && toopen.getItem(8).getType() != null && toopen.getItem(8).getType() == Material.getMaterial(175)){
					ArrayList<String> arrowlore = new ArrayList<String>();
					for (String s : standardInfoLore){
						String a = s;
						a = a.replace("{TOKENS}", item.getTokens(player) + "");
						arrowlore.add(a);
					}
					ItemStack stack = createitem.setLore(toopen.getItem(8), arrowlore);
					stack = createitem.setName(stack, me.yungweezy.tokens.tokens.gui.backName);
					toopen.setItem(8, stack);
				}

				int tokens = item.getTokens(player);

				//fileaccessor.reloadFileF("gui");
				//FileConfiguration gui = fileaccessor.getFileF("gui");

				String invname = toopen.getName();
				if (invname.equals(gui.mainmenu.getName())){
					ArrayList<String> infolore = new ArrayList<String>();
					for (String s : standardInfoLore){
						String a = s;
						a = a.replace("{TOKENS}", item.getTokens(player) + "");
						infolore.add(a);
					}
					ItemStack stand = new ItemStack(Material.getMaterial(175), 1);
					ItemStack stack = createitem.setLore(stand, infolore);
					stack = createitem.setName(stack, me.yungweezy.tokens.tokens.gui.infoName);
					toopen.setItem(4, stack);
				} else if (invname.equals(gui.enchants.getName())){
					for (int slot = 0; slot < 18; slot = slot + 1){
						//System.out.println(prices.enchantprice);
						//System.out.println(prices.enchantbyslot);
						if (prices.enchantbyslot.get(slot) != null){
							int price = 1000;
							try {
								price = prices.enchantprice.get(prices.enchantbyslot.get(slot).toLowerCase());
							} catch (Exception e){
								price = 106969;
							}
							ItemStack stack = toopen.getItem(slot);
							ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();

							lore.add("&3Price: &e" + price + "&e ❂");

							if (price <= tokens && player.hasPermission("enchants.access." + prices.enchantbyslot.get(slot))){
								lore.add("&a&o◄ Buy Now ►");
							} else {
								if (!player.hasPermission("enchants.access." + prices.enchantbyslot.get(slot))){
									lore.add("&6&o✗ Donor only item! ✗");
								} else {
									lore.add("&6&o✗ You can't afford this! ✗");
								}
							}
							ItemStack stacknew = createitem.setLore(stack, lore);
							toopen.setItem(slot, stacknew);
						}
					}
				} else if (invname.equals(gui.redeem.getName())){
					for (int slot = 0; slot < 8; slot = slot + 1){
						ItemStack stack = toopen.getItem(slot);
						ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();

						if (tokens >= toopen.getItem(slot).getAmount()){
							lore.add("&a&o◄ Buy Now ►");
						} else {
							lore.add("&6&o✗ You can't afford this! ✗");
						}

						stack = createitem.setLore(stack, lore);
						toopen.setItem(slot, stack);
					}
				} else if (invname.equals("Tokens - Keys")){
					for (int slot = 0; slot < 8; slot = slot + 1){
						if (toopen.getItem(slot) != null && toopen.getItem(slot).getType() != null){
							int price = prices.keyprice.get(slot);

							ItemStack stack = toopen.getItem(slot);
							ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();

							lore.add("&3Price: &e" + price + "&e ❂");

							if (tokens >= price){
								lore.add("&a&o◄ Buy Now ►");
							} else {
								lore.add("&6&o✗ You can't afford this! ✗");
							}

							stack = createitem.setLore(stack, lore);
							toopen.setItem(slot, stack);
						}
					}
				} else if (invname.equals(gui.backpacks.getName())){
					for (int slot = 0; slot < 8; slot = slot + 1){
						if (toopen.getItem(slot) != null && toopen.getItem(slot).getType() != null){
							int price = prices.bppricebyslot.get(slot);

							ItemStack stack = toopen.getItem(slot);
							ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();

							lore.add("&3Price: &e" + price + "&e ❂");

							if (tokens >= price){
								lore.add("&a&o◄ Buy Now ►");
							} else {
								lore.add("&6&o✗ You can't afford this! ✗");
							}

							stack = createitem.setLore(stack, lore);
							toopen.setItem(slot, stack);
						}
					}
				} else if (invname.equals(gui.gems.getName())){
					for (int slot = 0; slot < 8; slot = slot + 1){
						if (toopen.getItem(slot) != null && toopen.getItem(slot).getType() != null){
							int price = prices.gempricebyslot.get(slot);

							ItemStack stack = toopen.getItem(slot);
							ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();

							lore.add("&3Price: &e" + price + "&e ❂");

							if (tokens >= price){
								lore.add("&a&o◄ Buy Now ►");
							} else {
								lore.add("&6&o✗ You can't afford this! ✗");
							}

							stack = createitem.setLore(stack, lore);
							toopen.setItem(slot, stack);
						}
					}
				} else if (invname.equals(gui.items.getName())){
					for (int slot = 0; slot < 8; slot = slot + 1){
						if (toopen.getItem(slot) != null && toopen.getItem(slot).getType() != null){
							int price = prices.itempricebyslot.get(slot);

							ItemStack stack = toopen.getItem(slot);
							ArrayList<String> lore = (ArrayList<String>) stack.getItemMeta().getLore();

							lore.add("&3Price: &e" + price + "&e ❂");

							if (tokens >= price){
								lore.add("&a&o◄ Buy Now ►");
							} else {
								lore.add("&6&o✗ You can't afford this! ✗");
							}

							stack = createitem.setLore(stack, lore);
							toopen.setItem(slot, stack);
						}
					}
				}

				player.openInventory(toopen);
			}
		}, 1L);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event){
		if (event.getInventory() == null){
			return;
		}
		
		Inventory inv = event.getInventory();
		if (!ChatColor.stripColor(inv.getName()).toLowerCase().startsWith("tokens")){
			return;
		}
		
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == null || event.getCurrentItem().getType() == Material.AIR){
			return;
		}
		
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		
		if (event.getRawSlot() == 8){
			gui.openMenu(player, mainmenu);
			return;
		}
		
		int tokens = item.getTokens(player);
		
		String invname = inv.getName();
		if (invname.equals(gui.mainmenu.getName())){
			if (event.getRawSlot() > 8){
				return;
			}
			
			if (event.getSlot() == 1){
				//gui.openMenu(player, items);
			} else if (event.getSlot() == 2){
				gui.openMenu(player, enchants);
			} else if (event.getSlot() == 3){
				if (exchange == true){
					gui.openMenu(player, redeem);
				} 
			} else if (event.getSlot() == 5){
				gui.openMenu(player, gems);
			} else if (event.getSlot() == 6){
				gui.openMenu(player, backpacks);
			} else if (event.getSlot() == 7){
				//gui.openMenu(player, gems);
			} 
		} else if (invname.equals(gui.enchants.getName())){
			/*fileaccessor.reloadFileF("gui");
			FileConfiguration gui = fileaccessor.getFileF("gui");
			
			String enchtype = gui.getString("enchants." + event.getRawSlot() + ".type");*/
			
			if (prices.enchantbyslot.get(event.getRawSlot()) == null){
				return;
			}
			
			String enchtype = prices.enchantbyslot.get(event.getRawSlot());

			if (!player.hasPermission("enchants.access." + enchtype)){
				messageManager.sendMessage(player, "nop_ench");
				return;
			} 
			
			int price = prices.enchantprice.get(enchtype.toLowerCase());
			if (tokens < price){
				player.closeInventory();
				messageManager.sendMessage(player, "neo_ench");
				return;
			}
			
			int maxlvl = prices.maxlvls.get(enchtype.toLowerCase());
			int current = level.getLevel(player, enchtype);
			if (current + 1 > maxlvl){
				messageManager.sendMessage(player, "max_ench");
				return;
			}
			
			boolean success = level.addLevel(player, enchtype);
			if (success == false){
				messageManager.sendMessage(player, "pick_hand");
				return;
			} else {
				item.setBalance(player, item.getTokens(player) - price);
				log.addTransaction(player, price, "pickaxe_upgrade", "-");
				messageManager.sendMessage(player, "upg_pick");
			}
			
			me.yungweezy.tokens.tokens.gui.openMenu(player, me.yungweezy.tokens.tokens.gui.enchants);
		} else if (invname.equals(gui.redeem.getName())){
			item.giveToken(player, event.getCurrentItem().getAmount());
			log.addTransaction(player, event.getCurrentItem().getAmount(), "redeem_gui", "-");
			gui.openMenu(player, gui.redeem);
		} else if (invname.equals(gui.keys.getName())){
			if (prices.keyprice.get(event.getRawSlot()) == null){
				return;
			}
			
			int price = prices.keyprice.get(event.getRawSlot());
			
			if (tokens < price){
				player.closeInventory();
				messageManager.sendMessage(player, "neo_pur");
				return;
			}
			
			item.setBalance(player, item.getTokens(player) - price);
			log.addTransaction(player, price, "buy_key", "-");
			
			// ./cratekey give [player] [tier] [amount]
			String keytype = prices.keytype.get(event.getRawSlot());
			int keyamount = prices.keyamount.get(event.getRawSlot());
			
			String cmd = "cratekey give " + player.getName() + " " + keytype + " " + keyamount;
			main.getPlugin(main.class).getServer().dispatchCommand(main.getPlugin(main.class).getServer().getConsoleSender(), cmd);
			gui.openMenu(player, gui.keys);
		} else if (invname.equals(gui.backpacks.getName())){
			if (prices.bppricebyslot.get(event.getRawSlot()) == null){
				return;
			}
			
			int price = prices.bppricebyslot.get(event.getRawSlot());
			
			if (tokens < price){
				player.closeInventory();
				messageManager.sendMessage(player, "neo_pur");
				return;
			}
			
			item.setBalance(player, item.getTokens(player) - price);
			log.addTransaction(player, price, "buy_backpack", "-");
			backpack.giveRandomBackpack(player, prices.bpsizebyslot.get(event.getRawSlot()));
			player.sendMessage(ChatColor.GREEN + "Bought 1 " + ChatColor.translateAlternateColorCodes('&', backpack.getInvNameType(prices.bpsizebyslot.get(event.getRawSlot()))));
			gui.openMenu(player, gui.backpacks);
		} else if (invname.equals(gui.gems.getName())){
			if (me.yungweezy.tokens.tokens.gems.gettierbyslot.get(event.getRawSlot()) == null){
				return;
			}
			
			int price = prices.gempricebyslot.get(event.getRawSlot());
			
			if (tokens < price){
				player.closeInventory();
				messageManager.sendMessage(player, "neo_pur");
				return;
			}
			
			item.setBalance(player, tokens - price);
			log.addTransaction(player, price, "buy_buff", "-");
			
			ArrayList<String> lore = new ArrayList<String>();
			ArrayList<String> defaultlore = me.yungweezy.tokens.tokens.gems.defaultlore;
			
			for (String s : defaultlore){
				String add = s + "";
				add = add.replace("{TIER}", me.yungweezy.tokens.tokens.gems.gettierbyslot.get(event.getRawSlot()) + "x");
				add = add.replace("{TIME}", me.yungweezy.tokens.tokens.gems.gettimebyslot.get(event.getRawSlot()) + "");
				lore.add(add);
			}
			
			ItemStack stack = createitem.createItem(Material.EMERALD, 0, me.yungweezy.tokens.tokens.gems.name.replace("{TIER}", me.yungweezy.tokens.tokens.gems.gettierbyslot.get(event.getRawSlot()) + ""), lore);
			player.getInventory().addItem(stack);
			player.sendMessage(ChatColor.GREEN + "Bought 1 " + ChatColor.GRAY + me.yungweezy.tokens.tokens.gems.gettierbyslot.get(event.getRawSlot()) + "x" + ChatColor.GREEN + " multiplier!");
			gui.openMenu(player, gui.gems);
		} else if (invname.equals(gui.items.getName())){
			if (prices.itempricebyslot.get(event.getRawSlot()) == null){
				return;
			}
			
			int price = prices.itempricebyslot.get(event.getRawSlot());
			
			if (tokens < price){
				player.closeInventory();
				messageManager.sendMessage(player, "neo_pur");
				return;
			}
			
			ItemStack stack = prices.itembyslot.get(event.getRawSlot());
			
			item.setBalance(player, tokens - price);
			log.addTransaction(player, price, "buy_itemshop", "-");
			player.getInventory().addItem(stack);
			
			String purchasedname = stack.getType().name().toLowerCase();
			
			if (stack.getItemMeta() != null && stack.getItemMeta().getDisplayName() != null){
				purchasedname = stack.getItemMeta().getDisplayName();
			}
			
			player.sendMessage(ChatColor.GREEN + "Purchased " + purchasedname + ChatColor.RESET + ChatColor.GRAY + " (" + stack.getAmount() + ")");
			
			gui.openMenu(player, gui.items);
		} else {
			return;
		}
		
	}
	
}
