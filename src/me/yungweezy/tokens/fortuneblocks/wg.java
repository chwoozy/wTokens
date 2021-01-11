package me.yungweezy.tokens.fortuneblocks;

import me.yungweezy.tokens.main.main;

import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class wg {

	public static WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = main.getPlugin(main.class).getServer().getPluginManager().getPlugin("WorldGuard");
	    
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	
}
