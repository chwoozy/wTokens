package me.yungweezy.tokens.fortuneblocks;

import java.util.Random;

public class toadd {

	public static int toAdd() {
		Random rand = new Random();
		int negpos = rand.nextInt(4) + 1;
		
		int toadd = 0;
		if (negpos == 1){
			toadd = 1;
		} else if (negpos == 2){
			toadd = -1;
		} else if (negpos == 3){
			toadd = -2;
		} else {
			toadd = 2;
		}
		
		return toadd;
	}
	
	public static int getXZ() {
		Random rand = new Random();
		int xyz = rand.nextInt(3) + 1;
		return xyz;
	}
}
