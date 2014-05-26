/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate.effects;

import java.util.List;
import java.util.Map;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.gson.annotations.SerializedName;
import com.sbg.wickedtemplate.ESprite;
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
	
	//list of maps that json deserializes into
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
		POSITION
	}

	//Effect properties
	@SerializedName("effect_name")
	public EffectNames effectName;
	public float duration;
	public float[] durations; //not implemented
	public float startDelay;
	public float endPause;
	public float startValue;
	public float endValue;
	public boolean repeat;
	public boolean yoyo;
	public int reps;
	public float repDelay;
	public long totalDuration;
	protected Tween tween;
	protected Timeline timeline;
	
	//empty constructor required for json deserialization
	public Effect(){};
	
	public Effect(ESprite s, Effect e) {
		this.startDelay = e.startDelay;
		this.duration = e.duration;
		this.endPause = e.endPause;
		this.startValue = e.startValue;
		this.endValue = e.endValue;
		this.repeat = e.repeat;
		this.yoyo = e.yoyo;
		this.reps = e.reps;
		this.repDelay = e.repDelay;
		this.effectName = e.effectName;

		//figure out total duration of the effect
		if(reps <= 0 || !repeat) totalDuration = Utils.convertToMillis((startDelay+duration+endPause));
		else totalDuration = Utils.convertToMillis((startDelay+duration+endPause)*(reps+1)+repDelay*reps);
		
		//register our ESprite accessor
		Tween.registerAccessor(ESprite.class, new SpriteAccessor());
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
	public static Effect createNewEffect(Effect e, ESprite s) {
		EffectNames name = e.effectName;
		switch(name) {
		case NOEFFECT:
			return new NoEffect(s, e);
		case OPACITY:
			return new Opacity(s, e);
		case ROTATION:
			return new Rotation(s, e);
		case SCALE:
			return new Scale(s, e);
		case POSITION:
			return new Position(s, e);
		default:
			return new NoEffect(s, e);
		}
	}

	
}
