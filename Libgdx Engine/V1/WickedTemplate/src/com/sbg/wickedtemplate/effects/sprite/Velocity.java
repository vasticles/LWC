package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.ESprite;
import com.sbg.wickedtemplate.Effect;

public class Velocity extends Effect {

	public Velocity() {
	}

	public Velocity(Effect e, Object o) {
		super(e);
		
		//create timeline and insert a pre-pause if need
		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}

		//specify start value, specify end value and duration, and add resulting tweens to the existing timeline
		timeline.push(Tween.set(o, SpriteAccessor.VELOCITY).target(startValueA, startValueB))
				.push(Tween.to(o, SpriteAccessor.VELOCITY, duration).target(
						endValueA, endValueB));

		//add a post-pause if specified
		if (endPause > 0)
			timeline.pushPause(endPause);

		//process repetition if requested
		if (repeat) {
			//yoyo effect
			if (!yoyo)
				timeline.repeat(reps, repDelay).build();
			//regular repetition
			else
				timeline.repeatYoyo(reps, repDelay).build();
		} else
			timeline.build();
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

}
