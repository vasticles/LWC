package com.sbg.wickedtemplate.effects;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.ESprite;

public class Rotation extends Effect {

	public Rotation() {
	}

	public Rotation(ESprite s, Effect e) {
		super(s, e);

		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}

		timeline.push(Tween.set(s, SpriteAccessor.ROTATION).target(startValue))
				.push(Tween.to(s, SpriteAccessor.ROTATION, duration).target(
						endValue));

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
