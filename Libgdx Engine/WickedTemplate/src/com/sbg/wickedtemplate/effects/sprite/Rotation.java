package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.Effect;

public class Rotation extends Effect {

	public Rotation() {
	}

	public Rotation(Object s, Effect e) {
		super(e);

		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}

		timeline.push(Tween.set(s, SpriteAccessor.ROTATION).target(startValueA))
				.push(Tween.to(s, SpriteAccessor.ROTATION, duration).target(
						endValueA));

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
