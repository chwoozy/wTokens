package me.yungweezy.tokens.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class fileaccessor {

	
	private static FileConfiguration File = null;
	private static File customConfigFile = null;
	
	@SuppressWarnings("deprecation")
	public static void reloadFileF(String filename) {
		fileaccessor.saveFile(filename);
	    customConfigFile = new File(main.getPlugin(main.class).getDataFolder(), filename + ".yml");
	    
	    File = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    InputStream defConfigStream = main.getPlugin(main.class).getResource(filename + ".yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        File.setDefaults(defConfig);
	    }
	}
	
	public static FileConfiguration getFileF(String filename) {
	    if (File == null) {
	        reloadFileF(filename);
	    }
	    return File;
	}
	
	public static void saveFile(String filename) {
	    if (File == null || customConfigFile == null) {
	        return;
	    }
	    try {
	        fileaccessor.getFileF(filename).save(customConfigFile);
	    } catch (IOException ex) {
	        main.getPlugin(main.class).getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
	}
	
	public static void saveXDefault(String filename) {
		Plugin plugin = main.getPlugin(main.class);
		
		if (customConfigFile == null) {
	        customConfigFile = new File(plugin.getDataFolder(), filename + ".yml");
	    }
	    if (!customConfigFile.exists()) {            
	         plugin.saveResource(filename + ".yml", false);
	    }
	    
	    customConfigFile = null;
	}
}
