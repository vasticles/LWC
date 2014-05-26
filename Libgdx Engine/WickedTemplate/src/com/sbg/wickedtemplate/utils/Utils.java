package com.sbg.wickedtemplate.utils;

import java.util.Random;

import com.badlogic.gdx.utils.Logger;

/**
Simple Utility class that holds various useful methods.
 **/

public class Utils {
	public static Logger log;
	private static Random r = new Random();
	
	public Utils() {
//		log = new Logger()
	}
	
	public void debug() {
	}
	
	public static long convertToMillis(float f) {
		return (long) (f*1000);
	}
	
	public static long getRandomLong(float[] f) {
		if (f.length == 1) return (long) (f[0]*1000);
		else return (long) (f[r.nextInt(f.length)]*1000);
	}

	public static float getRandomFloat(float[] f) {
		if (f.length == 1) return f[0];
		else return f[r.nextInt(f.length)];
	}
	
	public static int getRandomInt(int[] i) {
		if (i.length == 1) return i[0];
		else return i[r.nextInt(i.length)];
	}
	
	public static float pixelToPercent(float f, float totalPix) {
		return f/totalPix*100;
	}
	
	public static float percentToPixel(float f, float totalPix) {
		return f/100*totalPix;
	}
}
