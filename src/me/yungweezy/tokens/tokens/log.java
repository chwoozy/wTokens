package me.yungweezy.tokens.tokens;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.yungweezy.tokens.main.fileaccessor;

public class log {
	
	private static ArrayList<String> transactions = new ArrayList<String>();

	public static String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("DD-HH-mm");
        return sdf.format(cal.getTime());
	}
	
	public static void export(){
		String filename = log.getTime();
		fileaccessor.reloadFileF(filename);
		FileConfiguration store = fileaccessor.getFileF(filename);
		store.set("transactions", transactions.toString());
		
		fileaccessor.saveFile(filename);
		
		transactions.clear();
	}
	
	public static void addTransaction(Player player, int amount, String action, String posneg){
		String total = log.getTime() + "==" + player.getName() + "==" + posneg + amount + "==" + action;
		transactions.add(total);
	}
}
