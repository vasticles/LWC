package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.Effect;

public class Opacity extends Effect {

	public Opacity() {
	}

	public Opacity(Object s, Effect e) {
		super(e);
		
		//create timeline and insert a pre-pause if needd
		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}
		
		startValueA /= 100f;
		endValueA /= 100f;

		//specify start value, specify end value and duration, and add resulting tweens to the existing timeline
		timeline.push(Tween.set(s, SpriteAccessor.ALPHA).target(startValueA))
				.push(Tween.to(s, SpriteAccessor.ALPHA, duration).target(
						endValueA)).build();

		//add a post-pause if specified
		if (endPause > 0)
			timeline.pushPause(endPause).build();

		//process repetition if requested
		if (repeat) {
			//yoyo effect
			if (!yoyo)
				timeline.repeat(reps, repDelay).build();
			//regular repetition
			else
				timeline.repeatYoyo(reps, repDelay).build();
		}
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
	}

}
