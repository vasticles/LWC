package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.ESprite;
import com.sbg.wickedtemplate.Effect;

public class Opacity extends Effect {

	public Opacity() {
	}

	public Opacity(Effect e, Object o) {
		super(e);
		
		
		//create timeline and insert a pre-pause if needed
		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}
		
		//convert from percent
		startValueA /= 100f;
		endValueA /= 100f;
		
		//if no start delay, then preset the start value directly
		if(startDelay <= 0) {
			ESprite s = (ESprite) o;
			s.setColor(s.getColor().r, s.getColor().g, s.getColor().b, startValueA);
		}

		//specify start value, specify end value and duration, and add resulting tweens to the existing timeline
		timeline.push(Tween.set(o, SpriteAccessor.ALPHA).target(startValueA))
				.push(Tween.to(o, SpriteAccessor.ALPHA, duration).target(
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
