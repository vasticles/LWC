package com.sbg.wickedtemplate.utils;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import com.sbg.wickedtemplate.LWP_Engine;

/**
Simple Utility class that holds various useful methods.
 **/

public class Utils {
	private static Logger log = LWP_Engine.log;
	private static Random r = new Random();
	private static long currentTime;
	
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
	
	public static Vector2 getRandomFloat(float[][] f) {
		Vector2 value = new Vector2();
		if (f[0].length == 1) value.x=f[0][0];
		else value.x=f[0][r.nextInt(f[0].length)];
		if (f[1].length == 1) value.y=f[1][0];
		else value.y=f[1][r.nextInt(f[1].length)];
		return value;
	}
	
	public static int getRandomInt(int[] i) {
		if (i.length == 1) return i[0];
		else return i[r.nextInt(i.length)];
	}
	
	public static Random getRandom() {
		return r;
	}
	
	public static float pixelToPercent(float f, float totalPix) {
		return f/totalPix*100;
	}
	
	public static float percentToPixel(float f, float totalPix) {
		return f/100*totalPix;
	}
	
	public static void startTimer() {
		currentTime = System.currentTimeMillis();
	}
	
	public static void stopTimer(String tag) {
		float seconds = (System.currentTimeMillis() - currentTime)/1000f;
//		if (millisPerUpdate >= 20)
			log.error(tag+seconds+" seconds");
	}
	
	public static void log(String string) {
		log.error(string);
	}
	
	public static float[][] stringArrToTwoDimFloatArr(String[] array) {
		float[][] newArr = new float[array.length][2];
		int ind = 0;
		for(String val : array) {
			newArr[ind][0] = Float.parseFloat(val.split(",")[0]);
			newArr[ind][1] = Float.parseFloat(val.split(",")[1]);
			ind++;
		}
		return newArr;
	}
}
