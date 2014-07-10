/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.google.gson.annotations.SerializedName;
import com.sbg.wickedtemplate.effects.sprite.NoEffect;
import com.sbg.wickedtemplate.effects.sprite.Opacity;
import com.sbg.wickedtemplate.effects.sprite.Position;
import com.sbg.wickedtemplate.effects.sprite.Rotation;
import com.sbg.wickedtemplate.effects.sprite.Scale;
import com.sbg.wickedtemplate.effects.sprite.SpriteAccessor;
import com.sbg.wickedtemplate.effects.sprite.Velocity;
import com.sbg.wickedtemplate.utils.Utils;

/**
Effect class is an abstract factory. Custom effects can be extended from this class and then created 
with a static method.
For now effects use the tween engine for manipulating sprites' properties.
Effects are created using the tween and timeline objects. Each extended class has a lot of redundant code
that may need to be optimized later.
Explanations can be found in the Opacity class.
At the moment only Opacity, Rotation, Scale and Position properties can be manipulated.
More will be added later. This structure hopefully allows for future expansion into more intricate effects that
the tween engine may not support.
An effect supports delays (pre or post), repetitions and delays in between, and repetition styles 
(standard or yoyo).
Tween engine supports built-in as well as custom easing functions, which will later be added. For more information
on easing functions, refer to the tween engine demo. 
 **/

public abstract class Effect {
	
	//list of property maps that json deserializes into
	public List<Map<String,?>> properties;
	
	public Effect effect;
	public enum EffectNames {
		
		@SerializedName("no_effect")
		NOEFFECT, 
		
		@SerializedName("opacity")
		OPACITY, 
	
		@SerializedName("rotation")
		ROTATION,
		
		@SerializedName("scale")
		SCALE,
		
		@SerializedName("position")
		POSITION,
		
		@SerializedName("velocity")
		VELOCITY
		
	}

	//Effect properties
	@SerializedName("effect_name")
	public EffectNames effectName;
	public boolean isDisabled;
	public float duration;
	public float[] durations; //not implemented
	public float startDelay;
	public float[] startDelays;
	public float endPause;
	public float[] endPauses;
	public String tweenMode; //incremental, constant, or random (implemented later)
	
	
	public float startValueA;
	public float[] startValuesA; //array of start values for the first (default) property
	
	public float startValueB;
	public float[] startValuesB; //array of start values for the second property (if applicable)
	
	public boolean startPairs; //pairs start values (e.g. coords x and y)
	
	public float endValueA;
	public float[] endValuesA; //array of end values for the first (default) property
	
	public float endValueB;
	public float[] endValuesB; //array of end values for the second property (if applicable)
	
	public boolean endPairs; //pairs end values (e.g. coords x and y)
	
	public boolean startEndAPairs;
	public boolean startEndBPairs; //pairs start and end values for tween mode purposes (only B values are used for now)
	public boolean startEndAllPairs;
	public boolean repeat;
	public boolean yoyo;
	public boolean repeatInfinitely;
	public int reps;
	public int[] repsRange;
	public float repDelay;
	public float[] repDelays;
	public float totalDuration;
	protected Tween tween;
	protected Timeline timeline;
	
	//empty constructor required for json deserialization
	public Effect(){};
	
	public Effect(Effect e) throws IllegalArgumentException {
		duration = Utils.getRandomFloat(e.durations);
		startDelay = Utils.getRandomFloat(e.startDelays);
		endPause = Utils.getRandomFloat(e.endPauses);
		tweenMode = e.tweenMode;
		//get random start values
		if(!e.startPairs) {
			startValueA = Utils.getRandomFloat(e.startValuesA);
			if(e.startValuesB != null) startValueB = Utils.getRandomFloat(e.startValuesB);
		} else {
			if(!checkArrayLengthEquality(e.startValuesA, e.startValuesB)) throw new IllegalArgumentException("Start value arrays are not of the same length");
			else {
				//if start values are pairs (same index), then get the pair
				int index = Utils.getRandom().nextInt(e.startValuesA.length);
				startValueA = e.startValuesA[index];
				startValueB = e.startValuesB[index];
				if(e.startEndAllPairs) {
					if(!checkArrayLengthEquality(e.startValuesA, e.endValuesB)) throw new IllegalArgumentException("Start and end value arrays are not of the same length");
					else {
						endValueA = e.endValuesA[index];
						endValueB = e.endValuesB[index];
					}
				}
			}
		}
		//get random end values
		
		if(tweenMode.equalsIgnoreCase("constant")) {
			endValueA = startValueA;
			endValueB = startValueB;
		} else if(!e.startEndAllPairs) {
			if(!e.endPairs) {
				endValueA = Utils.getRandomFloat(e.endValuesA);
				if(e.endValuesB != null) endValueB = Utils.getRandomFloat(e.endValuesB);
			} else {
				if(!checkArrayLengthEquality(e.endValuesA, e.endValuesB)) throw new IllegalArgumentException("End value arrays are not of the same length");
				else {
					//if end values are pairs (same index), then get the pair
					int index = Utils.getRandom().nextInt(e.endValuesA.length);
					endValueA = e.endValuesA[index];
					endValueB = e.endValuesB[index];
				}
			}
		}
		repeat = e.repeat;
		yoyo = e.yoyo;
		reps = Utils.getRandomInt(e.repsRange);
		repDelay = Utils.getRandomFloat(e.repDelays);
		effectName = e.effectName;
		
		//figure out total duration of the effect
		if(reps <= 0 || !repeat) totalDuration = startDelay+duration+endPause;
		else totalDuration = (startDelay+duration+endPause)*(reps+1)+repDelay*reps;
		
		//register our ESprite accessor
//		Tween.registerAccessor(ESprite.class, new SpriteAccessor());
	}
	
	private boolean checkArrayLengthEquality(float[] a, float[] b) {
		return a.length == b.length;
	}
	
	//not used with the tween engine
	public abstract void draw();
	
	public Tween getTween() {
		return tween;
	}
	
	public Timeline getTimeline() {
		return timeline;
	}
	
	//Effect factory method. Used for creating effects.
	public static Effect createNewEffect(Effect e, Object obj) {
		EffectNames name = e.effectName;
		switch(name) {
		case NOEFFECT:
			return new NoEffect(e);
		case OPACITY:
			return new Opacity(obj, e);
		case ROTATION:
			return new Rotation(obj, e);
		case SCALE:
			return new Scale(obj, e);
		case POSITION:
			return new Position(obj, e);
		case VELOCITY:
			return new Velocity(obj, e);
		default:
			return new NoEffect(e);
		}
	}

	
}
