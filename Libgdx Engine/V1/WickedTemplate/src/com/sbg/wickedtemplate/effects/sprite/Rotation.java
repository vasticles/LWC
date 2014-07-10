package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.ESprite;
import com.sbg.wickedtemplate.Effect;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.utils.Utils;

public class Rotation extends Effect {

	public Rotation() {
	}

	public Rotation(Effect e, Object o) {
		super(e);

		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}
		
		//if no start delay, then preset the start value directly
		if(startDelay <= 0) {
			ESprite s = (ESprite) o;
			s.setRotation(startValueA);
		}
		
		timeline.push(Tween.set(o, SpriteAccessor.ROTATION).target(startValueA))
				.push(Tween.to(o, SpriteAccessor.ROTATION, duration).target(endValueA));

		if (endPause > 0)
			timeline.pushPause(endPause);

		if (repeat) {
			if (!yoyo)
				timeline.repeat(reps, repDelay).build();
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
