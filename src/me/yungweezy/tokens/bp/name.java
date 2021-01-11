package me.yungweezy.tokens.bp;

import java.io.File;
import java.util.Random;

import org.bukkit.configuration.file.FileConfiguration;

import me.yungweezy.tokens.main.fileaccessor;
import me.yungweezy.tokens.main.main;

public class name {

	public static String createRandomID(){
		
		String possibleletters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890lsajfaldjfapiowur98731hl23";
		Random rand = new Random();
		
		boolean good = false;
		
		while (good == false){
			String id = "";
			for (int place = 0; place <= 18; place = place + 1){
				if (place == 6 || place == 11 || place == 16){
					id = id + "-";
				} else {
					int randomnr = rand.nextInt(possibleletters.length());
					String r = possibleletters.substring(randomnr, randomnr + 1);
					id = id + r;
				}
			}
			
			File fil = new File(main.getPlugin(main.class).getDataFolder() + "/" + id);
			if (!fil.exists()){ 
				fileaccessor.reloadFileF("backpacks/" + id);
				FileConfiguration file = fileaccessor.getFileF("backpacks/" + id);
				if (file.get("contents") == null){
					good = true;
					return id;
				}
			}
		}
		
		return null;
	}
	
}
